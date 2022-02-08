package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.Objects;

public class Kick extends SlashCommand {
    public Kick() {
        this.name = "kick";
        this.help = "Kicks a user from the server";
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "target", "user you want to kick")
                .setRequired(true));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Member target = Objects.requireNonNull(event.getOption("target")).getAsMember();
        Guild guild = event.getGuild();

        assert guild != null; assert target != null;
        guild.kick(target).queue();

        event.reply("Kicked " + target.getEffectiveName()).setEphemeral(true).queue();
    }
}
