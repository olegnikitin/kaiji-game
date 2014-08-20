package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rest/system-configuration")
@RestController
public class RestSystemConfiguration {

    @Autowired
    SystemConfigurationService systemConfigurationService;

    @RequestMapping(produces = "application/json",method = RequestMethod.GET)
    public ResponseEntity<SystemConfiguration>  getSystemConfiguration() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(systemConfigurationService.getSystemConfiguration(), headers, HttpStatus.OK);
    }

    /*
    Example "{"gameName": "Duel",
              "userName": "Zoro",
              "numberOfCards": 4,
              "numberOfStars": 3,
              "botType": "EASY",
              "gameConnectionTimeout": 20,
              "roundTimeout": 15}"
     */

    @RequestMapping(consumes = "application/json",method = RequestMethod.POST)
    public ResponseEntity setSystemConfiguration(@RequestBody SystemConfiguration systemConfiguration) {
        systemConfigurationService.saveSystemConfiguration(systemConfiguration);
        return new ResponseEntity(HttpStatus.OK);
    }
}
