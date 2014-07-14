package com.softserveinc.ita.kaiji.web.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;

/**
 * Controller for system configuring
 *
 * @author Shaposhnik Bohdan
 * @version 1.1
 * @since 01.04.14
 */

@Controller
@RequestMapping("/config")
public class SystemConfigurationController {

    private static final Logger LOG = Logger
            .getLogger(SystemConfigurationController.class);

    @Autowired
    SystemConfigurationService systemConfigurationService;

    @RequestMapping(method = RequestMethod.GET)
    public String initModelForBinding(Model model, HttpServletResponse response) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("initModelForBinding: started");
        }

        SystemConfiguration systemConfiguration = new SystemConfiguration();
        systemConfiguration = systemConfigurationService
                .getSystemConfiguration();

        model.addAttribute("systemConfiguration", systemConfiguration);

        if (LOG.isTraceEnabled()) {
            LOG.trace("initModelForBinding: systemConfiguration binded to the form");
        }

        return "system-configuration";
    }

    @RequestMapping(value = "handler", method = RequestMethod.POST)
    public String handlePost(
            @ModelAttribute("systemConfiguration") @Valid SystemConfiguration systemConfiguration,
            BindingResult br, HttpServletResponse response, Model model,
            @RequestParam String action) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Checking BindingResult for mistakes");
        }

        if (br.hasErrors()) {

            if (LOG.isInfoEnabled()) {
                LOG.info("Binding failed: systemConfiguration model is NOT VALID");
            }

            return "redirect:/config/";
        }
        switch (action) {
            case "refresh":
                systemConfigurationService.getSystemConfiguration();
                break;

            case "save":
                systemConfigurationService
                        .saveSystemConfiguration(systemConfiguration);
                break;
            default:
        }

        return "redirect:/config/";

    }
}
