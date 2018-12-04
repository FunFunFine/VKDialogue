package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UsersDataBase {
    private Map<Long, SimpleUserToken> _users = new HashMap<>();

    @Getter
    private Boolean _loaded;

    public UsersDataBase()
    {
        _loaded = false;
    }

    public void AddUser(Long id)
    {
        if (!_users.containsKey(id))
            _users.put(id, new SimpleUserToken(id));
    }

    public Boolean Contains(Long id)
    {
        return _users.containsKey(id);
    }

    public SimpleUserToken GetUser(Long id)
    {
        return _users.get(id);
    }

    public void LoadDatabase()
    {
        try
        {
            FileInputStream fis = new FileInputStream("users.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            _users = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
    }

    public void SaveDataBase()
    {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("users.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(_users);
            oos.close();
            fos.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
