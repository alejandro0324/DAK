package application.dak.DAK.backend.common.dto;

import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zone {
    private Integer id;
    private String name;
    private String uuid;
    private List<LatLon> coordinates;

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
