package application.dak.DAK.backend.common.dto;

import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Zone {
    Integer id;
    String name;
    ArrayList<LatLon> coordinates;
}
