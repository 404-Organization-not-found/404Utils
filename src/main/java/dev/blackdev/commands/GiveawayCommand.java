package dev.blackdev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GiveawayCommand extends ListenerAdapter {

    private final List<String> participants = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static CommandData buildCommand() {
        return Commands.slash("giveaway", "Start a giveaway");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("giveaway")) {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.reply("You do not have permission to use this command.").setEphemeral(true).queue();
                return;
            }

            TextInput titleInput = TextInput.create("title", "Title", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();
            TextInput descriptionInput = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
                    .setRequired(true)
                    .build();
            TextInput thumbnailInput = TextInput.create("thumbnail", "Thumbnail URL", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();
            TextInput durationInput = TextInput.create("duration", "Duration (e.g., 4d 5h 10m 4s)", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();
            TextInput prizeInput = TextInput.create("prize", "Prize", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("giveaway_modal", "Create Giveaway")
                    .addActionRow(titleInput)
                    .addActionRow(descriptionInput)
                    .addActionRow(thumbnailInput)
                    .addActionRow(durationInput)
                    .addActionRow(prizeInput)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("giveaway_modal")) {
            String title = event.getValue("title").getAsString();
            String description = event.getValue("description").getAsString();
            String thumbnailUrl = event.getValue("thumbnail").getAsString();
            String durationStr = event.getValue("duration").getAsString();
            String prize = event.getValue("prize").getAsString();

            int duration = parseDuration(durationStr);
            Instant endTime = Instant.now().plus(duration, ChronoUnit.SECONDS);

            EmbedBuilder embed = buildEmbed(title, description, thumbnailUrl, prize, endTime);
            Button participateButton = Button.primary("participate", "Participate");

            event.getChannel().sendMessageEmbeds(embed.build())
                    .setActionRow(participateButton)
                    .queue();

            event.reply("Giveaway started!").setEphemeral(true).queue();

            scheduler.schedule(() -> {
                if (participants.isEmpty()) {
                    event.getChannel().sendMessage("No participants in the giveaway.").queue();
                } else {
                    Random random = new Random();
                    String winner = participants.get(random.nextInt(participants.size()));
                    event.getChannel().sendMessage("Congratulations " + winner + "! You won the giveaway! Prize: " + prize).queue();
                }
                participants.clear();
            }, duration, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("participate")) {
            String userId = event.getUser().getAsMention();
            if (!participants.contains(userId)) {
                participants.add(userId);
                event.reply("You have been added to the giveaway!").setEphemeral(true).queue();
            } else {
                event.reply("You are already participating in the giveaway!").setEphemeral(true).queue();
            }
        }
    }

    private EmbedBuilder buildEmbed(String title, String description, String thumbnailUrl, String prize, Instant endTime) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.BLACK);
        embed.setTitle(title);
        embed.setDescription(description);
        embed.addField("Prize", prize, false);
        embed.setTimestamp(endTime);
        if (!thumbnailUrl.isEmpty()) {
            embed.setThumbnail(thumbnailUrl);
        }

        return embed;
    }

    private int parseDuration(String durationStr) {
        Pattern pattern = Pattern.compile("(\\d+d)?\\s*(\\d+h)?\\s*(\\d+m)?\\s*(\\d+s)?");
        Matcher matcher = pattern.matcher(durationStr);
        int totalSeconds = 0;

        if (matcher.matches()) {
            String days = matcher.group(1);
            String hours = matcher.group(2);
            String minutes = matcher.group(3);
            String seconds = matcher.group(4);

            if (days != null) {
                totalSeconds += Integer.parseInt(days.replace("d", "")) * 24 * 60 * 60;
            }
            if (hours != null) {
                totalSeconds += Integer.parseInt(hours.replace("h", "")) * 60 * 60;
            }
            if (minutes != null) {
                totalSeconds += Integer.parseInt(minutes.replace("m", "")) * 60;
            }
            if (seconds != null) {
                totalSeconds += Integer.parseInt(seconds.replace("s", ""));
            }
        }

        return totalSeconds;
    }
}