package application.dak.DAK.backend.zones.services;

import application.dak.DAK.backend.common.dto.Coordinate;
import application.dak.DAK.backend.common.dto.Zone;
import application.dak.DAK.backend.common.messages.ZoneMessage;
import application.dak.DAK.backend.zones.components.ZoneMessageSender;
import application.dak.DAK.backend.zones.mappers.CoordinateMapper;
import application.dak.DAK.backend.zones.mappers.ZoneMapper;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import application.dak.DAK.firebase.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZoneService {

    private static final String TAG = "ZoneService";

    private final ZoneMessageSender zoneMessageSender;
    private final ZoneMapper zoneMapper;
    private final CoordinateMapper coordinateMapper;

    @Autowired
    public ZoneService(ZoneMessageSender zoneMessageSender, ZoneMapper zoneMapper, CoordinateMapper coordinateMapper) {
        this.zoneMessageSender = zoneMessageSender;
        this.zoneMapper = zoneMapper;
        this.coordinateMapper = coordinateMapper;
    }

    public void addZoneAsync(final Zone zoneBeingEdited) {
        final ZoneMessage zoneMessage = ZoneMessage.builder()
                .name(zoneBeingEdited.getName())
                .uuid(zoneBeingEdited.getUuid())
                .coordinates(zoneBeingEdited.getCoordinates())
                .build();

        this.zoneMessageSender.sendNewZoneNotification(zoneMessage);
    }

    public void removeZone(String uuid) {
        zoneMapper.remove(uuid);
        FirestoreService.getInstance().log(TAG, "zone " + uuid + " removed");
    }

    public List<Zone> getZones() {
        List<Zone> zones = zoneMapper.getElements();
        zones.forEach(this::addCoordinatesToZone);
        return zones;
    }

    private void addCoordinatesToZone(Zone zone) {
        zone.setCoordinates(coordinateToLatLon(coordinateMapper.getCoordinates(zone.getUuid())));
    }

    private List<LatLon> coordinateToLatLon(List<Coordinate> coordinates) {
        List<LatLon> latLonList = new ArrayList<>();
        coordinates.forEach(i -> latLonList.add(coordinateToLatLon(i)));
        return latLonList;
    }

    private LatLon coordinateToLatLon(Coordinate coordinate) {
        return new LatLon(coordinate.getLat(), coordinate.getLng());
    }

    public Zone getZoneByID() {
        return null;
    }

    public void addZoneSync(Zone zone) {
        zoneMapper.add(zone);
        addCoordinatesSync(zone.getCoordinates(), zone.getUuid());
        FirestoreService.getInstance().log(TAG, "new zone added : " + zone);
    }

    public void addCoordinatesSync(List<LatLon> coordinates, String zoneUUID) {
        coordinates.forEach(i -> coordinateMapper.add(new Coordinate(i.getLat(), i.getLon(), zoneUUID)));
    }
}
