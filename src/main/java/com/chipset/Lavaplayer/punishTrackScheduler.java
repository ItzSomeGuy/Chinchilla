package com.chipset.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class punishTrackScheduler {
    public final AudioPlayer punishPlayer;

    public final trackScheduler punishScheduler;

    private final AudioPlayerSendHandler sendHandler;

    public punishTrackScheduler(AudioPlayerManager punishManager){
        this.punishPlayer = punishManager.createPlayer();
        this.punishScheduler = new trackScheduler((this.punishPlayer));
        this.punishPlayer.addListener(this.punishScheduler);
        this. sendHandler = new AudioPlayerSendHandler(this.punishPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

}
