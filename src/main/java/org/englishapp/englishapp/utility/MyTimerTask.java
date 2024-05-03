package org.englishapp.englishapp.utility;

import javafx.application.Platform;
import org.englishapp.englishapp.Controller.GuessWordGameController;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private GuessWordGameController guessWordGameController;
    public MyTimerTask(GuessWordGameController guessWordGameController){
        this.guessWordGameController = guessWordGameController;
    }
    @Override
    public void run() {
        Platform.runLater(()->this.guessWordGameController.handeSystickInterrupt());
         //this.guessWordGameController.handeSystickInterrupt();
    }
}
