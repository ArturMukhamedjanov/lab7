package changingClasses;

import model.Flat;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ServerReply implements Serializable {
    public ArrayList<String> massages;
    public ArrayList<Flat> flats;

    public ServerReply(ArrayList<String> massages, ArrayList<Flat> flats){
        this.massages = massages;
        this.flats = flats;
    }

    public void output_information(){
        if(massages != null) {
            Stream<String> massageStream = massages.stream();
            massageStream.forEach(System.out::println);
        }
        if(flats != null) {
            Stream<Flat> flatStream = flats.stream();
            flatStream.map(Flat::toString).forEach(System.out::println);
        }
    }
    public static ByteBuffer toBuffer(ServerReply obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        oos.close();
        baos.close();
        return buffer;
    }

    public static ServerReply toServerReply(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        InputStream byteStream = new ByteArrayInputStream(buffer.array(),0, buffer.limit());
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return (ServerReply)objStream.readObject();
    }
}
