package com.chipset.spade;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Spade extends SlashCommand {
    public Spade() {
        this.name = "spade";
        this.help = "When hanging with all the homies just ain't it";

        this.children = new SlashCommand[] {
                new Delete(),
                new Add()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}
    private static class Delete extends SlashCommand {
        public Delete() {
            this.name = "close";
            this.help = "closes a private channel";
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            Guild guild = event.getGuild(); assert guild != null;
            VoiceChannel vc = guild.getVoiceChannelsByName(event.getUser().getEffectiveName()+"'s Channel", true).get(0);
            assert category != null;

            vc.delete().queue();

            event.reply("Deleted").setEphemeral(true).queue();
        }
    }

    private static class Add extends SlashCommand {
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        public Add() {
            this.name = "add";
            this.help = "add a friend to a private channel";

            this.options = List.of(
                    new OptionData(OptionType.USER, "friend", "the person you want to add")
                            .setRequired(true));
        }

        @Override
protected void execute(SlashCommandEvent event) {
    Guild guild = event.getGuild();
    Member friend = Objects.requireNonNull(event.getOption("friend")).getAsMember();

    // Get the voice channel
    List<VoiceChannel> channels = guild.getVoiceChannelsByName(event.getUser().getEffectiveName()+"'s Channel", true);
    VoiceChannel vc;
    if (channels.isEmpty()) {
        // Create a new voice channel if it doesn't exist
        vc = guild.createVoiceChannel(event.getMember().getEffectiveName() + "'s Channel")
                .addPermissionOverride(guild.getPublicRole(), 0L, Permission.VIEW_CHANNEL.getRawValue()) // Deny VIEW_CHANNEL permission for @everyone role
                .addPermissionOverride(event.getMember(), Permission.VIEW_CHANNEL.getRawValue(), 0L) // Allow VIEW_CHANNEL permission for the creator
                .complete(); // Block until the channel is created
    } else {
        // Use the existing channel
        vc = channels.get(0);
    }

    // Add the friend to the channel
    vc.getManager().putMemberPermissionOverride(friend.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0L).complete();

    event.reply("Added " + friend.getAsMention() + " to " + vc.getAsMention() + "!").setEphemeral(true).queue();

            // Schedule a task to delete the channel if it's empty for more than 15 minutes
            scheduler.scheduleAtFixedRate(() -> {
                if (vc.getMembers().isEmpty()) {
                    vc.delete().queue(v -> scheduler.shutdown());
                }
            }, 15, 15, TimeUnit.MINUTES);
}
    }
}
