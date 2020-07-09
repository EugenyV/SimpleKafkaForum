package com.mera.eugeny.volosenkov.simpleforum.forumkafka.app;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers.MySerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Users extends CopyOnWriteArrayList<User> {
    private final static String usersDBPath= Paths.get(AppData.getDataPath(), "users.json").toString();
    public boolean addUser(User user) throws IOException {
        for ( User elem:this)
        {
            if(elem.getLogin().equals(user.getLogin()))
            {
                return false;
            }
        }
        this.add(user);
        return true;
    }

    public User findUser(String login) throws IOException {
        AtomicReference<User> result = new AtomicReference<User>(null);
        this.forEach((u)->{
            if (u.getLogin().equals(login)) {
                result.set(u);
            }});
        return result.get();
    }
    public static Users init() throws IOException {
        File usersDB = new File(usersDBPath);
        if(usersDB.exists())
        {
            if(usersDB.length()==0)
            {
                return new Users();
            }
            return (Users)MySerializer.Deserialize(usersDB, Users.class);
        }
        return new Users();
    }
    public void update() throws IOException {
        File citiesFileHandler = new File(usersDBPath);
        FileOutputStream writer = new FileOutputStream(citiesFileHandler);
        try {
            String resStr = MySerializer.Serialize(this);
            writer.write(resStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            writer.close();
        }
    }
}
