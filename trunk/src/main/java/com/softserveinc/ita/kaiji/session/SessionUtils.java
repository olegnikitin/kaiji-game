package com.softserveinc.ita.kaiji.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 10.08.14.
 */

public class SessionUtils {
    private static final SessionUtils sessionUtils = createSessionUtils();

    private static Map<String, SessionData> userSession;

    private SessionUtils() {
    }

    private static SessionUtils createSessionUtils() {
        userSession = Collections.synchronizedMap(new HashMap<String,SessionData>());
        return new SessionUtils();
    }

    public static Map<String, SessionData> getUserSession() {
        return userSession;
    }

    public static void setUserSession(Map<String, SessionData> userSession) {
        SessionUtils.userSession = userSession;
    }
}
