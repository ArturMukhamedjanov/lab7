package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * Gives information about collection
 */
public class InfoCommand implements Command{
    private String name = "info";
    private String discription = "info : gives information about collection.";
    private LinkedHashMapManager linkedHashMapManager;

    public InfoCommand(LinkedHashMapManager linkedHashMapManager){
        this.linkedHashMapManager = linkedHashMapManager;
    }
    /**
     * show information about collection
     * @return result of program
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login){
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<String> massages = new ArrayList<String>();
        massages.add("You are working with collection : " + linkedHashMapManager.getLinkedHashMap().getClass().toString() + ". Amount of elements : " + linkedHashMapManager.getLinkedHashMap().size() + ". List of keys :" + linkedHashMapManager.getLinkedHashMap().keySet().toString() + ".");
        return new ServerReply(massages, null);
    }

    /**
     * @return description od command
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
