package me.bluept.transcripts.transcripts;

import lombok.var;
import me.bluept.transcripts.Formatter;
import me.bluept.transcripts.transcripts.helpers.AttachmentsHelper;
import me.bluept.transcripts.transcripts.helpers.EmbedHelper;
import me.ryzeon.transcripts.DiscordHtmlTranscripts;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TranscriptBuilder {

    public InputStream generateFromMessages(Collection<Message> messages) throws IOException {
        Document document = Jsoup.parse(findTemplate(), "UTF-8");
        document.outputSettings().indentAmount(0).prettyPrint(true);
        buildHeader(document, messages.iterator().next().getChannel().asTextChannel());
        Element chatLog = document.getElementById("chatlog");
        for (Message message : messages.stream()
                .sorted(Comparator.comparing(ISnowflake::getTimeCreated))
                .collect(Collectors.toList())) {
            Element messageGroup = document.createElement("div");
            messageGroup.addClass("chatlog__message-group");

            buildReferenceMessage(document, message, messageGroup);
            var author = message.getAuthor();

            Element authorElement = document.createElement("div");
            authorElement.addClass("chatlog__author-avatar-container");

            Element authorAvatar = document.createElement("img");
            authorAvatar.addClass("chatlog__author-avatar");
            authorAvatar.attr("src", author.getAvatarUrl());
            authorAvatar.attr("alt", "Avatar");
            authorAvatar.attr("loading", "lazy");

            authorElement.appendChild(authorAvatar);
            messageGroup.appendChild(authorElement);

            // message content
            Element content = document.createElement("div");
            content.addClass("chatlog__messages");
            // message author name
            Element authorName = document.createElement("span");
            authorName.addClass("chatlog__author-name");
            // authorName.attr("title", author.getName()); // author.name
            authorName.attr("title", author.getName());
            authorName.text(author.getName());
            authorName.attr("data-user-id", author.getId());
            content.appendChild(authorName);

            if (author.isBot()) {
                Element botTag = document.createElement("span");
                botTag.addClass("chatlog__bot-tag").text("BOT");
                content.appendChild(botTag);
            }

            Element timestamp = document.createElement("span");
            timestamp.addClass("chatlog__timestamp");
            timestamp
                    .text(message.getTimeCreated().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            content.appendChild(timestamp);

            Element messageContent = document.createElement("div");
            messageContent.addClass("chatlog__message");
            messageContent.attr("data-message-id", message.getId());
            messageContent.attr("id", "message-" + message.getId());
            messageContent.attr("title", "Message sent: "
                    + message.getTimeCreated().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            if (!message.getContentDisplay().isEmpty()) {
                Element messageContentContent = document.createElement("div");
                messageContentContent.addClass("chatlog__content");

                Element messageContentContentMarkdown = document.createElement("div");
                messageContentContentMarkdown.addClass("markdown");

                Element messageContentContentMarkdownSpan = document.createElement("span");
                messageContentContentMarkdownSpan.addClass("preserve-whitespace");
                messageContentContentMarkdownSpan
                        .html(Formatter.format(message.getContentDisplay()));

                messageContentContentMarkdown.appendChild(messageContentContentMarkdownSpan);
                messageContentContent.appendChild(messageContentContentMarkdown);
                messageContent.appendChild(messageContentContent);
            }

            if (!message.getAttachments().isEmpty()) {
                for (Message.Attachment attach : message.getAttachments()) {
                    Element attachmentsDiv = document.createElement("div");
                    attachmentsDiv.addClass("chatlog__attachment");

                    messageContent.appendChild(AttachmentsHelper.buildAttachmentsElement(document, attach));
                }
            }

            if (!message.getEmbeds().isEmpty()) {
                for (MessageEmbed embed : message.getEmbeds()) {
                    content.appendChild(EmbedHelper.buildEmbedElement(document, embed));
                    content.appendChild(messageContent);
                }
            }

            messageGroup.appendChild(content);
            chatLog.appendChild(messageGroup);
        }

        return new ByteArrayInputStream(document.outerHtml().getBytes());
    }

    private static void buildHeader(Document document, TextChannel channel) {
        document.getElementsByClass("preamble__guild-icon")
                .first().attr("src", channel.getGuild().getIconUrl());

        document.getElementById("transcriptTitle").text(channel.getName());
        document.getElementById("guildname").text(channel.getGuild().getName());
        document.getElementById("ticketname").text(channel.getName());
    }

    private static void buildReferenceMessage(Document document, Message message, Element messageGroup) {
        if (message.getReferencedMessage() != null) {
            Element referenceSymbol = document.createElement("div");
            referenceSymbol.addClass("chatlog__reference-symbol");

            Element reference = document.createElement("div");
            reference.addClass("chatlog__reference");

            var referenceMessage = message.getReferencedMessage();
            User author = referenceMessage.getAuthor();
            Member member = message.getChannel().asTextChannel().getGuild().getMember(author);
            assert member != null;

            author.getAvatarUrl();
            author.getName();
            author.getName();
            referenceMessage.getId();
            referenceMessage.getContentDisplay();
            reference.html(referenceMessage.getContentDisplay().length() > 42
                    ? referenceMessage.getContentDisplay().substring(0, 42)
                    + "..."
                    : referenceMessage.getContentDisplay());

            messageGroup.appendChild(referenceSymbol);
            messageGroup.appendChild(reference);
        }
    }

    private static File findTemplate() {
        InputStream inputStream = DiscordHtmlTranscripts.class.getClassLoader().getResourceAsStream("template.html");
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: template.html");
        }
        try {
            File tempFile = File.createTempFile("template.html", ".tmp");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Error creating temporary file for: template.html", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception accordingly
            }
        }
    }
}
