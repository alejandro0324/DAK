package application.dak.DAK.backend.expeditions.services;

import application.dak.DAK.backend.common.models.Package;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static application.dak.DAK.backend.utils.Constants.CURRENT_LAT;
import static application.dak.DAK.backend.utils.Constants.CURRENT_LNG;

@Service
@Slf4j
public class ExpeditionService {

    private final GeoApiContext geoApiContext;

    @Autowired
    public ExpeditionService() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(System.getProperty("google.maps.api"))
                .build();
    }

    public void calculateDirections(List<Package> packages) {
        DirectionsApiRequest directionsRequest = new DirectionsApiRequest(geoApiContext);
        configureRequest(directionsRequest, packages);
        directionsRequest.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                directionsRequestSuccessfulResult(result);
            }

            @Override
            public void onFailure(Throwable e) {
                log.error("Something went wrong while calculating directions {}", e.getMessage());
            }
        });
    }

    private void directionsRequestSuccessfulResult(DirectionsResult result) {
        log.info("calculateDirections: routes: = " + result.routes[0].toString());
        log.info("duration = " + result.routes[0].legs[0].duration);
        log.info("distance = " + result.routes[0].legs[0].distance);
        log.info("result.geocodedWaypoints = " + Arrays.toString(result.geocodedWaypoints));
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
}

