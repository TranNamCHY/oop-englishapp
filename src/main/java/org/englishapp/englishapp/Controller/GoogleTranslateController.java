package org.englishapp.englishapp.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.englishapp.englishapp.utils.GoogleTranslateAPI;
import org.englishapp.englishapp.utils.GoogleTranslatePictureAPI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public class GoogleTranslateController implements Initializable {

    @FXML
    TextArea rightTextArea;

    @FXML
    TextArea leftTextArea;

    @FXML
    ChoiceBox firstLanguageChoiceBox;

    @FXML
    ChoiceBox secondLanguageChoiceBox;

    private final String[] sourceLanguage = {"English", "Vietnamese", "Auto"};
    private final String[] destLanguage = {"English", "Vietnamese"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        StateMachine.setGoogleTranslate();
        this.firstLanguageChoiceBox.getItems().addAll(this.sourceLanguage);
        this.firstLanguageChoiceBox.setValue(sourceLanguage[0]);
        this.secondLanguageChoiceBox.getItems().addAll(this.destLanguage);
        this.secondLanguageChoiceBox.setValue(destLanguage[1]);
    }

    public void handleDuplicateChoice1() {
        String choice1 = (String) this.firstLanguageChoiceBox.getValue();
        String choice2 = (String) this.secondLanguageChoiceBox.getValue();
        if (choice2 == choice1) {
            if (choice2 == "English") {
                this.secondLanguageChoiceBox.setValue(destLanguage[1]);
                this.firstLanguageChoiceBox.setValue(sourceLanguage[0]);
            } else if (choice2 == "Vietnamese") {
                this.firstLanguageChoiceBox.setValue(sourceLanguage[1]);
                this.secondLanguageChoiceBox.setValue(destLanguage[0]);
            }
        }
    }

    public void handleDuplicateChoice2() {
        String choice1 = (String) this.firstLanguageChoiceBox.getValue();
        String choice2 = (String) this.secondLanguageChoiceBox.getValue();
        if (choice2 == choice1) {
            if (choice2 == "English") {
                this.secondLanguageChoiceBox.setValue(destLanguage[0]);
                this.firstLanguageChoiceBox.setValue(sourceLanguage[1]);
            } else if (choice2 == "Vietnamese") {
                this.firstLanguageChoiceBox.setValue(sourceLanguage[0]);
                this.secondLanguageChoiceBox.setValue(destLanguage[1]);
            }
        }
    }

    public void handleTranslate() {
        String paragraph = this.leftTextArea.getText();
        String languageSource = (String) this.firstLanguageChoiceBox.getValue();
        String languageDest = (String) this.secondLanguageChoiceBox.getValue();
        String sourceLang = "";
        String destLang = "";
        String result = "";
        if (languageSource == this.sourceLanguage[0] && languageDest == this.destLanguage[1]) {
            try {
                result = GoogleTranslateAPI.translate(paragraph, GoogleTranslateAPI.ENGHLISH, GoogleTranslateAPI.VIETNAMESE);
            } catch (IOException | TimeoutException exception) {
                InterfaceController.handleTranslateFailedNotification();
                System.out.print(exception.getMessage());
                throw new RuntimeException();
            }
        } else if (languageSource == this.sourceLanguage[1] && languageDest == this.destLanguage[0]) {
            try {
                result = GoogleTranslateAPI.translate(paragraph, GoogleTranslateAPI.VIETNAMESE, GoogleTranslateAPI.ENGHLISH);
            } catch (IOException | TimeoutException exception) {
                InterfaceController.handleTranslateFailedNotification();
                throw new RuntimeException();
            }
        } else if (languageSource == this.sourceLanguage[2] && languageDest == this.destLanguage[0]) {
            try {
                result = GoogleTranslateAPI.translate(paragraph, GoogleTranslateAPI.AUTO, GoogleTranslateAPI.ENGHLISH);
            } catch (IOException | TimeoutException exception) {
                InterfaceController.handleTranslateFailedNotification();
                throw new RuntimeException();
            }
        } else if (languageSource == this.sourceLanguage[2] && languageDest == this.destLanguage[1]) {
            try {
                result = GoogleTranslateAPI.translate(paragraph, GoogleTranslateAPI.AUTO, GoogleTranslateAPI.VIETNAMESE);
            } catch (IOException | TimeoutException exception) {
                InterfaceController.handleTranslateFailedNotification();
                throw new RuntimeException();
            }
        }
        if (result != null) {
            this.rightTextArea.setText(result);
        }
    }

    public void ClickChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.PNG", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile == null){
            return;
        }
        String imagePath = selectedFile.getAbsolutePath();
        String scannedText = "";
        try{
            scannedText = GoogleTranslatePictureAPI.processImage(new File(imagePath));
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
        catch (TimeoutException exception){
            exception.printStackTrace();
        }
        if(scannedText != null){
            this.leftTextArea.setText(scannedText);
        }
        else {
            this.leftTextArea.setText("Some Error with GoogleTranslatePicture api");
        }
    }
}
