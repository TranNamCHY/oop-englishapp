package org.englishapp.englishapp.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class GoogleTranslateAPI {
    private static final String GOOGLE_TRANS_URL = "http://translate.google.com/translate_a/t?";
    public static final String ENGHLISH = "en";
    public static final String VIETNAMESE = "vi";
    public static final String AUTO = "auto";

    private static String speechAfterTranslated = "";
    private static ExecutorService executorService;

    public static String generateURL(String sourceLanguage, String destLanguage, String textSource) {
        textSource = textSource.replace("\n", " ");
        return GOOGLE_TRANS_URL +
                "client=gtrans" +
                "&sl=" + sourceLanguage +
                "&tl=" + destLanguage +
                "&hl=" + destLanguage +
                "&q=" + URLEncoder.encode(textSource, StandardCharsets.UTF_8);
    }

    public static String translate(String textSource, String sourceLanguage, String destLanguage) throws IOException, TimeoutException {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        String status = "";
        String urlGoogleTranslate = GoogleTranslateAPI.generateURL(sourceLanguage, destLanguage, textSource);
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws IOException {
                speechAfterTranslated = "";
                URL url = new URL(urlGoogleTranslate);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                //con.setRequestProperty("User-Agent", userAgent);
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

}
