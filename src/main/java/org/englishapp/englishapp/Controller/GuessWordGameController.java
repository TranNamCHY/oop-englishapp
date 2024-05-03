package org.englishapp.englishapp.Controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.englishapp.englishapp.utility.MyTimerTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GuessWordGameController implements Initializable, InterfaceController {

    @FXML
    Button buttonA;
    @FXML
    Button buttonB;
    @FXML
    Button buttonC;
    @FXML
    Button buttonD;
    @FXML
    ImageView showPicture;

    @FXML
    Label scoreCountLabel;

    @FXML
    Label correctQuestionCount;
    private List<String> wordList;
    private List<String> imageList;

    private List<String> temptWordList;

    public TextField timeClock;

    public int buttonNoStatus;
    public int buttonYesStatus;

    private int indexCorrectQuestion = 0;

    private GeneralAppController generalAppController;
    private int score = 0;
    public int timeCount;
    private int numberOfQuestion;
    private Timer timer;
    private int correctQuestion = 0;
    public static final String PATH_GUESS_GAME_TXT = "src//main//resources//org//englishapp//englishapp//Database//GuessWordGameDatabase";

    public static final String PATH_GUESS_GAME_IMAGE = "src//main//resources//org//englishapp//englishapp//icons//GuessWordGameImage//GuessGameImage";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setState();
        generalAppController = null;
        numberOfQuestion = 0;
        timeCount = 0;
        this.timer = null;
        this.indexCorrectQuestion = 0;
        this.score = 0;
        this.correctQuestion = 0;
        this.wordList = new ArrayList<>();
        this.imageList = new ArrayList<>();
        this.temptWordList = new ArrayList<>();
        try {
            this.loadWordList();
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
        for (String word : wordList) {
            this.temptWordList.add(word);
        }
        try {
            this.loadImageList();
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
        this.loadNextQuestion();
    }

    @Override
    public void setState() {
        StateMachine.setGame();
    }

    public void setGeneralAppController(GeneralAppController generalAppController) {
        this.generalAppController = generalAppController;
    }

    public void setCLock(int time) {
        this.timeClock.setText(String.format("%d", time));
    }

    public void handeSystickInterrupt() {
        if (this.timeCount < 10) {
            this.setCLock(this.timeCount);
            this.timeCount++;
        } else {
            this.loadNextQuestion();
        }
    }

    public static void loadFileToList(String filePath, List<String> list) throws IOException {
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
        } catch (IOException e) {
            System.err.println(e.toString());
            throw e;
        }

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            list.add(line);
        }

        bufferedReader.close();
        fileReader.close();
    }

    public void createTimer() {
        if (this.timer == null) {
            MyTimerTask myTimerTask = new MyTimerTask(this);
            this.timer = new Timer("Timer");
            this.timer.schedule(myTimerTask, 0, 1000);
        }
    }

    public void continueTimer() {
        this.timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask(this);
        this.timer.schedule(myTimerTask, 0, 1000);
    }

    private void loadNextQuestion() {
        //this.resetButtonStatus();
        if (this.numberOfQuestion > 10) {
            this.generalAppController.loadPlayAgain(this.score);
            if (this.timer != null) {
                this.timer.cancel();
            }
            return;
        }
        this.createTimer();
        this.numberOfQuestion++;
        this.setCLock(0);
        this.timeCount = 0;

        int initialSize = this.wordList.size();
        int currentSize = this.temptWordList.size();
        Random random = new Random();
        int[] arrayABCD = {-1, -1, -1, -1};
        int tempt = 0;
        int rightIdex = -1;
        rightIdex = random.nextInt(4);
        int randomIdexInDatabase = random.nextInt(currentSize);
        String rightAnswer = this.temptWordList.get(randomIdexInDatabase);
        String rightAnswerImagePath = this.imageList.get(randomIdexInDatabase);
        this.temptWordList.remove(randomIdexInDatabase);
        this.imageList.remove(randomIdexInDatabase);
        arrayABCD[rightIdex] = randomIdexInDatabase;
        int flag = 0;
        for (int i = 0; i < 4; i++) {
            if (i != rightIdex) {
                while (true) {
                    flag = 0;
                    tempt = random.nextInt(initialSize);
                    for (int j = 0; j < 4; j++) {
                        if (tempt == arrayABCD[j]) {
                            break;
                        } else if (tempt != arrayABCD[j] && j == 3) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        arrayABCD[i] = tempt;
                        break;
                    }
                }
            }
        }
        Image image = new Image("file:" + rightAnswerImagePath);
        this.showPicture.setImage(image);
        String[] result = new String[4];
        for (int i = 0; i < 4; i++) {
            if (i == rightIdex) {
                result[i] = rightAnswer;
            } else {
                result[i] = this.wordList.get(arrayABCD[i]);
            }
        }
        this.buttonA.setText(result[0]);
        this.buttonB.setText(result[1]);
        this.buttonC.setText(result[2]);
        this.buttonD.setText(result[3]);
        this.indexCorrectQuestion = rightIdex;
    }

    public void clearButtonCss(Button button){
        button.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
    }

    public void resetButtonStatus(){
        this.buttonA.getStyleClass().add("tab-button");
        this.buttonB.getStyleClass().add("tab-button");
        this.buttonC.getStyleClass().add("tab-button");
        this.buttonD.getStyleClass().add("tab-button");
    }
    public void chooseA() {
        if (this.indexCorrectQuestion == 0) {
            Platform.runLater(() -> {
                this.handleCorrectAnswer();
                this.buttonA.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonA.getStyleClass().add("correct-button");
            });
        } else {
            Platform.runLater(() -> {
                this.buttonA.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonA.getStyleClass().add("incorrect-button");
            });
        }
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(even->{
            this.buttonA.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
            this.buttonA.getStyleClass().add("tab-button");
            this.loadNextQuestion();
        });
        pauseTransition.play();
    }

    public void chooseB() {
        if (this.indexCorrectQuestion == 1) {
            Platform.runLater(() -> {
                this.handleCorrectAnswer();
                this.buttonB.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonB.getStyleClass().add("correct-button");
            });
        } else {
            Platform.runLater(() -> {
                this.buttonB.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonB.getStyleClass().add("incorrect-button");
            });
        }
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(even->{
            this.buttonB.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
            this.buttonB.getStyleClass().add("tab-button");
            this.loadNextQuestion();
        });
        pauseTransition.play();
    }

    public void chooseC() {
        if (this.indexCorrectQuestion == 2) {
            Platform.runLater(() -> {
                this.handleCorrectAnswer();
                this.buttonC.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonC.getStyleClass().add("correct-button");
            });
        } else {
            Platform.runLater(() -> {
                this.buttonC.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonC.getStyleClass().add("incorrect-button");
            });
        }
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(even->{
            this.buttonC.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
            this.buttonC.getStyleClass().add("tab-button");
            this.loadNextQuestion();
        });
        pauseTransition.play();
    }

    public void chooseD() {
        if (this.indexCorrectQuestion == 3) {
            Platform.runLater(() -> {
                this.handleCorrectAnswer();
                this.buttonD.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonD.getStyleClass().add("correct-button");
            });
        } else {
            Platform.runLater(() -> {
                this.buttonD.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
                this.buttonD.getStyleClass().add("incorrect-button");
            });
        }
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(even->{
            this.buttonD.getStyleClass().removeAll("tab-button", "incorrect-button", "correct-button");
            this.buttonD.getStyleClass().add("tab-button");
            this.loadNextQuestion();
        });
        pauseTransition.play();
    }

    public void handleCorrectAnswer() {
        this.correctQuestion += 1;
        this.score += 10;
        this.scoreCountLabel.setText(String.format("%d", score));
        this.correctQuestionCount.setText(String.format("%d", correctQuestion));
    }

    public void handleInCorrectAnswer() {

    }

    private void loadWordList() throws IOException {
        loadFileToList(PATH_GUESS_GAME_TXT, wordList);
    }

    private void loadImageList() throws IOException {
        for (String word : wordList) {
            String imagePath = PATH_GUESS_GAME_IMAGE + "/" + word + ".jpg";
            imageList.add(imagePath);
        }
    }

    public int onExitGame() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.displayAlert("Confirm", "Are you want to quit ?");
        if (this.buttonYesStatus == 1) {
            this.generalAppController.loadChooseGameWithoutCofirm();
            return 1;
        } else {
            this.continueTimer();
            return 0;
        }
    }

    public int onExitGameButtonClick() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.displayAlert("Confirm", "Are you want to quit ?");
        if (this.buttonYesStatus == 1) {
            this.generalAppController.loadChooseGameWithoutCofirm();
            return 1;
        } else {
            this.continueTimer();
            return 0;
        }
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
}
