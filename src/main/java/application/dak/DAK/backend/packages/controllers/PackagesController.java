package application.dak.DAK.backend.packages.controllers;

import application.dak.DAK.backend.common.models.*;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.packages.mappers.PackagesMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/packages")
@AllArgsConstructor
public class PackagesController {

    private final PackagesMapper packagesMapper;

    @GetMapping("/getAllPackages")
    public List<Package> getAllPackages() {
        return packagesMapper.getAllPackages();
    }

    @PostMapping("/createTracking")
    public Tracking createTracking() {
        Tracking tracking = new Tracking();
        packagesMapper.createTracking(tracking);
        packagesMapper.createTrackingDet(tracking.getId(), new Date(System.currentTimeMillis()));
        return tracking;
    }

    @PostMapping("/createPackage")
    public void createPackage(@RequestBody final Package pack) {
        packagesMapper.createPackage(pack);
    }

    @GetMapping("/getAllPackagesLike/{number}")
    public List<Package> getAllPackagesLike(@PathVariable("number") String number){
        return packagesMapper.getAllPackagesLike(number + "%");
    }

}
