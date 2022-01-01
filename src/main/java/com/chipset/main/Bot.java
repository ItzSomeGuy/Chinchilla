package com.chipset.main;


import com.chipset.slash_commands.*;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;


public class Bot {
    static Dotenv dotenv = Dotenv.load();
    static String token = dotenv.get("TOKEN");
    public static JDA jda;

    private static final EventWaiter eventWaiter = new EventWaiter();

    public static void main(String[] arguments) throws LoginException, InterruptedException {
        CommandClientBuilder commandClient = new CommandClientBuilder();
        commandClient.forceGuildOnly(847520841217343488L);
        commandClient.addSlashCommands(
                new Ban(),
                new Flip(),
                new PFP(),
                new Punish(),
                new Rename(),
                new Roll(),
                new Say()
        );
        commandClient.setOwnerId(192370343510409216L);
        CommandClient client = commandClient.build();

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.watching("airplanes"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.addEventListeners(
                client,
                eventWaiter,
                new ReadyListener()
        );

        jda = builder.build();
        jda.awaitReady();

        // toggle on and restart to clear bot slash commands
        //Objects.requireNonNull(jda.getGuildById(847520841217343488L)).updateCommands().queue();
    }

    public static EventWaiter getEventWaiter() {
        return eventWaiter;
    }
}
