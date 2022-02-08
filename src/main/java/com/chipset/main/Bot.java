package com.chipset.main;


import com.chipset.database.SQLiteDataSource;
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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.reflections.Reflections;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Bot {
    static Dotenv dotenv = Dotenv.load();
    static String token = dotenv.get("TOKEN");
    public static JDA jda;

    private static final EventWaiter eventWaiter = new EventWaiter();

    public static void main(String[] arguments) throws LoginException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        SQLiteDataSource.getConnection();

        CommandClientBuilder commandClient = new CommandClientBuilder();
        commandClient.forceGuildOnly(193117152709050368L);

        // for all slash commands in a package
        // add them to the commandClient
        Set<Class<? extends SlashCommand>> commands = findCommands("com.chipset.slash_commands");
        commands.addAll(findCommands("com.chipset.database"));
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

        JDABuilder builder = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.addEventListeners(
                client,
                eventWaiter,
                new ReadyListener(),
                new ChannelHandler(),
                new Bouncer()
        );

        jda = builder.build();
        jda.awaitReady();

        scheduleUpdate();
    }

    public static EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Set<Class<? extends SlashCommand>> findCommands(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(SlashCommand.class);
    }

    private static void scheduleUpdate() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime nextRun = now.withHour(4).withMinute(0).withSecond(0);

        if (now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Updating...");
            // update the database
            Guild guild = jda.getGuildById(193117152709050368L);
            assert guild != null;
            List<Member> members = guild.getMembers();

            for (Member member : members) {
                if (member.getUser().isBot()) continue;

                SQLiteDataSource.updateMember(guild, member);
            }
        },
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }
}
