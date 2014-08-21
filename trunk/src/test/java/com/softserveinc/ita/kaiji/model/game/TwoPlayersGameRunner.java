package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Paziy Evgeniy
 * @version 2.2
 * @since 01.04.14
 */
@WebAppConfiguration
public class TwoPlayersGameRunner {

    static GameInfo makeGameInfo() {
        Set<Player> playersSet = new HashSet<>();
        User testUser1 = new User("player1", "mail", "pass");
        Player player1 = new HumanPlayer(testUser1, 1, 1);
        User testUser2 = new User("player2", "mail", "pass");
        Player player2 = new HumanPlayer(testUser2, 1, 1);

        player1.setId(1);
        player2.setId(2);

        playersSet.add(player1);
        playersSet.add(player2);

        return new GameInfoImpl("Test Game", "no one", 1, 1, null, playersSet);
    }

    static void printPlayer(Player player) {
        System.out.print("ID: " + player.getId() + ";  ");
        System.out.print(player.getName() + ";  ");
        System.out.print("State: " + player.getState() + ";  ");
        System.out.println("Deck: " + player.getDeck().getCardsCopy());
    }

    static void printStatistic(Player player) {
        System.out.print(player.getName() + " <statistics> ");
        System.out.println(player.getStatistic().getStatistic());
    }

    static void printGameHistory(GameHistory gameHistory) {
        for (RoundResult rr : gameHistory.getRoundResults()) {
            for (Player p : rr.getPlayers()) {
                System.out.print("      " + p.getName() + " - " + rr.getDuelResult(p));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        GameInfo gameInfo = makeGameInfo();
        Game game = new TwoPlayerGameImpl(gameInfo);
        GameHistory gameHistory = game.getGameHistory();


        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        label:
        while (game.getState() != Game.State.GAME_FINISHED) {
            System.out.println("Game State: " + gameHistory.getGameStatus());
            for (Player p : gameInfo.getPlayers()) {
                printPlayer(p);
            }

            System.out.println("Commands: r(rock)  -  p(paper)  -  s(scissors)  -  q(quit - interrupting game)");
            System.out.print("Enter command: ");

            userInput = in.readLine();
            Card chosenCard = null;
            if (!userInput.isEmpty()) {
                switch (userInput) {
                    case "r":
                        chosenCard = Card.ROCK;
                        break;
                    case "p":
                        chosenCard = Card.PAPER;
                        break;
                    case "s":
                        chosenCard = Card.SCISSORS;
                        break;
                    case "q":
                        game.interruptGame();
                        break label;
                }
            }

            System.out.println("Chose player by id (1, 2)");
            System.out.print("Enter player id: ");

            userInput = in.readLine();

            int playerId = Integer.parseInt(userInput);
            Player chosenPlayer = null;
            for (Player p : gameInfo.getPlayers()) {
                if (p.getId() == playerId) {
                    chosenPlayer = p;
                }
            }

            try {
                game.makeTurn(chosenCard, chosenPlayer);
            } catch (Exception ex) {
                System.out.println();
                System.out.println(ex);
                System.out.println();
                continue;
            }

            System.out.println();
            printGameHistory(gameHistory);
            System.out.println();
        }

        System.out.println();
        System.out.println("Game state: " + gameHistory.getGameStatus());
        System.out.println("Game winner: " + gameHistory.getWinners());

        for (Player player : gameInfo.getPlayers()) {
            printStatistic(player);
        }
        for (Player p : gameInfo.getPlayers()) {
            printPlayer(p);
        }
    }
}
