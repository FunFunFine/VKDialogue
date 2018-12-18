package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UsersDataBase
{
    private Map<Long, SimpleUserToken> _users = new HashMap<>();

    @Getter
    private Boolean _loaded;

    public UsersDataBase()
    {
        _loaded = false;
    }

    void AddUser(Long id)
    {
        if (!_users.containsKey(id))
        {
            _users.put(id, new SimpleUserToken(id));
        }
    }

    public Boolean Contains(Long id)
    {
        return _users.containsKey(id);
    }

    SimpleUserToken GetUser(Long id)
    {
        return _users.get(id);
    }

    public void LoadDatabase()
    {
        try
        {
            if (false){ FileInputStream fis = new FileInputStream("users.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            _users = (HashMap<Long, SimpleUserToken>) ois.readObject();
            ois.close();
            fis.close();}
            _users = new HashMap<>();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }

    public void SaveDataBase()
    {
        try
        {
            if (false) {  FileOutputStream fos =
                    new FileOutputStream("users.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(_users);
            oos.close();
            fos.close();}
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
