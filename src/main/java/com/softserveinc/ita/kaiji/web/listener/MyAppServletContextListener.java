package com.softserveinc.ita.kaiji.web.listener;

import com.softserveinc.ita.kaiji.model.util.email.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyAppServletContextListener implements ServletContextListener {

    @Autowired
    private MailSender mailSender;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        ServletContext context = sce.getServletContext();
        mailSender.setFilePath(context.getRealPath("") + context.getInitParameter("email"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}