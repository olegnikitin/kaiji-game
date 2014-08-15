package com.softserveinc.ita.kaiji.session;


import javax.servlet.ServletContext;
import java.util.Date;
import java.util.TimerTask;

public class SessionTimer extends TimerTask {

    private String name;

    public SessionTimer(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if(System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime() > 20000L){
            if(SessionUtils.getUserSession().get(name).getSession() != null) {
                SessionUtils.getUserSession().get(name).getSession().invalidate();
            }
        }
    }
}
