package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import java.nio.file.Path;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 29.03.14.
 */
public interface SystemConfigurator {

    /**
     * Sets system configuration
     * to be the current system
     * configuration.
     * @param systemConfiguration
     */

    void setSystemConfiguration(SystemConfiguration systemConfiguration);

    /**
     * Returns current system configuration
     * @return systemConfiguration
     */

    SystemConfiguration getSystemConfiguration();

    /**
     * Loads system configuration
     * from a file. Sets it to be
     * the current system configuration.
     */

    void loadSystemConfiguration();

    /**
     * Sets system configuration
     * to be the current system
     * configuration.
     * Saves current system configuration
     * to a file.
     * @param systemConfiguration
     *
     */

    void saveSystemConfiguration(SystemConfiguration systemConfiguration);

    void setConfigurationFile(Path filePath);

}
