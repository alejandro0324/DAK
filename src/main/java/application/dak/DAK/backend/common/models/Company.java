package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends Client{
    private Integer clientId;
    private String RUT;
    private String BusinessName;

    @Override
    public String toString() {
        return "Company{" +
                "Email='" + Email + '\'' +
                ", Telephone='" + Telephone + '\'' +
                ", Direction='" + Direction + '\'' +
                ", RUT='" + RUT + '\'' +
                ", BusinessName='" + BusinessName + '\'' +
                '}';
    }
}
