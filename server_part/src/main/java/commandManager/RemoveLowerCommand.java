package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.BrokenScript;
import exceptions.ClientDisconnectedException;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * remove element, lower than entered
 */
public class RemoveLowerCommand implements Command{
    private String name = "remove_lower";
    private String discription = "remove_lower : delete all elements, lower than given.";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;

    private SocketChannel ch;
    private final Logger LOGGER = LoggerFactory.getLogger(RemoveLowerCommand.class);

    public RemoveLowerCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
        this.ch = ch;
    }

    /**
     * remove all elements, lower than entered
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws IOException, ClassNotFoundException {
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        try {
            ArrayList<Long> id_to_remove = dbManager.removeLowerElements(flat, login);
            linkedHashMapManager.removeLowerElements(flat, id_to_remove);
            ArrayList<String> massages = new ArrayList<>();
            massages.add("Elements were deleted successfully");
            return new ServerReply(massages, null);
        }catch (Exception e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Something wrong with database...");
            return new ServerReply(massages, null);
        }
    }

    /**
     * @return description of command
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @return name of command
     */
    public String getName() {
        return name;
    }
}
