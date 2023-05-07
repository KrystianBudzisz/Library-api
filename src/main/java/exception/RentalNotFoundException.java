package exception;

import jakarta.persistence.EntityNotFoundException;

public class RentalNotFoundException extends EntityNotFoundException {
    public RentalNotFoundException(String message) {
        super(message);
    }
}
