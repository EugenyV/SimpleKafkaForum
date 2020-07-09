package com.mera.eugeny.volosenkov.simpleforum.forumkafka.OutputData;

import lombok.Data;

@Data
public class SessionOut extends OutputBase {
    String session;

    public SessionOut(String session) {
        this.session = session;
    }
}
