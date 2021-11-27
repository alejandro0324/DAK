package application.dak.DAK.views.dashboard;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.PackageState;
import application.dak.DAK.backend.dashboard.services.DashboardClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;

import java.util.HashMap;
import java.util.List;

@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    private final DashboardClient dashboardClient;
    Grid<Package> grid = new Grid<>();
    TextField money = new TextField();
    Chart chart = new Chart(ChartType.PIE);

    public DashboardView(){
        dashboardClient = new DashboardClient();
        checkTravelProblems();
        ObjectMapper mapper = new ObjectMapper();
        List<Package> list = mapper.convertValue(dashboardClient.listDashboardPackages(), new TypeReference<List<Package>>(){});
        grid.setItems(list);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Package::getNumber).setHeader("Number");
        grid.addColumn(Package::getState).setHeader("State");
        grid.addColumn(Package::getStartDate).setHeader("Start Date");
        grid.addColumn(Package::getFinishDate).setHeader("Finish Date");
        grid.addColumn(Package::getExtraInfo).setHeader("Extra info.");
        Configuration conf = chart.getConfiguration();
        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize("0");
        options.setSize("75%");
        options.setCenter("50%", "50%");
        conf.setPlotOptions(options);
        DataSeries series = new DataSeries();
        Integer inLocal = 0;
        Integer inTravel = 0;
        Integer inTravelProblem = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState() == PackageState.IN_LOCAL) {
                inLocal++;
            }
            if (list.get(i).getState() == PackageState.IN_TRAVEL) {
                inTravel++;
            }
            if (list.get(i).getState() == PackageState.IN_TRAVEL_PROBLEM) {
                inTravelProblem++;
            }
        }
        series.add(new DataSeriesItem("IN_LOCAL", inLocal));
        series.add(new DataSeriesItem("IN_TRAVEL", inTravel));
        series.add(new DataSeriesItem("IN_TRAVEL_PROBLEM", inTravelProblem));
        conf.addSeries(series);
        String aux = getDailyIncome();
        if (aux != null) {
            money.setValue(aux);
        } else {
            money.setValue("Nothing to show");
        }
        money.setReadOnly(true);
        add(new H2("Daily income:"),
                money,
                new H2("Packages by state:"),
                grid,
                chart,
                new H4("IN_LOCAL: " + inLocal),
                new H4("IN_TRAVEL: " + inTravel),
                new H4("IN_TRAVEL_PROBLEM: " + inTravelProblem)
                );
    }

    public String getDailyIncome() {
        return dashboardClient.getDailyIncome();
    }

    public void checkTravelProblems() {
        dashboardClient.checkTravelProblems();
    }
}
