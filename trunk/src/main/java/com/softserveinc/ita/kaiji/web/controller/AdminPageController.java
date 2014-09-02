package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.MultiplayerGameInfoDto;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.util.email.MailSender;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * @author Paziy Evgeniy, Sydorenko Oleksandra
 * @version 2.0
 * @since 08.04.14
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminPageController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GameInfoEntityDAO gameInfoEntityDAO;

    @Autowired
    private GameHistoryEntityDAO gameHistoryEntityDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @RequestMapping
    public String adminAPI() {
        return "admin-panel";
    }

    @RequestMapping("/users")
    public String userPage(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("usersList", userDAO.findAll());
        return "admin-user";
    }

    @RequestMapping(value = "/users/save")
    public String saveUser(@ModelAttribute("newUser") User user) {
        user.setPassword(RandomStringUtils.randomAlphanumeric(7));
        mailSender.send(user.getEmail(), "Welcome to Kaiji", user.getNickname(), user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);

        return "redirect:/admin/users";
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
            return "redirect:/admin/users";
        }
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/users/remove")
    public String removeUser(@RequestParam("id") Integer id) {
        userDAO.delete(userDAO.findOne(id));
        return "redirect:/admin/users";
    }

    @RequestMapping("/gameinfo")
    public String gameInfoPage(Model model) {

        model.addAttribute("usersList", userDAO.findAll());
        model.addAttribute("gamesList", gameInfoEntityDAO.findAll());
        MultiplayerGameInfoDto multiplayerGameInfoDto = new MultiplayerGameInfoDto();
        multiplayerGameInfoDto.setGameName("Zoro");
        multiplayerGameInfoDto.setNumberOfStars(2);
        multiplayerGameInfoDto.setNumberOfCards(2);
        multiplayerGameInfoDto.setNumberOfPlayers(2);
        multiplayerGameInfoDto.setGameTimeout(1000);
        model.addAttribute("multiplayerGameInfo", multiplayerGameInfoDto);

        return "admin-gameinfo";
    }

    @RequestMapping(value = "/gameinfo", method = RequestMethod.POST)
    public String getGameInfoEntity(@RequestParam("userId") String userId, RedirectAttributes redirectAttributes) {
        if (!userId.equals("default")) {
            redirectAttributes.addFlashAttribute("userGames", gameInfoEntityDAO.findByUser(Integer.valueOf(userId)));
        }
        return "redirect:/admin/gameinfo";
    }

    @RequestMapping(value = "/gameinfo/remove")
    public String removeGameInfoEntity(@RequestParam("id") Integer id) {
        gameInfoEntityDAO.delete(id);
        return "redirect:/admin/gameinfo";
    }

    @RequestMapping(value = "/gamehistory")
    public String getGameHistoryPage(Model model) {
        model.addAttribute("usersList", userDAO.findAll());
        model.addAttribute("gameHistoryList", gameHistoryEntityDAO.findAll());
        return "admin-gamehistory";
    }

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
        return "redirect:/admin/gamehistory";
    }

    @RequestMapping(value = "/gamehistory/remove")
    public String removeGameHistoryEntity(@RequestParam("id") Integer id) {
        gameHistoryEntityDAO.delete(id);
        return "redirect:/admin/gamehistory";
    }

}
