package com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.UserSessions;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.Users;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;

import java.io.*;

public class MySerializer {
    private static ObjectMapper mapper = null;
    private static SimpleModule module = null;
    private static void  init()
    {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addSerializer(new UsersSerializer());
        module.addDeserializer(Users.class, new UsersDeserializer());
        module.addSerializer(new UserSerializer());
        module.addDeserializer(User.class, new UserDeserializer());
        module.addSerializer(new ActiveUserSerializer());
        module.addDeserializer(ActiveUser.class, new ActiveUserDeserializer());
        module.addSerializer(new UserSessionSerializer());
        module.addDeserializer(UserSessions.class, new UserSessionsDeserializer());
        mapper.registerModule(module);
    }
    public static String Serialize( Object obj) throws JsonProcessingException {
        if(mapper == null || module == null)
        {
            init();
        }
        return mapper.writeValueAsString(obj);
    }
    public static Object Deserialize(String jsonStr, Class resClass) throws JsonProcessingException {
        if(mapper == null || module == null)
        {
            init();
        }
        return mapper.readValue(jsonStr, resClass);
    }
    public static Object Deserialize(File file, Class resClass) throws IOException {
        if(mapper == null || module == null)
        {
            init();
        }
        StringBuilder builder = new StringBuilder();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String fileLine = reader.readLine();
        while(fileLine!=null)
        {
            builder.append( fileLine);
            fileLine = reader.readLine();
        }
        reader.close();
        stream.close();
        Object obj = mapper.readValue(builder.toString(), resClass);
        return obj;
    }
}
