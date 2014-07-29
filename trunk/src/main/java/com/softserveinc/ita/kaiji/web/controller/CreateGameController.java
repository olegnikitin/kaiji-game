package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.web.controller.async.GameChecker;
import com.softserveinc.ita.kaiji.web.controller.async.SecondPlayerChecker;
import com.softserveinc.ita.kaiji.web.controller.async.TimeoutListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Controller for New Game form
 *
 * @author Boiko Eduard
 * @version 2.0
 * @since 17.03.14
 */

@Controller
@RequestMapping("/game/new")
@SessionAttributes({"playerId", "gameInfo"})
public class CreateGameController {

    private static final Logger LOG = Logger.getLogger(CreateGameController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @RequestMapping(method = RequestMethod.GET)
    public String sendToForm(Model model, HttpServletResponse response) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("CreateGameController got GET-request");
        }

        SystemConfiguration systemConfiguration = systemConfigurationService.getSystemConfiguration();
        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName(systemConfiguration.getGameName());
        gameInfoDto.setPlayerName(systemConfiguration.getUserName());
        gameInfoDto.setNumberOfCards(systemConfiguration.getNumberOfCards());
        gameInfoDto.setBotType(systemConfiguration.getBotType());
        model.addAttribute("gameInfo", gameInfoDto);
        model.addAttribute("playerId", new Integer(0));

        if (LOG.isTraceEnabled()) {
            LOG.trace("gameInfoDto added to the model and will be sent to form");
        }
        return "create-game";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String initForm(
            @ModelAttribute("gameInfo") @Valid GameInfoDto gameInfoDto,
            BindingResult br) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Checking BindingResult for mistakes");
        }
        if (br.hasErrors()) {
            LOG.info("Registration failed: gameInfo model NOT VALID");
            return "create-game";
        }
        return "redirect:/game/new/start";
    }

    @RequestMapping(value = "start", method = RequestMethod.GET)
    public void startGame(@ModelAttribute("gameInfo") GameInfoDto gameInfoDto,
                          HttpServletRequest request, HttpServletResponse response,
                          Model model) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameInfoDto.setPlayerName(auth.getName());

        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(systemConfigurationService.getSystemConfiguration().getGameConnectionTimeout());
        asyncContext.addListener(new TimeoutListener(), request, response);
        Integer gameId = gameService.setGameInfo(gameInfoDto);
        Integer abandonedGameId = gameService.getAbandonedGameId(auth.getName(),gameId);
        if (abandonedGameId != null) {
            LOG.trace("Remove abandoned game from pool.");
            gameService.clearGameInfo(abandonedGameId);
        }

        Integer playerId = gameService.getPlayerIdFromGame(auth.getName(), gameId);
        model.addAttribute("playerId", playerId);
        asyncContext.start(new SecondPlayerChecker(asyncContext, gameId, gameService));

        if (LOG.isTraceEnabled()) {
            LOG.trace("Game started. AsyncContext working. ");
        }
    }

    @RequestMapping(value = "join", method = RequestMethod.GET)
    public void joinGame(@RequestParam String gameName,
                         @RequestParam(required = false) Integer infoId,
                         HttpServletRequest request, HttpServletResponse response,
                         Model model) throws IOException, ServletException {

        GameInfo info = gameService.getGameInfo(infoId);
        if (info.getPlayers().size() > 1) {

            request.setAttribute("manyPlayers", Boolean.TRUE);
            RequestDispatcher rd = request.getRequestDispatcher("/game/join");
            rd.forward(request, response);
        } else {
            final AsyncContext asyncContext = request.startAsync(request, response);
            asyncContext.setTimeout(systemConfigurationService
                    .getSystemConfiguration().getGameConnectionTimeout());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer playerId = gameService.addPlayer(auth.getName(), gameName);
            model.addAttribute("playerId", playerId);
            asyncContext.start(new GameChecker(asyncContext, gameName, gameService));
        }
    }
}
