package application.dak.DAK.backend.zones.components;

import application.dak.DAK.backend.common.dto.ListOfZones;
import application.dak.DAK.backend.common.dto.Zone;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ZonesClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public ZonesClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/zones/";
    }

    public String addZone(final Zone zone) {
        final String url = baseURL + "add";
        return restTemplate.postForObject(url, zone, String.class);
    }

    public List<Zone> getZones() {
        final String url = baseURL + "getZones";
        return restTemplate.getForObject(url, ListOfZones.class).getZones();
    }

    public void removeZone(String uuid) {
        final String url = baseURL + uuid;
        restTemplate.delete(url);
    }
}
