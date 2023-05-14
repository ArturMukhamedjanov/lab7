import commandManager.CommandListener;
import data_manager.LinkedHashMapManager;
import db_helper.DBManager;
import model.Flat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static LinkedHashMapManager linkedHashMapManager;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        DBManager dbManager = null;
        try {
            dbManager = new DBManager();
        } catch (SQLException | IOException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.info("Can't connect to database, please, check database url and reboot server.");
            System.exit(0);
        }
        try {
            LinkedHashMap<Integer, Flat>  linkedHashMap = dbManager.loadCollection();
            linkedHashMapManager = new LinkedHashMapManager(linkedHashMap);
        }catch (Exception e){
            LOGGER.info("Can't load data correctly.");
        }
        LOGGER.info("===Start server...===");
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 4044));
            while (true){
                SocketChannel serverClient = serverSocketChannel.accept();
                if(serverClient != null){
                    LOGGER.info("===Client is connected===");
                    executorService.submit(new CommandListener(linkedHashMapManager, dbManager, serverClient));
                }
            }

        }catch (Exception e){
            LOGGER.warn(e.getMessage());
            LOGGER.info("===Cant set up the server===");
        }


    }

}
