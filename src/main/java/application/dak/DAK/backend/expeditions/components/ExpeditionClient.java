package application.dak.DAK.backend.expeditions.components;

import application.dak.DAK.backend.common.models.DirectionsQueryResult;
import application.dak.DAK.backend.common.models.Package;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpeditionClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public ExpeditionClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/expedition/";
    }

    public void calculateDirections(List<Package> packages) {
        final String url = baseURL + "calculateRoute";
        restTemplate.postForObject(url, packages, String.class);
    }

    public DirectionsQueryResult getDirections(){
        return restTemplate.getForObject(baseURL, DirectionsQueryResult.class);
    }

    public void updatePackages(ArrayList<Package> packages) {
        String url = baseURL + "updatePackages";
        restTemplate.postForObject(url, packages, String.class);
    }
}
