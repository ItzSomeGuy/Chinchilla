package com.chipset.speech;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import java.io.File;
import java.io.IOException;

public class Speech {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        // set path to acoustic/language model
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            // set path to audio file
            File audioFile = new File(Speech.class.getClassLoader().getResource("AA.wav").getFile());

            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

            // start recognition process / prune prev cached data
            recognizer.startRecognition(true);

            System.out.println("transcribing...");

            while (true) {
                // retrieve the recognized speech
                String result = recognizer.getResult().getHypothesis();
                if (result != null) {
                    System.out.println("Recognized: " + result);
                    // have Bob do something with the text
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
