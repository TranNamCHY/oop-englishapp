package org.englishapp.englishapp.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseGameController implements Initializable, InterfaceController {

    @FXML
    private Label gameRule;

    @FXML
    private Button guessWordGameButton;

    @FXML
    private Label gameRuleLabel;
    @FXML
    private Button enterGameButton;
    private GeneralAppController generalAppController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setState();
        this.gameRuleLabel.setText("Guess Word Game: In this game, you will be given a picture about a thing or a phenomenon" +
                " and four choices that describes it. Your duty is choosing the answer that describe most exactly in one of them." +
                "\n\nFor each question, you will have ten seconds to prepare and choose your answer, with each correct answer, you" +
                " will be given ten point, and will have 11 question in summary.\n\nHave Fun and Good Luck !");
    }

    public void setGeneralAppController(GeneralAppController generalAppController) {
        this.generalAppController = generalAppController;
    }

    public void onGuessGameButtonClick() {
    }

    public void onEnterGameButtonClick() {
        this.generalAppController.loadGuessWordGame();
    }

    @Override
    public void setState() {
        StateMachine.setChooseGame();
    }
}
