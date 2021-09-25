package com.chipset.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class RenameSlash extends ListenerAdapter {
    public void onSlashCommand(SlashCommandEvent event) {
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
