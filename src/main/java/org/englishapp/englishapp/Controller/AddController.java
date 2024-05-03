package org.englishapp.englishapp.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.englishapp.englishapp.Management.ManagementDictionaryDatabase;

import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable, InterfaceController {

    @FXML
    private TextArea wordField;

    @FXML
    private TextArea pronunciationField;

    @FXML
    private TextArea shortDescArea;

    @FXML
    private HTMLEditor explainField;

    private ManagementDictionaryDatabase managementDictionaryDatabase;
    private int buttonYesStatus = 0;
    private int buttonNoStatus = 0;

    private String rawHTML = "";
    @Override
    public void initialize(URL location, ResourceBundle resources){
        this.setState();
        this.rawHTML = "<h1>%s</h1><h3><i>/%s/</i></h3><h2></h2><ul><li>%s</li></ul>";
    }

    @Override
    public void setState() {
        StateMachine.setAddNewWord();
    }

    public static void handleNotification(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showWarning();
    }

    public static void handleSuccessNotification(){
        Notifications notificationBuilder = Notifications.create()
                .title("Successfully Update")
                .text("Complete Add")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showConfirm();
    }

    public void setMangementDatabase(ManagementDictionaryDatabase managementDictionaryDatabase){
        this.managementDictionaryDatabase = managementDictionaryDatabase;
    }

    public boolean checkWordField(){
        String wordType = this.wordField.getText().trim();
        if(wordType.isEmpty()){
            handleNotification("Error","Empty Wordfield");
            return false;
        }
        if(this.managementDictionaryDatabase.isExist(wordType)){
            handleNotification("Error", "Word Existed");
            return false;
        }
        return true;
    }
    public void updateExplainFieldTemplate(KeyEvent event){
        String pronunciationField = String.format(this.rawHTML,
                 this.wordField.getText(),
                this.pronunciationField.getText(),
                this.shortDescArea.getText());
        this.explainField.setHtmlText(pronunciationField);
    }
    public void onAddButtonClick(){
        if(this.checkWordField() == false){
            return;
        }
        String wordField =  this.wordField.getText().trim();
        String shortDescrip = shortDescArea.getText();
        String prounciation = pronunciationField.getText();
        String explainField = String.format("<h1>%s</h1><h3><i>/%s/</i></h3><h2></h2><ul><li>%s</li></ul>",
        wordField,prounciation,shortDescrip);
        this.displayAlert("Cofirm","Do you want to add this word ?");
        if(this.buttonYesStatus == 1) {
            this.managementDictionaryDatabase.addWord(wordField, explainField, shortDescrip, prounciation);
            handleSuccessNotification();
            this.clearFields();
        }
    }

    private void clearFields(){
        this.wordField.setText("");
        this.explainField.setHtmlText("");
        this.pronunciationField.setText("");
        this.shortDescArea.setText("");
    }
    public void displayAlert(String title,String message) {
        buttonNoStatus = 0;
        buttonYesStatus = 0;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label =  new Label();
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
        layout.getChildren().addAll(label,buttonYes,buttonNo);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
