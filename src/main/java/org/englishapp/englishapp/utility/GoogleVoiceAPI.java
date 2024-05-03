package org.englishapp.englishapp.utility;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class GoogleVoiceAPI {
    private static final String GOOGLE_TRANS_AUDIO = "http://translate.google.com/translate_tts?";
    private static GoogleVoiceAPI voice;

    private static ExecutorService executorService;

    private GoogleVoiceAPI() {}

    public synchronized static GoogleVoiceAPI getInstance() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
        }
        if (voice == null) {
            voice = new GoogleVoiceAPI();
        }
        return voice;
    }

    public InputStream getAudio(String text, String languageOutput) throws IOException, NoRouteToHostException,UnknownHostException {
        String urlStr = generateSpeakURL(languageOutput, text);
        System.out.println(urlStr);
        URL newUrl = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) newUrl.openConnection();
        urlConn.setRequestMethod("GET");
        InputStream voiceSrc = urlConn.getInputStream();
        return new BufferedInputStream(voiceSrc);
    }

    public void playAudio(InputStream sound) throws JavaLayerException {
        Future<String> future = executorService.submit(new Callable<>() {
            @Override
            public String call() throws JavaLayerException {
                new Player(sound).play();
                return "completed";
            }
        });
    }

    private static String generateSpeakURL(String lang, String text) {
        return GOOGLE_TRANS_AUDIO + "?ie=UTF-8" +
                "&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&tl=" + lang +
                "&client=tw-ob";
    }

    public static void shutdownExecutorService() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
}