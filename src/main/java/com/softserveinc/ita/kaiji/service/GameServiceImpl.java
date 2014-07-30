package com.softserveinc.ita.kaiji.service;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.*;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.model.util.pool.ConcurrentPool;
import com.softserveinc.ita.kaiji.model.util.pool.ConcurrentPoolImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Bohdan Shaposhnik, Ievgen Sukhov
 * @version 3.0
 * @since 17.03.2014
 */
@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOG = Logger.getLogger(GameServiceImpl.class);

    private static final ConcurrentPool<Game, Integer> GAMES_SYNC = new ConcurrentPoolImpl<>();
    private static final ConcurrentPool<GameInfo, Integer> GAME_INFOS = new ConcurrentPoolImpl<>();

    @Autowired
    private GameFactory gameFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private GameHistoryEntityDAO gameHistoryEntityDAO;

    private Game getGameById(Integer gameId) throws IllegalArgumentException {
        return GAMES_SYNC.allocate(gameId);
    }

    @Override
    public synchronized Integer setGameInfo(GameInfoDto gameInfoDto) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("setGameInfo: we are in");
        }
        Set<Player> playersSet = new HashSet<Player>();

        playersSet.addAll(userService.createPlayer(gameInfoDto));

        if (LOG.isTraceEnabled()) {
            LOG.trace("setGameInfo: bot added to the set of players");
        }

        GameInfo newGameInfo = new GameInfoImpl(gameInfoDto.getGameName(),
                gameInfoDto.getPlayerName(), gameInfoDto.getNumberOfCards(),
                gameInfoDto.getBotType(), playersSet);
        if (gameInfoDto.getBotGame()) {
            newGameInfo.setGameType(Game.Type.BOT_GAME);
        } else {
            newGameInfo.setGameType(Game.Type.TWO_PLAYER_GAME);
        }
        newGameInfo.setId(GAME_INFOS.size());
        GAME_INFOS.put(newGameInfo);

        return newGameInfo.getId();

    }

    @Override
    public void finishGame(Integer gameId) throws IllegalArgumentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("finishGame: started");
        }
        Game game = getGameById(gameId);
        if (game != null && game.getState().equals(Game.State.GAME_FINISHED)) {

            Player playerBot = null;

            Iterator<Player> playerIterator = game.getGameHistory().getGameInfo().getPlayers().iterator();

            while (playerIterator.hasNext()) {
                playerBot = playerIterator.next();
                if (playerBot.isBot()) {
                    LOG.trace("Avoid write bot player to DB when game is completed");
                    playerIterator.remove();
                    break;
                }
            }

            GameHistoryEntity gameHistoryEntity = new GameHistoryEntity(game.getGameHistory());

            Iterator<User> userIterator;
            User currentUser;

            for (RoundResultEntity roundResult : gameHistoryEntity.getRoundResults()) {
                userIterator = roundResult.getRound().keySet().iterator();
                while (userIterator.hasNext()) {
                    currentUser = userIterator.next();
                    if (currentUser.getId() < 0){
                        userIterator.remove();
                    }
                }
            }

            userIterator = gameHistoryEntity.getWinners().iterator();

            while (userIterator.hasNext()) {
                currentUser = userIterator.next();
                if (currentUser.getId() < 0) {
                    userIterator.remove();
                    break;
                }
            }
            gameHistoryEntityDAO.save(gameHistoryEntity);

            GAMES_SYNC.release(gameId);
            game.finishGame();

            if (game.getGameInfo().getPlayers().size() != 2) {
                game.getGameInfo().getPlayers().add(playerBot);
            }

            for (Player p : game.getGameInfo().getPlayers()) {
                userService.removePlayer(p.getId());
            }
            GAME_INFOS.remove(game.getGameInfo().getId());
            GAMES_SYNC.remove(gameId);
        }
    }

    @PreAuthorize("hasRole(‘ADMIN_ROLE’)")
    @Override
    public void clearGameInfo(Integer gameInfoId) {
        GameInfo gameInfo = GAME_INFOS.allocate(gameInfoId);
        if (gameInfo != null) {
            for (Player p : gameInfo.getPlayers()) {
                userService.removePlayer(p.getId());
            }
            for (Game game : GAMES_SYNC) {
                if (game.getGameInfo() == gameInfo) {
                    GAMES_SYNC.remove(game.getId());
                }
            }
            GAME_INFOS.release(gameInfoId);
            GAME_INFOS.remove(gameInfoId);
        }
    }

    @Override
    public GameInfo getGameInfo(Integer gameId) throws IllegalArgumentException {
        GameInfo result = GAME_INFOS.allocate(gameId);
        GAME_INFOS.release(gameId);
        return result;
    }

    @Override
    public Set<GameInfo> getAllGameInfos() {
        Set<GameInfo> result = new HashSet<>();
        for (GameInfo gi : GAME_INFOS) {
            result.add(gi);
        }
        return result;
    }


    @Override
    public synchronized Integer createGame(GameInfo gameInfo) {

        Game game = gameFactory.makeGame(gameInfo);

        Player botPlayer = null;

        Iterator<Player> playerIterator = gameInfo.getPlayers().iterator();

        while (playerIterator.hasNext()) {
            botPlayer = playerIterator.next();
            if (botPlayer.isBot()) {
                LOG.trace("Avoid write bot player to DB while creating game");
                playerIterator.remove();
                break;
            }
        }

        //GameInfoEntity gameInfoEntity = new GameInfoEntity(gameInfo);
        //Don't save game to DB. We must do it after game completion
        /*Integer databaseId = gameInfoEntityDAO.save(gameInfoEntity);*/
        //gameInfo.setDatabaseId(databaseId);
        if (gameInfo.getPlayers().size() != 2) {
            LOG.trace("Add boot player " + botPlayer);
            gameInfo.getPlayers().add(botPlayer);
        }
        game.setId(GAMES_SYNC.size());
        GAMES_SYNC.put(game);

        if (LOG.isTraceEnabled()) {
            LOG.trace("createGame: game added");
        }

        return game.getId();
    }

    @Override
    public void makeTurn(Integer gameId, Integer playerId, Card card)
            throws IllegalArgumentException {
        Player activePlayer = userService.getPlayerById(playerId);

        Game game = getGameById(gameId);
        for (Player player : game.getGameInfo().getPlayers()) {
            if (player.isBot()) {
                Bot bot = (Bot) player;
                bot.setGameData(game.getGameHistory());
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("makeTurn: exucuting a turn");
        }
        game.makeTurn(card, activePlayer);
        GAMES_SYNC.release(gameId);

    }

    @Override
    public Game.State getGameStatus(Integer gameId)
            throws IllegalArgumentException {
        Game.State result = getGameById(gameId).getState();
        GAMES_SYNC.release(gameId);
        return result;
    }

    @Override
    public GameHistory getGameHistory(Integer gameId)
            throws IllegalArgumentException {
        GameHistory result = getGameById(gameId).getGameHistory();
        GAMES_SYNC.release(gameId);
        return result;
    }


    @Override
    public Integer getPlayerIdFromGame(Integer gameId)
            throws IllegalArgumentException {
        Game game = getGameById(gameId);
        for (Player player : game.getGameInfo().getPlayers()) {
            if (!player.isBot()) {
                GAMES_SYNC.release(gameId);
                return player.getId();
            }
        }
        return null;
    }


    @Override
    public Integer getPlayerIdFromGame(String playerName, Integer gameId)
            throws IllegalArgumentException {

        for (GameInfo gi : GAME_INFOS) {
            if (gi.getId().equals(gameId)) {
                for (Player p : gi.getPlayers()) {
                    if (p.getName().equals(playerName)) {
                        return p.getId();
                    }
                }
            }

        }
        return null;
    }

    @Override
    public Integer getAbandonedGameId(String userName, Integer gameId) {
        System.out.println("getAbandonedGameId");
        Integer abandonedGameId = null;
        for (GameInfo gi : GAME_INFOS) {
            if (gi.getGameType().equals(Game.Type.BOT_GAME) && !gi.getId().equals(gameId)) {
                for (Player player : gi.getPlayers()) {
                    if (player.getName().equals(userName)) {
                        abandonedGameId = gi.getId();
                        break;
                    }
                }
            }
        }
        return abandonedGameId;
    }

    @Override
    public Integer getGameId(String gameName) {
        for (Game g : GAMES_SYNC) {
            if (g.getGameInfo().getGameName().equals(gameName)) {
                return g.getId();
            }
        }
        return null;
    }

    @Override
    public Integer addPlayer(String nickname, String gameName) {
        for (GameInfo gi : GAME_INFOS) {
            if (gi.getGameName().equals(gameName)) {
                Player secondPlayer = userService.addPlayer(nickname, gi);
                gi.getPlayers().add(secondPlayer);
                return secondPlayer.getId();
            }

        }
        return null;
    }

}



