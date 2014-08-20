package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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

    @RequestMapping("/users")
    public String userPage(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("usersList", userDAO.findAll());
        return "admin-user";
    }

    @RequestMapping(value = "/users/save")
    public String saveUser(@ModelAttribute("newUser") User user) {
        user.setPassword("12345");
        userDAO.save(user);
        return "redirect:/dao/users";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String getUser(@RequestParam("nickname") String nickname, @RequestParam("email") String email,
                          RedirectAttributes redirectAttributes) {
        User user = null;
        if (!nickname.equals("") && !email.equals("")) {
            if (userDAO.findByEmail(email).getId().equals(userDAO.findByNickname(nickname).getId())) {
                user = userDAO.findByEmail(email);
            }
        } else if (!nickname.equals("")) {
            user = userDAO.findByNickname(nickname);
        } else if (!email.equals("")) {
            user = userDAO.findByEmail(email);
        } else {
            return "redirect:/dao/users";
        }
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/dao/users";
    }

    @RequestMapping(value = "/users/remove")
    public String removeUser(@RequestParam("id") Integer id) {
        userDAO.delete(userDAO.findOne(id));
        return "redirect:/dao/users";
    }

    @RequestMapping("/gameinfo")
    public String gameInfoPage(Model model) {
        model.addAttribute("usersList", userDAO.findAll());
        model.addAttribute("gamesList", gameInfoEntityDAO.findAll());
        return "admin-gameinfo";
    }

   /* @RequestMapping(value = "/save/game-info/{game-runtime-id}")
       public String saveGameInfoEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId) {

        GameInfo gameInfo = gameService.getGameInfo(gameId);
        GameInfoEntity gameInfoEntity = new GameInfoEntity(gameInfo);
        gameInfoEntityDAO.save(gameInfoEntity);
        model.addAttribute("data", gameInfoEntity);

        return "dao-test";
    }*/

    /*@RequestMapping(value = "/update/game-info/{game-runtime-id}/{saved-game-info-id}")
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
    }*/

    @RequestMapping(value = "/gameinfo", method = RequestMethod.POST)
    public String getGameInfoEntity(@RequestParam("userId") String userId, RedirectAttributes redirectAttributes) {
        if (!userId.equals("default")) {
            redirectAttributes.addFlashAttribute("userGames", gameInfoEntityDAO.findByUser(Integer.valueOf(userId)));
        }
        return "redirect:/dao/gameinfo";
    }

    @RequestMapping(value = "/gameinfo/remove")
    public String removeGameInfoEntity(@RequestParam("id") Integer id) {
        gameInfoEntityDAO.delete(gameInfoEntityDAO.findOne(id));
        return "redirect:/dao/gameinfo";
    }

    @RequestMapping(value = "/gamehistory")
    public String getGameHistoryPage(Model model) {
        model.addAttribute("usersList", userDAO.findAll());
        model.addAttribute("gameHistoryList", gameHistoryEntityDAO.findAll());
        return "admin-gamehistory";
    }

   /* @RequestMapping(value = "/save/game-history/{game-runtime-id}")
    public String saveGameHistoryEntity(
            Model model,
            @PathVariable(value = "game-runtime-id") Integer gameId) {

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameHistoryEntity gameHistoryEntity = new GameHistoryEntity(gameHistory);
        gameHistoryEntityDAO.save(gameHistoryEntity);
        model.addAttribute("data", gameHistoryEntity);

        return "dao-test";
    }*/

   /* @RequestMapping(value = "/update/game-info/{game-runtime-id}/{saved-game-history-id}")
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
    }*/

    @RequestMapping(value = "/gamehistory", method = RequestMethod.POST)
    public String getGameHistoryEntity(@RequestParam(value = "id") Integer id,
                                       @RequestParam(value = "userId") String userId, RedirectAttributes redirectAttributes) {
        List<GameHistoryEntity> result = new ArrayList<>();
        if (id != null) {
            GameHistoryEntity gameHistoryEntity = gameHistoryEntityDAO.findOne(id);
            if (gameHistoryEntity != null) {
                result.add(gameHistoryEntity);
            }
        } else if (!userId.equals("default")) {
            result.addAll(gameHistoryEntityDAO.findByWinner(Integer.valueOf(userId)));
        }

        redirectAttributes.addFlashAttribute("searchGameHistory", result);
        return "redirect:/dao/gamehistory";
    }

    @RequestMapping(value = "/gamehistory/remove")
    public String removeGameHistoryEntity(@RequestParam("id") Integer id) {
        gameHistoryEntityDAO.delete(gameHistoryEntityDAO.findOne(id));
        return "redirect:/dao/gamehistory";
    }

}
