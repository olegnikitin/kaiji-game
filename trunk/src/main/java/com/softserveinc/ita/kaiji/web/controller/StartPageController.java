package com.softserveinc.ita.kaiji.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ievgen_s
 * @since 13.03.14
 */

@Controller
public class StartPageController {

    @RequestMapping("/")
    public String getStartPage() {

        return "start-page";

    }

    @RequestMapping("/statistic/user")
    public String getStatisticPage() {
        return "statistics";
    }

}
