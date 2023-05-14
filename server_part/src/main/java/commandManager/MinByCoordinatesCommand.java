package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * Output element with the lowest value of coordinates
 */
public class MinByCoordinatesCommand implements Command{
    private String name = "min_by_coordinates";
    private String discription = "min_by_coordinates : output element with lowest coordinates.";
    private LinkedHashMapManager linkedHashMapManager;

    public MinByCoordinatesCommand(LinkedHashMapManager linkedHashMapManager){
        this.linkedHashMapManager = linkedHashMapManager;
    }

    /**
     * Output element with the lowest value of coordinates
     * @return result of command
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws BrokenScript {
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<Flat> flats = new ArrayList<>();
        flats.add(linkedHashMapManager.giveSmallestCoordinates());
        ServerReply serverReply = new ServerReply(null, flats);
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
