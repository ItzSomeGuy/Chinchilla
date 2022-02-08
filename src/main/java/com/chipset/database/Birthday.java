package com.chipset.database;

import com.chipset.database.SQLiteDataSource;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Birthday extends SlashCommand {
    public Birthday() {
        this.name = "birthday";
        this.help = "sets a user's birthday";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "day", "the day of the month").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "month", "the month of the year").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "year", "the year they were born").setRequired(true));
        options.add(new OptionData(OptionType.USER, "target", "the person whom you want to set the birthday of"));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        String birthday;
        Member member = null;

        Guild guild = event.getGuild();
        // if member is null, set member to the author of the message
        if (event.getOption("target") == null) {
            member = event.getMember();
        }

        String day = Objects.requireNonNull(event.getOption("day")).getAsString();
        // if day is 1-9, add a 0 to the front
        if (day.length() == 1) {
            day = "0" + day;
        } else if (day.length() > 2) {
            event.reply("Invalid day").setEphemeral(true).queue();
        }

        String month = Objects.requireNonNull(event.getOption("month")).getAsString();
        // if month is 1-9, add a 0 to the front
        if (month.length() == 1) {
            month = "0" + month;
        } else if (month.length() > 2) {
            event.reply("Invalid month").setEphemeral(true).queue();
        }

        String year = Objects.requireNonNull(event.getOption("year")).getAsString();
        // if year is two digits, add a 19 to the front
        if (year.length() == 2) {
            year = "19" + year;
        } else if (year.length() > 4) {
            event.reply("Invalid year").setEphemeral(true).queue();
        }

        birthday = day + "/" + month + "/" + year;

        assert guild != null; assert member != null;
        SQLiteDataSource.setBirthday(guild, member, birthday);

        event.reply("set birthday to " + birthday).setEphemeral(true).queue();
    }
}
