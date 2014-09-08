package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.Game.State;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.bot.Bot.Types;
import com.softserveinc.ita.kaiji.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Eugene Semenkov
 * @version 1.1.2
 * @since 28.03.14
 */

public class TestPlayGameController {

	private GameService gameService;
	private GameHistory gameHistory;
	private PlayGameController playGameController;
	
	@Before
	public void initPlayGameController(){
		playGameController = new PlayGameController();
	}

	@Test
	public void testInitGame() {
        User testUser1 = new User("Eric", "mail", "pass");
		Player player1 = new HumanPlayer(testUser1, 3 ,3);
        User testUser2 = new User("Shon", "mail", "pass");
		Player player2 = new HumanPlayer(testUser2, 3 ,3);
		player1.setId(0);
		player2.setId(1);

		Set<Player> players = new HashSet<Player>();
		players.add(player1);
		players.add(player2);

		GameInfo gameInfo = new GameInfoImpl(
				"gameName", 
				"ownerName", 
				3,
                3,
				Types.EASY, players,2);

		gameHistory = new GameHistory() {

			GameInfo gameInfo;

			@Override
			public void setId(Integer id) {
			}

			@Override
			public Integer getId() {
				return null;
			}

			@Override
			public Set<Player> getWinners() {
				return null;
			}

			@Override
			public List<RoundResult> getRoundResults() {
				return null;
			}

            @Override
            public RoundResult getLastRoundResultFor(Player player) {
                return null;
            }

			@Override
			public State getGameStatus() {
				return null;
			}

			@Override
			public GameInfo getGameInfo() {
				return gameInfo;
			}
		};
		
		ReflectionTestUtils.setField(gameHistory, "gameInfo", gameInfo);

		gameService = mock(GameService.class);
		when(gameService.getGameHistory(0)).thenReturn(gameHistory);
		ReflectionTestUtils.setField(playGameController, "gameService",
				gameService);

		ExtendedModelMap uiModel = new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = new MockHttpSession();
        session.setAttribute("gameId",0);
        when(request.getSession()).thenReturn(session);
        uiModel.addAttribute("playerId",0);
		String result = playGameController.initGame(0,request, uiModel);
		assertNotNull(result);
		assertEquals("PlayGameController must return the play-game view ",
				"play-game", result);

		Integer gameId = (Integer) uiModel.get("gameId");
		GameHistory gameHistory = (GameHistory) uiModel.get("gameHistory");
		Player player = (Player) uiModel.get("playerObject");
		Player enemy = (Player) uiModel.get("enemyObject");
		assertEquals("Init Game must set correct gameId in the model ",
                new Integer(0), gameId);
		assertEquals("Init Game must set correct gamehistory in the model ",
				this.gameHistory, gameHistory);

		assertEquals("Init Game must set correct player in the model ",
				player1, player);
		assertEquals("Init Game must set correct enemy in the model ", player2,
				enemy);
	}

}
