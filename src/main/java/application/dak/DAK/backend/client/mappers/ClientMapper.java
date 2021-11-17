package application.dak.DAK.backend.client.mappers;

import application.dak.DAK.backend.common.models.Client;
import application.dak.DAK.backend.common.models.Company;
import application.dak.DAK.backend.common.models.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClientMapper {

    @Select("SELECT * FROM T_Person")
    public List<Person> getAllPersons();

    @Select("SELECT * FROM T_Company")
    public List<Company> getAllCompanies();

    @Select("SELECT * FROM V_ActivePersons")
    public List<Person> listAllPersons();

    @Select("SELECT * FROM V_ActiveCompanies")
    public List<Company> listAllCompanies();

    @Select("UPDATE T_Client SET active = 0 WHERE Id = #{id}")
    public void removeClient(@Param("id") final Integer id);

    @Select("SELECT * FROM V_ActivePersons WHERE DNI LIKE #{DNI}")
    public List<Person> listAllPersonsLike(@Param("DNI") final String DNI);

    @Select("SELECT * FROM V_ActiveCompanies WHERE RUT LIKE #{RUT}")
    public List<Company> listAllCompaniesLike(@Param("RUT") final String RUT);

    @Select("SELECT description FROM T_ClientGroup WHERE Id = #{Id}")
    public String selectActualOption(@Param("Id") final Integer Id);

    @Insert("INSERT INTO T_Client(clientGroupId, email, telephone, direction, active) VALUES (1, #{client.Email}, #{client.Telephone}, #{client.Direction}, 1)")
    @Options(useGeneratedKeys = true, keyProperty="Id", keyColumn = "Id")
    public Integer addClient(@Param("client") final Client client);

    @Insert("INSERT INTO T_Person(clientId, DNI, name, surname) VALUES (#{person.Id}, #{person.DNI}, #{person.Name}, #{person.Surname})")
    public void addPerson(@Param("person") final Person person);

    @Insert("INSERT INTO T_Company(clientId, RUT, businessName) VALUES (#{company.Id}, #{company.RUT}, #{company.businessName})")
    public void addCompany(@Param("company") final Company company);

    @Select("SELECT * FROM T_Person WHERE clientId = #{Id}")
    public Person getPerson(@Param("Id") final Integer id);

    @Select("SELECT * FROM T_Company WHERE clientId = #{Id}")
    public Company getCompany(@Param("Id") final Integer id);

    @Select("SELECT * FROM T_Client WHERE Id = #{Id}")
    public Client getClient(@Param("Id") final Integer id);

    @Select("SELECT * FROM T_Person WHERE DNI = #{dni}")
    public Person getPersonByDNI(@Param("dni") final String dni);

    @Select("SELECT * FROM T_Company WHERE RUT = #{RUT}")
    public Company getCompanyByRUT(@Param("RUT") final String RUT);

    @Select("SELECT clientId FROM T_Person WHERE DNI = #{DNI}")
    public Integer getPersonId(@Param("DNI") final String DNI);

    @Select("SELECT clientId FROM T_Company WHERE RUT = #{RUT}")
    public Integer getCompanyRUT(@Param("RUT") final String RUT);

    @Update("UPDATE T_Company SET businessName = #{company.BusinessName} WHERE clientId = #{company.clientId}")
    public void editCompany(@Param("company") final Company company);

    @Update("UPDATE T_Person SET name = #{person.Name}, surname = #{person.Surname} WHERE clientId = #{person.clientId}")
    public void editPerson(@Param("person") final Person person);

    @Update("UPDATE T_Client SET email = #{client.Email}, telephone = #{client.Telephone}, direction = #{client.Direction} WHERE Id = #{client.Id}")
    public void editClient(@Param("client") final Client client);

    @Update("UPDATE T_Client SET clientGroupId = #{valueId} WHERE Id = #{id}")
    public void setClientGroup(@Param("id") final Integer id, @Param("valueId") final Integer valueId);

    @Select("SELECT Id FROM T_ClientGroup WHERE description = #{value}")
    public Integer getValueId(@Param("value") final String value);
}
