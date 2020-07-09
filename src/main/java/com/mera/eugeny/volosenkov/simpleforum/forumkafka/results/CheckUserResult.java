package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;

import lombok.Data;

@Data
public class CheckUserResult extends ResultBase {
    private CheckUserresultStatus status;

    public CheckUserResult(CheckUserresultStatus status) {
        this.status = status;
    }

    public CheckUserresultStatus getStatus() {
        return status;
    }
}
