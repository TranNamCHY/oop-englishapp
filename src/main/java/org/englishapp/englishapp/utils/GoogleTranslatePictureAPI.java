package org.englishapp.englishapp.utils;

import com.asprise.ocr.Ocr;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class GoogleTranslatePictureAPI {

    public static void main(String[] args) throws IOException, TimeoutException {
        //File file = new File("file_to_pic");
        //processImage(file);
    }

    private static void processImage(File file) throws IOException, TimeoutException {
        try {
            URL imageUrl = file.toURI().toURL();
            recognizeText(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void recognizeText(URL imageUrl) throws IOException, TimeoutException {
        Ocr.setUp();
        Ocr ocr = new Ocr();
        ocr.startEngine("eng", Ocr.SPEED_FAST);
        String result = ocr.recognize(new URL[]{imageUrl}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        ocr.stopEngine();
        GoogleTranslateAPI rs = new GoogleTranslateAPI();
        System.out.println(rs.translate(result, "en", "vi"));
    }
}


