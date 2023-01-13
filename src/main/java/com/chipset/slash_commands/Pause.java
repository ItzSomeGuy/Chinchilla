package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.Objects;

public class Pause extends SlashCommand {
    public Pause() {
        this.name = "pause";
        this.help = "Pauses currently playing music";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        audioPlayer.setPaused(true);

        event.reply("Paused").queue();
    }
}
