package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.Objects;

public class Resume extends SlashCommand {
    public Resume() {
        this.name = "resume";
        this.help = "Resumes currently paused song to allow it to continue playing";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        audioPlayer.setPaused(false);

        event.reply("Resumed").queue();
    }
}