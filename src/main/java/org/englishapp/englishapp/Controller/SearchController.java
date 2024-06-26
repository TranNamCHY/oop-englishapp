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
import javafx.scene.web.WebView;
import javafx.fxml.Initializable;
import org.englishapp.englishapp.EnglishAppLauncher;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.englishapp.englishapp.CustomObject.Word;
import org.englishapp.englishapp.utility.GoogleVoiceAPI;

public class SearchController implements Initializable, InterfaceController {

    @FXML
    private ListView showMatchestWord;

    @FXML
    private WebView displaySearchResult;

    private GeneralAppController generalAppController;

    private int buttonYesStatus = 0;
    private int buttonNoStatus = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setState();
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("InitialApplication.fxml"));
    }

    public ListView getShowMatchestWord(){
        return this.showMatchestWord;
    }

    @Override
    public void setState() {
        StateMachine.setInitAndSeacrch();
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
        this.generalAppController.getSearchBar().setText(wordType);
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
        String wordType = this.generalAppController.getSearchBar().getText();
        try {
            GoogleVoiceAPI.getInstance().playAudio(GoogleVoiceAPI.getInstance().getAudio(wordType,
                    "en-UK"));
        } catch (NoRouteToHostException | UnknownHostException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Please check your internet connection !");
        } catch (IOException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Empty search bar");
        } catch (JavaLayerException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Unexception error");
        }
    }

    public void handleClickOnUsAudio() {
        String wordType = this.generalAppController.getSearchBar().getText();
        try {
            GoogleVoiceAPI.getInstance().playAudio(GoogleVoiceAPI.getInstance().getAudio(wordType,
                    "en-US"));
        } catch (NoRouteToHostException | UnknownHostException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Please check your internet connection !");
        } catch (IOException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Empty search bar");
        } catch (JavaLayerException exception) {
            exception.printStackTrace();
            InterfaceController.handleErrorNotification("Error", "Unexception error");
        }
    }

    public void handleClickOnSave() {
        if (this.generalAppController.getSearchBar().getText() == null) {
            return;
        }
        String favoriteWord = this.generalAppController.getSearchBar().getText();
        if (this.generalAppController.getManagementFavorite().isExist(favoriteWord)) {
            this.displayAlert("Confirm", "This word existed in favorite list, do you want to remove it ?");
            if (this.buttonYesStatus != 1) {
                return;
            }
            this.generalAppController.getManagementFavorite().deleteWord(favoriteWord);
            InterfaceController.handleSuccessNotification("Successfully Update", "Complete removing from your favorite list");
        } else {
            this.displayAlert("Confirm", "Do you want to add to your favorite list ?");
            if (this.buttonYesStatus != 1) {
                return;
            }
            this.generalAppController.getManagementFavorite().addWord(favoriteWord);
            InterfaceController.handleSuccessNotification("Successfully Update", "Complete adding to your favorite list");
        }
    }

    public void handleClickOnDelete() {
        String wordType = this.generalAppController.getSearchBar().getText();
        Word resultWord = this.generalAppController.getHandleManagement().findWord(wordType);
        if (resultWord == null) {
            InterfaceController.handleErrorNotification("Delete failed !", "Could not find that word !");
        } else {
            this.displayAlert("Confirm", "Do you want to remove from dictionary ?");
            if (this.buttonYesStatus == 1) {
                this.generalAppController.getHandleManagement().deleteWord(wordType);
                InterfaceController.handleSuccessNotification("Succesfully", "Completely delete word !");
            }
        }
    }

    /* public static void handleNotification() {
        Notifications notificationBuilder = Notifications.create()
                .title("Delete failed !")
                .text("Could not find that word !")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showWarning();
    } */

}