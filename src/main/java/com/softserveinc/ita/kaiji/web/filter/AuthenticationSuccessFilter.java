package com.softserveinc.ita.kaiji.web.filter;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import com.softserveinc.ita.kaiji.chat.ActiveUsers;
import com.softserveinc.ita.kaiji.chat.ChatClientUpdateEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.model.User;

@Component("authenticationSuccessFilter")
public class AuthenticationSuccessFilter extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
        String name = authentication.getName();

        Set<String> users = (Set<String> )request.getSession().getServletContext().getAttribute("nicknames");
        users.add(name);
        ActiveUsers.setUsers(users);
        request.getSession().getServletContext().setAttribute("nicknames", users);
        request.getSession().setAttribute("nickname", name);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/users" ;
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
