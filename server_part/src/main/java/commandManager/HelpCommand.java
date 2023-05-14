package commandManager;

import changingClasses.ServerReply;
import model.Flat;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * command that show description of all commands
 */
public class HelpCommand implements Command{
    private String name = "help";
    private final ArrayList<String> commandDiscriptions;
    private String discription = "help : gives information about commands.";

    public HelpCommand(ArrayList<String> commandDiscriptions){
        this.commandDiscriptions = commandDiscriptions;
    }

    /**
     * show descriptions of all commands
     * @return result of command
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception{
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        ArrayList<String> massages = new ArrayList<>();
        for (String commandDiscription : commandDiscriptions) {
            massages.add(commandDiscription);
        }
        return new ServerReply(massages, null);
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
