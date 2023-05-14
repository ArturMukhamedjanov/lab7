import changingClasses.Arguments;
import changingClasses.ServerReply;
import exceptions.BrokenScript;
import exceptions.ScriptInScript;
import model.Flat;
import validator.Validator;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandListener {
    private static SocketChannel ch;
    private InputStream input;
    private ArrayList<String> commands_with_element;
    private static ArrayList<File> files = new ArrayList<>();

    public CommandListener(SocketChannel ch, InputStream input, ArrayList<String> commands_with_element) {
        this.commands_with_element = commands_with_element;
        this.ch = ch;
        this.input = input;
    }

    public void start_listening() throws IOException {
        try {
            ch.configureBlocking(false);
            Scanner scanner = new Scanner(input);
            if (input == System.in) {
                System.out.print("$");
            }
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" +");
                if (line.length == 0 | line.length > 2) {
                    if (System.in == input) {
                        System.out.println("Incorrect input format.");
                        continue;
                    }
                }
                if (line[0].equals("exit")) {
                    Arguments arguments = new Arguments(line[0], null,null);
                    ch.write(Arguments.toBuffer(arguments));
                    System.exit(0);
                }
                if (line[0].equals("execute_script")) {
                    try {
                        File initialFile = new File(line[1]);
                        InputStream targetStream = new FileInputStream(initialFile);
                        if (files.contains(initialFile)) {
                            throw new ScriptInScript();
                        }
                        files.add(initialFile);
                        CommandListener commandListener = new CommandListener(ch, targetStream, commands_with_element);
                        commandListener.start_listening();

                        if (!files.isEmpty()) {
                            files.remove(initialFile);
                        }
                        continue;
                    } catch (FileNotFoundException e) {
                        if (input != System.in) {
                            throw new BrokenScript();
                        }
                        System.out.println("File not found, repeat command");
                        continue;
                    }
                }
                Arguments arguments;
                if (line.length < 2) {
                    arguments = new Arguments(line[0], null,null);
                } else {
                    arguments = new Arguments(line[0], line[1],null);
                }
                if(commands_with_element.contains(line[0])){
                    Flat flat = Validator.getElement(input);
                    arguments.setFlat(flat);
                }
                ch.write(Arguments.toBuffer(arguments));
                ServerReply serverReply = null;
                int tries = 0;
                while (serverReply == null) {
                    try {
                        Thread.sleep(500);
                        tries++;
                        if (tries >= 30) {
                            System.out.println("Server doesn't reply for 15 seconds, disconnecting this client,try again later");
                            break;
                        }
                        ByteBuffer buffer = ByteBuffer.allocate(ch.socket().getReceiveBufferSize());
                        ch.read(buffer);
                        serverReply = ServerReply.toServerReply(buffer);
                    } catch (Exception e) {
                        //ignore
                    }
                }
                serverReply.output_information();
                if (input == System.in) {
                    System.out.print("$");
                }
            }

        } catch (BrokenScript | ScriptInScript | IOException e) {
            if (input != System.in) {
                files.clear();
                System.out.println("Error in script");
                System.out.print("$");
            }

        }
    }
}
