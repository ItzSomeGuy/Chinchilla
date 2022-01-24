package com.chipset.main;


import com.chipset.spade.ChannelHandler;
import com.chipset.spade.Spade;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.reflections.Reflections;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;


public class Bot {
    static Dotenv dotenv = Dotenv.load();
    static String token = dotenv.get("TOKEN");
    public static JDA jda;

    private static final EventWaiter eventWaiter = new EventWaiter();

    public static void main(String[] arguments) throws LoginException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CommandClientBuilder commandClient = new CommandClientBuilder();
        commandClient.forceGuildOnly(193117152709050368L);

        // for all slash commands in the com.chipset.slash_commands package
        // add them to the commandClient
        Set<Class<? extends SlashCommand>> commands = findCommands("com.chipset.slash_commands");
        for (Class<? extends SlashCommand> command : commands) {
            if (!command.getSimpleName().equals("Add") && !command.getSimpleName().equals("Remove")) {
                SlashCommand temp = command.getConstructor().newInstance();
                commandClient.addSlashCommand(temp);
            }
        }

        // add the spade commands
        commandClient.addSlashCommand(new Spade());

        commandClient.setOwnerId(192370343510409216L);
        commandClient.setActivity(Activity.listening("sick beats"));
        CommandClient client = commandClient.build();

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.addEventListeners(
                client,
                eventWaiter,
                new ReadyListener(),
                new ChannelHandler()
        );

        jda = builder.build();
        jda.awaitReady();

        // toggle on and restart to clear bot slash commands
//        Objects.requireNonNull(jda.getGuildById(847520841217343488L)).updateCommands().queue();
    }

    public static EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Set<Class<? extends SlashCommand>> findCommands(String packageName) {
        Reflections reflections = new Reflections("com.chipset.slash_commands");
        return reflections.getSubTypesOf(SlashCommand.class);
    }
}
