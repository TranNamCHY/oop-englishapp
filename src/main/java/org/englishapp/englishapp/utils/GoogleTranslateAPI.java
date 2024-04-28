package org.englishapp.englishapp.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class GoogleTranslateAPI {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final String userAgent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; "
            + "en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";
    private static final String GOOGLE_TRANS_URL = "http://translate.google.com/translate_a/t?";
    private static final Pattern WORD_PATTERN = Pattern.compile(
            "\\[\"(.*)\"]"
    );

    private static final Pattern AUTO_ENDING_PATTERN = Pattern.compile(
            "(.*)\",\".{2,5}$"
    );
    public static final String ENGHLISH = "en";
    public static final String VIETNAMESE = "vi";
    public static final String AUTO = "auto";

    private static String speechAfterTranslated = "";
    private static ExecutorService executorService;

    private static String generateNewToken() {
        byte[] ranBytes = new byte[24];
        secureRandom.nextBytes(ranBytes);
        return base64Encoder.encodeToString(ranBytes);
    }

    public static String generateURL(String sourceLanguage, String destLanguage, String textSource) {
        textSource = textSource.replace("\n", " ");
        return GOOGLE_TRANS_URL +
                "client=gtrans" +
                "&sl=" + sourceLanguage +
                "&tl=" + destLanguage +
                "&hl=" + destLanguage +
                //"&tk=" + generateNewToken() +
                "&q=" + URLEncoder.encode(textSource, StandardCharsets.UTF_8);
    }

    public static String translate(String textSource, String sourceLanguage, String destLanguage) throws IOException, TimeoutException {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        String status = "";
        String urlGoogleTranslate = GoogleTranslateAPI.generateURL(sourceLanguage, destLanguage, textSource);
        //System.out.println(urlGoogleTranslate);
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws IOException {
                speechAfterTranslated = "";
                URL url = new URL(urlGoogleTranslate);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", userAgent);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempt = "";
                while ((tempt = bufferedReader.readLine()) != null) {
                    speechAfterTranslated += tempt;
                }
                bufferedReader.close();
                return "completed";
            }
        });
        try {
            status = future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException exception) {
            throw new IOException(exception);
        } catch (TimeoutException exception) {
            throw new TimeoutException();
        }
        if (status == "completed") {
            StringBuilder tempt = new StringBuilder(speechAfterTranslated);
            tempt.delete(0, 2);
            tempt.delete(speechAfterTranslated.length() - 4, speechAfterTranslated.length() - 2);
            speechAfterTranslated = tempt.toString();
            if (sourceLanguage == AUTO) {
                tempt.delete(0, 1);
                tempt.delete(speechAfterTranslated.length() - 7, speechAfterTranslated.length() - 1);
            }
            return tempt.toString();
        }
        return null;
    }

    /* public static void handleNotification() {
        Notifications notificationBuilder = Notifications.create()
                .title("Translate failed !. Please check your internet connection")
                .text("Translate faield !")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT);
        notificationBuilder.showWarning();
    } */
}
