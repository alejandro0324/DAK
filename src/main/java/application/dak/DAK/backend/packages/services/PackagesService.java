package application.dak.DAK.backend.packages.services;

import application.dak.DAK.backend.common.models.StrategyPrice;
import application.dak.DAK.backend.common.models.StrategyPrice1;
import application.dak.DAK.backend.common.models.StrategyPrice2;
import application.dak.DAK.backend.common.models.StrategyPrice3;
import application.dak.DAK.views.packages.PackagesView;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.google.maps.*;
import com.google.maps.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static application.dak.DAK.backend.utils.Constants.*;
import static com.google.maps.FindPlaceFromTextRequest.InputType.TEXT_QUERY;

@Service
@Slf4j
public class PackagesService {
    public Integer tripTax;
    public Float KG;
    public Float KM;
    private StrategyPrice strategyPrice;
    private LatLng destination;
    private final GeoApiContext geoApiContext;

    public PackagesService() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(System.getProperty("google.maps.api"))
                .build();
    }

    public void setDestination(LatLng destination) {
        this.destination = destination; //TODO: Set the destination in any point
    }

    public void getCoordinatesFromAddress(String address) {
        FindPlaceFromTextRequest request = PlacesApi.findPlaceFromText(geoApiContext, address, TEXT_QUERY);
        request.language(SPANISH);
        request.fields(FindPlaceFromTextRequest.FieldMask.FORMATTED_ADDRESS, FindPlaceFromTextRequest.FieldMask.GEOMETRY);
        request.setCallback(new PendingResult.Callback<>() {
            @Override
            public void onResult(FindPlaceFromText findPlaceFromText) {
                HashMap<String, LatLng> coordinates = new HashMap<>();
                for (PlacesSearchResult candidate : findPlaceFromText.candidates) {
                    coordinates.put(candidate.formattedAddress, candidate.geometry.location);
                }
                PackagesView.autoCompleteAddressInput(coordinates);
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("An error occurred {}", throwable.getMessage());
            }
        });
    }

    public String calculatePrice() {
        return strategyPrice.execute().toString();
    }

    public void setStrategy(Integer groupId) {
        switch (groupId) {
            case 1:
                this.strategyPrice = new StrategyPrice1(tripTax);
                break;
            case 2:
                this.strategyPrice = new StrategyPrice2(KG, tripTax);
                break;
            case 3:
                calculateKilometres();
                break;
        }
    }

    private void calculateKilometres() {
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(geoApiContext);
        request.origins(new LatLng(CURRENT_LAT, CURRENT_LNG));
        request.units(Unit.METRIC);
        request.destinations(destination).setCallback(new PendingResult.Callback<>() {
            @Override
            public void onResult(DistanceMatrix distanceMatrix) {
                KM = (float) (distanceMatrix.rows[0].elements[0].distance.inMeters / 1000);
                strategyPrice = new StrategyPrice3(KG, KM);
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("An error occurred {}", throwable.getMessage());
            }
        });
    }
}
