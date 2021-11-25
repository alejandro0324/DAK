package application.dak.DAK.backend.dashboard.controllers;


import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.dashboard.mappers.DashboardMapper;
import application.dak.DAK.backend.packages.mappers.PackagesMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardMapper dashboardMapper;


    @GetMapping("/getDailyIncome")
    public String getDailyIncome() {
        return dashboardMapper.getDailyIncome();
    }

    @GetMapping("/listDashboardPackages")
    public List<Package> listDashboardPackages() {
        return dashboardMapper.listDashboardPackages();
    }
}
