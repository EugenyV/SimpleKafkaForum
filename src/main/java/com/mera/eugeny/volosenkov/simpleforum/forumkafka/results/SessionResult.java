package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;

import lombok.Data;

@Data
public class SessionResult extends ResultBase{
    private String sessionID;

    public SessionResult(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }
}
