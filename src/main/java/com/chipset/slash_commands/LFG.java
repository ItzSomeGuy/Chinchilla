package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * IDEAS
 *
 * manage LFG events
 * manage LFG channels
 * manage LFG permissions
 *
 */

public class LFG extends SlashCommand {
    public LFG() {
        this.name = "lfg";
        this.help = "looking for games";

        this.children = new SlashCommand[]{
                new SubLFGBirth(),
                new SubLFGAbort()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class SubLFGBirth extends SlashCommand {
        public SubLFGBirth() {
            this.name = "birth";
            this.help = "you get your partner preggo with your new LFG. Sadly your partner was lost in the process...";
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            /*
             * create modal
             * use data from modal to create LFG post
             * add parent to post
             */

            TextInput game = TextInput.create("game", "Game", TextInputStyle.SHORT)
                    .setPlaceholder("name of the game")
                    .setRequiredRange(1, 30)
                    .build();
            TextInput desc = TextInput.create("desc", "Description", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("no one is going to want to play it, but you might as well try to sell them on it")
                    .setRequiredRange(1, 2000)
                    .build();
            Modal modal = Modal.create("lfg_create", "New LFG")
                    .addActionRows(ActionRow.of(game), ActionRow.of(desc))
                    .build();
            event.replyModal(modal).queue();
        }
    }
    private static class SubLFGAbort extends SlashCommand {
        public SubLFGAbort() {
            this.name = "abort";
            this.help = "you better grab your coat hanger..";
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            EntitySelectMenu menu = EntitySelectMenu.create("lfg_abort", EntitySelectMenu.SelectTarget.CHANNEL)
                    .setPlaceholder("who gets the coat hanger tonight?")
                    .setChannelTypes(ChannelType.GUILD_PUBLIC_THREAD)
                    .setRequiredRange(1, 1)
                    .build();

            event.reply("What LFG would you like to abort?")
                    .addActionRow(menu)
                    .setEphemeral(true)
                    .queue();
        }
    }
}
