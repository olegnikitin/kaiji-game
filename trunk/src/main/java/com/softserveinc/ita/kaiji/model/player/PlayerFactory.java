package com.softserveinc.ita.kaiji.model.player;


import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.model.player.bot.BotFactory;
import com.softserveinc.ita.kaiji.model.player.creators.PlayerCreator;
import com.softserveinc.ita.kaiji.model.util.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Provides  player creation
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 30.03.14
 */

@Component
@Scope("singleton")
public final class PlayerFactory {

    private Factory<User, Player, PlayerCreator> playerFactory;

    @Autowired
    private BotFactory botFactory;
    @Autowired
    public  PlayerFactory(Set<PlayerCreator> creators) {
        playerFactory = new Factory<User, Player, PlayerCreator>(creators);
    }

    public Player makePlayer(User user, Integer cardNumber) {
        AbstractPlayer resultPlayer = (AbstractPlayer) playerFactory.create(user);
        resultPlayer.setDeck(cardNumber);
        return resultPlayer;
    }

    public Player makePlayer(Bot.Types type, Integer cardNumber) {
        return botFactory.createBot(type, cardNumber);
    }
}
