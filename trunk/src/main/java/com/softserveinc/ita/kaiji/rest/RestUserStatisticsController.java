package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ievgen Sukhov
 * @since 23.04.14
 * @version 1.0
 */


@RestController
@RequestMapping(value="/rest/statistic", produces="application/json")
public class RestUserStatisticsController {

    @Autowired
    UserService userService;

    @RequestMapping(value="user/{nickname}")
    public StatisticsDTO getPlayerStats(@PathVariable String nickname) {
         return userService.getStatsForUser(nickname);
    }

}
