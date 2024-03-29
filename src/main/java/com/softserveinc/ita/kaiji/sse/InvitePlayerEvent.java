package com.softserveinc.ita.kaiji.sse;

import com.google.gson.Gson;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.multiplayer.PlayersStatus;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.sse.dto.InvitePlayerDto;
import com.sun.deploy.util.Waiter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 26.08.14.
 */

@Controller
@RequestMapping("/multiplayer")
public class InvitePlayerEvent {

    @Autowired
    private ServerEventsSyncro serverEventsSyncro;

    private static final Logger LOG = Logger.getLogger(InvitePlayerEvent.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private SSEUtils sseUtils;

    @RequestMapping(value = "/invite/{gameId}", method = RequestMethod.GET)
    public
    @ResponseBody
    String sendMessage(@PathVariable("gameId") Integer gameId,
                       HttpServletResponse response, Principal principal) throws IOException {

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        Player currentPlayer = gameService.getPlayerByName(gameId, principal.getName());
        try {
            synchronized (PlayersStatus.getInvitePlayers().get(gameId)) {
                if(!currentPlayer.isUpdate()){
                    PlayersStatus.getInvitePlayers().get(gameId).wait();
                } else{
                    currentPlayer.forceUpdate(false);
                }
               }
        } catch (InterruptedException e) {
            LOG.error("Failed to send data from server " + e.getMessage());
        }
        List<InvitePlayerDto> playerDto = new ArrayList<>();
        Integer number = 0;

        Boolean isPlaying = currentPlayer.isPlaying();
        for (Player player : gameService.getAllOtherPlayers(gameId, principal.getName())) {

            playerDto.add(new InvitePlayerDto(++number, player.getName(),
                    isPlaying ? true : player.isPlaying()));
        }
        LOG.info("Update user info " +  new Gson().toJson(playerDto));
        return "data:" + new Gson().toJson(playerDto) + "\n\n";
    }
}
