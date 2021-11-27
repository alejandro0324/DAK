package application.dak.DAK.views.expedition;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.expeditions.components.ExpeditionClient;
import application.dak.DAK.backend.packages.services.PackagesClient;
import application.dak.DAK.firebase.FirestoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowingcode.vaadin.addons.googlemaps.*;
import com.google.maps.model.LatLng;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;

import java.util.*;

import static application.dak.DAK.backend.utils.Constants.*;

@PageTitle("Expedition")
public class ExpeditionView extends VerticalLayout {

    private static final String TAG = "ExpeditionView";
    private final PackagesClient packagesClient;
    private final ExpeditionClient expeditionClient;

    Grid<Package> expeditionGrid = new Grid<>();
    TextField expeditionFilter = new TextField();
    private static Set<Package> selectedPackages;
    VerticalLayout expeditionGridDiv = new VerticalLayout();
    VerticalLayout routeCalculationDiv = new VerticalLayout();
    GoogleMap map;

    public ExpeditionView() {
        routeCalculationDiv.setVisible(false);
        packagesClient = new PackagesClient();
        expeditionClient = new ExpeditionClient();
        selectedPackages = new HashSet<>();
        configureExpeditionsDiv();
        add(expeditionGridDiv, routeCalculationDiv);
    }

    private void configureRouteCalculationDiv() {
        configureGoogleMaps();
        H2 header = new H2("Calculate the best route for the packages selected");
        H4 packagesSelected = new H4("Packages selected: " + selectedPackages.size());
        HorizontalLayout actions = new HorizontalLayout();
        Button backToExpeditions = new Button("Back to the expeditions grid", new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));
        backToExpeditions.addClickListener(event -> goBack());
        Button calculateRoutes = new Button("Calculate routes", new Icon(VaadinIcon.ROAD));
        calculateRoutes.addClickListener(event -> {
            getDirections();
            FirestoreService.getInstance().log(TAG, "Route successfully displayed");
        });
        TextField carID = new TextField("Car ID");
        carID.setPlaceholder("Car ID");
        carID.setRequired(true);
        carID.setRequiredIndicatorVisible(true);
        Button shipPackages = new Button("Ship packages", new Icon(VaadinIcon.CAR));
        shipPackages.addClickListener(event -> updatePackages(carID.getValue()));
        actions.add(backToExpeditions, calculateRoutes, carID, shipPackages);
        actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
        routeCalculationDiv.add(header, packagesSelected, actions, map);
    }

    private void goBack() {
        selectedPackages = new HashSet<>();
        /*toggleDivs();*/
        expeditionGridDiv.setVisible(true);
        routeCalculationDiv.setVisible(false);
        expeditionGrid.setEnabled(true);
        expeditionGrid.deselectAll();
    }

    private void updatePackages(String carID) {
        selectedPackages.forEach(i -> i.setCarID(carID));
        expeditionClient.updatePackages(new ArrayList<>(selectedPackages));
        Notification.show("Packages have been sent to be delivered", 3000, Notification.Position.MIDDLE);
        goBack();
    }

    private void getDirections() {
        List<LatLng> decodedPath = expeditionClient.getDirections().getDecodedPath();
        if (decodedPath != null) {
            for (int i = 0; i < decodedPath.size() - 1; i++) {
                LatLng currentLatLng = decodedPath.get(i);
                if (i + 1 < decodedPath.size()) {
                    map.addPolygon(createPolyline(currentLatLng, decodedPath.get(i + 1)));
                }
            }
        }else{
            Notification.show("Directions cannot be fetched due to an unknown problem", 3000, Notification.Position.MIDDLE);
        }
    }

    public GoogleMapPolygon createPolyline(LatLng from, LatLng to) {
        GoogleMapPolygon polyline = new GoogleMapPolygon(Arrays.asList(
                new GoogleMapPoint(new LatLon(from.lat, from.lng)),
                new GoogleMapPoint(new LatLon(to.lat, to.lng))));
        polyline.setClosed(false);
        polyline.setStrokeColor(APP_COLOUR);
        return polyline;
    }

    private void configureGoogleMaps() {
        String apiKey = System.getProperty("google.maps.api");
        map = new GoogleMap(apiKey, null, null);
        map.setMapType(GoogleMap.MapType.ROADMAP);
        LatLon currentPosition = new LatLon(CURRENT_LAT, CURRENT_LNG);
        map.setCenter(currentPosition);
        map.setSizeFull();
        map.setWidthFull();
        map.setHeight(400, Unit.PIXELS);
        map.setZoom(11);
        markCurrentPosition(currentPosition);
        addMarkers();
    }

    private void markCurrentPosition(LatLon currentPosition) {
        GoogleMapMarker marker = new GoogleMapMarker("",
                currentPosition,
                false,
                "http://maps.gstatic.com/mapfiles/ridefinder-images/mm_20_blue.png");
        marker.addInfoWindow("Current position");
        marker.setInfoWindowVisible(true);
        map.addMarker(marker);
    }

    private void addMarkers() {
        selectedPackages.forEach(i -> {
            GoogleMapMarker marker = new GoogleMapMarker("", new LatLon(i.getLat(), i.getLng()), false);
            marker.addInfoWindow("<h4> PACKAGE INFORMATION </h4> " +
                    "<h5>Date:" + i.getStartDate() + "</h5>" +
                    "<h5>Price:" + i.getPrice() + "</h5>" +
                    "<h5>Address:" + i.getAddress() + "</h5>" +
                    "<h5> Weight (KM): " + i.getWeight() + " </h5>");
            map.addMarker(marker);
        });
    }

    private void configureExpeditionsDiv() {
        expeditionFilter.setPlaceholder("Filter by number...");
        ObjectMapper mapper = new ObjectMapper();
        List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackages(), new TypeReference<List<Package>>() {
        });
        setExpeditionsGrid(list);
        Button issueButton = new Button("Calculate routes");
        issueButton.addClickListener(e -> issuePackages());
        setListeners();
        expeditionGridDiv.add(new H2("List of all ready packages, check a maximum of " + MAX_WAYPOINTS + " packages to calculate the route"), issueButton, expeditionFilter, expeditionGrid);
    }

    private void setListeners() {
        expeditionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        expeditionFilter.addValueChangeListener(e -> updateExpeditionList());
        expeditionGrid.asMultiSelect().addValueChangeListener(this::valueChanged);
    }

    private void valueChanged(AbstractField.ComponentValueChangeEvent<Grid<Package>, Set<Package>> event) {
        if (selectedPackages.size() < MAX_WAYPOINTS) {
            selectedPackages = event.getValue();
        }

        if (selectedPackages.size() >= MAX_WAYPOINTS) {
            notifyLimitReached();
        }
    }

    private void notifyLimitReached() {
        String notification = "The limit of " + MAX_WAYPOINTS + " packages has been reached";
        Notification.show(notification, 3000, Notification.Position.MIDDLE);
        expeditionGrid.setEnabled(false);
    }

    private void setExpeditionsGrid(List<Package> list) {
        expeditionGrid.setItems(list);
        expeditionGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        expeditionGrid.addColumn(Package::getNumber).setHeader("Number");
        expeditionGrid.addColumn(Package::getState).setHeader("State");
        expeditionGrid.addColumn(Package::getStartDate).setHeader("Start Date");
        expeditionGrid.addColumn(Package::getFinishDate).setHeader("Finish Date");
        expeditionGrid.addColumn(Package::getExtraInfo).setHeader("Extra info.");
    }

    public void updateExpeditionList() {
        if (expeditionFilter.getValue().equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackages(), new TypeReference<List<Package>>() {
            });
            expeditionGrid.setItems(list);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackagesLike(expeditionFilter.getValue()), new TypeReference<List<Package>>() {
            });
            expeditionGrid.setItems(list);
        }
    }

    private void toggleDivs() {
        expeditionGridDiv.setVisible(!expeditionGrid.isVisible());
        routeCalculationDiv.setVisible(!routeCalculationDiv.isVisible());
    }

    public void issuePackages() {
        expeditionClient.calculateDirections(new ArrayList<>(selectedPackages));
        configureRouteCalculationDiv();
        toggleDivs();
    }
}
