package com.chipset.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class trackScheduler extends AudioEventAdapter {
    public AudioPlayer player;
    public static BlockingQueue<AudioTrack> queue;

    public boolean repeating = false;

//Test to ensure making public audio player and blockingqueue<AudioTrack> did not break normal audio playback in music code
    public trackScheduler(AudioPlayer player) {
        this.player = player;
        queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            queue.offer(track);
        }
    }
    public static boolean skipTo(int n){
        List<AudioTrack> list = new ArrayList<>(queue);
        if(n >= 21){
            return false;
        }
        queue.clear();
        queue.addAll(list.subList(n-1, list.size()));
        queue.addAll(list.subList(0, n-1));
        return true;
    }

    public void nextTrack() {
        this.player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (this.repeating){
                this.player.startTrack(track.makeClone(), false);
            }

            nextTrack();
        }
    }
}