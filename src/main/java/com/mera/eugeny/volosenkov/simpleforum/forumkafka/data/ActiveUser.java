package com.mera.eugeny.volosenkov.simpleforum.forumkafka.data;

import lombok.Data;

@Data
public class ActiveUser {
    private String login;
    private String sessionId;

    public ActiveUser(String login, String sessionId) {
        this.login = login;
        this.sessionId = sessionId;
    }

    public String getLogin() {
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }
}
