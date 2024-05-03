package org.englishapp.englishapp.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowScore implements Initializable, InterfaceController {

    private int score;

    public TextArea showResult;

    private GeneralAppController generalAppController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.score = 0;
        this.setState();
        if (this.showResult != null) {
            System.out.print("Just a test");
        }
    }

    @Override
    public void setState() {
        StateMachine.setPlayAgain();
    }

    public void setGeneralAppController(GeneralAppController generalAppController) {
        this.generalAppController = generalAppController;
    }

    public void setScore(int score) {
        this.score = score;
        int correctAnswer = (int) (score / 10);
        int inCorrectAnswere = 11 - correctAnswer;
        this.showResult.setText(String.format("Your Score: %d\nCorrect Answer: %d\nInCorrect Answer: %d", score, correctAnswer, inCorrectAnswere));
    }

    public void onExitGameButtonClick() {
        this.generalAppController.loadChooseGame();
    }

    public void clickPlayAgain() {
        this.generalAppController.loadGuessWordGame();
    }
}
