package changingClasses;

import model.Flat;

import java.io.*;
import java.nio.ByteBuffer;

public class Arguments implements Serializable {
    private String commandName;
    private String argument;
    private Flat flat;


    public Arguments(String commandName, String argument, Flat flat){
        this.commandName = commandName;
        this.argument = argument;
        this.flat = flat;
    }

    public String getArgument() {
        return argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getList(){
        if(argument != null){
            String[] args = new String[2];
            args[0] = commandName;
            args[1] = argument;
            return args;
        }
        else{
            String[] args = new String[1];
            args[0] = commandName;
            return args;
        }
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public Flat getFlat() {
        return flat;
    }

    public static ByteBuffer toBuffer(Arguments obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        oos.close();
        baos.close();
        return buffer;
    }

    public static Arguments toArguments(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        InputStream byteStream = new ByteArrayInputStream(buffer.array(),0, buffer.limit());
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        Arguments arguments = (Arguments) objStream.readObject();
        byteStream.close();
        objStream.close();
        return arguments;
    }
}
