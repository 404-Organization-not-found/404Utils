/*
//Copyright 404
//Licensed under Creative Commons Attribution-NonCommercial 4.0 International Public License
*/


package dev.blackdev.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class ClearMessagesCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("clear", "Delete a specified number of messages from a channel")
                .addOption(OptionType.INTEGER, "amount", "The number of messages to delete", true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("clear")) {
            if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                event.reply("You do not have permission to use this command.").setEphemeral(true).queue();
                return;
            }

            int amount = event.getOption("amount").getAsInt();
            TextChannel channel = (TextChannel) event.getChannel();
            channel.getHistory().retrievePast(amount).queue(messages -> {
                channel.deleteMessages(messages).queue();
                event.reply("Deleted " + amount + " messages.").setEphemeral(true).queue();
            });
        }
    }
}