package Exceptions;

/**
 * Created by Mario on 11/13/2016.
 */
public class DuplicateStreamException extends Exception{
    public DuplicateStreamException(){
        super();
    }

    public DuplicateStreamException(String message){
        super(message);
    }
}
