package com.chipset.context_menu;

import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

public class Reminder extends MessageContextMenu {
    public Reminder() { this.name = "Remind me"; }

    @Override
    protected void execute(@NotNull MessageContextMenuEvent event) {
        Message msg = event.getTarget();
        String id = msg.getId();

        StringSelectMenu menu = StringSelectMenu.create("reminder-"+id)
                .setPlaceholder("When should I remind you?")
                .setRequiredRange(1, 1)
                .addOption("5 seconds", "5 seconds")
                .addOption("1 hour", "1 hour")
                .addOption("3 hours", "3 hours")
                .addOption("12 hours", "12 hours")
                .addOption("1 day", "1 day")
                .build();

        event.reply("")
                .addActionRow(menu)
                .setEphemeral(true)
                .queue();
    }
}
