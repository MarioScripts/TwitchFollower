package Exceptions;

/**
 * Used when a stream is already in the streaam list
 */
public class DuplicateStreamException extends Exception {
    public DuplicateStreamException() {
        super();
    }

    public DuplicateStreamException(String message) {
        super(message);
    }
}
