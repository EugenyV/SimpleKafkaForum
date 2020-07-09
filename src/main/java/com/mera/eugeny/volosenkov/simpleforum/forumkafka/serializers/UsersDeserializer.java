package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.Users;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.IOException;
import java.util.Iterator;

public class UsersDeserializer  extends JsonDeserializer<Users> {
    @Override
    public Users deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        Iterator<JsonNode> it = node.iterator();
        Users result = new Users();
        while(it.hasNext())
        {
            JsonNode element =it.next();
            String str = element.toString();
            User dataElement = (User)MySerializer.Deserialize(str,User.class);
            result.addUser(dataElement);
        }
        return result;
    }
}
