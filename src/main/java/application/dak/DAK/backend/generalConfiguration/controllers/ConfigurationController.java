package application.dak.DAK.backend.generalConfiguration.controllers;

import application.dak.DAK.backend.generalConfiguration.mappers.ConfigurationMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configuration")
@AllArgsConstructor
public class ConfigurationController {

    private final ConfigurationMapper configurationMapper;

    @GetMapping("/getTripTax")
    public Integer getTripTax() {
        return configurationMapper.tripTax();
    }

    @PostMapping("/configSave/{tripTax}")
    public void configSave(@PathVariable("tripTax") Integer tripTax) {
        configurationMapper.configSave(tripTax);
    }

}
