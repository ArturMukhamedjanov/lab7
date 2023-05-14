package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * remove elements, which key is lower than entered
 */
public class RemoveLowerKeyCommand implements Command{
    private String name = "remove_lower_key";
    private String discription = "remove_lower_key : delete all elements with keys, lower than entered.";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;

    public RemoveLowerKeyCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }

    /**
     * remove all elements, which keys lower than entered
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat,String login) throws BrokenScript {
        try {
            if(arguments.length != 2){
                throw new InputMismatchException();
            }
            int key = Integer.parseInt(arguments[1]);
            if (linkedHashMapManager.checkKey(key)){
                ArrayList<String> massages = new ArrayList<String>();
                massages.add("Illegal argument, repeat command please.");
                return new ServerReply(massages, null);
            }
            ArrayList<Long> id_to_remove = dbManager.removeLowerKeyElements(key,login);
            linkedHashMapManager.removeLowerElementsByKey(key);
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Elements were deleted successfully");
            return new ServerReply(massages, null);
        } catch (InputMismatchException | NumberFormatException e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Illegal argument, repeat command please.");
            return new ServerReply(massages, null);
        } catch (SQLException e) {
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Something wrong with database.");
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
