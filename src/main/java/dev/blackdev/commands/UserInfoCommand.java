package dev.blackdev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class UserInfoCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("userinfo", "Display information about a user")
                .addOption(OptionType.USER, "user", "The user to get information about", true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("userinfo")) {
            var user = event.getOption("user").getAsUser();
            var member = event.getGuild().getMember(user);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.BLACK);
            embed.setTitle("User Info");
            embed.setThumbnail(user.getEffectiveAvatarUrl());
            embed.addField("Name", user.getName(), false);
            embed.addField("ID", user.getId(), false);
            embed.addField("Joined", member != null ? member.getTimeJoined().toString() : "N/A", false);
            event.replyEmbeds(embed.build()).queue();
        }
    }
}