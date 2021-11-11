package application.dak.DAK.views.zones;

import application.dak.DAK.backend.zones.services.ZoneService;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.github.juchar.colorpicker.ColorPickerField;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;

import java.awt.*;
import java.util.ArrayList;

@PageTitle("Zones")
public class ZonesView extends VerticalLayout {

    private GoogleMap map;
    private FormLayout formLayout;
    private ArrayList<GoogleMapMarker> markers;
    private ZoneService zoneService;

    public ZonesView() {
        zoneService = ZoneService.getInstance();
        H2 title = new H2("Zones");
        setHorizontalComponentAlignment(Alignment.CENTER, title);
        add(title);
        configureGoogleMaps();
        createForm();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        setHorizontalLayout(horizontalLayout);
        horizontalLayout.add(map);
        Button openForm = new Button("New zone", event -> horizontalLayout.add(formLayout));
        add(openForm, horizontalLayout);
        if(zoneService.addZone()){
            Notification.show("Congratulations!");
        }
    }

    private void setHorizontalLayout(HorizontalLayout horizontalLayout) {
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(true);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
    }

    private void createForm() {
        markers = new ArrayList<>();
        formLayout = new FormLayout();
        formLayout.setWidth(450, Unit.PIXELS);
        Button clearMap = new Button("Clear map", event -> removeMarkers());
        Icon clearMapIcon = new Icon(VaadinIcon.MAP_MARKER);
        clearMapIcon.setColor("ff8800");
        clearMap.setIcon(clearMapIcon);
        clearMap.setWidth("50%");
        formLayout.add(clearMap);
        TextField name = new TextField();
        name.setPlaceholder("Name");
        name.setLabel("Name");
        name.setRequiredIndicatorVisible(true);
        name.setRequired(true);

        Color colour = Color.BLACK;
        ColorPickerField colorPickerField = new ColorPickerField("Select colour", colour, "Set colour");
        setColourPicker(colorPickerField);

        formLayout.add(name, colorPickerField);
    }

    private void setColourPicker(ColorPickerField colorPickerField) {
        colorPickerField.setRgbEnabled(false);
        colorPickerField.setHslEnabled(false);
        colorPickerField.setHexEnabled(true);
        colorPickerField.setPalette(Color.RED, Color.GREEN, Color.BLUE);
        colorPickerField.setRequiredIndicatorVisible(true);
        colorPickerField.addValueChangeListener(event ->
                colorPickerField.getTextField().setValue(colorPickerField.getValue()));
        colorPickerField.setChangeFormatButtonVisible(true);
        colorPickerField.getTextField().setPlaceholder("Hello World");
    }

    private void removeMarkers() {
        for(GoogleMapMarker marker : markers){
            getUI().ifPresent(ui -> ui.access(() -> map.removeMarker(marker)));
        }
    }

    private void configureGoogleMaps() {
        String apiKey = System.getProperty("google.maps.api");
        map = new GoogleMap(apiKey, null, null);
        map.setMapType(GoogleMap.MapType.ROADMAP);
        map.goToCurrentLocation();
        map.setSizeFull();
        map.setWidthFull();
        map.setHeight(400, Unit.PIXELS);
        map.setZoom(11);
        map.addRightClickListener(event -> markers.add(new GoogleMapMarker("Coordinate",
                new LatLon(event.getLatitude(),
                event.getLongitude()), false)));
    }

}
