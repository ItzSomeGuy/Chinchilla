package com.chipset.dumbo;

import net.dv8tion.jda.api.audio.UserAudio;

import java.io.ByteArrayOutputStream;

public class AudioReceiveHandler implements net.dv8tion.jda.api.audio.AudioReceiveHandler {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public boolean canReceiveUser() {
        return true; // Enable receiving audio from individual users
    }

    public void onUserAudio(UserAudio userAudio) {
        byte[] audioData = userAudio.getAudioData(1.0); // Adjust the volume if needed
        outputStream.write(audioData, 0, audioData.length);
    }

    public byte[] getRecordedAudio() {
        return outputStream.toByteArray();
    }
}
