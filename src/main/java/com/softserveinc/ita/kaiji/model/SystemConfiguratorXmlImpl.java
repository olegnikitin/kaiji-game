package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import org.apache.log4j.Logger;
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
public class SystemConfiguratorXmlImpl implements SystemConfigurator {

    private static final Logger LOG = Logger.getLogger(SystemConfiguratorXmlImpl.class);

    private SystemConfiguration currentSystemConfiguration;
    private Path filePath;

    public SystemConfiguratorXmlImpl() {
        setFilePath();
        loadSystemConfiguration();
    }

    @Override
    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.currentSystemConfiguration = systemConfiguration;

    }

    @Override
    public SystemConfiguration getSystemConfiguration() {
        return this.currentSystemConfiguration;
    }

    @Override
    public void loadSystemConfiguration() {

        if (!Files.exists(filePath)) {
            createDefaultSystemConfiguration(filePath);
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("loadSystemConfiguration");
        }
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            if (LOG.isDebugEnabled()) {
                LOG.debug("unmarshalling file");
            }

            SystemConfiguration systemConfiguration = (SystemConfiguration) jaxbUnmarshaller.unmarshal(filePath.toFile());

            this.currentSystemConfiguration = systemConfiguration;

        } catch (JAXBException e) {
            LOG.error("Unable to load configuration from file. " + e.getMessage());
        }

    }

    @Override
    public void saveSystemConfiguration(SystemConfiguration systemConfiguration) {

        if (!Files.exists(filePath)) {
            createDefaultSystemConfiguration(filePath);
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("saveSystemConfiguration");
        }

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            this.currentSystemConfiguration = systemConfiguration;

            if (LOG.isDebugEnabled()) {
                LOG.debug("marshalling file");
            }

            jaxbMarshaller.marshal(currentSystemConfiguration, filePath.toFile());

        } catch (JAXBException e) {
            LOG.error("Unable to save configuration to file. " + e.getMessage());
        }

    }

    private void setFilePath() {

        Path path = Paths.get("system-configuration.xml");

        if (Files.exists(path)) {
            this.filePath = path;
            if (!(checkSystemConfiguration())) {
                createDefaultSystemConfiguration(path);
            }
        } else {
            createDefaultSystemConfiguration(path);
        }

    }

    private void createDefaultSystemConfiguration(Path path) {
        createFile(path);
        this.filePath = path;

        this.currentSystemConfiguration = new SystemConfiguration();
        currentSystemConfiguration.setGameName("Duel");
        currentSystemConfiguration.setUserName("Zoro");
        currentSystemConfiguration.setNumberOfCards(4);
        currentSystemConfiguration.setBotType(Bot.Types.EASY);
        currentSystemConfiguration.setGameConnectionTimeout(300000L);
        currentSystemConfiguration.setRoundTimeout(60000L);

        saveSystemConfiguration(currentSystemConfiguration);
    }

    private boolean checkSystemConfiguration() {
        loadSystemConfiguration();
        if (currentSystemConfiguration.getGameConnectionTimeout() != null
                || currentSystemConfiguration.getGameName() != null
                || currentSystemConfiguration.getBotType() != null
                || currentSystemConfiguration.getNumberOfCards() != null
                || currentSystemConfiguration.getRoundTimeout() != null
                || currentSystemConfiguration.getUserName() != null ) {
            return true;
        } else {
            return false;
        }
    }

    private void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            LOG.error("Unable to create new configuration file " + e.getMessage());
        }
    }
}
