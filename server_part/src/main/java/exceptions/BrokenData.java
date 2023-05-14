package exceptions;

public class BrokenData extends RuntimeException {
    public BrokenData(){
        System.out.println("Something wrong in data");
    }
}