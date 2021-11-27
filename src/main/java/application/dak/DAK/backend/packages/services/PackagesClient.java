package application.dak.DAK.backend.packages.services;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.Person;
import application.dak.DAK.backend.common.models.Tracking;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class PackagesClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public PackagesClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/packages/";
    }

    public List<Package> getAllPackages() {
        final String url = baseURL + "getAllPackages";
        List<Package> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public List<Package> getAllPackagesLike(String number) {
        final String url = baseURL + "getAllPackagesLike/" + number;
        List<Package> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public Integer createPackage(Package pack) {
        final String url = baseURL + "createPackage";
        return restTemplate.postForObject(url, pack, Integer.class);
    }

    public Tracking createTracking() {
        final String url = baseURL + "createTracking";
        return restTemplate.postForObject(url, null, Tracking.class);
    }

    public List<Package> listAllReadyPackages() {
        final String url = baseURL + "listAllReadyPackages";
        List<Package> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public List<Package> listAllReadyPackagesLike(String number) {
        final String url = baseURL + "listAllReadyPackagesLike/" + number;
        List<Package> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }
}
