package application.dak.DAK.views.main;

import application.dak.DAK.views.MainLayout;
import application.dak.DAK.views.login.LoginView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinService;

import static application.dak.DAK.views.login.LoginView.routes;

@PageTitle("Menu")
@CssImport("./main.css")
public class MainView extends VerticalLayout {

    public MainView() {
        Text text = new Text(VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("mail").toString());
        add(text);
    }
}
