package com.softserveinc.ita.kaiji.service;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.util.pool.ConcurrentPool;
import com.softserveinc.ita.kaiji.model.util.pool.ConcurrentPoolImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic implementation of {@link com.softserveinc.ita.kaiji.service.UserService}
 * for interactions with User and Player entities
 *
 * @author Ievgen Sukhov
 * @version 2.0
 * @since 12.04.14
 * <p/>
 * Will be redesigned later!
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PlayerFactory playerFactory;

    @Autowired
    private UserDAO userRepository;

    private static final ConcurrentPool<Player, Integer> PLAYERS = new ConcurrentPoolImpl<>();

    @Override
    public User getUserById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findUser(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Integer saveUser(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public synchronized Set<Player> createPlayer(GameInfoDto gameInfoDto) {

        Set<Player> result = new HashSet<>();
        User user = findUser(gameInfoDto.getPlayerName());
        Player player = playerFactory.makePlayer(user, gameInfoDto.getNumberOfCards(), gameInfoDto.getNumberOfStars());
        player.setId(PLAYERS.size());
        player.setGameWithStars(true);
        if (gameInfoDto.getBotGame()) {
            player.setGameWithStars(false);
        }
        PLAYERS.put(player);
        result.add(player);

        if (gameInfoDto.getBotGame()) {
            Player bot = playerFactory.makePlayer(gameInfoDto.getBotType(),
                    gameInfoDto.getNumberOfCards());
            bot.setId(PLAYERS.size());
            bot.setGameWithStars(false);
            PLAYERS.put(bot);
            result.add(bot);
        }

        return result;
    }

    @Override
    public Player getPlayerById(Integer playerId) {
        Player result = PLAYERS.allocate(playerId);
        PLAYERS.release(playerId);
        return result;
    }

    @Override
    public synchronized Player addPlayer(String nickname, GameInfo gameInfo) {
        User user = findUser(nickname);
        Player player = playerFactory.makePlayer(user, gameInfo.getNumberOfCards(), gameInfo.getNumberOfStars());
        player.setId(PLAYERS.size());
        player.setGameWithStars(true);
        PLAYERS.put(player);
        return player;
    }

    @Override
    public void removePlayer(Integer playerId) {
        PLAYERS.remove(playerId);
    }

    @Override
    public StatisticsDTO getStatsForUser(String nickname) {
        User user = userRepository.findByNickname(nickname);
        return userRepository.findStatistics(user);
    }


}
