package application.dak.DAK.backend.common.dto;

import com.flowingcode.vaadin.addons.googlemaps.LatLon;

import java.util.ArrayList;

public class Zone {
    Integer id;
    String name;
    String colour;
    ArrayList<LatLon> coordinates;
}
