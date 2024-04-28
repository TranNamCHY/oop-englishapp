package org.englishapp.englishapp.Controller;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public interface InterfaceController {
    public static String state = null;

    public static void handleSuccessNotification(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.show();
    }

    public static void handleTranslateFailedNotification() {
        Notifications notificationBuilder = Notifications.create()
                .title("Translate failed !. Please check your internet connection")
                .text("Translate faield !")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showWarning();
    }
}
