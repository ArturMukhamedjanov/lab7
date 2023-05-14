package commandManager;

import changingClasses.ServerReply;
import model.Flat;

import java.nio.channels.SelectionKey;

/**
 * Command interface that realize CommandPattern
 */
public interface Command {
    /**
     * Method that change collection depending on command
     * @return
     * @throws Exception
     */
    public ServerReply execution(String[] arguments, Flat flat, String login) throws Exception;

    /**
     * @return name of command
     */
    public String getName();

    /**
     * @return description of the command
     */
    public String getDiscription();
}
