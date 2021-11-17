package application.dak.DAK.backend.generalConfiguration.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConfigurationClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public ConfigurationClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/configuration/";
    }

    public Integer getTripTax() {
        final String url = baseURL + "getTripTax";
        return restTemplate.getForObject(url, Integer.class);
    }

    public void configSave(Integer tripTax) {
        final String url = baseURL + "configSave/" + tripTax;
        restTemplate.postForObject(url, tripTax, void.class);
    }
}
