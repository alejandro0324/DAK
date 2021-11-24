package application.dak.DAK.views.packages;

import application.dak.DAK.backend.client.services.ClientClient;
import application.dak.DAK.backend.common.dto.Zone;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.*;
import application.dak.DAK.backend.generalConfiguration.services.ConfigurationClient;
import application.dak.DAK.backend.packages.services.PackagePayment;
import application.dak.DAK.backend.packages.services.PackagesClient;
import application.dak.DAK.backend.packages.services.PackagesService;
import application.dak.DAK.backend.zones.components.ZonesClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowingcode.vaadin.addons.googlemaps.*;
import com.google.maps.model.LatLng;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static application.dak.DAK.backend.utils.Constants.*;

@Slf4j
@PageTitle("Packages")
public class PackagesView extends VerticalLayout {
    private final ConfigurationClient configurationClient;
    private final PackagesService packagesService = new PackagesService();
    private final PackagePayment packagePayment = new PackagePayment();
    private final ClientClient clientClient;
    private final ZonesClient clientZones;
    private GoogleMapMarker addressMarker;
    private static HashMap<String, LatLng> coordinatesByAddress = new HashMap<>();
    private GoogleMap map;
    PackagesClient packagesClient = new PackagesClient();
    Grid<Package> packagesGrid = new Grid();
    Button packagesReception = new Button("New reception");
    Tab tab4 = new Tab("Step 3");
    Tab tab3 = new Tab("Step 2");
    Tab tab2 = new Tab("Step 1");
    Tab tab1 = new Tab("List");
    Tabs navigator = new Tabs();
    TextField packagesFilter = new TextField();
    VerticalLayout listDiv = new VerticalLayout();
    VerticalLayout stepOneDiv = new VerticalLayout();
    VerticalLayout stepTwoDiv = new VerticalLayout();
    VerticalLayout stepThreeDiv = new VerticalLayout();
    TextField transmitterFilter = new TextField();
    Grid<Person> transmitterPersonTypeList = new Grid<>();
    Grid<Company> transmitterCompanyTypeList = new Grid<>();
    TextField receiverFilter = new TextField();
    Grid<Person> receiverPersonTypeList = new Grid<>();
    Grid<Company> receiverCompanyTypeList = new Grid<>();
    static Autocomplete autocomplete = new Autocomplete(5);
    Button backToList = new Button("Go back");
    NumberField weight = new NumberField("Weight");
    TextField finalPrice = new TextField();
    Select<String> transmitterTypeSelector = new Select<>();
    Select<String> receiverTypeSelector = new Select<>();
    Select<String> paymentTermSelector = new Select<>();
    VerticalLayout paymentTermDiv = new VerticalLayout();
    Dialog cashTerm = new Dialog();
    Dialog debitTerm = new Dialog();
    Dialog creditTerm = new Dialog();
    Dialog mercadoPagoTerm = new Dialog();
    TextField finalPaymentCreditResult = new TextField("Result");
    TextField finalPaymentDebitResult = new TextField("Result");
    TextField finalPaymentMercadoPagoResult = new TextField("Result");
    Button endMercadoPagoButton = new Button("End transaction");
    Button endDebitButton = new Button("End transaction");
    Button endCreditButton = new Button("End transaction");
    Button continueMercadoPagoButton = new Button("Continue");
    Button cancelMercadoPagoButton = new Button("Cancel");
    Button continueDebitButton = new Button("Continue");
    Button cancelDebitButton = new Button("Cancel");
    Button continueCreditButton = new Button("Continue");
    Button cancelCreditButton = new Button("Cancel");
    VerticalLayout stepThreeInfo = new VerticalLayout();
    H2 trackingId = new H2();
    H2 clientId = new H2();
    H2 packageId = new H2();
    TextField groupInfo = new TextField("Group info");

    public PackagesView() {
        configurationClient = new ConfigurationClient();
        clientZones = new ZonesClient();
        clientClient = new ClientClient();
        backToList.setVisible(false);
        stepOneDiv.setVisible(false);
        stepTwoDiv.setVisible(false);
        tab2.setEnabled(false);
        tab3.setEnabled(false);
        tab4.setEnabled(false);
        stepThreeDiv.setVisible(false);

        ObjectMapper mapper = new ObjectMapper();
        List<Package> list = mapper.convertValue(packagesClient.getAllPackages(), new TypeReference<List<Package>>() {
        });
        loadPackagesGrid(list);

        navigator.add(tab1, tab2, tab3, tab4);
        packagesFilter.setPlaceholder("Filter by Number...");
        packagesFilter.setWidth(30f, Unit.EM);
        packagesFilter.setValueChangeMode(ValueChangeMode.EAGER);
        packagesFilter.addValueChangeListener(e -> updatePackageList());
        listDiv.add(packagesReception, packagesFilter, packagesGrid);

        stepOneDiv.setAlignItems(Alignment.CENTER);
        transmitterTypeSelector.setItems("Person", "Company");
        transmitterTypeSelector.setPlaceholder("Type");
        transmitterFilter.setVisible(false);
        transmitterPersonTypeList.setVisible(false);
        transmitterCompanyTypeList.setVisible(false);
        receiverTypeSelector.setItems("Person", "Company");
        receiverTypeSelector.setPlaceholder("Type");
        receiverFilter.setVisible(false);
        receiverPersonTypeList.setVisible(false);
        receiverCompanyTypeList.setVisible(false);
        VerticalLayout transmitterLayout = new VerticalLayout();
        VerticalLayout receiverLayout = new VerticalLayout();
        transmitterLayout.add(new H2("Choose the transmitter:"),
                transmitterTypeSelector,
                transmitterFilter,
                transmitterPersonTypeList,
                transmitterCompanyTypeList);
        receiverLayout.add(new H2("Choose the receiver:"),
                receiverTypeSelector,
                receiverFilter,
                receiverPersonTypeList,
                receiverCompanyTypeList);
        SplitLayout splitClients = new SplitLayout(transmitterLayout, receiverLayout);
        splitClients.setSizeFull();
        configureGoogleMaps();
        configureAutoCompleteInput();
        weight.setPlaceholder("KG");
        Button continueButtonStepOne = new Button("Continue");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(autocomplete, weight, groupInfo);
        horizontalLayout.add(map, verticalLayout);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setPadding(true);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        stepOneDiv.add(backToList, splitClients, horizontalLayout, continueButtonStepOne);

        loadTransmitterPersonList();
        loadReceiverPersonList();

        finalPrice.setReadOnly(true);
        finalPrice.setWidth(30f, Unit.EM);
        paymentTermSelector.setPlaceholder("Please select a payment term...");
        paymentTermSelector.setWidth(30f, Unit.EM);
        paymentTermSelector.setItems("Credit", "Debit", "Cash", "MercadoPago");

        configureCashTerms();
        configureCreditTerms();
        configureDebitTerms();
        configureMercadoPago();

        stepTwoDiv.add(new H2("Final price:"), finalPrice, paymentTermSelector);

        stepThreeDiv.setAlignItems(Alignment.CENTER);
        stepThreeInfo.setAlignItems(Alignment.CENTER);
        Button finalOkButton = new Button("Exit");
        stepThreeInfo.add(trackingId, clientId, packageId);
        stepThreeDiv.add(new H1("Final package information:"), stepThreeInfo, finalOkButton);

        add(navigator, listDiv, stepOneDiv, stepTwoDiv, stepThreeDiv);
        setListeners();
        continueButtonStepOne.addClickListener(i -> stepOneFinish());
        finalOkButton.addClickListener(i -> returnToList());
    }

    private void setListeners() {
        transmitterTypeSelector.addValueChangeListener(i -> transmitterSelectedType(i.getValue()));
        receiverTypeSelector.addValueChangeListener(i -> receiverSelectedType(i.getValue()));
        paymentTermSelector.addValueChangeListener(i -> paymentTermSelected(i.getValue()));
        receiverFilter.setValueChangeMode(ValueChangeMode.EAGER);
        receiverFilter.addValueChangeListener(e -> updateReceiverList());
        transmitterFilter.setValueChangeMode(ValueChangeMode.EAGER);
        transmitterFilter.addValueChangeListener(e -> updateTransmitterList());
        packagesReception.addClickListener(i -> stepOne());
        setPaymentListeners();
    }

    private void setPaymentListeners() {
        endMercadoPagoButton.addClickListener(i -> {
            mercadoPagoTerm.close();
            stepThree();
        });
        endCreditButton.addClickListener(i -> {
            creditTerm.close();
            stepThree();
        });
        endDebitButton.addClickListener(i -> {
            debitTerm.close();
            stepThree();
        });
    }

    private void configureAutoCompleteInput() {
        autocomplete.setPlaceholder("Type the address where it is going to go...");
        autocomplete.setLabel("Type address");
        autocomplete.setWidth(30f, Unit.EM);
        autocomplete.addChangeListener(event -> {
            packagesService.getCoordinatesFromAddress(event.getValue());
            loadAddresses();
        });
        autocomplete.addAutocompleteValueAppliedListener(event -> {
            LatLng coordinate = coordinatesByAddress.get(event.getValue());
            packagesService.setDestination(coordinate);
            map.setCenter(new LatLon(coordinate.lat, coordinate.lng));
            addressMarker = new GoogleMapMarker("", new LatLon(coordinate.lat, coordinate.lng), false);
            map.addMarker(addressMarker);
        });
    }

    private void loadAddresses() {
        try {
            autoCompleteAddressInput(packagesService.getCoordinates());
        } catch (Exception e) {
            loadAddresses();
        }
    }

    private void configureCashTerms() {
        cashTerm.setCloseOnOutsideClick(false);
        Button continueCashButton = new Button("Continue");
        Button cancelCashButton = new Button("Cancel");
        cashTerm.add(new H2("Press continue when the payment ends..."), continueCashButton, new HtmlComponent("br"), new H3("Or cancel..."), cancelCashButton);
        continueCashButton.addClickListener(i -> {
            cashTerm.close();
            stepThree();
        });
        cancelCashButton.addClickListener(i -> {
            cashTerm.close();
            returnToList();
        });
    }

    private void configureCreditTerms() {
        creditTerm.setCloseOnOutsideClick(false);
        endCreditButton.setVisible(false);
        ProgressBar progressCreditBar = new ProgressBar();
        progressCreditBar.setIndeterminate(true);
        progressCreditBar.setVisible(true);
        finalPaymentCreditResult.setReadOnly(true);
        finalPaymentCreditResult.setVisible(true);
        creditTerm.add(new H2("Check the credit card with the post and press continue..."), continueCreditButton, progressCreditBar, finalPaymentCreditResult, endCreditButton, new HtmlComponent("br"), new H3("Or cancel..."), cancelCreditButton);
        continueCreditButton.addClickListener(i -> paymentLogic("Credit"));
        cancelCreditButton.addClickListener(i -> {
            creditTerm.close();
            returnToList();
        });
    }

    private void configureDebitTerms() {
        debitTerm.setCloseOnOutsideClick(false);
        endDebitButton.setVisible(false);
        ProgressBar progressDebitBar = new ProgressBar();
        progressDebitBar.setIndeterminate(true);
        progressDebitBar.setVisible(true);
        finalPaymentDebitResult.setReadOnly(true);
        finalPaymentDebitResult.setVisible(true);
        debitTerm.add(new H2("Check the debit card with the post and press continue..."), continueDebitButton, progressDebitBar, finalPaymentDebitResult, endDebitButton, new HtmlComponent("br"), new H3("Or cancel..."), cancelDebitButton);
        continueDebitButton.addClickListener(i -> paymentLogic("Debit"));
        cancelDebitButton.addClickListener(i -> {
            debitTerm.close();
            returnToList();
        });
    }

    private void configureMercadoPago() {
        mercadoPagoTerm.setCloseOnOutsideClick(false);
        endMercadoPagoButton.setVisible(false);
        ProgressBar progressMercadoPagoBar = new ProgressBar();
        EmailField mailMercadoPago = new EmailField("Email");
        mailMercadoPago.setRequiredIndicatorVisible(true);
        progressMercadoPagoBar.setIndeterminate(true);
        progressMercadoPagoBar.setVisible(true);
        finalPaymentMercadoPagoResult.setReadOnly(true);
        finalPaymentMercadoPagoResult.setVisible(true);
        mercadoPagoTerm.add(new H2("Complete the mail text field and press continue..."), mailMercadoPago, continueMercadoPagoButton, progressMercadoPagoBar, finalPaymentMercadoPagoResult, endMercadoPagoButton, new HtmlComponent("br"), new H3("Or cancel..."), cancelMercadoPagoButton);
        continueMercadoPagoButton.addClickListener(i -> paymentLogic("MercadoPago"));
        cancelDebitButton.addClickListener(i -> {
            mercadoPagoTerm.close();
            returnToList();
        });
    }

    private void loadPackagesGrid(List<Package> list) {
        packagesGrid.setItems(list);
        packagesGrid.addColumn(Package::getNumber).setHeader("Number");
        packagesGrid.addColumn(Package::getState).setHeader("State");
        packagesGrid.addColumn(Package::getStartDate).setHeader("Start Date");
        packagesGrid.addColumn(Package::getFinishDate).setHeader("Finish Date");
        packagesGrid.addColumn(Package::getExtraInfo).setHeader("Extra info.");
    }

    private void loadTransmitterPersonList() {
        transmitterPersonTypeList.addColumn(Person::getClientId).setHeader("Id");
        transmitterPersonTypeList.addColumn(Person::getDNI).setHeader("DNI");
        transmitterPersonTypeList.addColumn(Person::getName).setHeader("Name");
        transmitterPersonTypeList.addColumn(Person::getSurname).setHeader("Surname");
        transmitterCompanyTypeList.addColumn(Company::getClientId).setHeader("Id");
        transmitterCompanyTypeList.addColumn(Company::getRUT).setHeader("RUT");
        transmitterCompanyTypeList.addColumn(Company::getBusinessName).setHeader("Business Name");
    }

    public void loadReceiverPersonList() {
        receiverPersonTypeList.addColumn(Person::getClientId).setHeader("Id");
        receiverPersonTypeList.addColumn(Person::getDNI).setHeader("DNI");
        receiverPersonTypeList.addColumn(Person::getName).setHeader("Name");
        receiverPersonTypeList.addColumn(Person::getSurname).setHeader("Surname");
        receiverCompanyTypeList.addColumn(Company::getClientId).setHeader("Id");
        receiverCompanyTypeList.addColumn(Company::getRUT).setHeader("RUT");
        receiverCompanyTypeList.addColumn(Company::getBusinessName).setHeader("Business Name");
    }


    public void paymentLogic(String type) {
        packagePayment.setStrategy(type);
        var result = packagePayment.getResult();
        finalPaymentDebitResult.setValue(result);
        finalPaymentCreditResult.setValue(result);
        finalPaymentMercadoPagoResult.setValue(result);
        if (isResultSuccessful(result)) {
            paymentSuccessful();
        }
    }

    public boolean isResultSuccessful(String result) {
        return result.equals("Post: OK");
    }

    public void paymentSuccessful() {
        cancelMercadoPagoButton.setEnabled(false);
        cancelDebitButton.setEnabled(false);
        cancelCreditButton.setEnabled(false);
        continueCreditButton.setEnabled(false);
        continueMercadoPagoButton.setEnabled(false);
        continueDebitButton.setEnabled(false);
        debitTerm.setCloseOnEsc(false);
        mercadoPagoTerm.setCloseOnEsc(false);
        creditTerm.setCloseOnEsc(false);
        endMercadoPagoButton.setVisible(true);
        endDebitButton.setVisible(true);
        endCreditButton.setVisible(true);
    }

    public void stepThree() {
        navigator.setSelectedTab(tab4);
        tab3.setEnabled(false);
        tab4.setEnabled(true);
        stepTwoDiv.setVisible(false);
        stepThreeDiv.setVisible(true);

        Package pack = new Package();
        pack.setExtraInfo(groupInfo.getValue());
        pack.setStartDate(new Date(System.currentTimeMillis()));
        pack.setPrice(Float.valueOf(finalPrice.getValue()));
        pack.setLat((float) addressMarker.getPosition().getLat());
        pack.setLng((float) addressMarker.getPosition().getLon());
        pack.setAddress(autocomplete.getValue());
        pack.setState(PackageState.IN_LOCAL);
        pack.setPaymentTermId(getPaymentID());
        pack.setTransmitterId(Integer.parseInt(VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("transmitter").toString()));
        pack.setReceiverId(Integer.parseInt(VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("receiver").toString()));
        Tracking tracking = packagesClient.createTracking();
        pack.setTrackingId(tracking.getId());
        packagesClient.createPackage(pack);

        ObjectMapper mapper = new ObjectMapper();
        List<Package> list = mapper.convertValue(packagesClient.getAllPackages(), new TypeReference<List<Package>>() {
        });
        packagesGrid.setItems(list);
        //TODO: Create PDF with information of the package
        stepThreeInfo.add(new H2("Transmitter Id: " + pack.getTransmitterId()), new H2("Tracking Id: " + pack.getTrackingId()), new H2("Package number: " + pack.getNumber()));
    }

    private int getPaymentID() {
        switch (paymentTermSelector.getValue()) {
            case "Credit":
                return 2;
            case "Debit":
                return 1;
            case "MercadoPago":
                return 4;
            default:
                return 3;
        }
    }

    public void returnToList() {
        stepThreeDiv.setVisible(false);
        navigator.setSelectedTab(tab1);
        tab3.setEnabled(false);
        tab4.setEnabled(false);
        tab1.setEnabled(true);
        weight.clear();
        autocomplete.clear();
        receiverCompanyTypeList.setVisible(false);
        receiverPersonTypeList.setVisible(false);
        transmitterCompanyTypeList.setVisible(false);
        transmitterPersonTypeList.setVisible(false);
        receiverFilter.clear();
        transmitterFilter.clear();
        stepTwoDiv.setVisible(false);
        stepThreeDiv.setVisible(false);
        listDiv.setVisible(true);
        paymentTermSelector.clear();
    }

    public void paymentTermSelected(String value) {
        finalPaymentDebitResult.clear();
        finalPaymentCreditResult.clear();
        finalPaymentMercadoPagoResult.clear();
        paymentTermDiv.setVisible(true);
        switch (value) {
            case "Cash":
                cashTerm.open();
                break;
            case "Debit":
                debitTerm.open();
                break;
            case "Credit":
                creditTerm.open();
                break;
            case "MercadoPago":
                mercadoPagoTerm.open();
                break;
        }
    }

    public void updatePackageList() {
        if (packagesFilter.getValue().equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.getAllPackages(), new TypeReference<List<Package>>() {
            });
            packagesGrid.setItems(list);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            List<Package> list = mapper.convertValue(packagesClient.getAllPackagesLike(packagesFilter.getValue()), new TypeReference<List<Package>>() {
            });
            packagesGrid.setItems(list);
        }
    }

    public void updateReceiverList() {
        String type = receiverTypeSelector.getValue();
        if (type.equals("Person")) {
            if (receiverFilter.getValue().equals("")) {
                ObjectMapper mapper = new ObjectMapper();
                List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>() {
                });
                receiverPersonTypeList.setItems(list);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                List<Person> list = mapper.convertValue(clientClient.listAllPersonsLike(receiverFilter.getValue()), new TypeReference<List<Person>>() {
                });
                receiverPersonTypeList.setItems(list);
            }
        } else {
            if (receiverFilter.getValue().equals("")) {
                ObjectMapper mapper = new ObjectMapper();
                List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>() {
                });
                receiverCompanyTypeList.setItems(list);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                List<Company> list = mapper.convertValue(clientClient.listAllCompaniesLike(receiverFilter.getValue()), new TypeReference<List<Company>>() {
                });
                receiverCompanyTypeList.setItems(list);
            }
        }
    }

    public void updateTransmitterList() {
        String type = transmitterTypeSelector.getValue();
        if (type.equals("Person")) {
            if (transmitterFilter.getValue().equals("")) {
                ObjectMapper mapper = new ObjectMapper();
                List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>() {
                });
                transmitterPersonTypeList.setItems(list);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                List<Person> list = mapper.convertValue(clientClient.listAllPersonsLike(transmitterFilter.getValue()), new TypeReference<List<Person>>() {
                });
                transmitterPersonTypeList.setItems(list);
            }
        } else {
            if (transmitterFilter.getValue().equals("")) {
                ObjectMapper mapper = new ObjectMapper();
                List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>() {
                });
                transmitterCompanyTypeList.setItems(list);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                List<Company> list = mapper.convertValue(clientClient.listAllCompaniesLike(transmitterFilter.getValue()), new TypeReference<List<Company>>() {
                });
                transmitterCompanyTypeList.setItems(list);
            }
        }
    }

    public void stepOne() {
        navigator.setSelectedTab(tab2);
        backToList.setVisible(true);
        backToList.getStyle().set("left", "10px");
        tab2.setEnabled(true);
        tab1.setEnabled(false);
        listDiv.setVisible(false);
        stepOneDiv.setVisible(true);
        backToList.addClickListener(i -> backToList());
    }

    public void stepTwo() {
        navigator.setSelectedTab(tab3);
        backToList.setVisible(false);
        tab2.setEnabled(false);
        tab1.setEnabled(false);
        tab3.setEnabled(true);
        listDiv.setVisible(false);
        stepOneDiv.setVisible(false);
        finalPriceCalculator();
        stepTwoDiv.setVisible(true);
    }

    public void finalPriceCalculator() {
        Integer clientId = Integer.parseInt(VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("transmitter").toString());
        Client client = clientClient.getClient(clientId);
        packagesService.setKG(Float.parseFloat(weight.getValue().toString()));
        packagesService.tripTax = configurationClient.getTripTax();
        Integer groupId = client.getClientGroupId();
        packagesService.setStrategy(groupId);
        setValue();
    }

    private void setValue() {
        try {
            finalPrice.setValue(packagesService.calculatePrice());
        } catch (Exception e) {
            setValue();
        }
    }

    public void stepOneFinish() {
        boolean isOk = true;
        boolean same = false;
        try {
            if (VaadinService.getCurrentRequest().getWrappedSession()
                    .getAttribute("transmitter").toString().equals("")) {
                isOk = false;
            }
            if (VaadinService.getCurrentRequest().getWrappedSession()
                    .getAttribute("receiver").toString().equals("")) {
                isOk = false;
            }
            if (autocomplete.getValue().equals("")) {
                isOk = false;
            }
            if (weight.getValue().equals("")) {
                isOk = false;
            }
            if (VaadinService.getCurrentRequest().getWrappedSession()
                    .getAttribute("receiver").toString().equals(VaadinService.getCurrentRequest().getWrappedSession()
                            .getAttribute("transmitter").toString())) {
                isOk = false;
                same = true;
            }
            if (!isOk) {
                if (same) {
                    Notification.show("ERROR: The transmitter and the receiver are the same client");
                } else {
                    Notification.show("ERROR: Please complete the form");
                }
            } else {
                VaadinService.getCurrentRequest().getWrappedSession()
                        .setAttribute("address", autocomplete.getValue());
                VaadinService.getCurrentRequest().getWrappedSession()
                        .setAttribute("weight", weight.getValue());
                stepTwo();
            }
        } catch (Exception ex) {
            Notification.show("Please complete the form");
        }
    }

    public void backToList() {
        navigator.setSelectedTab(tab1);
        backToList.setVisible(false);
        tab2.setEnabled(false);
        tab1.setEnabled(true);
        listDiv.setVisible(true);
        stepOneDiv.setVisible(false);
    }

    public void transmitterSelectedType(String value) {
        if (value.equals("Person")) {
            transmitterFilter.setVisible(true);
            transmitterFilter.setPlaceholder("Filter by DNI");
            transmitterPersonTypeList.setVisible(true);
            transmitterCompanyTypeList.setVisible(false);
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>() {
            });
            transmitterPersonTypeList.setItems(list);
            transmitterPersonTypeList.asSingleSelect().addValueChangeListener(event -> VaadinService.getCurrentRequest().getWrappedSession()
                    .setAttribute("transmitter", event.getValue().getClientId()));
        } else {
            transmitterFilter.setVisible(true);
            transmitterFilter.setPlaceholder("Filter by RUT");
            transmitterPersonTypeList.setVisible(false);
            transmitterCompanyTypeList.setVisible(true);
            ObjectMapper mapper = new ObjectMapper();
            List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>() {
            });
            transmitterCompanyTypeList.setItems(list);
            transmitterCompanyTypeList.asSingleSelect().addValueChangeListener(event -> {
                VaadinService.getCurrentRequest().getWrappedSession()
                        .setAttribute("transmitter", event.getValue().getClientId());
            });
        }
    }

    public void receiverSelectedType(String value) {
        if (value.equals("Person")) {
            receiverFilter.setVisible(true);
            receiverFilter.setPlaceholder("Filter by DNI");
            receiverPersonTypeList.setVisible(true);
            receiverCompanyTypeList.setVisible(false);
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = mapper.convertValue(clientClient.listAllPersons(), new TypeReference<List<Person>>() {
            });
            receiverPersonTypeList.setItems(list);
            receiverPersonTypeList.asSingleSelect().addValueChangeListener(event -> {
                VaadinService.getCurrentRequest().getWrappedSession()
                        .setAttribute("receiver", event.getValue().getClientId());
            });
        } else {
            receiverFilter.setVisible(true);
            receiverFilter.setPlaceholder("Filter by RUT");
            receiverPersonTypeList.setVisible(false);
            receiverCompanyTypeList.setVisible(true);
            ObjectMapper mapper = new ObjectMapper();
            List<Company> list = mapper.convertValue(clientClient.listAllCompanies(), new TypeReference<List<Company>>() {
            });
            receiverCompanyTypeList.setItems(list);
            receiverCompanyTypeList.asSingleSelect().addValueChangeListener(event -> {
                VaadinService.getCurrentRequest().getWrappedSession()
                        .setAttribute("receiver", event.getValue().getClientId());
            });
        }
    }

    private void configureGoogleMaps() {
        String apiKey = System.getProperty("google.maps.api");
        map = new GoogleMap(apiKey, null, null);
        map.setMapType(GoogleMap.MapType.ROADMAP);
        map.setCenter(new LatLon(CURRENT_LAT, CURRENT_LNG));
        map.setSizeFull();
        map.setWidth(850, Unit.PIXELS);
        map.setHeight(350, Unit.PIXELS);
        map.setZoom(11);
        loadZones();
    }

    private void loadZones() {
        List<Zone> zones = clientZones.getZones();
        zones.forEach(this::displayZone);
    }

    private void displayZone(Zone zone) {
        List<GoogleMapPoint> points = new ArrayList<>();
        zone.getCoordinates().forEach(i -> points.add(new GoogleMapPoint(i)));
        GoogleMapPolygon polygon = map.addPolygon(points);
        polygon.setFillColor(APP_COLOUR);
        polygon.addClickListener(event -> addInfoWindowToPolygon(zone));
    }

    private void addInfoWindowToPolygon(Zone zone) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setCloseOnOutsideClick(true);
        VerticalLayout listZoneInfo = new VerticalLayout();
        listZoneInfo.add(
                new H4("Information of the zone"),
                new H5("Zone's UUID: " + zone.getUuid()),
                new H5("Zone's name: " + zone.getName()));
        dialog.add(listZoneInfo);
        dialog.open();
    }

    public void setOpened(boolean opened) {
        setVisible(opened);

        UI ui = UI.getCurrent();

        if (opened && this.getElement().getNode().getParent() == null && ui != null) {
            ui.beforeClientResponse(ui, (context) -> {
                // no need to encapsulate inside another component
                ui.add(this);
            });
        } else if (!opened) {
            getElement().removeFromParent();
        }
    }

    public void autoCompleteAddressInput(HashMap<String, LatLng> coordinates) {
        coordinatesByAddress = coordinates;
        getUI().ifPresent(ui -> ui.access(() -> {
            autocomplete.setOptions(new ArrayList<>(coordinates.keySet()));
        }));
    }
}
