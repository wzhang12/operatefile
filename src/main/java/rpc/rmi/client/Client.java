package rpc.rmi.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import rpc.rmi.ChatService;
import  rpc.rmi.DistributedChatServiceInfo;
import  rpc.rmi.InvalidMessageException;
import  rpc.rmi.NonValidatedMessage;

public class Client
{
    private static ChatService _chatService = null;
    private static String _name = null;
    private static final int POLL_MESSAGES_INTERVAL = 2000;
    private static final Scanner _systemIn = new Scanner(System.in);
    private static final String MESSAGE_PROMPT = "/";
    private static final String NO_NAME_ERROR_MSG = "Need to register a name first.";
    private static final String SYNTAX_ERROR_HEADER = "Command Syntax: ";
    private static final int AVERAGE_COUNT = 10;

    private Client()
    {
    }

    public static void main(String[] args)
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry(DistributedChatServiceInfo.PORT_NUMBER);
            _chatService = (ChatService) registry.lookup(DistributedChatServiceInfo.CHAT_SERVICE_BINDING);
        }
        catch (Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        Thread messagePoller = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (true)
                {
                    if (_name != null)
                    {
                        try
                        {
                            _chatService.ping(_name);
                        }
                        catch (RemoteException e1)
                        {
                            System.err.println("Unable to ping server.");
                        }
                        Queue<String> messages = null;

                        try
                        {
                            messages = _chatService.recieveMessages(_name);
                        }
                        catch (RemoteException e)
                        {
                            System.err.println("Unable to recieve messages from server.");
                        }

                        if (messages != null)
                        {
                            // print list in order
                            while (!messages.isEmpty())
                            {
                                System.out.println(messages.poll());
                            }
                        }

                    }

                    try
                    {
                        Thread.sleep(POLL_MESSAGES_INTERVAL);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

        });
        messagePoller.start();

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
                case "ping":
                    if (_name == null)
                    {
                        System.err.println(NO_NAME_ERROR_MSG);
                        break;
                    }
                    pingServer();
                    break;
                case "help":
                    // display help message
                    help();
                    break;
                case "exit":
                    // unregister name and exit system
                    unRegisterUser();
                    System.exit(0);
                    break;
                case "adduser":
                    // user must have entered a name
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "adduser <name>");
                        break;
                    }
                    // add user to server
                    registerUser(parameters.get(1));
                    break;
                case "listrooms":
                    displayChatRooms();
                    break;
                case "mlistrooms":
                    measureListRooms();
                    break;
                case "myrooms":
                    if (_name == null)
                    {
                        System.err.println(NO_NAME_ERROR_MSG);
                        break;
                    }
                    // request the rooms that already belong to the user
                    listJoinedRooms();
                    break;
                case "join":
                    if (_name == null)
                    {
                        System.err.println(NO_NAME_ERROR_MSG);
                        break;
                    }
                    // user must have entered a room to join
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "join <Chat Room Name>");
                        break;
                    }
                    joinChatRoom(parameters.get(1));
                    break;
                case "mjoin":
                    measureRegTime();
                    break;
                case "leave":
                    if (_name == null)
                    {
                        System.err.println(NO_NAME_ERROR_MSG);
                        break;
                    }
                    // user must have entered a room to leave
                    if (parameters.size() != 2)
                    {
                        System.err.println(SYNTAX_ERROR_HEADER + "leave <Chat Room Name>");
                        break;
                    }
                    leaveChatRoom(parameters.get(1));
                    break;
                case "msg":
                    if (_name == null)
                    {
                        System.err.println(NO_NAME_ERROR_MSG);
                        break;
                    }

                    //@formatter:off
                    /*
                     * Message parameters should follow the order:
                     * index        value
                     * 
                     * 0:           "msg"
                     * 1 to size-3: room names
                     * size-2:      MESSAGE_PROMPT
                     * size-1:      message content
                     */
                    //@formatter:on
                    if (parameters.size() >= 4 && parameters.get(parameters.size() - 2).equals(MESSAGE_PROMPT))
                    {
                        try
                        {
                            NonValidatedMessage msg = new NonValidatedMessage(parameters.get(parameters.size() - 1),
                                    new ArrayList<String>(parameters.subList(1, parameters.size() - 2)), _name,
                                    new Date());
                            _chatService.sendMessage(msg);
                        }
                        catch (InvalidMessageException | RemoteException | IllegalArgumentException e)
                        {
                            System.err.println(e.getMessage());
                        }
                    }
                    else{
                        System.out.println("zhang");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void measureRegTime()
    {
        // prints the average time it takes to list all the rooms on the server
        long average = 0;
        for (int i = 0; i < AVERAGE_COUNT; i++)
        {
            long start = System.currentTimeMillis();
            try
            {
                if (!_chatService.joinChatRoom(_name, "Test") || !_chatService.leaveChatRoom(_name, "Test"))
                {
                    System.err.println("Something went wrong.");
                }
            }
            catch (RemoteException e)
            {
                System.err.println("Couldn't join rooms.");
            }
            long end = System.currentTimeMillis();
            average = ((average * i) + (end - start)) / (i + 1);
        }
        System.out.println(average + " ms");
    }

    private static void measureListRooms()
    {
        // prints the average time it takes to list all the rooms on the server
        long average = 0;
        int numRooms = 0;
        for (int i = 0; i < AVERAGE_COUNT; i++)
        {
            long start = System.currentTimeMillis();
            try
            {
                numRooms = _chatService.getChatRoomListing().size();
            }
            catch (RemoteException e)
            {
                System.err.println("Couldn't list rooms.");
            }
            long end = System.currentTimeMillis();
            average = ((average * i) + (end - start)) / (i + 1);
        }
        System.out.println(average + " ms for " + numRooms + " rooms.");
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

                // if the token signifies a message, add everything after the
                // message prompt as a single token so as to not distort the
                // intended message
                if (token.equals(MESSAGE_PROMPT))
                {
                    parameters.add(inputParser.nextLine());
                }
            }
            while (inputParser.hasNext());
            inputParser.close();
        }

        return parameters;
    }

    /**
     * Prints all the rooms the user has joined.
     */
    private static void listJoinedRooms()
    {
        if (_name == null)
        {
            return;
        }
        try
        {
            Set<String> rooms = _chatService.getChatRoomListing(_name);
            System.out.println("Joined rooms:");
            for (String room : rooms)
            {
                System.out.println(room);
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private static void pingServer()
    {
        if (_name == null)
        {
            return;
        }
        try
        {
            System.out.println(_chatService.ping(_name));
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private static void leaveChatRoom(final String room)
    {
        try
        {
            if (_chatService.leaveChatRoom(_name, room))
            {
                System.out.println("Left " + room);
            }
            else
            {
                System.err.println("Could not leave " + room);
            }
        }
        catch (RemoteException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    private static void joinChatRoom(final String room)
    {
        try
        {
            if (_chatService.joinChatRoom(_name, room))
            {
                System.out.println("Joined " + room);
            }
            else
            {
                System.err.println("Could not join " + room);
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static void displayChatRooms()
    {
        /*
         * Get listing of chat rooms.
         */
        List<String> rooms = null;
        try
        {
            rooms = _chatService.getChatRoomListing();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        /*
         * Display the list to the user.
         */
        if (rooms != null)
        {
            System.out.println("The following rooms are available:");
            for (String room : rooms)
            {
                System.out.println(room);
            }
        }
    }

    private static void registerUser(final String name)
    {
        // unregister name first
        unRegisterUser();

        // attempt to register name with server
        if (name != null && !name.isEmpty())
        {
            try
            {
                if (!_chatService.registerUser(name))
                {
                    System.err.println("Failed to register " + name);
                }
                else
                {
                    _name = name;
                    System.out.println("Registered as " + name);
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void unRegisterUser()
    {
        if (_name != null)
        {
            try
            {
                if (!_chatService.unRegisterUser(_name))
                {
                    System.err.println("Failed to unregister " + _name);
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            _name = null;
        }
    }

    private static void help()
    {
        System.out.println("Welcome to the Distributed Chat Service.");
        System.out.println("Use this system to send and recieve messages.");
        System.out.println("There are several commands to control this system:");
        System.out.println("\texit : quits the system");
        System.out.println("\tping : pings the server for a response");
        System.out.println("\tadduser <user name> : attempts to register to the server with a new user name, "
                + "this unregisters your previous user name with the server.");
        System.out.println("\tlistrooms : displays the available chat rooms to join");
        System.out.println("\tmyrooms : displays the chat rooms that you are already in");
        System.out.println("\tjoin <chat room> : joins the specified chat room");
        System.out.println("\tleave <chat room> : leaves the specified chat room");
        System.out.println("\tmsg [[room] ...] / message : sends a message to the specified room(s). "
                + "Everything after the \"/\" will be sent as a text message to the rooms.");
        System.out.println("\tmlistrooms : averages the time it takes to list the rooms");
        System.out.println("\tmjoin : averages the time it takes to join and leave a room");
    }
}