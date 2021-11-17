package application.dak.DAK.backend.common.messages;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinateMessage {
    private String zoneUUID;
    private Double lat;
    private Double lng;
}
