package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.*;

/**
 * IDEAS
 *
 * manage LFG events
 * manage LFG channels
 * manage LFG permissions
 */

public class LFG extends SlashCommand {
    public LFG() {
        this.name = "lfg";
        this.help = "looking for games";

        this.children = new SlashCommand[]{
                new Add(),
                new Remove()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class Add extends SlashCommand {
        public Add() {
            this.name = "add";
            this.help = "create a new LFG";

            // sub command /lfg add <name>
            this.options = Collections.singletonList(
                    new OptionData(OptionType.STRING, "name", "the name of the LFG")
                            .setRequired(true));
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            /*
             * create LFG channel
             * create LFG role
             * post LFG join message
             */
            OptionMapping target = event.getOption("name");
            assert target != null;
            String name = target.getAsString();

            TextChannel lfgBoard = Objects.requireNonNull(event.getGuild()).getTextChannelById(925797193019973712L);
            assert lfgBoard != null;

            User creator = event.getUser();

            Guild guild = event.getGuild();
            assert guild != null;

            guild.createRole().setName(name).queue(role -> { // create LFG role
                guild.createTextChannel(name, guild.getCategoryById(925291735670718494L))
                        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .queue(textChannel -> { // create LFG channel
                            lfgBoard.sendMessage("a new LFG has been created for " + name + " react with \uD83E\uDD19 to join").queue(message -> { // create LFG board message
                                message.addReaction("\uD83E\uDD19").queue();

                                textChannel.getManager().setTopic(message.getId()).queue(); // put board message id in lfg channel
                            });
                        });
            });

            event.reply("created "+name).setEphemeral(true).queue();
        }
    }

    private static class Remove extends SlashCommand {
        public Remove() {
            this.name = "remove";
            this.help = "removes a LFG";

            // sub command /lfg remove <name>
            this.options = Collections.singletonList(
                    new OptionData(OptionType.STRING, "name", "the name of the LFG")
                            .setRequired(true));
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            /*
             * delete LFG channel
             * delete LFG role
             * delete LFG join message
             */
            OptionMapping target = event.getOption("name");
            assert target != null;
            String name = target.getAsString();

            TextChannel lfgBoard = Objects.requireNonNull(event.getGuild()).getTextChannelById(925797193019973712L);
            assert lfgBoard != null;

            Guild guild = event.getGuild();
            assert guild != null;

            // get LFG role
            Role role = guild.getRolesByName(name, true).get(0);
            // get LFG channel
            TextChannel tc = guild.getTextChannelsByName(name, true).get(0);
            // get LFG board message
            long topic = Long.parseLong(Objects.requireNonNull(tc.getTopic()));

            lfgBoard.retrieveMessageById(topic).queue(message -> message.delete().queue());

            role.delete().queue();
            tc.delete().queue();
            tc.delete().queue();

            event.reply("removed "+name).setEphemeral(true).queue();
        }
    }
}
