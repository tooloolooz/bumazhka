package ru.tooloolooz.bumazhka;

import java.io.Serial;

/**
 * Generic validation exception indicating that a value or object fails to meet required
 * constraints or business rules.
 * <p>
 * This runtime exception serves as a general-purpose validation error indicator that can
 * be used across various validation contexts. It signals that a validation check has failed
 * due to invalid input data, incorrect format, or violation of business rules.
 */
public final class NotValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code NotValidException} with the specified detail message.
     * <p>
     * The message should clearly describe the validation failure, including the invalid value
     * and the specific validation rule that was violated.
     *
     * @param message the detail message explaining the validation failure.
     */
    public NotValidException(final String message) {
        super(message);
    }
}
