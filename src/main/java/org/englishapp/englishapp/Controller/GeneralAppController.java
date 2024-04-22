package org.englishapp.englishapp.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javazoom.jl.decoder.JavaLayerException;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;
import org.controlsfx.tools.Borders;
import org.englishapp.englishapp.Management.ManagementFavorite;
import org.englishapp.englishapp.Management.ManagementHistoryDatabase;
import org.englishapp.englishapp.utils.TextToSpeech;
import javafx.scene.transform.Translate;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import org.englishapp.englishapp.HelloApplication;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.englishapp.englishapp.CustomObject.Word;
import org.englishapp.englishapp.Management.MangementDatabase;
import org.englishapp.englishapp.utils.GoogleVoiceAPI;

import javax.security.auth.login.AccountNotFoundException;

public class GeneralAppController implements Initializable, InterfaceController {

    @FXML
    private AnchorPane InitialPane;
    @FXML
    private Label welcomeText;

    @FXML
    private Button searchTab;

    @FXML
    private Button googleTranslateTab;

    @FXML
    private Button favoritesTab;

    @FXML
    private Button historyTab;

    @FXML
    private Button addToDictTab;

    @FXML
    private Button gameTab;

    @FXML
    private Pane LeftPaneTab;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ImageView HideMenuImage;

    @FXML
    private BorderPane SearchPane;

    @FXML
    public TextField searchBar;

    @FXML
    private ListView showMatchestWord;

    private TranslateTransition LeftPaneTransition;
    @FXML
    private Button ShowMenu;

    @FXML
    private WebView displaySearchResult;
    @FXML
    private ImageView UkAudio;

    @FXML
    private ListView historyList;

    @FXML
    private VBox fatherLeftPaneTab;

    @FXML
    private TextField searchHistory;
    public MangementDatabase handleManagement;

    private ManagementHistoryDatabase managementHistoryDatabase;

    public ManagementFavorite managementFavorite;

    private int isHistoryRequest = 0;
    private SearchController searchController;

    private AddController addController;

    public String searchedWord = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the firstState: Init and Searchs
        //StateMachine.setInitAndSeacrch();

        // Load SearcherController
        /* BorderPane newBorderPane;
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("SearchController.fxml"));
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.searchController = loader.getController();
        this.searchController.setGeneralAppController(this);
        this.ChangeMainBorderPane(newBorderPane); */
        this.loadSeacherController();
        this.isHistoryRequest = 0;
        // Create ultility class
        this.managementFavorite = new ManagementFavorite();
        this.managementHistoryDatabase = new ManagementHistoryDatabase();
        this.handleManagement = new MangementDatabase();
        this.LeftPaneTransition = new TranslateTransition(Duration.millis(300), LeftPaneTab);
        this.LeftPaneTransition.setFromX(0);
        this.LeftPaneTransition.setToX(-250);
    }

    public void loadSeacherController() {
        StateMachine.setInitAndSeacrch();
        this.ClearStatusButton();
        this.searchTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("SearchController.fxml"));
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.searchController = loader.getController();
        this.searchController.setGeneralAppController(this);
        this.ChangeMainBorderPane(newBorderPane);
    }

    public void ClearStatusButton() {
        searchTab.getStyleClass().removeAll("active");
        googleTranslateTab.getStyleClass().removeAll("active");
        favoritesTab.getStyleClass().removeAll("active");
        historyTab.getStyleClass().removeAll("active");
        addToDictTab.getStyleClass().removeAll("active");
        gameTab.getStyleClass().removeAll("active");
    }

    public void ChangeMainBorderPane(BorderPane newMainBorderPane) {
        this.mainBorderPane.setTop(newMainBorderPane.getTop());
        this.mainBorderPane.setLeft(newMainBorderPane.getLeft());
        this.mainBorderPane.setCenter(newMainBorderPane.getCenter());
        this.mainBorderPane.setRight(newMainBorderPane.getRight());
        this.mainBorderPane.setBottom(newMainBorderPane.getBottom());
    }

    public void HideMenu() {
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                historyList.setVisible(true);
                LeftPaneTab.setVisible(false);
                fatherLeftPaneTab.setVisible(false);
            }
        };
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        };
        if (this.isHistoryRequest == 1) {
            this.isHistoryRequest = 0;
            this.LeftPaneTransition.setOnFinished(event1);
        } else {
            this.LeftPaneTransition.setOnFinished(event2);
        }
        if (LeftPaneTab.getTranslateX() == 0) {
            LeftPaneTransition.playFromStart();
            Image image = new Image("file:D:\\OOP_LearningEnglighApp\\EnglishApp\\src\\main\\resources\\org\\englishapp\\englishapp\\icons\\tabs\\enter.png");
            HideMenuImage.setImage(image);
        } else {
            LeftPaneTab.setVisible(true);
            fatherLeftPaneTab.setVisible(true);
            historyList.setVisible(false);
            LeftPaneTransition.setRate(-1);
            LeftPaneTransition.play();
            HideMenuImage.setImage(new Image("file:src/main/resources/org/englishapp/englishapp/icons/tabs/exit.png"));
        }
    }

    public void disableListView() {
        this.searchController.disableListView();
    }

    public void checkIfEnterTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                this.startSearch();
            } catch (IOException exception) {
                System.out.print("Error with check Enter typed func\n");
            }
        }
    }

    public void findMatchestWord() {
        this.searchController.showMatchestWord.getItems().clear();
        this.searchController.showMatchestWord.setVisible(true);
        String wordType = null;
        wordType = searchBar.getText();
        this.handleManagement.findMatchestWord(wordType);
        String[] result = new String[5000];
        for (int i = 0; i < this.handleManagement.getSearchResultList().size(); i++) {
            result[i] = this.handleManagement.getSearchResultList().get(i).getWordType();
        }
        this.searchController.showMatchestWord.getItems().addAll(result);
    }

    public void handleChosenFromMenu() {
        String wordType = (String) this.searchController.showMatchestWord.getSelectionModel().getSelectedItem();
        Word resultWord = this.handleManagement.findWord(wordType);
        if (resultWord == null) {
            this.searchController.setDisplaySearchResult("Could not find that word!", "text/html");
        } else {
            this.searchController.setDisplaySearchResult(resultWord.getHtmlType(), "text/html");
        }
    }

    public void startSearch() throws IOException {
        String wordType = searchBar.getText();
        //System.out.print(wordType);
        if (StateMachine.state != StateMachine.InitAndSeacrch) {
            this.loadSeacherController();
        }
        Word resultWord = this.handleManagement.findWord(wordType);
        if (resultWord == null) {
            this.searchedWord = null;
            //this.searchController.displaySearchResult.getEngine().loadContent("Could not find that word!", "text/html");
            this.searchController.setDisplaySearchResult("Could not find that word!", "text/html");
        } else {
            this.searchedWord = wordType;
            this.managementHistoryDatabase.addWord(resultWord.getWordType());
            //System.out.print(resultWord.getWordType());
            //this.searchController.displaySearchResult.getEngine().loadContent(resultWord.getHtmlType(), "text/html");
            this.searchController.setDisplaySearchResult(resultWord.getHtmlType(), "text/html");
        }
    }

    public void handleClickOnUkAudio() {

    }

    public void handleClickOnUsAudio() {
        String wordType = searchBar.getText();
        try {
            GoogleVoiceAPI.getInstance().playAudio(GoogleVoiceAPI.getInstance().getAudio(wordType,
                    "en-US"));
        } catch (IOException | JavaLayerException e) {
            System.err.println("Failed to play Audio from Google, fallback to FreeTTS");
            TextToSpeech.speak(wordType);
        }
    }

    public void handleClickOnSave() {
        System.out.printf("Value of searched word: %s\n",this.searchedWord);
        if (this.searchedWord == null) {
            return;
        }
        String favoriteWord = this.searchedWord;
        if (this.managementFavorite.isExist(favoriteWord)) {
            handleNotification("Error", "Word existed");
        } else {
            this.managementFavorite.addWord(favoriteWord);
            handleSuccessNotification();
        }
    }

    public void handleClickOnDelete() {

    }

    public void loadGoogleTranslate() {
        StateMachine.setGoogleTranslate();
        this.ClearStatusButton();
        this.googleTranslateTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        try {
            newBorderPane = FXMLLoader.load(HelloApplication.class.getResource("googletranslate.fxml"));
        } catch (IOException exception) {
            throw new RuntimeException();
        }

        this.ChangeMainBorderPane(newBorderPane);
    }

    public void loadFavorites() {
        this.isHistoryRequest = 1;
        this.HideMenu();
        this.historyList.getItems().clear();
        String[] result = new String[100];
        List<Word> temptResult = this.managementFavorite.showHistory();
        for (int i = 0; i < temptResult.size(); i++) {
            result[i] = temptResult.get((temptResult.size()) - 1 - i).getWordType();
        }
        this.historyList.getItems().addAll(result);
    }

    public void chooseFromHistoryList() {
        String wordType = (String) this.historyList.getSelectionModel().getSelectedItem();
        Word resultWord = this.handleManagement.findWord(wordType);
        if (StateMachine.state != StateMachine.InitAndSeacrch) {
            this.loadSeacherController();
        }
        if (resultWord == null) {
            this.searchController.setDisplaySearchResult("Could not find that word!", "text/html");
        } else {
            this.managementHistoryDatabase.addWord(resultWord.getWordType());
            this.searchController.setDisplaySearchResult(resultWord.getHtmlType(), "text/html");
        }
    }

    public static void handleSuccessNotification() {
        Notifications notificationBuilder = Notifications.create()
                .title("Successfully Update")
                .text("Complete Add")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showConfirm();
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

    public void loadHistory() {
        this.isHistoryRequest = 1;
        this.HideMenu();
        this.historyList.getItems().clear();
        String[] result = new String[100];
        List<Word> temptResult = this.managementHistoryDatabase.showHistory();
        for (int i = 0; i < temptResult.size(); i++) {
            result[i] = temptResult.get((temptResult.size()) - 1 - i).getWordType();
        }
        this.historyList.getItems().addAll(result);
    }

    public void loadAddToDict() {
        StateMachine.setAddNewWord();
        this.ClearStatusButton();
        this.addToDictTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("AddController.fxml"));
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.addController = loader.getController();
        this.addController.setMangementDatabase(this.handleManagement);
        this.ChangeMainBorderPane(newBorderPane);
    }

    public void findMatchestWordFromHistory() {
        String wordType = this.searchHistory.getText();
        this.managementHistoryDatabase.findMatchestWord(wordType);
        List<Word> result = this.managementHistoryDatabase.getSearchResultList();
        String[] finalResult = new String[100];
        for (int i = 0; i < result.size() && i < 100; i++) {
            finalResult[i] = result.get(i).getWordType();
        }
        this.historyList.getItems().clear();
        this.historyList.getItems().addAll(finalResult);
    }

    public void loadChooseGame() {
        StateMachine.setChooseGame();
        this.ClearStatusButton();
        this.gameTab.getStyleClass().add("active");
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ChooseGame.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        ChooseGameController chooseGameController = loader.getController();
        chooseGameController.setGeneralAppController(this);
        this.ChangeMainBorderPane(newBorderPane);
    }

    public void loadGuessWordGame(){
        StateMachine.setGame();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("GuessWordGame.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        GuessWordGameController guessWordGameController = loader.getController();
        guessWordGameController.setGeneralAppController(this);
        this.ChangeMainBorderPane(newBorderPane);
    }

    public void loadPlayAgain(int score){
        StateMachine.setPlayAgain();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("PlayAgain.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        PlayAgainController playAgainController = loader.getController();
        playAgainController.setScore(score);
        this.ChangeMainBorderPane(newBorderPane);

    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}