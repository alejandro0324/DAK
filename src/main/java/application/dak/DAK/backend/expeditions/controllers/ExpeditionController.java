package application.dak.DAK.backend.expeditions.controllers;

import application.dak.DAK.backend.common.models.DirectionsQueryResult;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.expeditions.services.ExpeditionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/expedition")

public class ExpeditionController {
    private final ExpeditionService expeditionService;

    @PostMapping("/calculateRoute")
    public String getDirections(@RequestBody final List<Package> packages) {
        expeditionService.calculateDirections(packages);
        return null;
    }

    @GetMapping("/")
    public DirectionsQueryResult getDirections(){
        return this.expeditionService.getDirectionsResult();
    }

    @PostMapping("/updatePackages")
    public String updatePackages(@RequestBody final List<Package> packages){
        expeditionService.updatePackagesAsync(packages);
        return null;
    }
}
