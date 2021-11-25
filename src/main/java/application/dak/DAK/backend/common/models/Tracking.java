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
    private String id;
    private Double currentLat;
    private Double currentLng;
    private String carID;
    private Date dateOfTracking;
    private String stateOfTracking;
}
