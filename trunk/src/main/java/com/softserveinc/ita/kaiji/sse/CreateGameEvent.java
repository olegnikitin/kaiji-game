package com.softserveinc.ita.kaiji.sse;

import com.google.gson.Gson;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.sse.dto.CreatedGameInfoDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 26.08.14.
 */

@Controller
@RequestMapping("/joingame")
public class CreateGameEvent {


    @Autowired
    private ServerEventsSyncro serverEventsSyncro;

    private static final Logger LOG = Logger.getLogger(CreateGameEvent.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private SSEUtils sseUtils;

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public
    @ResponseBody
    String sendMessage(HttpServletResponse response) throws IOException {

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        try {
            synchronized (serverEventsSyncro.getCreatedGames()) {
                serverEventsSyncro.getCreatedGames().wait();
            }
        } catch (InterruptedException e) {
            LOG.error("Failed to send data from server " + e.getMessage());
        }


        List<CreatedGameInfoDto> gamesInfo = new ArrayList<>();
        Integer number = 0;
        for (GameInfo info : gameService.getRealPlayerInGame()) {
            gamesInfo.add(sseUtils.ToGameInfoDto(info, ++number));
        }
        LOG.info("Send updatedata Syncro sse");

        return "data:" + new Gson().toJson(gamesInfo) + "\n\n";
    }
}
