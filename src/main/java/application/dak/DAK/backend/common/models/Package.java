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
    public Integer Number;
    public Float Price;
    public Date StartDate;
    public Date FinishDate;
    public String ExtraInfo;
    public PackageState State;
    public float lat;
    public float lng;
    public String trackingId;
    public Integer paymentTermId;
    public Integer transmitterId;
    public Integer receiverId;
}
