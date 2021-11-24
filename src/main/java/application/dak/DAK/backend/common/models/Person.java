package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person extends Client implements Serializable {
    private Integer clientId;
    private String Name;
    private String Surname;
    private String DNI;

    @Override
    public String toString() {
        return "Person{" +
                "Email='" + Email + '\'' +
                "Telephone='" + Telephone + '\'' +
                "Direction='" + Direction + '\'' +
                "Name='" + Name + '\'' +
                "Surname='" + Surname + '\'' +
                "DNI='" + DNI + '\'' +
                '}';
    }
}
