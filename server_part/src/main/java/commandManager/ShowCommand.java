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
 *  output all fields of all elements in collection
 */
public class ShowCommand implements Command{
    private String name = "show";
    private String discription = "show : output all elements of collection.";
    private LinkedHashMapManager linkedHashMapManager;

    private DBManager dbManager;

    public ShowCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }

    /**
     * output all elements in collection
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat,String login) throws BrokenScript {
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<String> massages = new ArrayList<>();
        ArrayList<Flat> flats = new ArrayList<Flat>();
        ArrayList<Long> your_id = dbManager.getFlatsId(login);
        massages.add("Ids of your elements that you can change:");
        massages.add(your_id.toString());
        massages.add("keys:");
        for(int key : linkedHashMapManager.getLinkedHashMap().keySet()){
            massages.add(Integer.toString(key));
            flats.add(linkedHashMapManager.getLinkedHashMap().get(key));
        }
        ServerReply serverReply = new ServerReply(massages, flats);
        return serverReply;
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
