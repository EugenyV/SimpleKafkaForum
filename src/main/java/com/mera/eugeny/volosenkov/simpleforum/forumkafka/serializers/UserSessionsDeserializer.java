package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.UserSessions;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.Users;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.IOException;
import java.util.Iterator;

public class UserSessionsDeserializer  extends JsonDeserializer<UserSessions> {
    @Override
    public UserSessions deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        Iterator<JsonNode> it = node.iterator();
        UserSessions result = new UserSessions();
        while(it.hasNext())
        {
            JsonNode element =it.next();
            String str = element.toString();
            ActiveUser dataElement = (ActiveUser)MySerializer.Deserialize(str, ActiveUser.class);
            result.addActiveUser(dataElement);
        }
        return result;
    }
}
