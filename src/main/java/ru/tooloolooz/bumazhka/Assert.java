package ru.tooloolooz.bumazhka;

import org.jspecify.annotations.Nullable;

/**
 * Utility class for condition checking and exception throwing.
 * Provides methods for performing runtime condition checks.
 */
final class Assert {

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private Assert() {
        unsupported("Utility class should not be instantiated");
    }

    /**
     * Ensures that an object reference is not {@code null}.
     * Throws an {@code IllegalArgumentException} with the specified message if the object is {@code null}.
     *
     * @param obj     the object reference to check for nullity.
     * @param message the message to be used in the exception.
     * @throws IllegalArgumentException if {@code obj} is {@code null}.
     */
    public static void notNull(@Nullable final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throws an {@code UnsupportedOperationException} with the specified message.
     *
     * @param message the message to be used in the exception.
     * @throws UnsupportedOperationException always.
     */
    public static void unsupported(final String message) {
        throw new UnsupportedOperationException(message);
    }
}
