package application.dak.DAK.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        Menu.add(new Icon(VaadinIcon.ELLIPSIS_DOTS_H));
        Tab Orders = new Tab("Packages");
        Orders.add(new Icon(VaadinIcon.PACKAGE));
        Tab Expedition = new Tab("Expedition");
        Expedition.add(new Icon(VaadinIcon.PAPERPLANE));
        Tab Zones = new Tab("Zones");
        Zones.add(new Icon(VaadinIcon.MAP_MARKER));
        Tab Clients = new Tab("Clients");
        Clients.add(new Icon(VaadinIcon.GROUP));
        Tab Dashboard = new Tab("Dashboard");
        Dashboard.add(new Icon(VaadinIcon.TRENDING_UP));
        Tab Configuration = new Tab("Configuration");
        Configuration.add(new Icon(VaadinIcon.SCREWDRIVER));
        Tab Logout = new Tab("Logout");
        Logout.add(new Icon(VaadinIcon.EXIT_O));
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
