package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.dto.MultiplayerGameInfoDto;
import com.softserveinc.ita.kaiji.model.util.multiplayer.ConvertMultiplayerDto;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.web.controller.async.GameChecker;
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
@RequestMapping("/game/multiplayer/play")
public class PlayMultiplayerGame {

    private static final Logger LOG = Logger.getLogger(PlayMultiplayerGame.class);

    @RequestMapping(method = RequestMethod.GET)
    public String createGame() {
        return "join-multiplayer-game";
    }


}
