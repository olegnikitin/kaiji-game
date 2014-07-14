package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import org.junit.*;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 30.03.14.
 */
public class SystemConfiguratorXmlImplTest {

    private SystemConfiguration systemConfiguration;
    private SystemConfigurator systemConfigurator;

    @Before
    public void setUp() {
        systemConfigurator = new SystemConfiguratorXmlImpl();
    }

    @After
    public void tearDown() {
        systemConfiguration = null;
        systemConfigurator = null;
    }

    @Ignore
    @Test
    public void loadSystemConfigurationTest() {

    }

    @Test
    public void saveSystemConfigurationTest() {
/*
        try {
            Field field = SystemConfiguratorXmlImpl.class.getDeclaredField("xmlFilePath");
            field.setAccessible(true);
          //  field.set(systemConfigurator, new String("src/test/java/com/softserveinc/ita/kaiji/model/system-configuration-test.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
/*

        systemConfiguration = new SystemConfiguration();
        systemConfiguration.setGameTimeOut(21122L);
       // systemConfiguration.setBotType("HARD");
        systemConfigurator.setSystemConfiguration(systemConfiguration);

       // systemConfigurator.saveSystemConfiguration();
        systemConfigurator.loadSystemConfiguration();

        SystemConfiguration sysConf = systemConfigurator.getSystemConfiguration();
        Long actualGameTimeOut = sysConf.getGameTimeOut();
        Long expectedGameTimeOut = 21122L;
        assertEquals(expectedGameTimeOut, actualGameTimeOut);
*/


    }

}
