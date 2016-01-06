package rpc.rmi.server;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class User
{
    private final String _name;
    private final PriorityQueue<ValidatedMessage> _messages = new PriorityQueue<ValidatedMessage>();
    private final Set<String> _rooms = new HashSet<String>();
    private Date _lastPing;

    public User(final String name)
    {
        _name = name;
        _lastPing = new Date();
    }

    public String getName()
    {
        return _name;
    }

    public synchronized void addMessage(final ValidatedMessage msg)
    {
        _messages.add(msg);
    }

    public synchronized Queue<String> getMessages()
    {
        Queue<String> messages = new LinkedList<String>();

        while (!_messages.isEmpty())
        {
            ValidatedMessage msg = _messages.remove();
            messages.add(msg.getSender().getName() + " to " + msg.getRecipient().getName() + " at " + msg.getTimeSent()
                    + ": " + msg.getContent());
        }
        return messages;
    }

    public synchronized boolean hasMessages()
    {
        return !_messages.isEmpty();
    }

    public Date getLastPing()
    {
        return _lastPing;
    }

    public void setLastPing(Date lastPing)
    {
        this._lastPing = lastPing;
    }

    public synchronized void joinRoom(final String room)
    {
        _rooms.add(room);
    }

    public synchronized void leaveRoom(final String room)
    {
        _rooms.remove(room);
    }

    public synchronized Set<String> getRooms()
    {
        return _rooms;
    }

    @Override
    public int hashCode()
    {
        return _name.hashCode();
    }
}
