package com.mera.eugeny.volosenkov.simpleforum.forumkafka.results;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.OutputData.OutputBase;
import lombok.Data;

import java.io.OutputStreamWriter;
@Data
public class DataResult  extends ResultBase{
    OutputBase data;

    public DataResult(OutputBase data) {
        this.data = data;
    }
}
