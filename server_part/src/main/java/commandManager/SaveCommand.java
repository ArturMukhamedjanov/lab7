package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * save collection in file
 */
public class SaveCommand implements Command{
    private String name = "save";
    private String discription = "save : save collection into a file.";
    private LinkedHashMapManager linkedHashMapManager;

    public SaveCommand(LinkedHashMapManager linkedHashMapManager){
        this.linkedHashMapManager = linkedHashMapManager;
    }

    /**
     * save collection in file, if file was not found, throw exception
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws BrokenScript {
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        try {
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Collection is saved successfully");
            return new ServerReply(massages, null);
        }
        catch (Exception e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("File was not found");
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
