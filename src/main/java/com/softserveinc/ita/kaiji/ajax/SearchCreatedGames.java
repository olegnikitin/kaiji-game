package com.softserveinc.ita.kaiji.ajax;

import com.google.gson.Gson;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/createdgames")
public class SearchCreatedGames extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SearchCreatedGames.class);

    @Autowired
    private GameService gameService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {

        response.setContentType("application/json");
        try {
            String gameName = request.getParameter("joinGameName");
            List<String> foundGames = new ArrayList<>();

            for (GameInfo gameInfo : gameService.getAllGameInfos()) {
                if (gameInfo.getGameName().startsWith(gameName)) {
                    foundGames.add(gameInfo.getGameName());
                }
            }
            String searchList = new Gson().toJson(foundGames);
            response.getWriter().write(searchList);
        } catch (IOException e) {
            LOG.error("Unable to find created games. " + e.getMessage());
        }
    }
}
