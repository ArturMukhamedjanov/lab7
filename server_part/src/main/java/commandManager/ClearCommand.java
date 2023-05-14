package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * Clear command for collection
 */
public class ClearCommand implements Command{
    private String name = "clear";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;
    private String discription = "clear : clear collection.";

    public ClearCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }
    /**
     * Method clears collection by calling clear method in LinkedHashmapmanager
     * @return result of the method
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception{
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<Long> id_to_remove = dbManager.clearCollection(login);
        linkedHashMapManager.clearCollection(id_to_remove);
        ArrayList<String> massages = new ArrayList<String>();
        massages.add("Collection was cleared successfully");
        return new ServerReply(massages, null);
    }

    /**
     * @return description of the command
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @return name of the command
     */
    public String getName() {
        return name;
    }
}
