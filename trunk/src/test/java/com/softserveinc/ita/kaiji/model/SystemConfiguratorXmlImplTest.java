package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 30.03.14.
 */
public class SystemConfiguratorXmlImplTest {
    private static final String CONFIGURATION_FILE = "system-configuration-test.xml";
    private static SystemConfigurator systemConfigurator;
    private static Path filePath;
    private SystemConfiguration systemConfiguration;

    @BeforeClass
    public static void setUp() {
        filePath = Paths.get(CONFIGURATION_FILE);
        systemConfigurator = new SystemConfiguratorXmlImpl();
    }

    @Before
    public void initSystemConfiguration() {
        systemConfiguration = new SystemConfiguration();
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            Files.createFile(filePath);

            systemConfiguration.setGameConnectionTimeout(200L);
            systemConfiguration.setRoundTimeout(50L);
            systemConfiguration.setGameName("Test");
            systemConfiguration.setUserName("User");
            systemConfiguration.setNumberOfStars(3);
            systemConfiguration.setBotType(Bot.Types.HARD);
            systemConfiguration.setNumberOfCards(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void tearDown() {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loadSystemConfigurationTest() {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(systemConfiguration, filePath.toFile());

            systemConfigurator.setConfigurationFile(filePath);
            systemConfigurator.loadSystemConfiguration();
            SystemConfiguration expectedSystemConfiguration = systemConfigurator.getSystemConfiguration();

            assertEquals(systemConfiguration, expectedSystemConfiguration);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveSystemConfiguration() {

        try {

            systemConfigurator.saveSystemConfiguration(systemConfiguration);
            JAXBContext jaxbContext = JAXBContext.newInstance(SystemConfiguration.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SystemConfiguration savedSystemConfiguration = (SystemConfiguration) jaxbUnmarshaller
                    .unmarshal(filePath.toFile());
            assertEquals(systemConfiguration, savedSystemConfiguration);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
