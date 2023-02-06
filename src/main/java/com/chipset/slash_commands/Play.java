package com.chipset.slash_commands;

import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
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
        options.add(new OptionData(OptionType.STRING, "link", "Link you want played in voice channel").setRequired(true));
        options.add(new OptionData(OptionType.BOOLEAN, "shuffle", "Shuffle a playlist of songs into a random order").setRequired(false));
        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        // gets voice channel of user who ran the command


        VoiceChannel vc = event.getMember().getVoiceState().getChannel().asVoiceChannel();
        if (vc != null) {
            AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
            manager.openAudioConnection(vc);
            // Succ my ass

            OptionMapping option = event.getOption("link");
            OptionMapping option2 = event.getOption("shuffle");
            boolean shuffle;
            try {
                shuffle = Objects.requireNonNull(event.getOption("shuffle")).getAsBoolean();
            } catch (NullPointerException e) {
                shuffle = false;
            }

            if (!shuffle) {
                assert option != null;
                String link = option.getAsString();
                TextChannel channel = event.getTextChannel();
                PlayerManager.getInstance().loadAndPlay(channel, link, false);
                event.reply("adding to queue:").queue();
            }
            if (shuffle) {
                assert option != null;
                String link = option.getAsString();
                TextChannel channel = event.getTextChannel();
                PlayerManager.getInstance().loadAndPlay(channel, link, true);
                event.reply("adding to queue:").queue();

            }
        } else {
            event.reply("Please join a voice channel before running this command").setEphemeral(true).queue();
        }
    }
}
