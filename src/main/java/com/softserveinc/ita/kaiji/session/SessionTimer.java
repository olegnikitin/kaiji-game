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
        System.err.println("Saved time " + SessionUtils.getUserSession().get(name).getCurrentTime() + " " +
                            "Current time " + System.currentTimeMillis() +
                            " Diff " + (System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime()));
        if(System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime() > 20000L){
            if(SessionUtils.getUserSession().get(name).getSession() != null) {
                SessionUtils.getUserSession().get(name).getSession().invalidate();
            }
        }
    }
}
