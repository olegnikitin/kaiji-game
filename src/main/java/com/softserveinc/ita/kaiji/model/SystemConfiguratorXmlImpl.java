package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softserveinc.ita.kaiji.model.player.bot.Bot;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Vladyslav Shelest
 * @version 1.8
 * @since 30.03.14.
 */
@Component
@Scope("singleton")
public class SystemConfiguratorXmlImpl implements SystemConfigurator {

    private static final Logger LOG = Logger.getLogger(SystemConfiguratorXmlImpl.class);

    private static final String CONFIGURATION_FILE = "system-configuration.xml";
    private static final String DEFAULT_GAME_NAME = "Duel";
    private static final String DEFAULT_USER_NAME = "Zoro";
    private static final Integer DEFAULT_CARDS_NUMBER = 4;
    private static final Bot.Types DEFAULT_BOT_TYPE = Bot.Types.EASY;
    //300000L -> 5 minutes
    private static final Long DEFAULT_GAME_CONNECTION_TIMEOUT = 300000L;
    //60000L -> 1 minute
    private static final Long DEFAULT_ROUND_CONNECTION_TIMEOUT = 60000L;

    private SystemConfiguration currentSystemConfiguration;
    private Path filePath;

    public SystemConfiguratorXmlImpl() {
        this.filePath = Paths.get(CONFIGURATION_FILE);
    }

    @Override
    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.currentSystemConfiguration = systemConfiguration;
    }

    @Override
    public SystemConfiguration getSystemConfiguration() {
        loadSystemConfiguration();

        return this.currentSystemConfiguration;
    }

    @Override
    public void loadSystemConfiguration() {

        if (Files.notExists(filePath)) {
            LOG.trace("Create new configuration");
            createDefaultSystemConfiguration(filePath);
            return;
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("loadSystemConfiguration");
        }
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            this.currentSystemConfiguration = (SystemConfiguration) jaxbUnmarshaller.unmarshal(filePath.toFile());
            if (!checkSystemConfiguration()) {
                throw new RuntimeException("Incorrect configuration parameters");
            }

        } catch (JAXBException e) {
            LOG.error("Unable to load configuration from file. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveSystemConfiguration(SystemConfiguration systemConfiguration) {

        if (Files.notExists(filePath)) {
            LOG.trace("create new configuration file");
            createFile(filePath);
            return;
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("saveSystemConfiguration");
        }

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(systemConfiguration, filePath.toFile());

        } catch (JAXBException e) {
            LOG.error("Unable to save configuration to file. " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void createDefaultSystemConfiguration(Path path) {

        createFile(path);

        this.currentSystemConfiguration = new SystemConfiguration();
        currentSystemConfiguration.setGameName(DEFAULT_GAME_NAME);
        currentSystemConfiguration.setUserName(DEFAULT_USER_NAME);
        currentSystemConfiguration.setNumberOfCards(DEFAULT_CARDS_NUMBER);
        currentSystemConfiguration.setBotType(DEFAULT_BOT_TYPE);
        currentSystemConfiguration.setGameConnectionTimeout(DEFAULT_GAME_CONNECTION_TIMEOUT);
        currentSystemConfiguration.setRoundTimeout(DEFAULT_ROUND_CONNECTION_TIMEOUT);

        saveSystemConfiguration(currentSystemConfiguration);
    }

    private boolean checkSystemConfiguration() {
        return (currentSystemConfiguration.getGameConnectionTimeout() != null
                && currentSystemConfiguration.getGameName() != null
                && currentSystemConfiguration.getBotType() != null
                && currentSystemConfiguration.getNumberOfCards() != null
                && currentSystemConfiguration.getRoundTimeout() != null
                && currentSystemConfiguration.getUserName() != null);

    }

    private void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            LOG.error("Unable to create new configuration file " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
