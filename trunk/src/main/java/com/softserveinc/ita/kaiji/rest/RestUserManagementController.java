package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.rest.dto.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.dto.GameInfoRestDto;
import com.softserveinc.ita.kaiji.rest.dto.UserRestDto;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.UserRole;
import com.softserveinc.ita.kaiji.rest.dto.GameHistoryRestDto;
import com.softserveinc.ita.kaiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Konstantin Shevchuk
 * @version 1.5
 * @since 14.07.14.
 */

@RestController
@RequestMapping("rest/management")
public class RestUserManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GameInfoEntityDAO gameInfoEntityDAO;

    @Autowired
    private GameHistoryEntityDAO gameHistoryEntityDAO;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ConvertToRestDto convertToRestDto;

    private HttpHeaders headers;

    private static final Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");

    @RequestMapping(value = "/statistic/{nickname}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<StatisticsDTO> getPlayerStats(@PathVariable("nickname") String nickname) {

        if (userDAO.findByNickname(nickname) == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(userService.getStatsForUser(nickname), headers, HttpStatus.OK);//Response.ok(userService.getStatsForUser(nickname)).build();
    }

    @RequestMapping(value = "/users", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<UserRestDto>> getUsers() {
        List<UserRestDto> users = new ArrayList<>();
        for (User user : userDAO.findAll()) {
            users.add(convertToRestDto.userToDto(user));
        }
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{nickname}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<UserRestDto> getUser(@PathVariable("nickname") String nickname) {

        if (userDAO.findByNickname(nickname) == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(convertToRestDto.userToDto(userDAO.findByNickname(nickname)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/add", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<UserRestDto> AddUser(@RequestParam("name") String name,
                                               @RequestParam("nickname") String nickname,
                                               @RequestParam("email") String email,
                                               @RequestParam("password") String password) {

        if (name == null || nickname == null || email == null || password == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if ("".equals(name) || "".equals(nickname) || "".equals(email) || "".equals(password)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (!pattern.matcher(email).matches()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (userDAO.findByNickname(nickname) != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (userDAO.findByEmail(email) != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(name);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegistrationDate(new Date());
        user.getRoles().add(UserRole.USER_ROLE);
        userDAO.save(user);
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(convertToRestDto.userToDto(userDAO.findByNickname(nickname)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/delete/{nickname}", method = RequestMethod.DELETE)
    public ResponseEntity removeUser(@PathVariable("nickname") String nickname) {

        User user = userDAO.findByNickname(nickname);
        if (userDAO.findByNickname(nickname) == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userDAO.delete(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/gameinfos", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<GameInfoRestDto>> getAllGameInfo() {

        List<GameInfoRestDto> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.findAll()) {
            gameInfos.add(convertToRestDto.gameInfoToDto(gameInfo));
        }
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(gameInfos, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/gameinfo/{nickname}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<GameInfoRestDto> getGameInfoByUser(@PathVariable("nickname") String nickname) {

        User user = userDAO.findByNickname(nickname);
        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<GameInfoRestDto> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.findByUser(user.getId())) {
            gameInfos.add(convertToRestDto.gameInfoToDto(gameInfo));
        }
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(gameInfos, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/gameinfo/delete/{gameId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteGameInfo(@PathVariable("gameId") Integer gameId) {

        GameInfoEntity gameInfo = gameInfoEntityDAO.findOne(gameId);
        if (gameInfo == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        gameInfoEntityDAO.delete(gameInfo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/gamehistories", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<GameHistoryRestDto>> getAllGameHistory() {

        List<GameHistoryRestDto> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.findAll()) {
            gameHistories.add(convertToRestDto.gameHistoryToDto(gameHistory));
        }
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(gameHistories, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/gamehistory/delete/{gameId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteGameHistory(@PathVariable("gameId") Integer gameId) {

        GameHistoryEntity gameHistory = gameHistoryEntityDAO.findOne(gameId);
        if (gameHistory == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        gameHistoryEntityDAO.delete(gameHistory);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/gamehistory/{nickname}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<GameHistoryRestDto>> getGameHistoryByUser(@PathVariable("nickname") String nickname) {

        User user = userDAO.findByNickname(nickname);
        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<GameHistoryRestDto> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.findByWinner(user.getId())) {
            gameHistories.add(convertToRestDto.gameHistoryToDto(gameHistory));
        }
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(gameHistories, headers, HttpStatus.OK);
    }
}