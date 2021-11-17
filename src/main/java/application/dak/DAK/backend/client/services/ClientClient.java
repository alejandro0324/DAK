package application.dak.DAK.backend.client.services;

import application.dak.DAK.backend.common.models.Client;
import application.dak.DAK.backend.common.models.Company;
import application.dak.DAK.backend.common.models.Person;
import application.dak.DAK.backend.common.models.SelectDto;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ClientClient {
    private final RestTemplate restTemplate;
    private final String baseURL;

    public ClientClient() {
        this.restTemplate = new RestTemplate();
        this.baseURL = "http://localhost:8080/client/";
    }

    public List<String> addPerson(Person person) {
        final String url = baseURL + "addPerson";
        List<String> list = new ArrayList<>();
        return restTemplate.postForObject(url, person,  list.getClass());
    }

    public List<String> addCompany(Company company) {
        final String url = baseURL + "addCompany";
        List<String> list = new ArrayList<>();
        return restTemplate.postForObject(url, company,  list.getClass());
    }

    public List<Person> listAllPersons() {
        final String url = baseURL + "listAllPersons";
        List<Person> list = new ArrayList<>();
        return restTemplate.getForObject(url,  list.getClass());
    }

    public List<Company> listAllCompanies() {
        final String url = baseURL + "listAllCompanies";
        List<Company> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public void removePerson(Person person) {
        final String url = baseURL + "removePerson";
        restTemplate.postForObject(url, person, void.class);
    }

    public void removeCompany(Company company) {
        final String url = baseURL + "removeCompany";
        restTemplate.postForObject(url, company, void.class);
    }

    public Integer isPerson(Client client) {
        final String url = baseURL + "isPerson/" + client.getId().toString();
        return restTemplate.getForObject(url, Integer.class);
    }

    public Person getPerson(Integer id) {
        final String url = baseURL + "getPerson/" + id.toString();
        return restTemplate.getForObject(url, Person.class);
    }

    public Company getCompany(Integer id) {
        final String url = baseURL + "getCompany/" + id.toString();
        return restTemplate.getForObject(url, Company.class);
    }

    public Client getClient(Integer id) {
        final String url = baseURL + "getClient/" + id.toString();
        return restTemplate.getForObject(url, Client.class);
    }

    public Integer personExist(String dni) {
        final String url = baseURL + "personExist/" + dni;
        return restTemplate.getForObject(url, Integer.class);
    }

    public Integer companyExist(String RUT) {
        final String url = baseURL + "companyExist/" + RUT;
        return restTemplate.getForObject(url, Integer.class);
    }

    public void editPerson(Integer id, String name, String surname, String dni, String mail, String telephone, String direction) {
        final String url = baseURL + "editPerson";
        Person person = new Person(id, name, surname, dni);
        person.setEmail(mail);
        person.setTelephone(telephone);
        person.setDirection(direction);
        restTemplate.postForObject(url, person, void.class);
    }

    public void editCompany(Integer id, String RUT, String businessName, String mail, String direction, String telephone) {
        final String url = baseURL + "editCompany";
        Company company = new Company(id, RUT, businessName);
        company.setEmail(mail);
        company.setTelephone(telephone);
        company.setDirection(direction);
        restTemplate.postForObject(url, company, void.class);
    }

    public Integer getPersonId(String dni) {
        final String url = baseURL + "getPersonId/" + dni;
        return restTemplate.getForObject(url, Integer.class);
    }

    public Integer getCompanyId(String RUT) {
        final String url = baseURL + "getCompanyId/" + RUT;
        return restTemplate.getForObject(url, Integer.class);
    }

    public List<Person> listAllPersonsLike(String DNI) {
        final String url = baseURL + "listAllPersonsLike/" + DNI;
        List<Person> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public List<Company> listAllCompaniesLike(String RUT) {
        final String url = baseURL + "listAllCompaniesLike/" + RUT;
        List<Company> list = new ArrayList<>();
        return restTemplate.getForObject(url, list.getClass());
    }

    public String selectActualOption(Integer Id) {
        final String url = baseURL + "selectActualOption/" + Id;
        return restTemplate.getForObject(url, String.class);
    }

    public void setClientGroup(Integer id, String value) {
        final String url = baseURL + "setClientGroup/";
        SelectDto selectDto = new SelectDto(id, value);
        restTemplate.postForObject(url, selectDto, void.class);
    }
}
