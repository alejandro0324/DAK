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
public class Tracking {
    public String Id;
    public float currentLat;
    public float currentLng;
    public String carID;
    public Date dateOfTracking;
    public String stateOfTracking;
    public String information;
}
