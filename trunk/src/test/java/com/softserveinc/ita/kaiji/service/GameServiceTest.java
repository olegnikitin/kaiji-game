package com.softserveinc.ita.kaiji.service;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.exception.game.NoSuchPlayerInGameException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
        "file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class GameServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private GameService service;

    @Autowired
    private UserService userService;


    private static GameInfoDto dto = new GameInfoDto();
    private static Integer gameId = null;

    @BeforeClass
    public static void setUp() throws Exception {

        dto.setBotType(Bot.Types.EASY);
        dto.setGameName("testGame");
        dto.setBotGame(true);
        dto.setNumberOfCards(3);
        dto.setNumberOfStars(3);
        dto.setPlayerName("petya");
        dto.setBotGame(true);
    }

    @Before
    public void init() {
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @After
    public void tearDown() throws Exception {
        gameId = null;
    }

    @Test
    public void finishGameTest() {

        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
        for (Player player : service.getGameInfo(infoId).getPlayers()) {
            player.finish();
        }

        Game game = service.getGameById(gameId);
        game.finishGame();
        service.releaseGameById(gameId);
        service.finishGame(gameId);

        assertNull(service.getGameInfo(gameId));
    }

    @Test
    public void createGameTest() {
        GameInfo gameInfo = service.getGameInfo(service.setGameInfo(dto));
        gameId = service.createGame(gameInfo);
        assertNotNull(gameId);
    }

    @Test
    public void getPlayerByIdTest() {
        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
        Integer playerId = service.getPlayerIdFromGame(gameId);

        String expected = dto.getPlayerName();
        String actual = userService.getPlayerById(playerId).getName();

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void getPlayerIdFromGameThrowExceptionTest() {
        service.getPlayerIdFromGame(null);
    }

    @Test(expected = NullPointerException.class)
    public void finishGameThrowExceptionTest() {
        service.finishGame(null);
    }

    @Test(expected = NullPointerException.class)
    public void getGameInfoThrowExceptionTest() {
        service.getGameInfo(null);
    }

    @Test(expected = NullPointerException.class)
    public void getGameStatusThrowExceptionTest() {
        service.getGameStatus(null);
    }

    @Test(expected = NullPointerException.class)
    public void getGameHistoryThrowExceptionTest() {
        service.getGameHistory(null);
    }


    @Test(expected = NoSuchPlayerInGameException.class)
    public void makeTurnThrowExceptionTest() {
        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
        Integer playerId = service.getPlayerIdFromGame(gameId);
        service.releaseGameById(gameId);
        service.makeTurn(gameId, playerId - 1, Card.PAPER);
    }

}
