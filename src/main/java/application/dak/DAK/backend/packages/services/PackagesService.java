package application.dak.DAK.backend.packages.services;

import application.dak.DAK.backend.common.models.StrategyPrice;
import application.dak.DAK.backend.common.models.StrategyPrice1;
import application.dak.DAK.backend.common.models.StrategyPrice2;
import application.dak.DAK.backend.common.models.StrategyPrice3;
import com.google.maps.*;
import com.google.maps.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static application.dak.DAK.backend.utils.Constants.*;
import static com.google.maps.FindPlaceFromTextRequest.InputType.TEXT_QUERY;

@Slf4j
public class PackagesService {
    public Integer tripTax;


    private Float KG;
    private Float KM;
    private static StrategyPrice strategyPrice;
    private LatLng destination;
    private HashMap<String, LatLng> coordinates;
    private final GeoApiContext geoApiContext;

    public HashMap<String, LatLng> getCoordinates() {
        return coordinates;
    }

    public void setKG(Float KG) {
        this.KG = KG;
    }

    public void setKM(Float KM) {
        this.KM = KM;
    }

    public Float getKG() {
        return KG;
    }

    public Float getKM() {
        return KM;
    }

    public PackagesService() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(System.getProperty("google.maps.api"))
                .build();
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String calculatePrice() {
        return String.valueOf(strategyPrice.execute());
    }

    public void setStrategy(Integer groupId) {
        switch (groupId) {
            case 1:
                strategyPrice = new StrategyPrice1();
                break;
            case 2:
                strategyPrice = new StrategyPrice2(KG, tripTax);
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
                strategyPrice = new StrategyPrice3(KG, KM, tripTax);
            }
            @Override
            public void onFailure(Throwable throwable) {
                log.error("An error occurred {}", throwable.getMessage());
            }
        });
    }

    public void getCoordinatesFromAddress(String address) {
        FindPlaceFromTextRequest request = PlacesApi.findPlaceFromText(geoApiContext, address, TEXT_QUERY);
        request.language(SPANISH);
        request.fields(FindPlaceFromTextRequest.FieldMask.FORMATTED_ADDRESS, FindPlaceFromTextRequest.FieldMask.GEOMETRY);
        request.setCallback(new PendingResult.Callback<>() {
            @Override
            public void onResult(FindPlaceFromText findPlaceFromText) {
                coordinates = new HashMap<>();
                for (PlacesSearchResult candidate : findPlaceFromText.candidates) {
                    log.info(candidate.formattedAddress);
                    coordinates.put(candidate.formattedAddress, candidate.geometry.location);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("An error occurred {}", throwable.getMessage());
            }
        });
    }
}
