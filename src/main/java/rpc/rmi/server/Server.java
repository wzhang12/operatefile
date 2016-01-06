package rpc.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import  rpc.rmi.ChatService;
import  rpc.rmi.DistributedChatServiceInfo;
import  rpc.rmi.InvalidMessageException;
import  rpc.rmi.NonValidatedMessage;

public class Server implements ChatService
{
    private static final HashMap<String, ChatRoom> _rooms = new HashMap<String, ChatRoom>();
    private static final HashMap<String, User> _users = new HashMap<String, User>();
    private static final int TIME_UNTIL_DISCONNECT_USER_SECONDS = 5;
    private static final int CUSTODIAN_SLEEP_INTERVAL_MILLIS = 2500;
    private static final Scanner _systemIn = new Scanner(System.in);
    private static final int CLIENT_RECIEVE_MESSAGE_WAIT_TIME = 5;
    private static final int CLIENT_WAIT_INTERVAL_MILLIS = 1000;
    private static final String SYNTAX_ERROR_HEADER = "Command Syntax: ";
    private static boolean _shutdown = false;

    public Server()
    {
    }

    public static void main(String args[])
    {
        Registry registry = null;
        ChatService chatService = null;

        try
        {
            // create server
            Server obj = new Server();

            // create registry
            registry = LocateRegistry.createRegistry(DistributedChatServiceInfo.PORT_NUMBER);

            // export server
            chatService = (ChatService) UnicastRemoteObject.exportObject(obj, 0);

            registry.bind(DistributedChatServiceInfo.CHAT_SERVICE_BINDING, chatService);
        }
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        /*
         * Removes users that have abandoned.
         */
        Thread custodian = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (true)
                {
                    Date now = new Date();
                    for (User user : _users.values())
                    {
                        long nowMilis = now.getTime();
                        long lastUpdateMilis = user.getLastPing().getTime();
                        if ((nowMilis - lastUpdateMilis) / 1000 > TIME_UNTIL_DISCONNECT_USER_SECONDS)
                        {
                            removeUser(user.getName());
                        }
                    }
                    try
                    {
                        Thread.sleep(CUSTODIAN_SLEEP_INTERVAL_MILLIS);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        custodian.start();

        makeRoom("Test");
        System.out.println("Type a command. Type \"help\" for assistance.");
        while (true)
        {
            ArrayList<String> parameters = getCommand();
            if (parameters == null || parameters.isEmpty())
            {
                continue;
            }

            switch (parameters.get(0).toLowerCase())
            {
                case "help":
                    // display help message
                    help();
                    break;
                case "shutdown":
                    shutdown();
                    break;
                case "listrooms":
                    displayChatRooms();
                    break;
                case "genrooms":
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "genRooms <number of rooms>");
                        break;
                    }
                    genRooms(Integer.parseInt(parameters.get(1)));
                    break;
                case "clrrooms":
                    clearRooms();
                    makeRoom("Test");
                    break;
                case "mkroom":
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "mkroom <name>");
                        break;
                    }

                    if (makeRoom(parameters.get(1)))
                    {
                        System.out.println(parameters.get(1) + " made.");
                    }
                    else
                    {
                        System.err.println("Could not make " + parameters.get(0));
                    }
                    break;
                case "rmroom":
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "rmroom <name>");
                        break;
                    }

                    if (removeRoom(parameters.get(1)))
                    {
                        System.out.println(parameters.get(1) + " removed.");
                    }
                    else
                    {
                        System.err.println("Could not remove " + parameters.get(1));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void clearRooms()
    {
        List<ChatRoom> rooms = new LinkedList<ChatRoom>(_rooms.values());
        for (ChatRoom room : rooms)
        {
            removeRoom(room.getName());
        }
    }

    private static void genRooms(int numRooms)
    {
        for (int i = 0; i < numRooms; i++)
        {
            makeRoom("" + i);
        }
    }

    private static boolean removeRoom(final String room)
    {
        if (room != null && !room.isEmpty() && _rooms.containsKey(room))
        {
            _rooms.get(room).clearUsers();
            _rooms.remove(room);
            return true;
        }
        return false;
    }

    private static boolean makeRoom(final String room)
    {
        if (room != null && !room.isEmpty() && !_rooms.containsKey(room))
        {
            _rooms.put(room, new ChatRoom(room));
            return true;
        }
        return false;
    }

    private static void shutdown()
    {
        _shutdown = true;

        // send a message to all rooms about shutdown
        String shutdownMessage = "The server is shutting down now.";
        Date now = new Date();
        User admin = new User("Server Admin");
        for (ChatRoom room : _rooms.values())
        {
            try
            {
                room.broadcastMessage(new ValidatedMessage(shutdownMessage, room, admin, now));
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            catch (InvalidMessageException e)
            {
                e.printStackTrace();
            }
        }

        // wait for each user to get the rest of their messages
        System.out.println("Waiting for clients to recieve final messages.");
        for (int i = CLIENT_RECIEVE_MESSAGE_WAIT_TIME; i > 0; i--)
        {
            try
            {
                System.out.println(i + " ...");
                Thread.sleep(CLIENT_WAIT_INTERVAL_MILLIS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    private static void help()
    {
        System.out.println("Welcome to the Distributed Chat Service.");
        System.out.println("Use this system to manage the system.");
        System.out.println("There are several commands to control this system:");
        System.out.println("\tshutdown : quits the system");
        System.out.println("\tlistrooms : displays the available chat rooms to join");
        System.out.println("\tmkroom <room name> : makes a new chat room");
        System.out.println("\trmroom <room name> : removes a chat room");
        System.out
                .println("\tgenRooms <number of rooms> : generate a number of randomly named rooms. (for metrics testing)");
        System.out.println("\tclrRooms : deletes all chat rooms");
    }

    private static void displayChatRooms()
    {
        System.out.println("The following rooms are available:");
        List<ChatRoom> rooms = new LinkedList<ChatRoom>(_rooms.values());
        Collections.sort(rooms);
        for (ChatRoom room : rooms)
        {
            System.out.println(room.getName());
        }
    }

    private static ArrayList<String> getCommand()
    {
        ArrayList<String> parameters = null;

        String input = _systemIn.nextLine();
        if (input != null && !input.equals(""))
        {
            // parse the whole line into tokens
            parameters = new ArrayList<String>();
            Scanner inputParser = new Scanner(input);
            do
            {
                // add the token to the list
                String token = inputParser.next();
                parameters.add(token);
            }
            while (inputParser.hasNext());
            inputParser.close();
        }

        return parameters;
    }

    @Override
    public String sayHello()
    {
        return "Hello, world!";
    }

    @Override
    public boolean registerUser(final String name) throws RemoteException
    {
        if (_shutdown)
        {
            return false;
        }
        // return false if user already exists with this name
        if (_users.containsKey(name))
        {
            return false;
        }

        // otherwise create the user, add them to the users list, and return
        // true
        User user = new User(name);
        _users.put(name, user);
        return true;
    }

    private static boolean removeUser(final String name)
    {
        if (!_users.containsKey(name))
        {
            // cannot remove a user if they don't exist
            return false;
        }

        User user = _users.remove(name);
        for (ChatRoom room : _rooms.values())
        {
            room.removeUser(user);
        }
        return true;
    }

    @Override
    public boolean unRegisterUser(final String name) throws RemoteException
    {
        return removeUser(name);
    }

    @Override
    public List<String> getChatRoomListing() throws RemoteException
    {
        if (_shutdown)
        {
            return null;
        }
        List<String> rooms = new LinkedList<String>();
        for (ChatRoom room : _rooms.values())
        {
            rooms.add(room.getName());
        }
        return rooms;
    }

    @Override
    public boolean joinChatRoom(final String name, final String chatRoom) throws RemoteException,
            IllegalArgumentException
    {
        if (_shutdown)
        {
            return false;
        }
        return getRoom(chatRoom).addUser(getUser(name));
    }

    @Override
    public boolean leaveChatRoom(final String name, final String chatRoom) throws RemoteException,
            IllegalArgumentException
    {
        return getRoom(chatRoom).removeUser(getUser(name));
    }

    @Override
    public void sendMessage(final NonValidatedMessage msg) throws RemoteException, InvalidMessageException,
            IllegalArgumentException
    {
        if (_shutdown)
        {
            return;
        }
        // check the message is good first
        // throws an exception if it is not good
        List<ValidatedMessage> messages = checkMsg(msg);

        // broadcast the message
        for (ValidatedMessage message : messages)
        {
            message.getRecipient().broadcastMessage(message);
        }
    }

    @Override
    public Queue<String> recieveMessages(final String name) throws RemoteException
    {
        return getUser(name).getMessages();
    }

    /**
     * Guaranteed to return a user or throw an exception because of invalid
     * name.
     * 
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    private static User getUser(final String name) throws IllegalArgumentException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("User name is null.");
        }
        User user = _users.get(name);
        if (user == null)
        {
            throw new IllegalArgumentException("User: " + name + " is unregistered.");
        }
        return user;
    }

    /**
     * Guaranteed to return a chat room or throw an exception because of invalid
     * name.
     * 
     * @param chatRoom
     * @return
     * @throws IllegalArgumentException
     */
    private static ChatRoom getRoom(final String chatRoom) throws IllegalArgumentException
    {
        if (chatRoom == null)
        {
            throw new IllegalArgumentException("Chat room is null.");
        }

        ChatRoom room = _rooms.get(chatRoom);
        if (room == null)
        {
            throw new IllegalArgumentException("Chat room: " + chatRoom + " does not exist.");
        }
        return room;
    }

    /**
     * Checks to make sure every part of a message is valid.
     * 
     * @param msg
     * @return
     * @throws InvalidMessageException
     */
    private static List<ValidatedMessage> checkMsg(final NonValidatedMessage msg) throws InvalidMessageException,
            IllegalArgumentException
    {
        if (msg.getContent() == null)
        {
            throw new InvalidMessageException("Content is null.");
        }

        User user = getUser(msg.getSender());

        if (msg.getTimeSent() == null)
        {
            throw new InvalidMessageException("Date/Time is null.");
        }

        Date now = new Date();
        if (msg.getTimeSent().compareTo(now) > 0)
        {
            throw new InvalidMessageException("Date/Time is in the future.");
        }

        if (msg.getRecipientList() == null)
        {
            throw new InvalidMessageException("Recipient list is null.");
        }

        List<ValidatedMessage> messages = new LinkedList<ValidatedMessage>();
        // checks to make sure all the rooms on the list exist
        for (String roomName : msg.getRecipientList())
        {
            ChatRoom room = getRoom(roomName);
            if (room.checkUserExists(user))
            {
                try
                {
                    messages.add(new ValidatedMessage(msg.getContent(), room, user, msg.getTimeSent()));
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    @Override
    public String ping(final String name) throws RemoteException, IllegalArgumentException
    {
        Date now = new Date();
        getUser(name).setLastPing(now);
        return "Server responding to ping : " + now;
    }

    @Override
    public Set<String> getChatRoomListing(final String name) throws RemoteException
    {
        return getUser(name).getRooms();
    }

}