package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.chipset.Lavaplayer.trackScheduler;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Skip extends SlashCommand {
    public Skip() {
        this.name = "skip";
        this.help = "Skips currently playing song to move on to next in queue";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "song", "Skip to a specific point in the playlist").setRequired(false));
        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        OptionMapping option = event.getOption("song");
        int n = option.getAsInt();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.reply("There's nothing playing right now.").setEphemeral(true).queue();
        } else if (event.getOption("song") != null) {
            if(trackScheduler.skipTo(n)){
                for (int i = 2; i < n-1; i++) {
                }
                musicManager.scheduler.nextTrack();
                event.reply("Skipped to song " + n).queue();
            } else {
                event.reply("Could not skip to song " + n + ". The number must be between 1 and the size of the visible queue.").queue();
            }
        } if (event.getOption("song") == null) {
            musicManager.scheduler.nextTrack();
            event.reply("Skipped current song.").queue();
        }
    }
}