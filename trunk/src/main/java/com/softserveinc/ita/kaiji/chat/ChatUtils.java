package com.softserveinc.ita.kaiji.chat;

import java.util.*;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 03.08.14.
 */

public class ChatUtils {

    private static final ChatUtils chatUtils = createChatUtils();
    private static Set<String> activeUsers;
    private static List<String> messages;
    private static Map<String, Boolean> unReadMessages;

    private ChatUtils() {
    }

    private static ChatUtils createChatUtils() {
        activeUsers = new HashSet<String>();
        messages = new LinkedList<>();
        unReadMessages = new HashMap<>();
        return new ChatUtils();
    }

    public static Set<String> getActiveUsers() {
        return activeUsers;
    }

    public static void setActiveUsers(Set<String> activeUsers) {
        ChatUtils.activeUsers = activeUsers;
    }

    public static List<String> getMessages() {
        return messages;
    }

    public static void setMessages(List<String> messages) {
        ChatUtils.messages = messages;
    }

    public static Map<String, Boolean> getUnReadMessages() {
        return unReadMessages;
    }

    public static void setUnReadMessages(Map<String, Boolean> unReadMessages) {
        ChatUtils.unReadMessages = unReadMessages;
    }
}
