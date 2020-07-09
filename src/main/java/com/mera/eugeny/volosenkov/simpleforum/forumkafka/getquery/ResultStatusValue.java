package com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.ResultBase;

public class ResultStatusValue {
    boolean isConsumed;
    ResultBase result;
    public ResultStatusValue(boolean isConsumed, ResultBase result) {
        this.isConsumed = isConsumed;
        this.result = result;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
    }

    public ResultBase getResult() {
        return result;
    }

    public void setResult(ResultBase result) {
        this.result = result;
    }
}
