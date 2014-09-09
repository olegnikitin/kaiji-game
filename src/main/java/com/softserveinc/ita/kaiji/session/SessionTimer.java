package com.softserveinc.ita.kaiji.session;

import org.apache.log4j.Logger;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SessionTimer extends TimerTask {

    private static final Logger LOG = Logger.getLogger(SessionTimer.class);

    // session timeout = 30 seconds
    private static final Long timeout = 30L;

    private String name;

    public SessionTimer(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - SessionUtils.getUserSession().get(name).getCurrentTime() >
                TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.SECONDS)) {
            if (SessionUtils.getUserSession().get(name).getSession() != null) {
            /*
                SessionUtils.getUserSession().get(name).getSession().invalidate();
                LOG.info(name  + " session timeouted.");
            */
            }
        }
    }
}
