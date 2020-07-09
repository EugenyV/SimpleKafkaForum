package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.IOException;
import java.util.List;

public class UserSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
    {
        if(value!=null)
        {
            jgen.writeStartObject();
            jgen.writeStringField("login", value.getLogin());
            jgen.writeStringField("password", value.getPassword());
            jgen.writeEndObject();
        }
    }
    @Override
    public Class<User> handledType() {
        return User.class;
    }
}
