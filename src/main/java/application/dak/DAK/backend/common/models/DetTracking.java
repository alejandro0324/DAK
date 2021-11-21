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
public class DetTracking {
    public Integer Id;
    public Integer trackingId;
    public Date date;
    public float lat;
    public float lng;
}
