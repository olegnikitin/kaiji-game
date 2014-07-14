package com.softserveinc.ita.kaiji.web.controller;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Evgenii Semenkov
 * @since March 14, 2014
 */

public class TestStartPageController {

    @Test
    public void testStartPageController() {

        StartPageController startPageController = new StartPageController();

        Assert.assertEquals("StartPageController must return the start-page view ", "start-page", startPageController.getStartPage());

    }

}
