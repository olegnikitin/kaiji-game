package com.softserveinc.ita.kaiji.session;


import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SessionTimer extends TimerTask {

    // session timeout = 20 seconds
    private static final Long timeout = 20L;

    private String name;

    public SessionTimer(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.err.println("Current Time " + System.currentTimeMillis() +
                " Saved Time " + SessionUtils.getUserSession().get(name).getCurrentTime()
                + " Diff " + (System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime() + " ms"));
        if (System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime() >
                TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.SECONDS)) {
            if (SessionUtils.getUserSession().get(name).getSession() != null) {
                SessionUtils.getUserSession().get(name).getSession().invalidate();
            }
        }
    }
}
