package com.softserveinc.ita.kaiji.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
        "file:src/test/resources/application-context-test.xml"})
public class GameServiceTest {

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
		dto.setPlayerName("testPlayer");
        dto.setBotGame(true);
	}

	@After
	public void tearDown() throws Exception {
			gameId = null;
	}

	@Ignore
	@Test
	public void finishGameTest() {

        boolean thrown = false;
        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
		service.finishGame(gameId);
		try {
			service.getGameInfo(gameId);
		} catch (IllegalArgumentException iae) {
			thrown = true;
		}
		assertFalse(thrown);
	}
    @Ignore
	@Test
	public void createGameTest() {
		boolean thrown = false;
        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
		try {
			service.getGameInfo(gameId);
		} catch (IllegalArgumentException iae) {
			thrown = true;
		}
		assertFalse(thrown);
	}
    @Ignore
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

    @Ignore
	@Test(expected = IllegalArgumentException.class)
	public void makeTurnThrowExceptionTest() {
        Integer infoId = service.setGameInfo(dto);
        gameId = service.createGame(service.getGameInfo(infoId));
		service.makeTurn(null, service.getPlayerIdFromGame(gameId), Card.PAPER);
	}

}
