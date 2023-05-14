package commandManager;

import changingClasses.Arguments;
import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.BrokenScript;
import exceptions.ClientDisconnectedException;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Command that add new element to collection
 */
public class InsertCommand implements Command{
    private String name = "insert";
    private String discription = "insert {integer key} : add new element to collection.";
    private LinkedHashMapManager linkedHashMapManager;
    private final Logger LOGGER = LoggerFactory.getLogger(InsertCommand.class);
    private DBManager dbManager;

    public InsertCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }
    /**
     * input element and add it into collection
     * @return result of command
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception{
        if(arguments.length < 2){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command has arguments. Repeat command please");
            return new ServerReply(massages, null);
        }
        int key = 0;
        try {
            key = Integer.parseInt(arguments[1]);
            if(!linkedHashMapManager.checkKey(key)) {
                throw new InputMismatchException();
            }
        }catch(InputMismatchException | NumberFormatException e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Illegal argument, repeat command please.");
            return new ServerReply(massages, null);
        }
        flat.setId(linkedHashMapManager.get_new_id());
        try {
            dbManager.addElement(key, flat, login);
        }catch (Exception e){
            System.out.println(e.getMessage());
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Something wrong with database, repeat command please.");
            return new ServerReply(massages, null);
        }
        linkedHashMapManager.addElement(key, flat);
        ArrayList<String> massages = new ArrayList<String>();
        massages.add("Element was added successfully.");
        return new ServerReply(massages, null);
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
