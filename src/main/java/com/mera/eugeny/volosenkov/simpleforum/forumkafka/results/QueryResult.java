package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;
import lombok.Data;
@Data
public class QueryResult extends ResultBase {
    private Long queryResult;

    public QueryResult(Long queryResult) {
        this.queryResult = queryResult;
    }
}
