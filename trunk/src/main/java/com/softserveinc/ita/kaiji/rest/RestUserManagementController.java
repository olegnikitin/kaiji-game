package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.UserRole;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.rest.convertors.GameHistoryJson;
import com.softserveinc.ita.kaiji.rest.convertors.GameInfoJson;
import com.softserveinc.ita.kaiji.rest.convertors.ToJsonConvertor;
import com.softserveinc.ita.kaiji.rest.convertors.UserJson;
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


/**
 * @author Ievgen Sukhov
 * @since 23.04.14
 * @version 1.0
 */

@Path("/management")
@Component
public class RestUserManagementController {

    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    private GameInfoEntityDAO gameInfoEntityDAO;

    @Autowired
    private GameHistoryEntityDAO gameHistoryEntityDAO;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ToJsonConvertor toJsonConvertor;

    private static final Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");

    //http://localhost:8080/rest/management/statistic?nickname=petya
    @GET
    @Path("/statistic")
    @Produces("application/json")
    public Response getPlayerStats(@QueryParam("nickname") String nickname) {
        if("".equals(nickname)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if(userDAO.getByNickname(nickname) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(userService.getStatsForUser(nickname)).build();
    }

    //http://localhost:8080/rest/management/users
    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getUsers() {
        List<UserJson> users = new ArrayList<>();
        for(User user: userDAO.getAll()){
            users.add(toJsonConvertor.UserToJson(user));
        }
        return Response.ok(users).build();
    }

    //http://localhost:8080/rest/management/user?nickname=petya
    @GET
    @Path("/user")
    @Produces("application/json")
    public Response getUser(@QueryParam("nickname") String nickname) {
        if("".equals(nickname)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if(userDAO.getByNickname(nickname) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(toJsonConvertor.UserToJson(userDAO.getByNickname(nickname))).build();
    }


    //http://localhost:8080/rest/management/user/add?name=sasha&nickname=ssh&email=1345@gmail.com&password=123
    @POST
    @Path("/user/add")
    @Produces("application/json")
    public Response AddUser(@QueryParam("name") String name,
                               @QueryParam ("nickname") String nickname,
                               @QueryParam("email") String email,
                               @QueryParam("password") String password) {
        if("".equals(name) && "".equals(nickname) && "".equals(email) && "".equals(password)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if(!pattern.matcher(email).matches()){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if(userDAO.getByNickname(nickname) != null){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if(userDAO.getByEmail(email) != null){
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
        return Response.ok(toJsonConvertor.UserToJson(user)).build();
    }

    //http://localhost:8080/rest/management/user/delete?nickname=ssh
    @POST
    @Path("/user/delete")
    @Produces("application/json")
    public Response removeUser(@QueryParam ("nickname") String nickname) {

        if("".equals(nickname) ){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        User user = userDAO.getByNickname(nickname);
        if(userDAO.getByNickname(nickname) == null){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        userDAO.delete(user);
        return Response.ok(toJsonConvertor.UserToJson(user)).build();
    }

    //http://localhost:8080/rest/management/gameinfos
    @GET
    @Path("/gameinfos")
    @Produces("application/json")
    public Response getAllGameInfo() {

        List<GameInfoJson> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.getAll()){
            gameInfos.add(toJsonConvertor.GameInfoToJson(gameInfo));
        }
        return Response.ok(gameInfos).build();
    }

    //http://localhost:8080/rest/management/gameinfo?nickname=petya
    @GET
    @Path("/gameinfo")
    @Produces("application/json")
    public Response getGameInfoByUser(@QueryParam ("nickname") String nickname) {
        if("".equals(nickname)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        User user = userDAO.getByNickname(nickname);
        if(user == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        List<GameInfoJson> gameInfos = new ArrayList<>();
        for (GameInfoEntity gameInfo : gameInfoEntityDAO.getGameInfoFor(user.getId())){
            gameInfos.add(toJsonConvertor.GameInfoToJson(gameInfo));
        }
        return Response.ok(gameInfos).build();
    }

    //http://localhost:8080/rest/management/gameinfo/delete?gameId=13
    @POST
    @Path("/gameinfo/delete")
    @Produces("application/json")
    public Response deleteGameInfo(@QueryParam ("gameId") Integer gameId) {
        if("".equals(gameId)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        GameInfoEntity gameInfo = gameInfoEntityDAO.get(gameId);
        if(gameInfo == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        gameInfoEntityDAO.delete(gameInfo);
        return Response.ok(toJsonConvertor.GameInfoToJson(gameInfo)).build();
    }

    //http://localhost:8080/rest/management/gamehistories
    @GET
    @Path("/gamehistories")
    @Produces("application/json")
    public Response getAllGameHistory() {
        List<GameHistoryJson> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.getAll()){
            gameHistories.add(toJsonConvertor.GameHistoryToJson(gameHistory));
        }
        return Response.ok(gameHistories).build();
    }

    //http://localhost:8080/rest/management/gamehistory/delete?gameId=?
    @POST
    @Path("/gamehistory/delete")
    @Produces("application/json")
    public Response deleteGameHistory(@QueryParam ("gameId") Integer gameId) {
        if("".equals(gameId)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        GameHistoryEntity gameHistory = gameHistoryEntityDAO.get(gameId);
        if(gameHistory == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        gameHistoryEntityDAO.delete(gameHistory);
        return Response.ok(toJsonConvertor.GameHistoryToJson(gameHistory)).build();
    }


    //http://localhost:8080/rest/management/gamehistory?nickname=?
    @GET
    @Path("/gamehistory")
    @Produces("application/json")
    public Response getGameHistoryByUser(@QueryParam ("nickname") String nickname) {

        if("".equals(nickname)){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        User user = userDAO.getByNickname(nickname);
        if(user == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        List<GameHistoryJson> gameHistories = new ArrayList<>();
        for (GameHistoryEntity gameHistory : gameHistoryEntityDAO.getGameHistoryByWinner(user.getId())){
            gameHistories.add(toJsonConvertor.GameHistoryToJson(gameHistory));
        }
        return Response.ok(gameHistories).build();
    }

}
