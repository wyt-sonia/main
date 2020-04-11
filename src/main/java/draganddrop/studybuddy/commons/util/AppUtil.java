package draganddrop.studybuddy.commons.util;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.MainApp;

import javafx.scene.image.Image;

/**
 * A container for App specific utility functions
 */
public class AppUtil {

    public static Image getImage(String imagePath) {
        requireNonNull(imagePath);
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalArgumentException if {@code condition} is false.
     */
    public static void checkArgument(Boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalArgumentException with {@code errorMessage} if {@code condition} is false.
     */
    public static void checkArgument(Boolean isValid, String errorMessage) {
        if (!isValid) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
