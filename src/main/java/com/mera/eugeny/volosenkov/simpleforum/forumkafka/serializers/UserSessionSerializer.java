package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.UserSessions;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.Users;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.IOException;

public class UserSessionSerializer extends JsonSerializer<UserSessions> {
    @Override
    public void serialize(UserSessions value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
    {
        if(value!=null)
        {
            jgen.writeStartArray();
            if(value.size()!=0) {
                for(ActiveUser elem: value)
                {
                    String Str = MySerializer.Serialize(elem);
                    byte[] strBytes = Str.getBytes();
                    jgen.writeObject(elem);
                }

            }
            jgen.writeEndArray();

        }
    }

    @Override
    public Class<UserSessions> handledType() {
        return UserSessions.class;
    }
}
