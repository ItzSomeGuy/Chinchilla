package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rename extends SlashCommand {
    public Rename() {
        this.name = "rename"; // must be lowercase
        this.help = "renames a user";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "target", "the person whom you want to rename"));
        options.add(new OptionData(OptionType.STRING, "new_nickname", "the cool nickname you thought of"));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member target;
        String newNickname;

        if (event.getName().equals("rename")) {
            try { target = Objects.requireNonNull(event.getOption("target")).getAsMember(); }
            catch (NullPointerException e) { target = event.getMember(); }

            try { newNickname = Objects.requireNonNull(event.getOption("new_nickname")).getAsString(); }
            catch (NullPointerException e) { assert target != null; newNickname = target.getUser().getName(); }

            assert target != null;
            target.modifyNickname(newNickname).queue();

            event.reply("renamed " + target.getEffectiveName() + " to " + newNickname).setEphemeral(true).queue();
        }
    }
}
