package application.dak.DAK.backend.expeditions.controllers;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.expeditions.services.ExpeditionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
