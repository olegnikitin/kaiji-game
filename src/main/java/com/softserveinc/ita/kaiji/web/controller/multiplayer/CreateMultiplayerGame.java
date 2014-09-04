package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.dto.MultiplayerGameInfoDto;
import com.softserveinc.ita.kaiji.model.util.multiplayer.ConvertMultiplayerDto;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.web.controller.async.GameChecker;
import com.softserveinc.ita.kaiji.web.controller.async.TimeoutListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/game/multiplayer/new")
public class CreateMultiplayerGame {

    private static final Logger LOG = Logger.getLogger(CreateMultiplayerGame.class);

    @Autowired
    private ConvertMultiplayerDto convertMultiplayerDto;

    @Autowired
    private GameService gameService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @RequestMapping(method = RequestMethod.POST)
    public String createGame(@ModelAttribute("multiplayerGameInfo") @Valid MultiplayerGameInfoDto multiplayerGameInfoDto,
                             BindingResult result) {

        System.err.println(multiplayerGameInfoDto);
        if (result.hasErrors()) {
            LOG.error("Multiplayer game creation failed: gameInfo model NOT VALID");
            return "redirect:/admin/gameinfo";
        }

        LOG.trace("Multiplayer game created");
        gameService.setGameInfo(convertMultiplayerDto.toGameInfoDto(multiplayerGameInfoDto));
        return "redirect:/admin/gameinfo";
    }

    @RequestMapping(value = "join", method = RequestMethod.GET)
    public void joinGame(@RequestParam String gameName,
                         @RequestParam Integer infoId,
                         HttpServletRequest request, HttpServletResponse response,
                         Model model) throws IOException, ServletException {

        System.err.println("Join multiplayer");
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.addListener(new TimeoutListener(), request, response);
        Long timeout = TimeUnit.MILLISECONDS.convert(systemConfigurationService
                .getSystemConfiguration().getGameConnectionTimeout(), TimeUnit.MILLISECONDS.SECONDS);
        asyncContext.setTimeout(timeout);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer playerId = gameService.addPlayer(auth.getName(), gameName);
        //model.addAttribute("playerId", playerId);

        asyncContext.start(new MultiplayerWaiter(asyncContext, infoId, gameService, timeout));

    }
}
