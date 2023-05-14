package exceptions;

/**
 * exception that is used when there is error in script
 */
public class ScriptInScript extends RuntimeException {
    public ScriptInScript(){
        System.out.println("Executing script in script");
    }
}