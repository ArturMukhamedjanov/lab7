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
 * update element by entered id
 */
public class UpdateCommand implements Command{
    private String name = "update";
    private String discription = "update : update element in collections with given id.";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;
    private final Logger LOGGER = LoggerFactory.getLogger(UpdateCommand.class);

    public UpdateCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }

    /**
     * Read element from console or file, then call method update in LinkedHahMapManager, to update element by id.
     * @return result of work
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat,String login) throws BrokenScript, IOException, ClassNotFoundException {
        Long id = (long)0;
        try {
            if (arguments.length != 2) {
                throw new InputMismatchException();
            }
            id = Long.parseLong(arguments[1]);
            if(!linkedHashMapManager.checkId(id)) {
                throw new InputMismatchException();
            }
        }catch (InputMismatchException | NumberFormatException e) {
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Illegal value for id. Repeat command, please.");
            return new ServerReply(massages, null);
        }
        try {
            flat.setId(id);
            ArrayList<Long> id_to_remove = dbManager.updateElement(id, flat, login);
            linkedHashMapManager.updateElement(id, flat, id_to_remove);
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Element was updated successfully");
            return new ServerReply(massages, null);
        }
        catch (Exception e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Something wrong with database");
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
