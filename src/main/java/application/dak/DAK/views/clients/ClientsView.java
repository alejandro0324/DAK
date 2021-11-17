package application.dak.DAK.views.clients;

import application.dak.DAK.backend.client.services.ClientClient;
import application.dak.DAK.backend.common.models.*;
import application.dak.DAK.backend.generalConfiguration.services.ConfigurationClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Clients")
public class ClientsView extends VerticalLayout {

    private final ClientClient clientClient;
    VerticalLayout newClientDiv = new VerticalLayout();
    VerticalLayout clientsListDiv = new VerticalLayout();
    VerticalLayout personDiv = new VerticalLayout();
    VerticalLayout companyDiv = new VerticalLayout();
    Grid<Person> personsGrid = new Grid<>();
    Grid<Company> companiesGrid = new Grid<>();
    Tab newClient = new Tab("Client master");
    Tab clientsList = new Tab("Clients list");
    Tabs tabs = new Tabs();
    Select<String> select = new Select<>();
    TextField personName = new TextField("Name");
    TextField personSurname = new TextField("Surname");
    TextField personDNI = new TextField("DNI");
    TextField RUT = new TextField("RUT");
    TextField businessName = new TextField("Business name");
    TextField personEmail = new TextField("Email");
    TextField personTelephone = new TextField("Telephone");
    TextField personDirection = new TextField("Direction");
    TextField companyEmail = new TextField("Email");
    TextField companyTelephone = new TextField("Telephone");
    TextField companyDirection = new TextField("Direction");
    TextField personFilter = new TextField();
    TextField companyFilter = new TextField();

    public ClientsView() {
        clientClient = new ClientClient();
        tabs.add(clientsList, newClient);

        personName.setRequired(true);
        personName.setWidth(30f, Unit.EM);
        personName.setPlaceholder("");
        personSurname.setRequired(true);
        personSurname.setWidth(30f, Unit.EM);
        personSurname.setPlaceholder("");
        personDNI.setRequired(true);
        personDNI.setWidth(30f, Unit.EM);
        personDNI.setPlaceholder("");

        RUT.setRequired(true);
        RUT.setWidth(30f, Unit.EM);
        RUT.setPlaceholder("");
        businessName.setRequired(true);
        businessName.setWidth(30f, Unit.EM);
        businessName.setPlaceholder("");

        personEmail.setRequired(true);
        personEmail.setWidth(30f, Unit.EM);
        personEmail.setPlaceholder("");
        personTelephone.setRequired(true);
        personTelephone.setWidth(30f, Unit.EM);
        personTelephone.setPlaceholder("");
        personDirection.setRequired(true);
        personDirection.setWidth(30f, Unit.EM);
        personDirection.setPlaceholder("");

        companyEmail.setRequired(true);
        companyEmail.setWidth(30f, Unit.EM);
        companyEmail.setPlaceholder("");
        companyTelephone.setRequired(true);
        companyTelephone.setWidth(30f, Unit.EM);
        companyTelephone.setPlaceholder("");
        companyDirection.setRequired(true);
        companyDirection.setWidth(30f, Unit.EM);
        companyDirection.setPlaceholder("");

        Button personCreate = new Button("Continue");
        Button companyCreate = new Button("Continue");

        select.setItems("Person", "Company");
        select.setPlaceholder("Type");
        select.getStyle().set("position", "absolute");
        select.getStyle().set("right", "5px");

        personDiv.add(personName, personSurname, personDNI, personEmail, personTelephone, personDirection, personCreate);
        companyDiv.add(RUT, businessName, companyEmail, companyTelephone, companyDirection, companyCreate);
        personDiv.setAlignItems(Alignment.CENTER);
        companyDiv.setAlignItems(Alignment.CENTER);

        newClientDiv.add(select, personDiv, companyDiv);

        ObjectMapper mapper = new ObjectMapper();
        List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>(){});
        personsGrid.setItems(list);
        personsGrid.addColumn(Person::getClientId).setHeader("Id");
        personsGrid.addColumn(Person::getDNI).setHeader("DNI");
        personsGrid.addColumn(Person::getName).setHeader("Name");
        personsGrid.addColumn(Person::getSurname).setHeader("Surname");
        personsGrid.addComponentColumn(this::removeButtonPerson).setHeader("Remove");
        personsGrid.addComponentColumn(this::editButtonPerson).setHeader("Edit");
        personsGrid.addComponentColumn(this::selectButtonPerson).setHeader("Client group");

        List<Company> list2 = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>(){});
        companiesGrid.setItems(list2);
        companiesGrid.addColumn(Company::getClientId ).setHeader("Id");
        companiesGrid.addColumn(Company::getRUT).setHeader("RUT");
        companiesGrid.addColumn(Company::getBusinessName).setHeader("Business Name");
        companiesGrid.addComponentColumn(this::removeButtonCompany).setHeader("Remove");
        companiesGrid.addComponentColumn(this::editButtonCompanies).setHeader("Edit");
        companiesGrid.addComponentColumn(this::selectButtonCompany).setHeader("Client group");

        personFilter.setPlaceholder("Filter by DNI...");
        personFilter.setClearButtonVisible(true);
        personFilter.setWidth(30f, Unit.EM);
        companyFilter.setPlaceholder("Filter by RUT...");
        companyFilter.setClearButtonVisible(true);
        companyFilter.setWidth(30f, Unit.EM);
        clientsListDiv.add(new H2("Persons list"), personFilter, personsGrid, new H2("Companies list"), companyFilter, companiesGrid);

        add(tabs, newClientDiv, clientsListDiv);
        newClientDiv.setAlignItems(Alignment.CENTER);
        clientsListDiv.setAlignItems(Alignment.CENTER);
        newClientDiv.setVisible(false);
        personDiv.setVisible(false);
        companyDiv.setVisible(false);
        personCreate.addClickListener(i -> addNewPerson(personName.getValue(), personSurname.getValue(), personDNI.getValue(), personEmail.getValue(), personTelephone.getValue(), personDirection.getValue()));
        companyCreate.addClickListener(i -> addNewCompany(RUT.getValue(), businessName.getValue(), companyEmail.getValue(), companyDirection.getValue(), companyTelephone.getValue()));
        select.addValueChangeListener(i -> selectNavigator(i.getValue()));
        tabs.addSelectedChangeListener(selectedChangeEvent -> navigator(tabs.getSelectedTab().getLabel()));
        personFilter.setValueChangeMode(ValueChangeMode.EAGER);
        personFilter.addValueChangeListener(e -> updatePersonList());
        companyFilter.setValueChangeMode(ValueChangeMode.EAGER);
        companyFilter.addValueChangeListener(e -> updateCompaniesList());
    }

    public void navigator(String label) {
        switch (label) {
            case "Client master":
                newClientDiv.setVisible(true);
                clientsListDiv.setVisible(false);
                select.setVisible(true);
                personDiv.setVisible(false);
                companyDiv.setVisible(false);
                select.setValue("");
                break;
            case "Clients list":
                newClientDiv.setVisible(false);
                clientsListDiv.setVisible(true);
                break;
        }
    }

    public void selectNavigator(String selectedOption) {
        switch (selectedOption) {
            case "Person":
                personDiv.setVisible(true);
                companyDiv.setVisible(false);
                personName.setPlaceholder("");
                personSurname.setPlaceholder("");
                personEmail.setPlaceholder("");
                personDirection.setPlaceholder("");
                personDNI.setPlaceholder("");
                personTelephone.setPlaceholder("");
                personName.setValue("");
                personSurname.setValue("");
                personEmail.setValue("");
                personDirection.setValue("");
                personDNI.setValue("");
                personTelephone.setValue("");
                personName.setInvalid(false);
                personSurname.setInvalid(false);
                personEmail.setInvalid(false);
                personDirection.setInvalid(false);
                personDNI.setInvalid(false);
                personTelephone.setInvalid(false);
                personDNI.setReadOnly(false);
                break;
            case "Company":
                personDiv.setVisible(false);
                companyDiv.setVisible(true);
                companyDirection.setPlaceholder("");
                companyEmail.setPlaceholder("");
                companyTelephone.setPlaceholder("");
                businessName.setPlaceholder("");
                RUT.setPlaceholder("");
                companyDirection.setValue("");
                companyEmail.setValue("");
                companyTelephone.setValue("");
                businessName.setValue("");
                RUT.setValue("");
                companyDirection.setInvalid(false);
                companyEmail.setInvalid(false);
                companyTelephone.setInvalid(false);
                businessName.setInvalid(false);
                RUT.setInvalid(false);
                RUT.setReadOnly(false);
                break;
        }
    }

    public Button removeButtonPerson(Person person){
        return new Button("Remove", clickEvent -> {
            clientClient.removePerson(person);
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>(){});
            personsGrid.setItems(list);
        });
    }

    public Button removeButtonCompany(Company company){
        return new Button("Remove", clickEvent -> {
            clientClient.removeCompany(company);
            ObjectMapper mapper = new ObjectMapper();
            List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>(){});
            companiesGrid.setItems(list);
        });
    }

    public Button editButtonPerson(Person person){
        return new Button("Edit", clickEvent -> {
                tabs.setSelectedTab(newClient);
                navigator("Client master");
                select.setVisible(false);
                personDiv.setVisible(true);
                companyDiv.setVisible(false);
                Client clientSelected = clientClient.getClient(person.getClientId());
                personName.setValue(person.getName());
                personName.setPlaceholder(person.getName());
                personSurname.setValue(person.getSurname());
                personSurname.setPlaceholder(person.getSurname());
                personDNI.setValue(person.getDNI());
                personDNI.setReadOnly(true);
                personDirection.setValue(clientSelected.getDirection());
                personDirection.setPlaceholder(clientSelected.getDirection());
                personTelephone.setValue(clientSelected.getTelephone());
                personTelephone.setPlaceholder(clientSelected.getTelephone());
                personEmail.setValue(clientSelected.getEmail());
                personEmail.setPlaceholder(clientSelected.getEmail());
        });
    }

    public Button editButtonCompanies(Company company){
        return new Button("Edit", clickEvent -> {
            tabs.setSelectedTab(newClient);
            navigator("Client master");
            select.setVisible(false);
            companyDiv.setVisible(true);
            personDiv.setVisible(false);
            Client clientSelected = clientClient.getClient(company.getClientId());
            businessName.setValue(company.getBusinessName());
            businessName.setPlaceholder(company.getBusinessName());
            RUT.setValue(company.getRUT());
            RUT.setReadOnly(true);
            companyDirection.setValue(clientSelected.getDirection());
            companyDirection.setPlaceholder(clientSelected.getDirection());
            companyTelephone.setValue(clientSelected.getTelephone());
            companyTelephone.setPlaceholder(clientSelected.getTelephone());
            companyEmail.setValue(clientSelected.getEmail());
            companyEmail.setPlaceholder(clientSelected.getEmail());
        });
    }

    private void addNewPerson(String name, String surname, String dni, String mail, String telephone, String direction) {
        if (clientClient.personExist(dni) == 1) {
            clientClient.editPerson(clientClient.getPersonId(dni), name, surname, dni, mail, telephone, direction);
            Notification.show("Person correctly edited");
        } else {
            Person person = new Person(null, name, surname, dni);
            person.setDirection(direction);
            person.setTelephone(telephone);
            person.setEmail(mail);
            List<String> text = clientClient.addPerson(person);
            for (String s : text) {
                Notification.show(s);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>(){});
        personsGrid.setItems(list);
    }

    private void addNewCompany(String RUT, String businessName, String mail, String direction, String telephone) {
        if (clientClient.companyExist(RUT) == 1) {
            clientClient.editCompany(clientClient.getCompanyId(RUT), RUT, businessName, mail, direction, telephone);
            Notification.show("Company correctly edited");
        } else {
            Company company = new Company(null, RUT, businessName);
            company.setDirection(direction);
            company.setTelephone(telephone);
            company.setEmail(mail);
            List<String> text = clientClient.addCompany(company);
            for (String s : text) {
                Notification.show(s);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>(){});
        companiesGrid.setItems(list);
    }

    public void updatePersonList(){
        if (personFilter.getValue().equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>(){});
            personsGrid.setItems(list);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = mapper.convertValue(clientClient.listAllPersonsLike(personFilter.getValue()), new TypeReference<List<Person>>(){});
            personsGrid.setItems(list);
        }
    }

    public void updateCompaniesList(){
        if (companyFilter.getValue().equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>(){});
            companiesGrid.setItems(list);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            List<Company> list = mapper.convertValue(clientClient.listAllCompaniesLike(companyFilter.getValue()), new TypeReference<List<Company>>(){});
            companiesGrid.setItems(list);
        }
    }

    public Select selectButtonPerson(Person person) {
        Select select = new Select();
        select.setItems("Default price", "KG price", "KG/KM price");
        select.setValue(clientClient.selectActualOption(clientClient.getPersonId(person.getDNI())));
        select.addValueChangeListener(i -> selectGroupChanger(person.getClientId(), i.getValue().toString()));
        return select;
    }

    public Select selectButtonCompany(Company company) {
        Select select = new Select();
        select.setItems("Default price", "KG price", "KG/KM price");
        select.setValue(clientClient.selectActualOption(clientClient.getCompanyId(company.getRUT())));
        select.addValueChangeListener(i -> selectGroupChanger(company.getClientId(), i.getValue().toString()));
        return select;
    }

    public void selectGroupChanger(Integer id, String value) {
        clientClient.setClientGroup(id, value);
        ObjectMapper mapper = new ObjectMapper();
        List<Company> listCompanies = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>(){});
        companiesGrid.setItems(listCompanies);
        List<Person> listPersons = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>(){});
        personsGrid.setItems(listPersons);
    }


}
