package application.dak.DAK.backend.zones.services;

import application.dak.DAK.backend.common.dto.Zone;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private static ZoneService zoneService = new ZoneService();

    public static ZoneService getInstance(){
        if(zoneService == null)
            zoneService = new ZoneService();
        return zoneService;
    }

    public void addZoneAsync(Zone zoneBeingEdited){

    }

    public boolean removeZone(){
        return false;
    }

    public List<Zone> getZones(){
        return null;
    }

    public Zone getZoneByID(){
        return null;
    }
}
