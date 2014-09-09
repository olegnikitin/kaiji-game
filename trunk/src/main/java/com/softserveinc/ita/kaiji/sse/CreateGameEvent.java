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
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/joingame")
public class CreateGameEvent {


    @Autowired
    private SyncroCreatedGames syncroCreatedGames;

    //Update created games each 3 seconds
    //todo should sent game list if detects some changes
    private static final Long CREATED_GAME_UPDATE = 3L;

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
            synchronized (syncroCreatedGames) {
                syncroCreatedGames.wait();
            }
        } catch (InterruptedException e) {
            LOG.error("Failed to send data from server " + e.getMessage());
        }

      /*  try {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(CREATED_GAME_UPDATE, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOG.error("Failed to send data from server " + e.getMessage());
        }*/

        List<CreatedGameInfoDto> gamesInfo = new ArrayList<>();
        Integer number = 0;
        for (GameInfo info : gameService.getRealPlayerGames()) {
            gamesInfo.add(sseUtils.ToGameInfoDto(info, ++number));
        }
        LOG.info("Send updatedata Syncro sse");

        return "data:" + new Gson().toJson(gamesInfo) + "\n\n";
    }
}
