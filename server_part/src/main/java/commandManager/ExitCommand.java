package commandManager;

import changingClasses.ServerReply;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * Exit program
 */
public class ExitCommand implements Command{
    private String name = "exit";
    private String discription = "exit : exit programme(without saving).";

    /**
     * shut down program
     * @return result of command
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception{
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<String> massages = new ArrayList<String>();
        massages.add("Exiting...");
        System.exit(0);
        return new ServerReply(massages, null);
    }

    /**
     * @return description of file
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @return name of file
     */
    public String getName() {
        return name;
    }
}
