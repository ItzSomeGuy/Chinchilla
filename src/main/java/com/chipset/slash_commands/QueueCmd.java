package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCmd extends SlashCommand {
    private long timeInMillis;

    public QueueCmd() {
        this.name = "queue";
        this.help = "Shows current song queue";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        TextChannel channel = event.getTextChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            event.reply("Queue is currently empty").setEphemeral(true).queue();
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final MessageAction messageAction = channel.sendMessage("**Current Queue**\n");  //TODO: convert to StringBuilder

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i +1 ))
                    .append(" `")
                    .append(info.title)
                    .append(" by ")
                    .append(info.author)
                    .append("' ['")
                    .append(formatTime(track.getDuration()))
                    .append("`] \n");
        }
        if (trackList.size() > trackCount){
            messageAction.append("And `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` more . . .");
        }
        messageAction.queue();
    }
    private String formatTime(long duration) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

        //TODO: add reply
    }
}