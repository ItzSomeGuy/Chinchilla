package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.Objects;

public class Skip extends SlashCommand {
    public Skip() {
        this.name = "skip";
        this.help = "Skips currently playing song to move on to next in queue";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.reply("There's nothing playing right now.").setEphemeral(true).queue();
        }
        else{
            musicManager.scheduler.nextTrack();
            event.reply("Skipped current song.").queue();
        }
    }
}