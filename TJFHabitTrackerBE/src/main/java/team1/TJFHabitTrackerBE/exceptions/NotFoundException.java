package team1.TJFHabitTrackerBE.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Elemento con id " + id + " non trovato!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
