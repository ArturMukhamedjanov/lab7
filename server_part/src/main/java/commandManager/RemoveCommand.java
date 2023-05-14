package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * remove element by entered id
 */
public class RemoveCommand implements Command{
    private String name = "remove_key";
    private String discription = "remove_key : delete element from collection by given key.";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;

    public RemoveCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }

    /**
     * remove element by given key
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws BrokenScript {
        try {
            if(arguments.length != 2){
                throw new InputMismatchException();
            }
            int key = Integer.parseInt(arguments[1]);
            if(linkedHashMapManager.checkKey(key)){
                throw new InputMismatchException();
            }
            ArrayList<Long> id_to_remove = dbManager.removeElement(key, login);
            linkedHashMapManager.removeElement(key, id_to_remove);
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Elements were deleted successfully");
            return new ServerReply(massages, null);
        } catch (InputMismatchException e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Entered value is incorrect. Repeat command, please.");
            return new ServerReply(massages, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String getDiscription() {
        return discription;
    }

    public String getName() {
        return name;
    }
}
