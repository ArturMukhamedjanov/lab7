package commandManager;

import changingClasses.ServerReply;
import data_manager.LinkedHashMapManager;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Counts
 */
public class CountLessThanNumberOfRoomsCommand implements Command{
    private String name = "count_less_than_number_of_rooms";
    private String discription = "count_less_than_number_of_rooms: output amount of elements with number of rooms, lower than given.";
    private LinkedHashMapManager linkedHashMapManager;

    public CountLessThanNumberOfRoomsCommand(LinkedHashMapManager linkedHashMapManager){
        this.linkedHashMapManager = linkedHashMapManager;
    }
    /**
     * counts of elements, which value of number of rooms less than given, by calling count_elements_less_than_number_of_rooms method in LinkedHashMapManager
     * @return result of command
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception{
        try {
            if(arguments.length != 2){
                throw new InputMismatchException();
            }
            long numbers_of_rooms = Long.parseLong(arguments[1]);
            ArrayList<String> massages = new ArrayList<String>();
            massages.add(linkedHashMapManager.countElementsLessThanNumberOfRooms(numbers_of_rooms));
            return new ServerReply(massages, null);
        } catch (InputMismatchException | NumberFormatException e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Entered value is incorrect. Repeat command, please.");
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
