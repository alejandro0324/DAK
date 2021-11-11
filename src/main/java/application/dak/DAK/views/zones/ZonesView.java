package application.dak.DAK.views.zones;

import application.dak.DAK.backend.common.dto.Zone;
import application.dak.DAK.backend.zones.services.ZoneService;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.github.juchar.colorpicker.ColorPickerFieldRaw;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Zones")
@Slf4j
public class ZonesView extends VerticalLayout {

    private GoogleMap map;
    private FormLayout formLayout;
    private ArrayList<GoogleMapMarker> markers;
    private final ZoneService zoneService;
    private final HorizontalLayout container;
    private Binder<Zone> binder;

    public ZonesView() {
        zoneService = ZoneService.getInstance();
        H2 title = new H2("Zones");
        setHorizontalComponentAlignment(Alignment.CENTER, title);
        add(title);
        configureGoogleMaps();
        createForm();
        container = new HorizontalLayout();
        setHorizontalLayout(container);
        Button openForm = new Button("New zone", event -> container.add(formLayout));
        add(openForm, container);
    }

    private void setHorizontalLayout(HorizontalLayout horizontalLayout) {
        horizontalLayout.setSizeFull();
        horizontalLayout.add(map);
    }

    private void createForm() {
        instantiateFormVariables();
        Zone zoneBeingEdited = new Zone();
        formLayout.setWidth(450, Unit.PIXELS);
        HorizontalLayout configurationButtons = new HorizontalLayout();
        setButtons(configurationButtons);
        formLayout.add(configurationButtons);

        TextField name = new TextField();
        setNameTextField(name);

        ColorPickerFieldRaw colorPickerField = new ColorPickerFieldRaw();
        setColourPicker(colorPickerField);

        Label infoLabel = new Label("Do not forget to specify the coordinates of the zone");

        Button save = new Button("Save");
        Button reset = new Button("Reset");
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");
        setBindingForName(name);
        setBindingForColour(colorPickerField);
        save.addClickListener(event -> validateForm(zoneBeingEdited));

        reset.addClickListener(event -> binder.readBean(null));

        formLayout.add(name);
        formLayout.addFormItem(colorPickerField, new Label("Select a colour"));
        formLayout.add(infoLabel, actions);
    }

    private void instantiateFormVariables() {
        binder = new Binder<>();
        markers = new ArrayList<>();
        formLayout = new FormLayout();
        map.addRightClickListener(event -> addMarkers(new LatLon(event.getLatitude(), event.getLongitude())));
    }

    private void validateForm(Zone zoneBeingEdited) {
        if (isValid(zoneBeingEdited)) {
            log.info("name of zone: {} ", zoneBeingEdited.getName());
            log.info("colour of zone: {}", zoneBeingEdited.getColour());
        } else {
            showErrors();
        }
    }

    private boolean isValid(Zone zoneBeingEdited) {
        return binder.writeBeanIfValid(zoneBeingEdited) && markers.size() > 0;
    }

    private void setButtons(HorizontalLayout layout) {
        Button clearMap = new Button("Clear map", event -> removeMarkers());
        clearMap.setIcon(new Icon(VaadinIcon.MAP_MARKER));
        Button collapseForm = new Button("Collapse", e -> container.remove(formLayout));
        collapseForm.setIcon(new Icon(VaadinIcon.COMPRESS_SQUARE));

        layout.add(clearMap, collapseForm);
    }

    private void setColourPicker(ColorPickerFieldRaw colorPickerField) {
        colorPickerField.setRequiredIndicatorVisible(true);
        colorPickerField.setHexEnabled(true);
        colorPickerField.setHslEnabled(false);
        colorPickerField.setRgbEnabled(false);
        colorPickerField.setValue("ff8800");
    }

    private void setNameTextField(TextField name) {
        name.setPlaceholder("Name");
        name.setLabel("Name");
        name.setRequiredIndicatorVisible(true);
        name.setRequired(true);
    }

    private void setBindingForName(TextField name) {
        SerializablePredicate<String> namePredicate = value -> !name.getValue().trim().isEmpty();
        Binder.Binding<Zone, String> nameBinding = binder.forField(name)
                .withNullRepresentation("")
                .withValidator(namePredicate,
                        "Please specify the name of the zone")
                .bind(Zone::getName, Zone::setName);

        name.addValueChangeListener(event -> nameBinding.validate());
    }

    private void setBindingForColour(ColorPickerFieldRaw colour) {
        SerializablePredicate<String> colourPredicate = value -> colour.getTextField().getValue() != null && !colour.getValue().trim().isEmpty();
        Binder.Binding<Zone, String> colourBinding = binder.forField(colour)
                .withNullRepresentation("ff8800")
                .withValidator(colourPredicate,
                        "Please specify the colour of the zone")
                .bind(Zone::getColour, Zone::setColour);

        colour.addValueChangeListener(event -> colourBinding.validate());
    }

    private void showErrors() {
        BinderValidationStatus<Zone> validate = binder.validate();
        String errorText = validate.getFieldValidationStatuses()
                .stream().filter(BindingValidationStatus::isError)
                .map(BindingValidationStatus::getMessage)
                .map(Optional::get).distinct()
                .collect(Collectors.joining(", "));
        Notification.show(errorText);
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

    }

    private void addMarkers(LatLon latLon) {
        GoogleMapMarker marker = new GoogleMapMarker("Coordinate", latLon, false);
        markers.add(marker);
        map.addMarker(marker);
    }

    private void removeMarkers() {
        for (GoogleMapMarker marker : markers) {
            getUI().ifPresent(ui -> ui.access(() -> map.removeMarker(marker)));
        }
        markers = new ArrayList<>();
    }
}
