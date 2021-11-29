package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Package {
    private Integer Number;
    private Float Price;
    private Date StartDate;
    private Date FinishDate;
    private String ExtraInfo;
    private PackageState State;
    private float lat;
    private float lng;
    private String trackingId;
    private Integer paymentTermId;
    private Integer transmitterId;
    private Integer receiverId;
    private String address;
    private Double weight;
    private String carID;
    private String employee;

    @Override
    public String toString() {
        return "Package{" +
                " Price=" + Price +
                "StartDate=" + StartDate +
                ", trackingId='" + trackingId + '\'' +
                ", address='" + address + '\'' +
                ", weight=" + weight +
                '}';
    }
}
