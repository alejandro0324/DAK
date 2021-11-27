package application.dak.DAK.backend.expeditions.services;

import application.dak.DAK.backend.common.models.DirectionsQueryResult;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.PackageState;
import application.dak.DAK.backend.common.models.Tracking;
import application.dak.DAK.backend.expeditions.components.ExpeditionSender;
import application.dak.DAK.backend.expeditions.mappers.TrackingMapper;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static application.dak.DAK.backend.utils.Constants.CURRENT_LAT;
import static application.dak.DAK.backend.utils.Constants.CURRENT_LNG;

@Service
@Slf4j
public class ExpeditionService {

    private final GeoApiContext geoApiContext;
    private DirectionsQueryResult directionsResult;
    private final ExpeditionSender expeditionSender;
    private final TrackingMapper trackingMapper;

    public DirectionsQueryResult getDirectionsResult() {
        return directionsResult;
    }

    @Autowired
    public ExpeditionService(ExpeditionSender sender, TrackingMapper mapper) {
        this.expeditionSender = sender;
        this.trackingMapper = mapper;
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(System.getProperty("google.maps.api"))
                .build();
    }

    public void calculateDirections(List<Package> packages) {
        DirectionsApiRequest directionsRequest = new DirectionsApiRequest(geoApiContext);
        configureRequest(directionsRequest, packages);
        directionsRequest.setCallback(new PendingResult.Callback<>() {
            @Override
            public void onResult(DirectionsResult result) {
                directionsRequestSuccessfulResult(result.routes);
            }

            @Override
            public void onFailure(Throwable e) {
                log.error("Something went wrong while calculating directions {}", e.getMessage());
            }
        });
    }

    private void directionsRequestSuccessfulResult(DirectionsRoute[] routes) {
        for (DirectionsRoute route : routes) {
            List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
            directionsResult = new DirectionsQueryResult(decodedPath);
        }
    }

    private void configureRequest(DirectionsApiRequest directionsRequest, List<Package> packages) {
        directionsRequest.alternatives(false);
        directionsRequest.origin(new LatLng(CURRENT_LAT, CURRENT_LNG));
        List<LatLng> coordinates = new ArrayList<>();
        packages.forEach(i -> coordinates.add(new LatLng(i.getLat(), i.getLng())));
        Package lastPackage = packages.get(packages.size() - 1);
        LatLng destination = new LatLng(lastPackage.getLat(), lastPackage.getLng());
        directionsRequest.waypoints(coordinates.toArray(new LatLng[0]));
        directionsRequest.optimizeWaypoints(true);
        directionsRequest.destination(destination);
    }

    public void updatePackagesAsync(List<Package> packages) {
        packages.forEach(expeditionSender::notifyPackagesUpdate);
    }

    public void updatePackageSync(Package pack) {
        trackingMapper.updatePackage(PackageState.IN_TRAVEL.toString(), pack.getNumber());
        Tracking tracking = new Tracking();
        tracking.setId(pack.getTrackingId());
        tracking.setStateOfTracking(PackageState.IN_TRAVEL.toString());
        tracking.setCarID(pack.getCarID());
        trackingMapper.updateTracking(tracking);
    }
}

