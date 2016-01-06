package rpc.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class NonValidatedMessage implements Serializable
{
    private static final long serialVersionUID = 8879361265628040436L;
    private final String _content;
    private final List<String> _recipient;
    private final String _sender;
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
    public NonValidatedMessage(final String content, final List<String> recipient, final String sender,
            final Date timeSent) throws InvalidMessageException, RemoteException
    {
        super();
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

    public List<String> getRecipientList()
    {
        return _recipient;
    }

    public String getSender()
    {
        return _sender;
    }

    public Date getTimeSent()
    {
        return _timeSent;
    }

}
