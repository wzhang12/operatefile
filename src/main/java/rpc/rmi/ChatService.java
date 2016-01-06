package rpc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public interface ChatService extends Remote
{
    String sayHello() throws RemoteException;

    String ping(final String name) throws RemoteException, IllegalArgumentException;

    /**
     * Attempts to register a new user to the system given a name. Returns false
     * if the name already exists with a user, true is successful.
     * 
     * @param name
     * @return
     * @throws RemoteException
     */
    boolean registerUser(final String name) throws RemoteException;

    /**
     * Removes a user from the system.
     * 
     * @param name
     * @return
     * @throws RemoteException
     */
    boolean unRegisterUser(final String name) throws RemoteException;

    /**
     * Returns a listing of all the current chat rooms.
     * 
     * @return
     * @throws RemoteException
     */
    List<String> getChatRoomListing() throws RemoteException;

    /**
     * Returns a listing of all the current chat rooms the given user belongs
     * to.
     * 
     * @return
     * @throws RemoteException
     */
    Set<String> getChatRoomListing(final String user) throws RemoteException;

    /**
     * Attempts to join a user to a chat room. Returns false if the user is
     * already in the room.
     * 
     * @param name
     * @return
     * @throws RemoteException
     */
    boolean joinChatRoom(final String user, final String chatRoom) throws RemoteException, IllegalArgumentException;

    /**
     * Attempts to have the user leave the chat room. Returns false if the user
     * does not belong to the room.
     * 
     * @param name
     * @return
     * @throws RemoteException
     */
    boolean leaveChatRoom(final String user, final String chatRoom) throws RemoteException;

    /**
     * Attempts to send the given message. Throws an InvalidMessageException if
     * the Message has null members.
     * 
     * @param message
     * @return
     * @throws RemoteException
     * @throws InvalidMessageException
     */
    void sendMessage(final NonValidatedMessage message) throws RemoteException, InvalidMessageException;

    /**
     * Returns all the messages from all the chat rooms the user belongs to
     * since the chat room was joined by the user or the last time this method
     * was called for this user.
     * 
     * @return
     * @throws RemoteException
     */
    Queue<String> recieveMessages(final String user) throws RemoteException;
}