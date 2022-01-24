package com.chipset.spade;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Spade extends SlashCommand {
    public Spade() {
        this.name = "spade";
        this.help = "Spade command";

        this.children = new SlashCommand[]{
                new Create(),
                new Delete(),
                new Add(),
                new Remove()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class Create extends SlashCommand {
        public Create() {
            this.name = "create";
            this.help = "Create a private hole";

            this.options = Collections.singletonList(
                    new OptionData(OptionType.USER, "user", "the person you want to invite")
                            .setRequired(true));
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            Member[] members = new Member[2];
            members[0] = event.getMember();
            members[1] = Objects.requireNonNull(event.getOption("user")).getAsMember();

            Guild guild = event.getGuild(); assert guild != null;

            // create a private hole
            guild.createCategory(event.getUser().getName()).queue(category -> {
                // set permissions
                category.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                category.createPermissionOverride(members[0]).setAllow(Permission.ALL_PERMISSIONS).queue();
                category.createPermissionOverride(members[1]).setAllow(Permission.VIEW_CHANNEL).queue();

                // create a text channel
                guild.createTextChannel(event.getUser().getName()+"'s Rock", category).queue();

                // create a voice channel
                guild.createVoiceChannel(event.getUser().getName()+"'s Hole", category).queue(voiceChannel
                        -> event.reply(voiceChannel.getAsMention()).setEphemeral(true).queue());

                ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

                Runnable taskDelete = () -> {
                    VoiceChannel vc = category.getVoiceChannels().get(0);

                    if (vc.getMembers().isEmpty()) {
                        List<GuildChannel> channels = category.getChannels();

                        // delete channels
                        for (GuildChannel channel : channels) {
                            channel.delete().queue();
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // delete category
                        category.delete().queue();

                        // clean up executor
                        ses.shutdown();
                    }
                };

                ses.scheduleAtFixedRate(taskDelete, 15, 15, TimeUnit.MINUTES);
            });
        }
    }

    private static class Delete extends SlashCommand {
        public Delete() {
            this.name = "delete";
            this.help = "delete a private hole";
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            Guild guild = event.getGuild(); assert guild != null;
            net.dv8tion.jda.api.entities.Category category = guild.getCategoriesByName(event.getUser().getName(), true).get(0);
            assert category != null;

            List<GuildChannel> channels = category.getChannels();

            // delete channels
            for (GuildChannel channel : channels) {
                channel.delete().queue();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // delete category
            category.delete().queue();

            event.reply("Deleted").setEphemeral(true).queue();
        }
    }

    private static class Add extends SlashCommand {
        public Add() {
            this.name = "add";
            this.help = "add a user to a private hole";

            this.options = Collections.singletonList(
                    new OptionData(OptionType.USER, "user", "the person you want to add")
                            .setRequired(true));
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            Guild guild = event.getGuild(); assert guild != null;
            net.dv8tion.jda.api.entities.Category category = guild.getCategoriesByName(event.getUser().getName(), true).get(0);
            assert category != null;

            Member member = Objects.requireNonNull(event.getOption("user")).getAsMember(); assert member != null;

            // add permissions
            category.createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queue();

            List<GuildChannel> channels = category.getChannels();

            for (GuildChannel channel : channels) {
                channel.createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queue();
            }

            event.reply("Added "+member.getEffectiveName()).setEphemeral(true).queue();
        }
    }

    private static class Remove extends SlashCommand {
        public Remove() {
            this.name = "remove";
            this.help = "remove a user to a private hole";

            this.options = Collections.singletonList(
                    new OptionData(OptionType.USER, "user", "the person you want to remove")
                            .setRequired(true));
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            Guild guild = event.getGuild(); assert guild != null;
            net.dv8tion.jda.api.entities.Category category = guild.getCategoriesByName(event.getUser().getName(), true).get(0);
            assert category != null;

            Member member = Objects.requireNonNull(event.getOption("user")).getAsMember(); assert member != null;

            // remove permissions
            Objects.requireNonNull(category.getPermissionOverride(member)).delete().queueAfter(2L, TimeUnit.SECONDS);

            List<GuildChannel> channels = category.getChannels();

            for (GuildChannel channel : channels) {
                Objects.requireNonNull(channel.getPermissionOverride(member)).delete().queueAfter(2L, TimeUnit.SECONDS);
            }

            event.reply("Removed "+member.getEffectiveName()).setEphemeral(true).queue();
        }
    }
}
