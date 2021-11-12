package application.dak.DAK.backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
    private Double lat;
    private Double lng;
    private String zoneUUID;
}
