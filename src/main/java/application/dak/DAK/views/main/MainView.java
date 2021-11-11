package application.dak.DAK.views.main;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinService;

@PageTitle("Menu")
public class MainView extends VerticalLayout {

    public MainView() {
        Text text = new Text(VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("mail").toString());
        add(text);
    }
}
