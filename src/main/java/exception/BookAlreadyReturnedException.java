package exception;

public class BookAlreadyReturnedException extends RuntimeException {
    public BookAlreadyReturnedException(Long message) {
        super(String.valueOf(message));
    }
}
