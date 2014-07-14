package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 08.04.14
 */
@Controller
@RequestMapping(value = "/dao")
public class TestDAOController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GameInfoEntityDAO gameInfoEntityDAO;

    @Autowired
    private GameHistoryEntityDAO gameHistoryEntityDAO;

    @Autowired
    private GameService gameService;

    @RequestMapping
    public String daoAPI() {
        return "dao-test";
    }

//  !!!!!!!!!!!!!!!!!!!  User

    @RequestMapping(value = "/save/user/{nick}/{email}")
    public String saveUser(
            Model model,
            @PathVariable(value = "nick") String nick,
            @PathVariable(value = "email") String email) {
        User user = new User(nick, email, "pass");
        userDAO.save(user);

        model.addAttribute("data", user);
        return "dao-test";
    }

    @RequestMapping(value = "/get/user/{id}")
    public String getUser(Model model, @PathVariable(value = "id") Integer id) {
        model.addAttribute("data", userDAO.get(id));
        return "dao-test";

    }

    @RequestMapping(value = "/get/user/all")
    public String getAllUsers(Model model) {
        model.addAttribute("data", userDAO.getAll());
        return "dao-test";
    }

    @RequestMapping(value = "/remove/user/{id}")
    public String removeUser(@PathVariable(value = "id") Integer id) {
        userDAO.delete(userDAO.get(id));
        return "dao-test";
    }

//  !!!!!!!!!!!!!!!!!!!  GameInfoEntity

    @RequestMapping(value = "/save/game-info/{game-runtime-id}")
    public String saveGameInfoEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId) {

        GameInfo gameInfo = gameService.getGameInfo(gameId);
        GameInfoEntity gameInfoEntity = new GameInfoEntity(gameInfo);
        gameInfoEntityDAO.save(gameInfoEntity);
        model.addAttribute("data", gameInfoEntity);

        return "dao-test";
    }

    @RequestMapping(value = "/update/game-info/{game-runtime-id}/{saved-game-info-id}")
    public String updateGameInfoEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId,
            @PathVariable(value = "saved-game-info-id") Integer gameInfoEntityId) {

        GameInfo gameInfo = gameService.getGameInfo(gameId);
        GameInfoEntity gameInfoEntity = new GameInfoEntity(gameInfo);
        gameInfoEntity.setId(gameInfoEntityId);
        gameInfoEntityDAO.update(gameInfoEntity);
        model.addAttribute("data", gameInfoEntity);

        return "dao-test";
    }

    @RequestMapping(value = "/get/game-info/{id}")
    public String getGameInfoEntity(Model model, @PathVariable(value = "id") Integer id) {
        model.addAttribute("data", gameInfoEntityDAO.get(id));
        return "dao-test";
    }

    @RequestMapping(value = "/get/game-info/all")
    public String getGameInfoEntity(Model model) {
        model.addAttribute("data", gameInfoEntityDAO.getAll());
        return "dao-test";
    }

    @RequestMapping(value = "/remove/game-info/{id}")
    public String removeGameInfoEntity(@PathVariable(value = "id") Integer id) {
        gameInfoEntityDAO.delete(gameInfoEntityDAO.get(id));
        return "dao-test";
    }


//  !!!!!!!!!!!!!!!!!!!  GameHistoryEntity

    @RequestMapping(value = "/save/game-history/{game-runtime-id}")
    public String saveGameHistoryEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId) {

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameHistoryEntity gameHistoryEntity = new GameHistoryEntity(gameHistory);
        gameHistoryEntityDAO.save(gameHistoryEntity);
        model.addAttribute("data", gameHistoryEntity);

        return "dao-test";
    }

    @RequestMapping(value = "/update/game-info/{game-runtime-id}/{saved-game-history-id}")
    public String updateGameHistoryEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId,
            @PathVariable(value = "saved-game-history-id") Integer gameHistoryEntityId) {

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameHistoryEntity gameHistoryEntity = new GameHistoryEntity(gameHistory);
        gameHistoryEntity.setId(gameHistoryEntityId);
        gameHistoryEntityDAO.update(gameHistoryEntity);
        model.addAttribute("data", gameHistoryEntity);

        return "dao-test";
    }

    @RequestMapping(value = "/get/game-history/{id}")
    public String getGameHistoryEntity(Model model, @PathVariable(value = "id") Integer id) {
        model.addAttribute("data", gameHistoryEntityDAO.get(id));
        return "dao-test";
    }

    @RequestMapping(value = "/get/game-history/all")
    public String getGameHistoryEntity(Model model) {
        model.addAttribute("data", gameHistoryEntityDAO.getAll());
        return "dao-test";
    }

    @RequestMapping(value = "/remove/game-history/{id}")
    public String removeGameHistoryEntity(@PathVariable(value = "id") Integer id) {
        gameHistoryEntityDAO.delete(gameHistoryEntityDAO.get(id));
        return "dao-test";
    }

}
