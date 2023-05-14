package commandManager;

import changingClasses.ServerReply;
import exceptions.BrokenScript;
import model.Flat;

import java.io.InputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

/**
 * show seven(or less, if there were not seven commands before) previous commands
 */
public class HistoryCommand implements Command{
    private String name = "history";
    private String discription = "history : output 7 previous commands(without arguments).";
    ArrayList<String> commandHistory;

    public HistoryCommand(ArrayList<String> command_history){
        this.commandHistory = command_history;
    }
    /**
     * output 7 last commands
     * @return result of command
     * @throws BrokenScript
     */
    public ServerReply execution(String[] arguments, Flat flat, String login){
        if(arguments.length > 1){
            ArrayList<String> massages = new ArrayList<String>();
            massages.add("This command doesnt have arguments, repeat command please.");
            return new ServerReply(massages, null);
        }
        int beggining = commandHistory.size() - 7;
        if(beggining < 0){
            beggining = 0;
        }
        ArrayList<String> massages = new ArrayList<String>();
        for(int i = beggining; i < commandHistory.size(); i++){
            massages.add(commandHistory.get(i));
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
