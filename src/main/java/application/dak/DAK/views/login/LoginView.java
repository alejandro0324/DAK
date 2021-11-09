package application.dak.DAK.views.login;

import application.dak.DAK.views.MainLayout;
import application.dak.DAK.views.clients.ClientsView;
import application.dak.DAK.views.configuration.ConfigurationView;
import application.dak.DAK.views.dashboard.DashboardView;
import application.dak.DAK.views.expedition.ExpeditionView;
import application.dak.DAK.views.main.MainView;
import application.dak.DAK.views.packages.PackagesView;
import application.dak.DAK.views.zones.ZonesView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport("./lumo.css")
@Theme(value = Lumo.class)
@JsModule("./login.js")
@JsModule("https://www.gstatic.com/firebasejs/ui/4.8.1/firebase-ui-auth.js")
@JsModule("https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js")
@PageTitle("Login")
@Route(" ")
public class LoginView extends VerticalLayout {

    Button loginButton = new Button("Login");
    EmailField mail = new EmailField("Mail");
    PasswordField password = new PasswordField("Password");

    public LoginView() {
        routeStarter();
        UI.getCurrent().getPage().executeJs("ns.initApp()");
        mail.focus();
        mail.setRequiredIndicatorVisible(true);
        password.setRequired(true);
        add(new H1("Welcome! DAK©"), mail, password, loginButton);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        loginButton.addClickListener(buttonClickEvent -> UI.getCurrent().getPage()
                .executeJs("lg.login($0, $1, $2)", mail.getValue(), password.getValue(), this));
    }

    @ClientCallable
    public void loginOk(String mail){
        VaadinService.getCurrentRequest().getWrappedSession()
                .setAttribute("mail", mail);
        routeSetter();
        UI.getCurrent().navigate("Menu");
    }

    @ClientCallable
    public void loginError(){
        password.setInvalid(true);
        mail.setInvalid(true);
    }

    public void routeStarter(){
        routes = RouteConfiguration.forSessionScope();
        routes.removeRoute("Menu");
        routes.removeRoute("Clients");
        routes.removeRoute("Configuration");
        routes.removeRoute("Dashboard");
        routes.removeRoute("Expedition");
        routes.removeRoute("Packages");
        routes.removeRoute("Zones");
    }

    public void routeSetter(){
        routes.setRoute("Menu", MainView.class, MainLayout.class);
        routes.setRoute("Clients", ClientsView.class, MainLayout.class);
        routes.setRoute("Configuration", ConfigurationView.class, MainLayout.class);
        routes.setRoute("Dashboard", DashboardView.class, MainLayout.class);
        routes.setRoute("Expedition", ExpeditionView.class, MainLayout.class);
        routes.setRoute("Packages", PackagesView.class, MainLayout.class);
        routes.setRoute("Zones", ZonesView.class, MainLayout.class);
    }

    public static RouteConfiguration routes;
}
