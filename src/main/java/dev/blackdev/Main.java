package dev.blackdev;

import dev.blackdev.commands.*;
import dev.blackdev.events.Gateway;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.Color;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    static Dotenv dotenv = Dotenv.load();
    public static JDA jdabuilder = JDABuilder.create(dotenv.get("TOKEN"), Arrays.asList(GatewayIntent.values())).build();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final String[] activities = {
            "404" + " \uD83D\uDCBB",
            "Coding " + "\uD83D\uDC68\u200D\uD83D\uDCBB",
            " " + "\uD83C\uDDE9\uD83C\uDDEA"
    };

    private static int activityIndex = 0;

    public static void main(String[] args) {

        TextChannel ticketChannel = jdabuilder.getTextChannelById(1332094477766099008L);
        if (ticketChannel != null) {
            ticketChannel.getIterableHistory().queue(messages -> {
                for (var message : messages) {
                    message.delete().queue();
                }
            });

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.BLACK);
            embed.setTitle("Create a Ticket");
            embed.setDescription("Select the type of ticket you want to create:");

            StringSelectMenu menu = StringSelectMenu.create("ticket_type")
                    .addOption("Giveaway Claim", "giveaway_claim")
                    .addOption("User Report", "user_report")
                    .addOption("Bug Report", "bug_report")
                    .build();

            ticketChannel.sendMessageEmbeds(embed.build())
                    .addActionRow(menu)
                    .queue();
        }



        jdabuilder.addEventListener(new Gateway());
        jdabuilder.addEventListener(new EmbendCommand());
        jdabuilder.addEventListener(new PingCommand());
        jdabuilder.addEventListener(new UserInfoCommand());
        jdabuilder.addEventListener(new ServerInfoCommand());
        jdabuilder.addEventListener(new RoleInfoCommand());
        jdabuilder.addEventListener(new AvatarCommand());
        jdabuilder.addEventListener(new ClearMessagesCommand());
        jdabuilder.addEventListener(new GiveawayCommand());

        jdabuilder.updateCommands().addCommands(
                EmbendCommand.buildCommand(),
                PingCommand.buildCommand(),
                UserInfoCommand.buildCommand(),
                ServerInfoCommand.buildCommand(),
                RoleInfoCommand.buildCommand(),
                AvatarCommand.buildCommand(),
                ClearMessagesCommand.buildCommand(),
                GiveawayCommand.buildCommand()
        ).queue();
        System.out.println("Commands loaded");

        scheduler.scheduleAtFixedRate(() -> {
            jdabuilder.getPresence().setActivity(Activity.customStatus(activities[activityIndex]));
            activityIndex = (activityIndex + 1) % activities.length;
        }, 0, 5, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                shutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static void shutdown() throws InterruptedException {
        jdabuilder.getPresence().setStatus(OnlineStatus.OFFLINE);
        jdabuilder.shutdown();
        if (!jdabuilder.awaitShutdown(3, TimeUnit.SECONDS))
            jdabuilder.shutdownNow();
        scheduler.shutdown();
    }
}