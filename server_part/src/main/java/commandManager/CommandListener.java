package commandManager;

import changingClasses.Arguments;
import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.ClientDisconnectedException;
import model.Flat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * class that listen input and execute needed command
 */
public class CommandListener implements Runnable{
    /**
     * List of all command that program has
     */
    private ArrayList<Command> commandList = new ArrayList<Command>();
    private ArrayList<String> commandDiscriptions = new ArrayList<String>();
    private LinkedHashMapManager linkedHashMapManager;
    private Selector selector;
    private String login = null;
    private SocketChannel socketChannel;
    private ArrayList<String> commandHistory = new ArrayList<String>();
    private ArrayList<String> commandsWithElements = new ArrayList<>();
    private DBManager dbManager;
    private final Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);

    public CommandListener(LinkedHashMapManager linkedHashMapManager, DBManager dbManager, SocketChannel socketChannel) {
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
        this.socketChannel = socketChannel;
        addBasicCommands();
        addCommandsWithElements();
    }

    /**
     * Method that get value of input, parse it, and execute command
     *
     * @throws Exception
     */
    public void run() {
        try {
            accept();
            register();
            while(true) {
                Arguments args = null;
                ServerReply result = null;
                try {
                    args = readArgument();
                } catch (Exception e) {
                    continue;
                }
                Flat flat = null;
                if (args.getFlat() != null) {
                    flat = args.getFlat();
                }
                String[] arguments = args.getList();
                boolean commandFound = false;
                for (Command command : commandList) {
                    if (arguments[0].equals(command.getName())) {
                        commandFound = true;
                        if (arguments[0].equals("exit")) {
                            LOGGER.info("===Client disconnected===");
                            return;
                        } else {
                            result = command.execution(arguments, flat, login);
                        }
                        commandHistory.add(command.getName());
                    }
                }
                if (!commandFound) {
                    ArrayList<String> massages = new ArrayList<>();
                    massages.add("Command wasn't found.");
                    result = new ServerReply(massages, null);
                    LOGGER.info("===ServerReply was sanded===");
                }
                if (result != null) {
                    sendResult(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void accept() throws IOException {
        socketChannel.configureBlocking(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream obj;
        try {
            obj = new ObjectOutputStream(baos);
            obj.writeObject(commandsWithElements);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        socketChannel.write(buffer);
    }

    private void register() throws IOException, ClassNotFoundException, SQLException {
        boolean registered = false;
        while(!registered) {
            Arguments arguments = readArgument();
            if(arguments.getCommandName().equals("check_login")){
                login = arguments.getArgument();
                String password = dbManager.checkLogin(login);
                if(password == null){
                    ArrayList<String> massages = new ArrayList<>();
                    massages.add("unknown_login");
                    ServerReply result = new ServerReply(massages, null);
                    sendResult(result);
                }
                else{
                    ArrayList<String> massages = new ArrayList<>();
                    massages.add("enter_password");
                    ServerReply result = new ServerReply(massages, null);
                    sendResult(result);
                }
            }
            if(arguments.getCommandName().equals("check_password")){
                String password = arguments.getArgument();
                if(dbManager.checkPassword(login, password)){
                    ArrayList<String> massages = new ArrayList<>();
                    massages.add("correct");
                    ServerReply result = new ServerReply(massages, null);
                    sendResult(result);
                    return;
                }else{
                    ArrayList<String> massages = new ArrayList<>();
                    massages.add("incorrect");
                    ServerReply result = new ServerReply(massages, null);
                    sendResult(result);
                }
            }
            if(arguments.getCommandName().equals("register_new_user")){
                String password = arguments.getArgument();
                dbManager.registerNewUser(login, password);
                ArrayList<String> massages = new ArrayList<>();
                massages.add("correct");
                ServerReply result = new ServerReply(massages, null);
                sendResult(result);
                return;
            }
        }

    }
    private Arguments readArgument() {
        Arguments args = null;
        while (args == null) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(700000);
                socketChannel.read(buffer);
                args = Arguments.toArguments(buffer);
                LOGGER.info("===Clients request is received===");
            } catch (Exception ignore) {
                //ignore
            }
        }
        return args;
    }

    /*private Flat readFlat(SelectionKey key) throws ClientDisconnectedException, IOException {
        LOGGER.info("===Waiting for element from client===");
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.configureBlocking(false);
        Flat flat = null;
        int tries = 0;
        while(flat == null) {
            try {
                Thread.sleep(500);
                tries++;
                if(tries >= 180){
                    LOGGER.info("Client doesn't reply for 1,5 minutes, disconnecting this client");
                    ArrayList<String> massage = new ArrayList<String>();
                    massage.add("You are disconnected for inactivity");
                    socketChannel.write(ServerReply.toBuffer(new ServerReply(massage, null)));
                    throw new ClientDisconnectedException();
                }
                ByteBuffer buffer = ByteBuffer.allocate(socketChannel.socket().getReceiveBufferSize());
                socketChannel.read(buffer);
                flat = Flat.toFlat(buffer);
            }catch (ClientDisconnectedException e){
                throw e;
            }
            catch (Exception ignore){
                //ignore because nothing was sanded
            }
        }
        LOGGER.info("===Flat is received===");
        return flat;
    }*/

    /*private void sendRequestForFlat(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ArrayList<String> massages = new ArrayList<String>();
        massages.add("Enter_element");
        socketChannel.write(ServerReply.toBuffer(new ServerReply(massages, null)));
        LOGGER.info("===Request was send===");
    }*/

    private void sendResult(ServerReply result) throws IOException {
        socketChannel.write(ServerReply.toBuffer(result));
        LOGGER.info("===Result was sand===");
    }
    /**
     * Method that add command in list of commands that can be executed
     *
     * @param command
     */
    public void addCommand(Command command) {
        commandDiscriptions.add(command.getDiscription());
        commandList.add(command);
    }

    /**
     * add basic commands
     */
    public void addBasicCommands() {
        addCommand(new ClearCommand(linkedHashMapManager, dbManager));
        addCommand(new CountLessThanNumberOfRoomsCommand(linkedHashMapManager));
        addCommand(new HelpCommand(commandDiscriptions));
        addCommand(new HistoryCommand(commandHistory));
        addCommand(new InfoCommand(linkedHashMapManager));
        addCommand(new InsertCommand(linkedHashMapManager, dbManager));
        addCommand(new MinByCoordinatesCommand(linkedHashMapManager));
        addCommand(new RemoveAllByNumbersOfRoomsCommand(linkedHashMapManager, dbManager));
        addCommand(new RemoveCommand(linkedHashMapManager, dbManager));
        addCommand(new RemoveLowerCommand(linkedHashMapManager, dbManager));
        addCommand(new RemoveLowerKeyCommand(linkedHashMapManager, dbManager));
        addCommand(new ShowCommand(linkedHashMapManager, dbManager));
        addCommand(new UpdateCommand(linkedHashMapManager, dbManager));
        addCommand(new ExitCommand());
    }
    private void addCommandsWithElements(){
        commandsWithElements.add(new InsertCommand(linkedHashMapManager, dbManager).getName());
        commandsWithElements.add(new RemoveLowerCommand(linkedHashMapManager, dbManager).getName());
        commandsWithElements.add(new UpdateCommand(linkedHashMapManager, dbManager).getName());
    }
}
