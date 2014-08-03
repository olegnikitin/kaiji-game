package com.softserveinc.ita.kaiji.chat;

import java.util.HashSet;
import java.util.Set;

public class ActiveUsers {

    private static final ActiveUsers activeUsers = createActiveUsers();
    private static Set<String> users;

    private ActiveUsers(){}

    private static ActiveUsers createActiveUsers() {
        users = new HashSet<>();
        return new ActiveUsers();
    }

    public static Set<String> getUsers() {
        return users;
    }

    public static void setUsers(Set<String> activeUsers) {
        users = activeUsers;
    }
}
