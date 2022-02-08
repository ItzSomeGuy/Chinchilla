package com.chipset.database;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Rank extends SlashCommand {
    public Rank() {
        this.name = "rank";
        this.help = "Sets the rank of the user.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "rank", "1-5").setRequired(true));
        options.add(new OptionData(OptionType.USER, "target", "The user to set the rank of."));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        int rank = Integer.parseInt(Objects.requireNonNull(event.getOption("rank")).getAsString());
        Member member = null;

        // if member is null, set member to the author of the message
        if (event.getOption("target") == null) {
            member = event.getMember();
        }

        assert guild != null; assert member != null;
        SQLiteDataSource.editRank(guild, member, rank);

        event.reply("Rank set to " + rank).setEphemeral(true).queue();
    }
}
