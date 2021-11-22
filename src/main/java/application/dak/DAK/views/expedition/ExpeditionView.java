package application.dak.DAK.views.expedition;

import application.dak.DAK.backend.client.services.ClientClient;
import application.dak.DAK.backend.common.models.Company;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.Person;
import application.dak.DAK.backend.packages.services.PackagesClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Sets;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PageTitle("Expedition")
public class ExpeditionView extends VerticalLayout {

    Grid<Package> expeditionGrid = new Grid<>();
    TextField expeditionFilter = new TextField();
    private final PackagesClient packagesClient;
    Set<Package> selectedPackages = new HashSet<>();

    public ExpeditionView(){
        packagesClient = new PackagesClient();
        VerticalLayout expeditionGridDiv = new VerticalLayout();
        expeditionFilter.setPlaceholder("Filter by number...");
        ObjectMapper mapper = new ObjectMapper();
        List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackages(), new TypeReference<List<Package>>(){});
        expeditionGrid.setItems(list);
        expeditionGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        expeditionGrid.addColumn(Package::getNumber).setHeader("Number");
        expeditionGrid.addColumn(Package::getState).setHeader("State");
        expeditionGrid.addColumn(Package::getStartDate).setHeader("Start Date");
        expeditionGrid.addColumn(Package::getFinishDate).setHeader("Finish Date");
        expeditionGrid.addColumn(Package::getExtraInfo).setHeader("Extra info.");
        Button issueButton = new Button("Issue");
        expeditionGridDiv.add(new H2("List of all ready packages, check to issue them"), expeditionFilter, expeditionGrid);
        add(expeditionGridDiv, issueButton);
        expeditionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        expeditionFilter.addValueChangeListener(e -> updateExpeditionList());
        issueButton.addClickListener(e -> issuePackages());
        expeditionGrid.asMultiSelect().addValueChangeListener(event -> {
            selectedPackages = event.getValue();
        });
    }

    public void updateExpeditionList() {
        if (expeditionFilter.getValue().equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackages(), new TypeReference<List<Package>>(){});
            expeditionGrid.setItems(list);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.listAllReadyPackagesLike(expeditionFilter.getValue()), new TypeReference<List<Package>>(){});
            expeditionGrid.setItems(list);
        }
    }

    public void issuePackages() {

    }
}
