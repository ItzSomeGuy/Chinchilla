package com.chipset.slash_commands;

import com.chipset.main.Bot;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lock extends SlashCommand {
    public Lock() {
        this.name = "lock";
        this.help = "toggles lock on the channel you're in";
        this.options = List.of(new OptionData(OptionType.BOOLEAN, "lock", "lock or unlock?", true));
        this.ownerCommand = true;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        boolean toggle = event.getOption("lock").getAsBoolean();
        boolean inVoice = event.getMember().getVoiceState().inAudioChannel();

        if (inVoice) {
            final VoiceChannel vc = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            VoiceChannelManager vcManager = vc.getManager();

            if (toggle) { // LOCK
                // save existing permission overrides
                Bot.overrides = vc.getPermissionOverrides();

                // remove all permission overrides in VC
                List<PermissionOverride> overrides = vc.getPermissionOverrides();
                for (PermissionOverride override : overrides) {
                    System.out.println(override.getPermissionHolder().toString());
                    vcManager.removePermissionOverride(override.getPermissionHolder()).queueAfter(overrides.indexOf(override), TimeUnit.SECONDS);
                }

                // deny @everyone role to view the channel
                vcManager.putRolePermissionOverride(vc.getGuild().getPublicRole().getIdLong(), 0L, Permission.VIEW_CHANNEL.getRawValue()).queue(success -> {
                    // add perms for all users in VC
                    List<Member> members = vc.getMembers();
                    for (Member member : members) {
                        System.out.println(member.getEffectiveName());
                        vcManager.putMemberPermissionOverride(member.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0L).queueAfter(members.indexOf(member), TimeUnit.SECONDS);
                    }

                    event.reply("The channel has been locked!").setEphemeral(true).queue();
                });
            } else { // UNLOCK
                // remove all permission overrides in VC
                List<PermissionOverride> overrides = vc.getPermissionOverrides();
                for (PermissionOverride override : overrides) {
                    vcManager.removePermissionOverride(override.getPermissionHolder()).queueAfter(overrides.indexOf(override), TimeUnit.SECONDS);
                }

                // restore original permission overrides
                for (PermissionOverride override : Bot.overrides) {
                    vcManager.putPermissionOverride(override.getPermissionHolder(), override.getAllowedRaw(), override.getDeniedRaw()).queue();
                }

                event.reply("The channel has been unlocked!").setEphemeral(true).queue();
            }
        } else if (toggle && Bot.overrides != null) {
            System.out.println("should not show");
            event.reply("this command is in use, please unlock the currently locked channel before locking another one.").setEphemeral(true).queue();
        } else {
            System.out.println("should not show");
            event.reply("you must be in a voice channel to use this command.").setEphemeral(true).queue();
        }
    }
}
