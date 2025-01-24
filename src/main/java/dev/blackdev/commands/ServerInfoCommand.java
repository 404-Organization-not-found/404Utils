package dev.blackdev.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class ServerInfoCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("serverinfo", "Display information about the server");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("serverinfo")) {
            var guild = event.getGuild();
            event.reply("Server Info:\n" +
                    "Name: " + guild.getName() + "\n" +
                    "ID: " + guild.getId() + "\n" +
                    "Members: " + guild.getMemberCount()).queue();
        }
    }
}