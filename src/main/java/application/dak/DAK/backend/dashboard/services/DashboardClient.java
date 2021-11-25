package application.dak.DAK.backend.dashboard.services;

import application.dak.DAK.backend.common.models.Package;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class DashboardClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public DashboardClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/dashboard/";
    }

    public String getDailyIncome() {
        final String url = baseURL + "getDailyIncome";
        return restTemplate.getForObject(url, String.class);
    }

    public List<Package> listDashboardPackages() {
        final String url = baseURL + "listDashboardPackages";
        List<Package> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }
}
