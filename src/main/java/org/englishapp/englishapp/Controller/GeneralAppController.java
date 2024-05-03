package org.englishapp.englishapp.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.englishapp.englishapp.Management.ManagementFavorite;
import org.englishapp.englishapp.Management.ManagementHistoryDatabase;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import org.englishapp.englishapp.EnglishAppLauncher;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.englishapp.englishapp.CustomObject.Word;
import org.englishapp.englishapp.Management.ManagementDictionaryDatabase;

public class GeneralAppController implements Initializable, InterfaceController {

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
    private TextField searchBar;

    private TranslateTransition LeftPaneTransition;

    @FXML
    private ListView historyList;

    @FXML
    private VBox fatherLeftPaneTab;

    @FXML
    private TextField searchHistory;
    private ManagementDictionaryDatabase handleManagement;

    private ManagementHistoryDatabase managementHistoryDatabase;

    private ManagementFavorite managementFavorite;

    private int isHistoryRequest = 0;
    private SearchController searchController;

    private AddController addController;
    private GuessWordGameController guessWordGameController;

    private String searchedWord = "";

    public ManagementDictionaryDatabase getHandleManagement(){
        return this.handleManagement;
    }

    public ManagementFavorite getManagementFavorite(){
        return this.managementFavorite;
    }
    public TextField getSearchBar(){
        return this.searchBar;
    }
    @Override
    public void setState() {
        StateMachine.setInitAndSeacrch();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadSeacherController();
        this.isHistoryRequest = 0;
        this.setState();
        this.managementFavorite = new ManagementFavorite();
        this.managementHistoryDatabase = new ManagementHistoryDatabase();
        this.handleManagement = new ManagementDictionaryDatabase();
        this.LeftPaneTransition = new TranslateTransition(Duration.millis(300), LeftPaneTab);
        this.LeftPaneTransition.setFromX(0);
        this.LeftPaneTransition.setToX(-250);
    }

    public void ChangeController(BorderPane newMainBorderPane) {
        this.mainBorderPane.setTop(newMainBorderPane.getTop());
        this.mainBorderPane.setLeft(newMainBorderPane.getLeft());
        this.mainBorderPane.setCenter(newMainBorderPane.getCenter());
        this.mainBorderPane.setRight(newMainBorderPane.getRight());
        this.mainBorderPane.setBottom(newMainBorderPane.getBottom());
    }

    public int confirmBeforeQuitGame() {
        if (StateMachine.state != StateMachine.Game) {
            return 1;
        }
        return this.guessWordGameController.onExitGame();
    }

    public void ClearStatusButton() {
        searchTab.getStyleClass().removeAll("active");
        googleTranslateTab.getStyleClass().removeAll("active");
        favoritesTab.getStyleClass().removeAll("active");
        historyTab.getStyleClass().removeAll("active");
        addToDictTab.getStyleClass().removeAll("active");
        gameTab.getStyleClass().removeAll("active");
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
        this.searchController.getShowMatchestWord().getItems().clear();
        this.searchController.getShowMatchestWord().setVisible(true);
        String wordType = null;
        wordType = searchBar.getText();
        this.handleManagement.findMatchestWord(wordType);
        String[] result = new String[5000];
        for (int i = 0; i < this.handleManagement.getSearchResultList().size(); i++) {
            result[i] = this.handleManagement.getSearchResultList().get(i).getWordType();
        }
        this.searchController.getShowMatchestWord().getItems().addAll(result);
    }

    public void handleChosenFromMenu() {
        String wordType = (String) this.searchController.getShowMatchestWord().getSelectionModel().getSelectedItem();
        Word resultWord = this.handleManagement.findWord(wordType);
        if (resultWord == null) {
            this.searchController.setDisplaySearchResult("Could not find that word!", "text/html");
        } else {
            this.searchController.setDisplaySearchResult(resultWord.getHtmlType(), "text/html");
        }
    }

    public void startSearch() throws IOException {
        String wordType = searchBar.getText();
        if (StateMachine.state != StateMachine.InitAndSeacrch) {
            this.loadSeacherController();
        }
        Word resultWord = this.handleManagement.findWord(wordType);
        if (resultWord == null) {
            this.searchedWord = null;
            this.searchController.setDisplaySearchResult("Could not find that word!", "text/html");
        } else {
            this.searchedWord = wordType;
            this.managementHistoryDatabase.addWord(resultWord.getWordType());
            this.searchController.setDisplaySearchResult(resultWord.getHtmlType(), "text/html");
        }
    }

    public void handleClickOnSave() {
        if (this.searchedWord == null) {
            return;
        }
        String favoriteWord = this.searchedWord;
        if (this.managementFavorite.isExist(favoriteWord)) {
        } else {
            this.managementFavorite.addWord(favoriteWord);
            InterfaceController.handleSuccessNotification("Successfully Update", "Complete adding to your favorite list");
        }
    }

    public void chooseFromHistoryList() {
        String wordType = (String) this.historyList.getSelectionModel().getSelectedItem();
        Word resultWord = this.handleManagement.findWord(wordType);
        this.searchBar.setText(wordType);
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

    public void loadGoogleTranslate() {
        if (this.confirmBeforeQuitGame() == 0) {
            return;
        }
        StateMachine.setGoogleTranslate();
        this.ClearStatusButton();
        this.googleTranslateTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        try {
            newBorderPane = FXMLLoader.load(EnglishAppLauncher.class.getResource("googletranslate.fxml"));
        } catch (IOException exception) {
            throw new RuntimeException();
        }

        this.ChangeController(newBorderPane);
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
        if (this.confirmBeforeQuitGame() == 0) {
            return;
        }
        StateMachine.setAddNewWord();
        this.ClearStatusButton();
        this.addToDictTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("AddController.fxml"));
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.addController = loader.getController();
        this.addController.setMangementDatabase(this.handleManagement);
        this.ChangeController(newBorderPane);
    }

    public void loadChooseGame() {
        if (this.confirmBeforeQuitGame() == 0) {
            return;
        }
        StateMachine.setChooseGame();
        this.ClearStatusButton();
        this.gameTab.getStyleClass().add("active");
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("ChooseGame.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        ChooseGameController chooseGameController = loader.getController();
        chooseGameController.setGeneralAppController(this);
        this.ChangeController(newBorderPane);
    }

    public void loadChooseGameWithoutCofirm() {
        StateMachine.setChooseGame();
        this.ClearStatusButton();
        this.gameTab.getStyleClass().add("active");
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("ChooseGame.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        ChooseGameController chooseGameController = loader.getController();
        chooseGameController.setGeneralAppController(this);
        this.ChangeController(newBorderPane);
    }

    public void loadGuessWordGame() {
        StateMachine.setGame();
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("GuessWordGame.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.guessWordGameController = loader.getController();
        this.guessWordGameController.setGeneralAppController(this);
        this.ChangeController(newBorderPane);
    }

    public void loadPlayAgain(int score) {
        StateMachine.setPlayAgain();
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("PlayAgain.fxml"));
        BorderPane newBorderPane;
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        ShowScore showScore = loader.getController();
        showScore.setGeneralAppController(this);
        showScore.setScore(score);
        this.ChangeController(newBorderPane);

    }

    public void loadSeacherController() {
        if (this.confirmBeforeQuitGame() == 0) {
            return;
        }
        StateMachine.setInitAndSeacrch();
        this.ClearStatusButton();
        this.searchTab.getStyleClass().add("active");
        BorderPane newBorderPane;
        FXMLLoader loader = new FXMLLoader(EnglishAppLauncher.class.getResource("SearchController.fxml"));
        try {
            newBorderPane = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
        this.searchController = loader.getController();
        this.searchController.setGeneralAppController(this);
        this.ChangeController(newBorderPane);
    }

}