package application.dak.DAK.views.configuration;

import application.dak.DAK.backend.common.models.User;
import application.dak.DAK.backend.common.parser.UserParser;
import application.dak.DAK.backend.generalConfiguration.services.ConfigurationClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.PageTitle;
import application.dak.DAK.firebase.FirestoreService;

import java.util.List;

@PageTitle("Configuration")
@JsModule("./login.js")
@JsModule("https://www.gstatic.com/firebasejs/ui/4.8.1/firebase-ui-auth.js")
@JsModule("https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js")
public class ConfigurationView extends VerticalLayout {
    private final String TAG = "ConfigurationView";
    private final ConfigurationClient configurationClient;

    VerticalLayout generalConfigurationDiv = new VerticalLayout();
    VerticalLayout newUserDiv = new VerticalLayout();
    VerticalLayout usersListDiv = new VerticalLayout();
    EmailField mail = new EmailField("New mail");
    PasswordField password = new PasswordField("New password");
    Button newUserButton = new Button("Create");
    IntegerField generalTripTax = new IntegerField();
    UserParser userParser = new UserParser();
    Grid<User> usersGrid = new Grid<>();

    public ConfigurationView(){
        FirestoreService.setFirestore();
        configurationClient = new ConfigurationClient();
        try {
        Tab usersList = new Tab("Users list");
        Tab newUser = new Tab("New user");
        Tab generalConfiguration = new Tab("General configuration");
        Tabs tabs = new Tabs(usersList, newUser, generalConfiguration);

        ListUsersPage listUsersPage = FirebaseAuth.getInstance().listUsers(null);
        List<User> listUsers = userParser.userParse(listUsersPage);
        usersGrid.setItems(listUsers);
        usersGrid.addColumn(User::getMail).setHeader("Mail");
        usersGrid.addComponentColumn(this::removeButton).setHeader("Remove");
        usersListDiv.setAlignItems(Alignment.CENTER);
        usersListDiv.add(usersGrid);
        usersListDiv.setVisible(true);

        password.setMinLength(6);
        newUserDiv.add(new H1("Create a new user:"), mail, password, newUserButton);
        newUserDiv.setVisible(false);
        newUserDiv.setAlignItems(Alignment.CENTER);

        Button generalConfigurationButton = new Button("Save changes");
        generalTripTax.setLabel("General Trip Tax (Currently: " + configurationClient.getTripTax() + ")");
        generalTripTax.setWidth(30f, Unit.EM);
        generalConfigurationDiv.add(generalTripTax, generalConfigurationButton);
        generalConfigurationDiv.setVisible(false);
        generalConfigurationDiv.setAlignItems(Alignment.CENTER);

        add(tabs, usersListDiv, newUserDiv, generalConfigurationDiv);
        tabs.addSelectedChangeListener(selectedChangeEvent -> navigator(tabs.getSelectedTab().getLabel()));
        newUserButton.addClickListener(buttonClickEvent -> newUser(mail.getValue(), password.getValue()));
        generalConfigurationButton.addClickListener(buttonClickEvent -> configSave(generalTripTax.getValue()));
        } catch (FirebaseAuthException ex) {
            System.out.println(ex.getAuthErrorCode());
        }
    }

    public void navigator(String label) {
        switch (label) {
            case "Users list":
                newUserDiv.setVisible(false);
                generalConfigurationDiv.setVisible(false);
                usersListDiv.setVisible(true);
                break;
            case "New user":
                newUserDiv.setVisible(true);
                generalConfigurationDiv.setVisible(false);
                usersListDiv.setVisible(false);
                break;
            case "General configuration":
                newUserDiv.setVisible(false);
                generalConfigurationDiv.setVisible(true);
                usersListDiv.setVisible(false);
                break;
        }
    }

    public Button removeButton(User user) {
        return new Button("Remove", clickEvent -> {
            try {
                FirebaseAuth.getInstance().deleteUser(user.getUUId());
                ListUsersPage listUsersPage = FirebaseAuth.getInstance().listUsers(null);
                List<User> listUsers = userParser.userParse(listUsersPage);
                usersGrid.setItems(listUsers);
                FirestoreService.getInstance().log(TAG, "User with uid of: " + user.getUUId() + " was successfully deleted");
            } catch (FirebaseAuthException ex) {
                System.out.println(ex.getAuthErrorCode());
            }
        });
    }

    public void newUser(String mail, String password){
            UI.getCurrent().getPage().executeJs("ns.initApp()");
            UI.getCurrent().getPage()
                    .executeJs("cr.create($0, $1, $2)", mail, password, this);
    }

    @ClientCallable
    public void createOk() {
        try {
            Notification.show("User correctly created");
            mail.setValue("");
            password.setValue("");
            mail.setInvalid(false);
            password.setInvalid(false);
            ListUsersPage listUsersPage = FirebaseAuth.getInstance().listUsers(null);
            List<User> listUsers = userParser.userParse(listUsersPage);
            usersGrid.setItems(listUsers);
            FirestoreService.getInstance().log(TAG, "User correctly created");
        }catch (FirebaseAuthException ex) {
            ex.getAuthErrorCode();
        }
    }

    @ClientCallable
    public void createFail() {
        Notification.show("User not created");
        mail.isInvalid();
        password.isInvalid();
    }

    public void configSave(Integer TripTax) {
        configurationClient.configSave(TripTax);
        generalTripTax.setLabel("General Trip Tax (Currently: " + configurationClient.getTripTax() + ")");
        Notification.show("Save completed");
        FirestoreService.getInstance().log(TAG, "Trip tax successfully updated");
    }
}
