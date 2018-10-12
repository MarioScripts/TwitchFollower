package Exceptions;

/**
 * Used when the specified stream channel does not exist
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
