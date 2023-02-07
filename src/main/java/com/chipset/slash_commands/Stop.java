package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.Objects;

public class Stop extends SlashCommand {
        public Stop() {
            this.name = "stop";
            this.help = "Stops all music playing";
        }

    @Override
    protected void execute(SlashCommandEvent event) {
        VoiceChannel vc = event.getMember().getVoiceState().getChannel().asVoiceChannel();

        //check to see if bot is in voice channel goes here

        if (vc == null) {
            event.reply("Please join a voice channel before running this command").setEphemeral(true).queue();
        } else {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();
        }
        event.reply("Stopping").setEphemeral(true).queue();
    }
}
