package com.chipset.database;

import com.chipset.database.SQLiteDataSource;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;
import java.util.Objects;

public class UpdateDB extends SlashCommand {
    public UpdateDB() {
        this.name = "updatedb";
        this.help = "Updates the database with the latest data";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        List<Member> members = Objects.requireNonNull(event.getGuild()).getMembers();
        Guild guild = event.getGuild();

        for (Member member : members) {
            if (member.getUser().isBot()) continue;

            SQLiteDataSource.updateMember(guild, member);
        }
        event.reply("DB updated!").setEphemeral(true).queue();
    }
}
