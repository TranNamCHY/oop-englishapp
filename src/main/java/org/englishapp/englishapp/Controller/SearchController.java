package org.englishapp.englishapp.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.controlsfx.control.Notifications;
import org.englishapp.englishapp.utils.TextToSpeech;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import org.englishapp.englishapp.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.englishapp.englishapp.CustomObject.Word;
import org.englishapp.englishapp.utils.GoogleVoiceAPI;

public class SearchController implements Initializable, InterfaceController {

    @FXML
    public ListView showMatchestWord;


    @FXML
    private WebView displaySearchResult;

    private GeneralAppController generalAppController;

    private int buttonYesStatus = 0;
    private int buttonNoStatus = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StateMachine.setInitAndSeacrch();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("InitialApplication.fxml"));
    }

    public void displayAlert(String title, String message) {
        buttonNoStatus = 0;
        buttonYesStatus = 0;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        Button buttonYes = new Button("Yes");
        Button buttonNo = new Button("No");
        buttonYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonYesStatus = 1;
                window.close();
            }
        });

        buttonNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonNoStatus = 1;
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, buttonYes, buttonNo);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void setGeneralAppController(GeneralAppController controller) {
        this.generalAppController = controller;
    }

    public void disableListView() {
        this.showMatchestWord.setVisible(false);
    }

    public void handleChosenFromMenu() {
        String wordType = (String) this.showMatchestWord.getSelectionModel().getSelectedItem();
        this.generalAppController.searchBar.setText(wordType);
        /* Word resultWord = this.generalAppController.handleManagement.findWord(wordType);
        if (resultWord == null) {
            displaySearchResult.getEngine().loadContent("Could not find that word!", "text/html");
        } else {
            displaySearchResult.getEngine().loadContent(resultWord.getHtmlType(), "text/html");
        } */
        try {
            this.generalAppController.startSearch();
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
    }

    public void setDisplaySearchResult(String html, String typeFile) {
        this.displaySearchResult.getEngine().loadContent(html, typeFile);
    }

    public void handleClickOnUkAudio() {
        String wordType = this.generalAppController.searchBar.getText();
        try {
            GoogleVoiceAPI.getInstance().playAudio(GoogleVoiceAPI.getInstance().getAudio(wordType,
                    "en-UK"));
        } catch (IOException | JavaLayerException e) {
            System.err.println("Failed to play Audio from Google, fallback to FreeTTS");
            TextToSpeech.speak(wordType);
        }
    }

    public void handleClickOnUsAudio() {
        String wordType = this.generalAppController.searchBar.getText();
        try {
            GoogleVoiceAPI.getInstance().playAudio(GoogleVoiceAPI.getInstance().getAudio(wordType,
                    "en-US"));
        } catch (IOException | JavaLayerException e) {
            System.err.println("Failed to play Audio from Google, fallback to FreeTTS");
            TextToSpeech.speak(wordType);
        }
    }

    public void handleClickOnSave() {
        System.out.printf("Value of searched word: %s\n", this.generalAppController.searchBar.getText());
        if (this.generalAppController.searchBar.getText() == null) {
            return;
        }
        String favoriteWord = this.generalAppController.searchBar.getText();
        if (this.generalAppController.managementFavorite.isExist(favoriteWord)) {
            this.displayAlert("Confirm","This word existed in favorite list, do you want to remove it ?");
            if(this.buttonYesStatus != 1){
                return;
            }
            this.generalAppController.managementFavorite.deleteWord(favoriteWord);
            InterfaceController.handleSuccessNotification("Successfully Update","Complete removing from your favorite list");
        } else {
            this.displayAlert("Confirm", "Are you sure ?");
            if(this.buttonYesStatus != 1){
                return;
            }
            this.generalAppController.managementFavorite.addWord(favoriteWord);
            InterfaceController.handleSuccessNotification("Successfully Update","Complete adding to your favorite list");
        }
    }

    public void handleClickOnDelete() {
        String wordType = this.generalAppController.searchBar.getText();
        Word resultWord = this.generalAppController.handleManagement.findWord(wordType);
        if (resultWord == null) {
            handleNotification();
        } else {
            this.displayAlert("Confirm", "Are you sure ?");
            if (this.buttonYesStatus == 1) {
                this.generalAppController.handleManagement.deleteWord(wordType);
            }
        }
    }

    public static void handleNotification() {
        Notifications notificationBuilder = Notifications.create()
                .title("Delete failed !")
                .text("Could not find that word !")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showWarning();
    }

}