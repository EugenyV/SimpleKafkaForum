package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;

import lombok.Data;

@Data
public class RegUserResult extends ResultBase {
    private UserResultStatus status;

    public RegUserResult(UserResultStatus status) {
        this.status = status;
    }
}
