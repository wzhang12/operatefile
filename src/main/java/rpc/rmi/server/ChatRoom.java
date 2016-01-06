package rpc.rmi.server;

import java.util.HashMap;

public class ChatRoom implements Comparable<ChatRoom>
{
    private final String _name;
    private final HashMap<String, User> _users;

    public ChatRoom(final String name)
    {
        _name = name;
        _users = new HashMap<String, User>();
    }

    public String getName()
    {
        return _name;
    }

    /**
     * Adds a user to the chatroom. Will not add a user with the same name
     * twice. (users are unique within a chatroom)
     * 
     * @param user
     */
    public synchronized boolean addUser(final User user)
    {
        // check if user is already in group
        if (_users.containsKey(user.getName()))
        {
            return false;
        }

        _users.put(user.getName(), user);
        user.joinRoom(_name);
        return true;
    }

    public synchronized boolean checkUserExists(final User user)
    {
        return _users.containsKey(user.getName());
    }

    public synchronized void clearUsers()
    {
        for (User user : _users.values())
        {
            user.leaveRoom(_name);
        }
        _users.clear();
    }

    public synchronized boolean removeUser(final User user)
    {
        if (!_users.containsKey(user.getName()))
        {
            return false;
        }

        _users.remove(user.getName());
        user.leaveRoom(_name);
        return true;
    }

    public synchronized void broadcastMessage(final ValidatedMessage msg)
    {
        // remove the sender from the list temporarily so they don't get
        // their
        // own message
        _users.remove(msg.getSender().getName());

        // send the message to all other users
        for (User user : _users.values())
        {
            user.addMessage(msg);
        }

        // add the user to the list again
        _users.put(msg.getSender().getName(), msg.getSender());

    }

    @Override
    public int compareTo(ChatRoom o)
    {
        return _name.compareTo(o.getName());
    }

    @Override
    public int hashCode()
    {
        return _name.hashCode();
    }
}
