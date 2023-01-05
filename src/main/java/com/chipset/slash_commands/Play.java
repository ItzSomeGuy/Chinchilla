package com.chipset.slash_commands;

import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Play extends SlashCommand {
    public Play() {
        this.name = "play";
        this.help = "Usage: /Play (Insert youtube link) | Plays audio from any youtube link";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link", "Link you want played in voice channel") .setRequired(true));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        //gets voice channel of user who ran the command
        VoiceChannel vc = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
        if (vc == null) {
            event.reply("Please join a voice channel before running this command").setEphemeral(true).queue();
        }

        Member self = Objects.requireNonNull(event.getGuild()).getSelfMember();
        VoiceChannel connectedChannel = Objects.requireNonNull(self.getVoiceState()).getChannel();

        if (connectedChannel == null) {
            AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
            manager.openAudioConnection(vc);
            // fuckYouAaron
        }

        OptionMapping option = event.getOption("link");
        assert option != null;
        String link  = option.getAsString();
        TextChannel channel =  event.getTextChannel();
        PlayerManager.getInstance().loadAndPlay(channel, link);

        event.reply("adding to queue:").queue();
    }
}