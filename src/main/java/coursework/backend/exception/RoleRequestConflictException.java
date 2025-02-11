package coursework.backend.exception;

public class RoleRequestConflictException extends RuntimeException {
    public RoleRequestConflictException(String message) {
        super(message);
    }
}
