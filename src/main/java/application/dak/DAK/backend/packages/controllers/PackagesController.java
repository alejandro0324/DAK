package application.dak.DAK.backend.packages.controllers;

import application.dak.DAK.backend.common.models.*;
import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.packages.mappers.PackagesMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static application.dak.DAK.backend.utils.Constants.CURRENT_LAT;
import static application.dak.DAK.backend.utils.Constants.CURRENT_LNG;

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
        Tracking tracking = new Tracking(UUID.randomUUID().toString(), CURRENT_LAT, CURRENT_LNG, null, new Date(System.currentTimeMillis()), PackageState.IN_LOCAL.toString());
        packagesMapper.createTracking(tracking);
        packagesMapper.createTrackingDet(tracking);
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

    @GetMapping("/listAllReadyPackagesLike/{number}")
    public List<Package> listAllReadyPackagesLike(@PathVariable("number") String number){
        List<Package> list = packagesMapper.listAllReadyPackagesLike(number + "%");
        return list;
    }

    @GetMapping("/listAllReadyPackages")
    public List<Package> listAllReadyPackages(){
        List<Package> list = packagesMapper.listAllReadyPackages();
        return list;
    }

}
