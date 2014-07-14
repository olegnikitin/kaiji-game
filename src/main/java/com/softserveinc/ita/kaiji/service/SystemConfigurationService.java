package com.softserveinc.ita.kaiji.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.SystemConfiguratorXmlImpl;

/**
 * @author Bohdan Shaposhnik
 * @version 1.0
 * @since 01.04.14.
 */
@Service
public class SystemConfigurationService {

    @Autowired
    SystemConfiguratorXmlImpl systemConfigurator;


    /**
     * Sets system configuration
     * to be the current system
     * configuration.
     *
     * @param systemConfiguration
     */
    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        systemConfigurator.setSystemConfiguration(systemConfiguration);
    }

    /**
     * Returns current system configuration
     *
     * @return systemConfiguration
     */
    public SystemConfiguration getSystemConfiguration() {
        return systemConfigurator.getSystemConfiguration();
    }

    /**
     * Loads system configuration
     * from a file. Sets it to be
     * the current system configuration.
     */
    public void loadSystemConfiguration() {
        systemConfigurator.loadSystemConfiguration();
    }

    /**
     * Saves current system configuration
     * to a file.
     */
    public void saveSystemConfiguration(SystemConfiguration systemConfiguration) {
        systemConfigurator.saveSystemConfiguration(systemConfiguration);
    }

}
