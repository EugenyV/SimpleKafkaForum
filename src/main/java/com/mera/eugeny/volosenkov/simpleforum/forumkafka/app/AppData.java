package com.mera.eugeny.volosenkov.simpleforum.forumkafka.app;

import java.io.IOException;

public class AppData {
    public static ResultStatusCash resultStatusTab;
    public static Users usersTab;
    private static  long lastTimeUpdate;
    public static UserSessions activeUsers;
    private static final long UPDATING_INTERVAL=5000l;
    private static final String dataPath = "C:\\Users\\Dectlab\\git\\forumkafka\\data";

    public static void init() throws IOException {
        usersTab = Users.init();
        resultStatusTab = new ResultStatusCash();
        activeUsers = UserSessions.init();
    }

    public static void check_update() throws IOException {
        if((System.nanoTime() - lastTimeUpdate/1000000)>UPDATING_INTERVAL)
        {
            update();
            lastTimeUpdate = System.nanoTime();
        }
    }

    public static String getDataPath() {
        return dataPath;
    }

    private static void update() throws IOException {
        usersTab.update();
    }
}
