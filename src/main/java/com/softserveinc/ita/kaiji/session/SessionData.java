package com.softserveinc.ita.kaiji.session;

import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 10.08.14.
 */

public class SessionData {

    private Long currentTime;
    private Timer timer;
    private HttpSession session;

    public SessionData(Long currentTime, Timer timer, HttpSession session) {
        this.currentTime = currentTime;
        this.timer = timer;
        this.session = session;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
}
