package application.dak.DAK.backend.common.messages;


import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneMessage {
    private String name;
    private String uuid;
    private List<LatLon> coordinates;
}
