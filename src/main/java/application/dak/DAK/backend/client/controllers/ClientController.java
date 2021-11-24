package application.dak.DAK.backend.client.controllers;

import application.dak.DAK.backend.client.mappers.ClientMapper;
import application.dak.DAK.backend.common.models.Client;
import application.dak.DAK.backend.common.models.Company;
import application.dak.DAK.backend.common.models.Person;
import application.dak.DAK.backend.common.models.SelectDto;
import application.dak.DAK.backend.generalConfiguration.mappers.ConfigurationMapper;
import application.dak.DAK.firebase.FirestoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ClientController {

    private final ClientMapper clientMapper;
    private final String TAG = "ClientController";

    @PostMapping("/addPerson")
    public List<String> addPerson(@RequestBody final Person person) {
        List<String> isOk = new ArrayList<>();
        List<Person> persons = clientMapper.getAllPersons();
        for (Person value : persons) {
            if (value.getDNI().equals(person.getDNI())) {
                isOk.add("ERROR: Another person has the same DNI");
            }
        }

        if (isOk.isEmpty()) {
            clientMapper.addClient(person);
            clientMapper.addPerson(person);
            isOk.add("Person correctly signed up, Id: " + person.getId());
        }
        FirestoreService.getInstance().log(TAG, "Client (Person) successfully created " + person);
        return isOk;
    }

    @PostMapping("/addCompany")
    public List<String> addCompany(@RequestBody final Company company) {
        List<String> isOk = new ArrayList<>();
        List<Company> companies = clientMapper.getAllCompanies();
        for (Company value : companies) {
            if (value.getRUT().equals(company.getRUT())) {
                isOk.add("ERROR: Another company have the same RUT");
            }
        }

        if (isOk.isEmpty()) {
            clientMapper.addClient(company);
            clientMapper.addCompany(company);
            isOk.add("Company correctly sign in, Id: " + company.getId());
        }

        FirestoreService.getInstance().log(TAG, "Client (Company) successfully created " + company);
        return isOk;
    }

    @GetMapping("/listAllPersons")
    public List<Person> listAllPersons() {
        return clientMapper.listAllPersons();
    }

    @GetMapping("/listAllCompanies")
    public List<Company> listAllCompanies() {
        return clientMapper.listAllCompanies();
    }

    @PostMapping("/removePerson")
    public void removePerson(@RequestBody final Person person) {
        clientMapper.removeClient(person.getClientId());
    }

    @PostMapping("/removeCompany")
    public void removeCompany(@RequestBody final Company company) {
        clientMapper.removeClient(company.getClientId());
    }

    @PostMapping("/editPerson")
    public void editPerson(@RequestBody final Person person) {
        clientMapper.editPerson(person);
        Client client = new Client(person.getClientId(), person.getClientId(), person.getEmail(), person.getTelephone(), person.getDirection());
        clientMapper.editClient(client);
    }

    @PostMapping("/editCompany")
    public void editCompany(@RequestBody final Company company) {
        clientMapper.editCompany(company);
        Client client = new Client(company.getClientId(), company.getClientId(), company.getEmail(), company.getTelephone(), company.getDirection());
        clientMapper.editClient(client);
    }

    @GetMapping("/isPerson/{id}")
    public Integer isPerson(@PathVariable("id") Integer id) {
        Person person = clientMapper.getPerson(id);
        if (person == null) {
            return 0;
        }
        return 1;
    }

    @GetMapping("/getPerson/{id}")
    public Person getPerson(@PathVariable("id") Integer id){
        return clientMapper.getPerson(id);
    }

    @GetMapping("/getCompany/{id}")
    public Company getCompany(@PathVariable("id") Integer id){
        return clientMapper.getCompany(id);
    }

    @GetMapping("/getClient/{id}")
    public Client getClient(@PathVariable("id") Integer id){
        return clientMapper.getClient(id);
    }

    @GetMapping("/personExist/{dni}")
    public Integer personExist(@PathVariable("dni") String dni){
        Person person = clientMapper.getPersonByDNI(dni);
        if (person == null) {
            return 0;
        }
        return 1;
    }

    @GetMapping("/companyExist/{RUT}")
    public Integer companyExist(@PathVariable("RUT") String RUT){
        Company company = clientMapper.getCompanyByRUT(RUT);
        if (company == null) {
            return 0;
        }
        return 1;
    }

    @GetMapping("/getPersonId/{dni}")
    public Integer getPersonId(@PathVariable("dni") String dni){
        return clientMapper.getPersonId(dni);
    }

    @GetMapping("/getCompanyId/{RUT}")
    public Integer getCompanyId(@PathVariable("RUT") String RUT){
        return clientMapper.getCompanyRUT(RUT);
    }

    @GetMapping("/listAllPersonsLike/{DNI}")
    public List<Person> listAllPersonsLike(@PathVariable("DNI") String DNI){
        List<Person> personList = clientMapper.listAllPersonsLike(DNI + "%");
        return personList;
    }

    @GetMapping("/listAllCompaniesLike/{RUT}")
    public List<Company> listAllCompaniesLike(@PathVariable("RUT") String RUT){
        List<Company> companies = clientMapper.listAllCompaniesLike(RUT + "%");
        return companies;
    }

    @GetMapping("/selectActualOption/{Id}")
    public String selectActualOption(@PathVariable("Id") Integer Id){
        Integer groupId = this.getClient(Id).getClientGroupId();
        return clientMapper.selectActualOption(groupId);
    }

    @PostMapping("/setClientGroup")
    public void setClientGroup(@RequestBody final SelectDto selectDto) {
        Integer valueId = clientMapper.getValueId(selectDto.getValue());
        clientMapper.setClientGroup(selectDto.getId(), valueId);
    }
}
