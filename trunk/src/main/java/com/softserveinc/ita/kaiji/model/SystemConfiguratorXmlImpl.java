package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private static final String DEFAULT_GAME_NAME = "Duel";
    private static final String DEFAULT_USER_NAME = "Zoro";
    private static final Integer DEFAULT_CARDS_NUMBER = 4;
    private static final Integer DEFAULT_STARS_NUMBER = 3;
    private static final Bot.Types DEFAULT_BOT_TYPE = Bot.Types.EASY;
    // 260 seconds -> 4 minutes
    private static final Long DEFAULT_GAME_CONNECTION_TIMEOUT = 260L;
    // 90 seconds
    private static final Long DEFAULT_ROUND_CONNECTION_TIMEOUT = 90L;

    private static final Integer DEFAULT_PLAYERS_NUMBER = 5;

    // 60 minutes -> 1 hour
    private static final Long DEFAULT_MULTIUSER_GAME_DURATION = 60L;


    private SystemConfiguration currentSystemConfiguration;
    private Path filePath;

    public SystemConfiguratorXmlImpl() {
        this.filePath = Paths.get("system-configuration.xml");
    }

    @Override
    public void setConfigurationFile(Path filePath) {
        this.filePath = filePath;
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
                throw new IllegalArgumentException("Incorrect configuration parameters");
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
        currentSystemConfiguration.setNumberOfStars(DEFAULT_STARS_NUMBER);
        currentSystemConfiguration.setBotType(DEFAULT_BOT_TYPE);
        currentSystemConfiguration.setGameConnectionTimeout(DEFAULT_GAME_CONNECTION_TIMEOUT);
        currentSystemConfiguration.setRoundTimeout(DEFAULT_ROUND_CONNECTION_TIMEOUT);
        currentSystemConfiguration.setNumberOfPlayers(DEFAULT_PLAYERS_NUMBER);
        currentSystemConfiguration.setMultiplayerGameDuration(DEFAULT_MULTIUSER_GAME_DURATION);

        saveSystemConfiguration(currentSystemConfiguration);
    }

    private boolean checkSystemConfiguration() {
        return currentSystemConfiguration.getGameConnectionTimeout() != null
                && currentSystemConfiguration.getGameName() != null
                && currentSystemConfiguration.getBotType() != null
                && currentSystemConfiguration.getNumberOfCards() != null
                && currentSystemConfiguration.getRoundTimeout() != null
                && currentSystemConfiguration.getUserName() != null
                && currentSystemConfiguration.getNumberOfStars() != null
                && currentSystemConfiguration.getNumberOfPlayers() != null
                && currentSystemConfiguration.getMultiplayerGameDuration() != null;

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
