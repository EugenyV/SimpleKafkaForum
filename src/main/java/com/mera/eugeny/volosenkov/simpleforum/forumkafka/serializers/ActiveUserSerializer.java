package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.IOException;

public class ActiveUserSerializer extends JsonSerializer<ActiveUser> {
    @Override
    public void serialize(ActiveUser value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
    {
        if(value!=null)
        {
            jgen.writeStartObject();
            jgen.writeStringField("login", value.getLogin());
            jgen.writeStringField("sessionId", value.getSessionId());
            jgen.writeEndObject();
        }
    }
    @Override
    public Class<ActiveUser> handledType() {
        return ActiveUser.class;
    }
}
