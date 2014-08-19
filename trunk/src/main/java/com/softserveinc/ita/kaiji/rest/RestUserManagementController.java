package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.rest.dto.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.dto.GameInfoRestDto;
import com.softserveinc.ita.kaiji.rest.dto.UserRestDto;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.UserRole;
import com.softserveinc.ita.kaiji.rest.dto.GameHistoryRestDto;
import com.softserveinc.ita.kaiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Path("/management")
@Component
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

    private static final Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");

    //http://localhost:8080/rest/management/statistic/user/petya
    @GET
    @Path("/statistic/{nickname}")
    @Produces("application/json")
    public Response getPlayerStats(@PathParam("nickname") String nickname) {
        if (nickname == null || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (userDAO.getByNickname(nickname) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        return Response.ok(userService.getStatsForUser(nickname)).build();
    }

    //http://localhost:8080/rest/management/users
    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getUsers() {
        List<UserRestDto> users = new ArrayList<>();
        for (User user : userDAO.getAll()) {
            users.add(convertToRestDto.userToDto(user));
        }

        return Response.ok(users).build();
    }

    //http://localhost:8080/rest/management/user/petya
    @GET
    @Path("/user/{nickname}")
    @Produces("application/json")
    public Response getUser(@PathParam("nickname") String nickname) {
        if (nickname == null || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (userDAO.getByNickname(nickname) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        return Response.ok(convertToRestDto.userToDto(userDAO.getByNickname(nickname))).build();
    }


    //http://localhost:8080/rest/management/user/add?name=sasha&nickname=ssh&email=1345@gmail.com&password=123
    @POST
    @Path("/user/add")
    @Produces("application/json")
    public Response AddUser(@QueryParam("name") String name,
                            @QueryParam("nickname") String nickname,
                            @QueryParam("email") String email,
                            @QueryParam("password") String password) {

        if (name == null || nickname == null || email == null || password == null) {
            System.err.println("NULL");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if ("".equals(name) || "".equals(nickname) || "".equals(email) || "".equals(password)) {
            System.err.println("EMPTY");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (!pattern.matcher(email).matches()) {
            System.err.println("WRONG EMAIL");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (userDAO.getByNickname(nickname) != null) {
            System.err.println("NICKNAME");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (userDAO.getByEmail(email) != null) {
            System.err.println("EMAIL EXIST");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        User user = new User();
        user.setName(name);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegistrationDate(new Date());
        user.getRoles().add(UserRole.USER_ROLE);
        userDAO.save(user);

        return Response.ok(convertToRestDto.userToDto(user)).build();
    }

    //http://localhost:8080/rest/management/user/delete/ssh
    @DELETE
    @Path("/user/delete/{nickname}")
    public Response removeUser(@PathParam("nickname") String nickname) {

        if (nickname == null || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        User user = userDAO.getByNickname(nickname);
        if (userDAO.getByNickname(nickname) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        userDAO.delete(user);
        return Response.ok().build();
    }

    //http://localhost:8080/rest/management/gameinfos
    @GET
    @Path("/gameinfos")
    @Produces("application/json")
    public Response getAllGameInfo() {

        List<GameInfoRestDto> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.getAll()) {
            gameInfos.add(convertToRestDto.gameInfoToDto(gameInfo));
        }
        return Response.ok(gameInfos).build();
    }

    //http://localhost:8080/rest/management/gameinfo/=petya
    @GET
    @Path("/gameinfo/{nickname}")
    @Produces("application/json")
    public Response getGameInfoByUser(@PathParam("nickname") String nickname) {

        if (nickname == null || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        User user = userDAO.getByNickname(nickname);
        if (user == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        List<GameInfoRestDto> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.getGameInfoFor(user.getId())) {
            gameInfos.add(convertToRestDto.gameInfoToDto(gameInfo));
        }
        return Response.ok(gameInfos).build();
    }

    //http://localhost:8080/rest/management/gameinfo/delete/13
    @DELETE
    @Path("/gameinfo/delete/{gameId}")
    public Response deleteGameInfo(@PathParam("gameId") Integer gameId) {

        if (gameId == null || "".equals(gameId)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        GameInfoEntity gameInfo = gameInfoEntityDAO.get(gameId);
        if (gameInfo == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        gameInfoEntityDAO.delete(gameInfo);
        return Response.ok().build();
    }

    //http://localhost:8080/rest/management/gamehistories
    @GET
    @Path("/gamehistories")
    @Produces("application/json")
    public Response getAllGameHistory() {

        List<GameHistoryRestDto> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.getAll()) {
            gameHistories.add(convertToRestDto.gameHistoryToDto(gameHistory));
        }
        return Response.ok(gameHistories).build();
    }

    //http://localhost:8080/rest/management/gamehistory/delete/12
    @DELETE
    @Path("/gamehistory/delete/{gameId}")
    public Response deleteGameHistory(@PathParam("gameId") Integer gameId) {

        if (gameId == null || "".equals(gameId)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        GameHistoryEntity gameHistory = gameHistoryEntityDAO.get(gameId);
        if (gameHistory == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        gameHistoryEntityDAO.delete(gameHistory);

        return Response.ok().build();
    }

    //http://localhost:8080/rest/management/gameHistory/vasya
    @GET
    @Path("/gamehistory/{nickname}")
    @Produces("application/json")
    public Response getGameHistoryByUser(@PathParam("nickname") String nickname) {

        if (nickname == null || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        User user = userDAO.getByNickname(nickname);
        if (user == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        List<GameHistoryRestDto> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.getGameHistoryByWinner(user.getId())) {
            gameHistories.add(convertToRestDto.gameHistoryToDto(gameHistory));
        }

        return Response.ok(gameHistories).build();
    }
}