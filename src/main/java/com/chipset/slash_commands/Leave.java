package com.chipset.slash_commands;

import com.chipset.Lavaplayer.GuildMusicManager;
import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;


import java.util.Objects;

public class Leave extends SlashCommand {
    public Leave() {
        this.name = "leave";
        this.help = "Allows bot to leave voice channel that it is currently connected to";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member bot = event.getGuild().getMemberById(847520350291492974L);
        VoiceChannel vc =  bot.getVoiceState().getChannel().asVoiceChannel();

        AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();

        if (vc == null) {
            event.reply("Please join a voice channel before running this command").setEphemeral(true).queue();
        } else {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();
        }

        event.reply("I'm out, bitch.").setEphemeral(true).queue();
        manager.closeAudioConnection();
    }

}
