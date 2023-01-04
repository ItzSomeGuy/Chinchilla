package com.chipset.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;

    public final trackScheduler scheduler;

    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new trackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        this.audioPlayer.setVolume(20);
    }

    public AudioPlayer audioPlayer(){
        return audioPlayer;
    }


    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public void setVolume(int volume) { audioPlayer.setVolume(volume); }

    public boolean isPlaying() { return audioPlayer.getPlayingTrack() != null; }
}
