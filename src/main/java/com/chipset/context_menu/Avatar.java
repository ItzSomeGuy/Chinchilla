package com.chipset.context_menu;

import com.jagrosh.jdautilities.command.UserContextMenu;
import com.jagrosh.jdautilities.command.UserContextMenuEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Avatar extends UserContextMenu {
    public Avatar() {
        this.name = "Avatar";
    }

    @Override
    protected void execute(@NotNull UserContextMenuEvent event) {
        String url = Objects.requireNonNull(event.getTargetMember()).getEffectiveAvatarUrl();

        event.reply(url).setEphemeral(true).queue();
    }
}
