package com.chipset.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public AudioPlayer punishPlayer;
    public final trackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new trackScheduler(this.audioPlayer);
        this.punishPlayer = manager.createPlayer();
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

    public void setVolume(int volume) { this.audioPlayer.setVolume(volume); }

    public int getVolume() { return this.audioPlayer.getVolume(); }

    public boolean isPlaying() { return audioPlayer.getPlayingTrack() != null; }
}
