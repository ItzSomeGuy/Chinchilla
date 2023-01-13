package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCmd extends SlashCommand {

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
        final StringBuilder sb = new StringBuilder("__**Current Queue**__\n");

        final AudioTrack currentTrack = musicManager.audioPlayer.getPlayingTrack();
        if (currentTrack != null) {
            final AudioTrackInfo info = currentTrack.getInfo();
            final long duration = currentTrack.getDuration();
            final String formattedTime = formatTime(duration);

            sb.append("\n")
                    .append("**__Now Playing:__**\n ")
                    .append(" `")
                    .append(info.title)
                    .append(" by ")
                    .append(info.author)
                    .append("' ['")
                    .append(formattedTime)
                    .append("`] \n")
                    .append("\n")
                    .append("**__Coming Up:__** \n");

        }

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();
            final long duration = track.getDuration();
            final String formattedTime = formatTime(duration);

                  sb.append('#')
                    .append((i + 1))
                    .append(" `")
                    .append(info.title)
                    .append(" by ")
                    .append(info.author)
                    .append("' ['")
                    .append(formattedTime)
                    .append("`] \n");
        }
        if (trackList.size() > trackCount) {
            sb.append("And `")
                    .append((trackList.size() - trackCount))
                    .append("` more . . .");
        }
        event.reply(sb.toString()).queue();
    }

    private String formatTime(long duration) {
        final long hours = duration / TimeUnit.HOURS.toMillis(1);
        final long minutes = duration / TimeUnit.MINUTES.toMillis(1);
        final long seconds = duration % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}