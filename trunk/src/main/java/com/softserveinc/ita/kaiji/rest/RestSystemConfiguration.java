package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/system-configuration")
public class RestSystemConfiguration {

    @Autowired
    SystemConfigurationService systemConfigurationService;

    @GET
    @Produces("application/json")
    public Response getSystemConfiguration() {
        return Response.ok(systemConfigurationService.getSystemConfiguration()).build();
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
    @POST
    @Consumes("application/json")
    public Response setSystemConfiguration(SystemConfiguration systemConfiguration) {
        systemConfigurationService.saveSystemConfiguration(systemConfiguration);
        return Response.ok().build();
    }
}
