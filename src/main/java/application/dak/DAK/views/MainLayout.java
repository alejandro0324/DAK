package application.dak.DAK.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.PWA;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "My App", shortName = "My App", enableInstallPrompt = false)
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("DAK");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tab Menu = new Tab("Menu");
        Tab Orders = new Tab("Packages");
        Tab Expedition = new Tab("Expedition");
        Tab Zones = new Tab("Zones");
        Tab Clients = new Tab("Clients");
        Tab Dashboard = new Tab("Dashboard");
        Tab Configuration = new Tab("Configuration");
        Tab Logout = new Tab("Logout");
        Tabs tabs = new Tabs(Menu, Orders, Expedition, Zones, Clients, Dashboard, Configuration, Logout);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setAutoselect(false);

        addToDrawer(tabs);
        addToNavbar(toggle, title);

        tabs.addSelectedChangeListener(event -> navigator(tabs.getSelectedTab()));
    }

    public void navigator(Tab tabSelected) {
        switch (tabSelected.getLabel()) {
            case "Menu":
                UI.getCurrent().navigate("Menu");
                break;
            case "Packages":
                UI.getCurrent().navigate("Packages");
                break;
            case "Expedition":
                UI.getCurrent().navigate("Expedition");
                break;
            case "Zones":
                UI.getCurrent().navigate("Zones");
                break;
            case "Clients":
                UI.getCurrent().navigate("Clients");
                break;
            case "Dashboard":
                UI.getCurrent().navigate("Dashboard");
                break;
            case "Configuration":
                UI.getCurrent().navigate("Configuration");
                break;
            case "Logout":
                UI.getCurrent().navigate(" ");
                break;
        }
    }
}
