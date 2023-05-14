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
 * Remove all elements, which value of number of rooms equals to entered value
 */
public class RemoveAllByNumbersOfRoomsCommand implements Command{
    private String name = "remove_all_by_number_of_rooms";
    private String discription = "remove_all_by_number_of_rooms : delete all elements with given number of rooms.";
    private LinkedHashMapManager linkedHashMapManager;
    private DBManager dbManager;

    public RemoveAllByNumbersOfRoomsCommand(LinkedHashMapManager linkedHashMapManager, DBManager dbManager){
        this.linkedHashMapManager = linkedHashMapManager;
        this.dbManager = dbManager;
    }

    /**
     * Remove all elements, which value of number of rooms equals to entered value
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws BrokenScript {
        try {
            if(arguments.length != 2){
                throw new InputMismatchException();
            }
            long numbers_of_rooms = Long.parseLong(arguments[1]);
            ArrayList<Long> id_to_remove = dbManager.removeElementsByNumberOfRooms(numbers_of_rooms, login);
            linkedHashMapManager.removeElementByNumberOfRooms(numbers_of_rooms, id_to_remove);
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Elements were deleted successfully");
            return new ServerReply(massages, null);
        } catch (InputMismatchException | NumberFormatException e){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("Entered value is incorrect. Repeat command, please.");
            return new ServerReply(massages, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
