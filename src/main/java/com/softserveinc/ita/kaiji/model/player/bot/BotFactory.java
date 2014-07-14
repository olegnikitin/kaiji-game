package com.softserveinc.ita.kaiji.model.player.bot;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Manages bot creation based on data from services and controllers
 * @author  Ievgen Sukhov
 * @since 19.03.14.
 * @version 1.2
 */

@Component
@Scope("singleton")
public class BotFactory {

    private static final Logger LOG = Logger.getLogger(BotFactory.class);

    private BotFactory() {}

    /**
     * Creates instances of <code>Bot</code> variations
     * @param type type of bot to create (difficulty)
     * @param cardCount integer number of cards of one type in deck
     * @return <code>Bot</code> instance
     */
    public  Bot createBot(Bot.Types type, Integer cardCount) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Creating bot with type : " + type);
        }
        Bot bot;
        switch (type) {
            case EASY: {
                bot = new EasyBotImpl(cardCount);
                dataGenerator(bot, type);
                return bot;
            }
            case MEDIUM:{
                bot = new MediumBotImpl(cardCount);
                dataGenerator(bot, type);
                return bot;
            }
            case HARD:{
                bot = new HardBotImpl(cardCount);
                dataGenerator(bot, type);
                return bot;
            }
            default:
                return new EasyBotImpl(cardCount);

        }
    }

    private  void dataGenerator(Bot bot, Bot.Types type) {
        Integer randomId = new Random().nextInt(100000);
        bot.setName(String.format("%s BOT %d", type, randomId));
        bot.setType(type);
        bot.setId(randomId);
    }

}
