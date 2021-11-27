package application.dak.DAK.backend.common.models;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DirectionsQueryResult {
    private List<LatLng> decodedPath;
}
