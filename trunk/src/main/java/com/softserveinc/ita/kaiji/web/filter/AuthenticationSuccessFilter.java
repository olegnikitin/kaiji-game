package com.softserveinc.ita.kaiji.web.filter;

import com.softserveinc.ita.kaiji.chat.ChatClientUpdateEndpoint;
import com.softserveinc.ita.kaiji.chat.ChatUtils;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.util.email.MailSender;
import com.softserveinc.ita.kaiji.session.SessionData;
import com.softserveinc.ita.kaiji.session.SessionTimer;
import com.softserveinc.ita.kaiji.session.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@Component("authenticationSuccessFilter")
public class AuthenticationSuccessFilter extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
        ServletException {

        String name = authentication.getName();
        request.getSession().setAttribute("nickname", name);
        ChatUtils.getUnReadMessages().put(name,false);
        ChatUtils.getActiveUsers().add(name);

        TimerTask timerTask = new SessionTimer(name);
        Timer timer = new Timer(true);
        SessionUtils.getUserSession().put(name, new SessionData(System.currentTimeMillis(),timer,request.getSession()));
        timer.scheduleAtFixedRate(timerTask, 0, 3*1000);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String url = request.getRequestURL().toString();

        int tempString = url.indexOf("//") + 2;
        String hostname = url.substring(tempString, url.indexOf('/', tempString));
        request.getSession().getServletContext().setAttribute("hostname",hostname);

        String uri = "ws://" + hostname + "/users" ;
        try {
            container.connectToServer(ChatClientUpdateEndpoint.class,
                    URI.create(uri));
        } catch (IOException | javax.websocket.DeploymentException e) {
            System.out.println(e.getMessage() + " " + URI.create(uri));
        }

        User user = userDAO.getByNickname(name);
        if (user != null) {
            Cookie userCookie = new Cookie("personId", user.getId().toString());
            userCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(userCookie);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
