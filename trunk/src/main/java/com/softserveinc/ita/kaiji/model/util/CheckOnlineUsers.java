package com.softserveinc.ita.kaiji.model.util;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CheckOnlineUsers implements HttpSessionListener {

    public static final String KEY = CheckOnlineUsers.class.getName();

    private Set<String> activeUsers = Collections.synchronizedSet(new HashSet<String>());


    public void sessionCreated(HttpSessionEvent se) {
        registerInServletContext(se.getSession().getServletContext());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        Set<String> users = (Set<String>) se.getSession().getServletContext().getAttribute("nicknames");
        users.remove(se.getSession().getAttribute("nickname"));
    }


    private void registerInServletContext(ServletContext servletContext) {

        if (servletContext.getAttribute(KEY) == null) {
            servletContext.setAttribute("nicknames", activeUsers);
            servletContext.setAttribute(KEY, this);
        }
    }

}
