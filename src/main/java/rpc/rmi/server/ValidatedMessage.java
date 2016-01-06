package rpc.rmi.server;

import java.rmi.RemoteException;
import java.util.Date;

import  rpc.rmi.InvalidMessageException;

public class ValidatedMessage implements Comparable<ValidatedMessage>
{
    private final String _content;
    private final ChatRoom _recipient;
    private final User _sender;
    private final Date _timeSent;

    /**
     * A message is sent from a single user to all members of one or more
     * chatrooms.
     * 
     * @param content
     * @param recipient
     * @param sender
     * @param timeSent
     * @throws InvalidMessageException
     */
    public ValidatedMessage(final String content, final ChatRoom recipient, final User sender, final Date timeSent)
            throws InvalidMessageException, RemoteException
    {
        if (content == null)
        {
            throw new InvalidMessageException("Message content is null.");
        }
        _content = content;

        if (recipient == null)
        {
            throw new InvalidMessageException("Message recipient is null.");
        }
        _recipient = recipient;

        if (sender == null)
        {
            throw new InvalidMessageException("Message sender is null.");
        }
        _sender = sender;

        if (timeSent == null)
        {
            throw new InvalidMessageException("Message time is null.");
        }
        _timeSent = timeSent;
    }

    public String getContent()
    {
        return _content;
    }

    public ChatRoom getRecipient()
    {
        return _recipient;
    }

    public User getSender()
    {
        return _sender;
    }

    public Date getTimeSent()
    {
        return _timeSent;
    }

    @Override
    public int compareTo(ValidatedMessage msg)
    {
        return _timeSent.compareTo(msg.getTimeSent());
    }

}
