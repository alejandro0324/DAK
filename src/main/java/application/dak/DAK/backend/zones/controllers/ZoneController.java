package application.dak.DAK.backend.zones.controllers;

import application.dak.DAK.backend.common.dto.ListOfZones;
import application.dak.DAK.backend.common.dto.Zone;
import application.dak.DAK.backend.zones.services.ZoneService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/zones")
@AllArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping("/add")
    public String testController(@RequestBody final Zone zone) {
        zone.setUuid(UUID.randomUUID().toString());
        zoneService.addZoneAsync(zone);
        return null;
    }

    @GetMapping("/getZones")
    public ListOfZones getZones(){
        return new ListOfZones(zoneService.getZones());
    }

    @DeleteMapping("/{uuid}")
    public void removeZone(@PathVariable("uuid") final String uuid){
        zoneService.removeZone(uuid);
    }
}
