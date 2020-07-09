package com.mera.eugeny.volosenkov.simpleforum.forumkafka.app;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers.MySerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class UserSessions extends CopyOnWriteArrayList<ActiveUser> {
    //need for generate
    private static final int GENERATORS_NUM=8;
    private final static String usersSessionsDBPath= Paths.get(AppData.getDataPath(), "sessions.json").toString();
    private static Random rand = new Random();
    private static AtomicLong[] generatorNodes = new AtomicLong[GENERATORS_NUM];
    public static String getSessionID()
    {
        Long resultLongSession = 0l;
        if (generatorNodes[0]==null)
        {
            for(int id = 0; id< GENERATORS_NUM;id++)
            {
                generatorNodes[id] = new AtomicLong(0);
            }
        }

        for( AtomicLong elem: generatorNodes)
        {
            resultLongSession*=1000;
            int randNum = rand.nextInt(1000000);
            long curr_val = elem.get();
            if((randNum+curr_val)%2l==0l)
            {
                resultLongSession+= elem.incrementAndGet();
            }
            else
            {
                resultLongSession+=curr_val;
            }
        }
        return String.valueOf(resultLongSession);
    }
    public boolean addActiveUser(ActiveUser user) throws IOException {
        AtomicBoolean isExist= new AtomicBoolean(false);
        this.forEach((o)->{if(o.getLogin().equals(user.getLogin()))
        {
            isExist.set(true);

        }});
        if(isExist.get())
        {
            return false;

        }
        else
        {
            this.add(user);
            update();
            return true;
        }

    }

    public void deleteActiveUser(String session)
    {
        this.remove(findActiveUserBySession(session));
    }
    public ActiveUser findActiveUserBySession(String session)
    {
        ActiveUser res = null;
        for (ActiveUser activeUser : this) {
            if(activeUser.getSessionId().equals(session))
            {
                res = activeUser;
            }
        }
        return res;
    }
    public static UserSessions init() throws IOException {
        File sessionsDB = new File(usersSessionsDBPath);
        if(sessionsDB.exists())
        {
            if (sessionsDB.length()==0)
            {
                return new UserSessions();
            }
            return (UserSessions) MySerializer.Deserialize(sessionsDB, UserSessions.class);
        }
        return new UserSessions();
    }

    public void update() throws IOException {
        File citiesFileHandler = new File(usersSessionsDBPath);
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
