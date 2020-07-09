package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;

public enum UserResultStatus {
    OK,
    USER_EXIST_ALREADY,
    RAISE_BY_TIMEOUT,
    USER_EXIST,
    USER_IS_NOT_EXISTS,
    INCORRECT_QUERY_ID
}
