/*
//Copyright 404
//Licensed under Creative Commons Attribution-NonCommercial 4.0 International Public License
*/

package dev.blackdev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class EmbendCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("createembed", "Creates an embed message");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("createembed")) {
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
            TextInput imageInput = TextInput.create("image", "Image URL", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();
            TextInput colorInput = TextInput.create("color", "Color (hex)", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();
            TextInput thumbnailInput = TextInput.create("thumbnail", "Thumbnail URL", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("createembed_modal", "Create Embed")
                    .addActionRow(titleInput)
                    .addActionRow(descriptionInput)
                    .addActionRow(imageInput)
                    .addActionRow(colorInput)
                    .addActionRow(thumbnailInput)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("createembed_modal")) {
            String title = event.getValue("title").getAsString();
            String description = event.getValue("description").getAsString();
            String imageUrl = event.getValue("image").getAsString();
            String colorHex = event.getValue("color").getAsString();
            String thumbnailUrl = event.getValue("thumbnail").getAsString();

            Color color = Color.BLACK;
            if (!colorHex.isEmpty()) {
                try {
                    color = Color.decode(colorHex);
                } catch (NumberFormatException e) {
                    event.reply("Invalid color format. Using default color.").setEphemeral(true).queue();
                }
            }

            EmbedBuilder embed = buildEmbed(title, description, imageUrl, color, thumbnailUrl);

            event.getChannel().sendMessageEmbeds(embed.build()).queue();
            event.reply("Embed created!").setEphemeral(true).queue();
        }
    }

    private EmbedBuilder buildEmbed(String title, String description, String imageUrl, Color color, String thumbnailUrl) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(color);
        embed.setTitle(title);
        embed.setDescription(description);
        if (!imageUrl.isEmpty()) {
            embed.setImage(imageUrl);
        }
        if (!thumbnailUrl.isEmpty()) {
            embed.setThumbnail(thumbnailUrl);
        }

        return embed;
    }
}