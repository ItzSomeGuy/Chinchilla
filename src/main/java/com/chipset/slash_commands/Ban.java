package com.chipset.slash_commands;

import com.chipset.main.Bot;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ban extends SlashCommand {

    private Member target;
    private String reason;

    public Ban() {
        this.name = "ban";
        this.help = "ban someone";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "target", "the person you want to ban").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "reason", "why they probably deserve this"));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        try {
            reason = Objects.requireNonNull(event.getOption("event")).getAsString();
        } catch (NullPointerException ignored) {}

        target = Objects.requireNonNull(event.getOption("target")).getAsMember();
        TextChannel tc = event.getTextChannel();
        long id = Objects.requireNonNull(event.getMember()).getIdLong();

        // confirmation
        event.reply("do they really deserve it?")
                .addActionRow(
                        Button.secondary("abort", "forgive them"),
                        Button.danger("ban", "ban them")
                )
                .setEphemeral(true)
                .queue(message -> Bot.getEventWaiter().waitForEvent(
                        ButtonClickEvent.class,
                        e -> {
                            // Ignore Bots... again
                            if(e.getUser().isBot())
                            { return false; }
                            // Make sure the user reacting is the same.
                            return Objects.requireNonNull(e.getMember()).getIdLong() == id;
                        },
                        e -> {
                            String selection = e.getComponentId();

                            if (selection.equals("ban")) {
                                if (reason != null) {
                                    target.ban(0, reason).queue();
                                } else {
                                    target.ban(0).queue();
                                }
                                e.editComponents().setActionRow(
                                        Button.danger("ban", "banned").asDisabled()
                                ).queue();
                            } else if (e.getComponentId().equals("abort")) {
                                e.editComponents().setActionRow(
                                        Button.secondary("abort", "forgiven").asDisabled()
                                ).queue();
                            }
                        }
                ));
    }
}
