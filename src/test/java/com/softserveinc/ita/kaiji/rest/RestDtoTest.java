package com.softserveinc.ita.kaiji.rest;


import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.UserRole;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.rest.dto.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = TestServiceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RestDtoTest {

    @Autowired
    private ConvertToRestDto convertToRestDto;

    private static GameInfoEntity gameInfoEntity;

    private static  User user1;

    private static  User user2;

    @Mock
    private GameHistoryEntity gameHistoryMock;

    @Mock
    private GameInfo gameInfoMock;

    private static RoundResultEntity roundResult;

    @BeforeClass
    public static void setUp() {
        user1 = new User();
        user2 = new User();
        gameInfoEntity = new GameInfoEntity();
        roundResult = new RoundResultEntity();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convertUserToDto() {

        Date registrationDate = new Date();
        user1.setId(1);
        user1.setNickname("nick");
        user1.setName("name");
        user1.setPassword("$2a$10$b1zfl0hlzgLdH6G3mFgA9eJVh492pfJBAKOUys.90INubpb76VqZy");
        user1.setEmail("email@gmail.com");
        user1.getRoles().add(UserRole.USER_ROLE);
        user1.setRegistrationDate(registrationDate);
        user1.setGameInfoEntities(new HashSet<GameInfoEntity>());

        UserRestDto userToDto = convertToRestDto.userToDto(user1);

        assertTrue(userToDto.getId().equals(1)
                && userToDto.getEmail().equals("email@gmail.com")
                && userToDto.getName().equals("name")
                && userToDto.getNickname().equals("nick")
                && userToDto.getRoles().contains(UserRole.USER_ROLE)
                && userToDto.getRegistrationDate().
                equals(registrationDate.toString().substring(0, registrationDate.toString().length() - 2)));
    }

    @Test
    public void convertGameInfoToDto() {

        user1.setNickname("player1");
        user2.setNickname("player2");

        Date startGameTime = new Date();
        Date finishGameTime = new Date(startGameTime.getTime() + 10000);

        gameInfoEntity.setId(1);
        gameInfoEntity.setGameName("Zoro");
        gameInfoEntity.setUsers(new HashSet<User>() {{
                              add(user1);
                              add(user2);
                          }}
        );
        gameInfoEntity.setGameType(Game.Type.TWO_PLAYER_GAME);
        gameInfoEntity.setNumberOfCards(4);
        gameInfoEntity.setGameHistoryEntity(new GameHistoryEntity());
        gameInfoEntity.setGameStartTime(startGameTime);
        gameInfoEntity.setGameFinishTime(finishGameTime);

        GameInfoRestDto gameInfoRestDto = convertToRestDto.gameInfoToDto(gameInfoEntity);

        assertTrue(gameInfoRestDto.getId().equals(1)
                && gameInfoRestDto.getGameName().equals("Zoro")
                && gameInfoRestDto.getNumberOfCards().equals(4)
                && gameInfoRestDto.getGameType().equals(Game.Type.TWO_PLAYER_GAME)
                && gameInfoRestDto.getUsers().contains("player1")
                && gameInfoRestDto.getUsers().contains("player2")
                && gameInfoRestDto.getGameStartTime().
                equals(startGameTime.toString().substring(0, startGameTime.toString().length() - 2))
                && gameInfoRestDto.getGameFinishTime().
                equals(finishGameTime.toString().substring(0, finishGameTime.toString().length() - 2)));

    }

    @Test
    public void convertRoundResultToDto() {

        user1.setName("user");
        Map<User, RoundResultEntity.Entry> round = new HashMap<>();
        RoundResultEntity.Entry entry = new RoundResultEntity.Entry(Card.ROCK, Card.DuelResult.DRAW);
        round.put(user1, entry);

        roundResult.setId(1);
        roundResult.setNumber(2);
        roundResult.setRound(round);

        RoundResultRestDto roundResultRestDto = convertToRestDto.roundResultToDto(roundResult);

        assertTrue(roundResultRestDto.getId().equals(1)
                && roundResultRestDto.getEntries().get(0).getCard().equals(Card.ROCK)
                && roundResultRestDto.getEntries().get(0).getDuelResult().equals(Card.DuelResult.DRAW)
                && roundResultRestDto.getEntries().get(0).getUserName().equals("user"));
    }

    @Test
    public void convertGameHistoryToDto() {

        user1.setName("user");
        user1.setNickname("nick");
        Map<User, RoundResultEntity.Entry> round = new HashMap<>();
        RoundResultEntity.Entry entry = new RoundResultEntity.Entry(Card.ROCK, Card.DuelResult.DRAW);
        round.put(user1, entry);

        roundResult.setId(1);
        roundResult.setNumber(2);
        roundResult.setRound(round);

        gameInfoEntity.setUsers(new HashSet<User>() {{
            add(user1);
        }});

        gameInfoEntity.setGameStartTime(new Date());
        gameInfoEntity.setGameFinishTime(new Date());

        Mockito.when(gameHistoryMock.getId()).thenReturn(1);
        Mockito.when(gameHistoryMock.getWinners()).thenReturn(new HashSet<User>() {{
            add(user1);}});
        Mockito.when(gameHistoryMock.getGameState()).thenReturn((Game.State.GAME_PLAYING));
        Mockito.when(gameHistoryMock.getRoundResults()).thenReturn(new HashSet<RoundResultEntity>() {{
            add(roundResult);
        }});
        Mockito.when(gameHistoryMock.getGameInfo()).thenReturn(gameInfoEntity);

        GameHistoryRestDto gameHistoryRestDto = convertToRestDto.gameHistoryToDto(gameHistoryMock);

        assertTrue(gameHistoryRestDto.getId().equals(1)
                    && gameHistoryRestDto.getGameState().equals(Game.State.GAME_PLAYING)
                    && gameHistoryRestDto.getWinners().contains("nick")
                    && gameHistoryRestDto.getRoundResults().get(0).getId().equals(1)
                    && gameHistoryRestDto.getRoundResults().get(0).getEntries().get(0).getCard().equals(Card.ROCK)
                    && gameHistoryRestDto.getRoundResults().get(0).getEntries().get(0).getDuelResult().equals(Card.DuelResult.DRAW)
                    && gameHistoryRestDto.getRoundResults().get(0).getEntries().get(0).getUserName().equals("user"));
    }

    @Test
    public void convertJoinGameInfoToDto() {

        Mockito.when(gameInfoMock.getId()).thenReturn(1);
        Mockito.when(gameInfoMock.getGameName()).thenReturn("testGame");
        Mockito.when(gameInfoMock.getPlayers()).thenReturn(new HashSet<Player>());
        Mockito.when(gameInfoMock.getNumberOfCards()).thenReturn(3);

        GameJoinRestDto gameJoinRestDto  = convertToRestDto.joinGameInfoToDto(gameInfoMock);

        assertTrue(gameJoinRestDto.getId().equals(1)
                && gameJoinRestDto.getGameName().equals("testGame")
                && gameJoinRestDto.getPlayers() != null
                && gameJoinRestDto.getNumberOfCards().equals(3));
    }
}
