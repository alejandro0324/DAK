package application.dak.DAK.views.main;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinService;

@PageTitle("Menu")
@CssImport("./main.css")
public class MainView extends VerticalLayout {

    public MainView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        Image image = new Image("newuser.png", "Logo");
        add(image);
    }
}
