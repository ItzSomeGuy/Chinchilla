package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Punish extends SlashCommand {
    public Punish() {
        this.name = "punish";
        this.help = "punishes a user";

        this.options = Collections.singletonList(new OptionData(OptionType.USER, "target", "the soon to be damned").setRequired(true));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        String[] names = {"Literal Hell", "The Void",
                "The Shadow Realm ", "The Firing Squad",
                "Guantanamo Bay", "6 Feet Under",
                "The Gulag", "Back to Canada",
                "The Crucifixion Cross", "The Midnight Facility"};       //List of possible channel names

        Member target = Objects.requireNonNull(event.getOption("target")).getAsMember(); //Defines the target as whoever the user selects

        assert target != null; //target cannot be null
        if (Objects.requireNonNull(target.getVoiceState()).inVoiceChannel()) { //if statement checks if target is in voice channel
            Random nameArr = new Random();
            String randomName = names[nameArr.nextInt(names.length)];
            Guild guild = event.getGuild();
            assert guild != null;

            guild.createVoiceChannel(randomName).queue(response -> {
                guild.moveVoiceMember(target, response).queue();    //Moves target to punishment voice channel

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                event.reply(target.getAsMention()+" was punished").setEphemeral(true).queue();

                while (true) {
                    List<Member> listMembers = response.getMembers();
                    int memberAmount = listMembers.size();

                    if (memberAmount == 0) {
                        Objects.requireNonNull(target.getGuild().getVoiceChannelById(response.getIdLong())).delete().queue();     //Gets the target's current channel (which should be the punishment channel) and proceeds to delete that channel
                        break;
                    }
                }
            });
        }
    }

}
