package me.bluept.transcripts.transcripts.helpers;

import me.bluept.transcripts.Formatter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmbedHelper {
    private static Element embedContentContainer;
    private static Element embedContent;
    private static Element embedText;


    /**
     * @param document The HTML document (template.html)
     * @param embed The embed from the message embed
     * @return returns the embedDiv
     */
    public static Element buildEmbedElement(Document document, MessageEmbed embed) {
        Element embedDiv = document.createElement("div");
        embedDiv.addClass("chatlog__embed");

        embedContentContainer = document.createElement("div");
        embedContentContainer.addClass("chatlog__embed-content-container");

        embedContent = document.createElement("div");
        embedContent.addClass("chatlog__embed-content");

        embedText = document.createElement("div");
        embedText.addClass("chatlog__embed-text");

        if (embed.getColor() != null) {
            Element embedColorPill = document.createElement("div");
            embedColorPill.addClass("chatlog__embed-color-pill");
            embedColorPill.attr("style",
                    "background-color: #" + Formatter.toHex(embed.getColor()));

            embedDiv.appendChild(embedColorPill);
        }

        embedAuthor(document, embed);
        embedTitle(document, embed);
        embedDescription(document, embed);
        embedFields(document, embed);
        embedContent.appendChild(embedText);
        embedThumbnail(document, embed);
        embedContentContainer.appendChild(embedContent);
        embedImage(document, embed);
        embedFooter(document, embed);

        embedDiv.appendChild(embedContentContainer);
        return embedDiv;
    }

    private static void embedAuthor(Document document, MessageEmbed embed) {
        MessageEmbed.AuthorInfo author = embed.getAuthor();
        if (author != null && author.getName() != null) {
            Element embedAuthor = document.createElement("div");
            embedAuthor.addClass("chatlog__embed-author");

            String iconUrl = author.getIconUrl();
            if (iconUrl != null) {
                Element embedAuthorIcon = document.createElement("img");
                embedAuthorIcon.addClass("chatlog__embed-author-icon");
                embedAuthorIcon.attr("src", iconUrl);
                embedAuthorIcon.attr("alt", "Author icon");
                embedAuthorIcon.attr("loading", "lazy");

                embedAuthor.appendChild(embedAuthorIcon);
            }

            Element embedAuthorName = document.createElement("span");
            embedAuthorName.addClass("chatlog__embed-author-name");

            String url = author.getUrl();
            if (url != null) {
                Element embedAuthorNameLink = document.createElement("a");
                embedAuthorNameLink.addClass("chatlog__embed-author-name-link");
                embedAuthorNameLink.attr("href", url);
                embedAuthorNameLink.text(author.getName());

                embedAuthorName.appendChild(embedAuthorNameLink);
            } else {
                embedAuthorName.text(author.getName());
            }

            embedAuthor.appendChild(embedAuthorName);
            embedText.appendChild(embedAuthor);
        }
    }

    private static void embedTitle(Document document, MessageEmbed embed) {
        String title = embed.getTitle();
        if (title != null) {
            Element embedTitle = document.createElement("div");
            embedTitle.addClass("chatlog__embed-title");

            if (embed.getUrl() != null) {
                Element embedTitleLink = document.createElement("a");
                embedTitleLink.addClass("chatlog__embed-title-link");
                embedTitleLink.attr("href", embed.getUrl());

                Element embedTitleMarkdown = document.createElement("div");
                embedTitleMarkdown.addClass("markdown preserve-whitespace")
                        .html(Formatter.format(title));

                embedTitleLink.appendChild(embedTitleMarkdown);
                embedTitle.appendChild(embedTitleLink);
            } else {
                Element embedTitleMarkdown = document.createElement("div");
                embedTitleMarkdown.addClass("markdown preserve-whitespace")
                        .html(Formatter.format(title));

                embedTitle.appendChild(embedTitleMarkdown);
            }
            embedText.appendChild(embedTitle);
        }
    }

    private static void embedDescription(Document document, MessageEmbed embed) {
        String description = embed.getDescription();
        if (description != null) {
            Element embedDescription = document.createElement("div");
            embedDescription.addClass("chatlog__embed-description");

            Element embedDescriptionMarkdown = document.createElement("div");
            embedDescriptionMarkdown.addClass("markdown preserve-whitespace");
            embedDescriptionMarkdown
                    .html(Formatter.format(description));

            embedDescription.appendChild(embedDescriptionMarkdown);
            embedText.appendChild(embedDescription);
        }
    }

    private static void embedFields(Document document, MessageEmbed embed) {
        List<MessageEmbed.Field> fields = embed.getFields();
        if (!fields.isEmpty()) {
            Element embedFields = document.createElement("div");
            embedFields.addClass("chatlog__embed-fields");

            for (MessageEmbed.Field field : fields) {
                Element embedField = document.createElement("div");
                embedField.addClass(field.isInline() ? "chatlog__embed-field-inline"
                        : "chatlog__embed-field");

                //Name
                Element embedFieldName = document.createElement("div");
                embedFieldName.addClass("chatlog__embed-field-name");

                Element embedFieldNameMarkdown = document.createElement("div");
                embedFieldNameMarkdown.addClass("markdown preserve-whitespace");
                embedFieldNameMarkdown.html((field.getName() != null) ? field.getName() : "NULL");

                embedFieldName.appendChild(embedFieldNameMarkdown);
                embedField.appendChild(embedFieldName);


                //Value
                Element embedFieldValue = document.createElement("div");
                embedFieldValue.addClass("chatlog__embed-field-value");

                Element embedFieldValueMarkdown = document.createElement("div");
                embedFieldValueMarkdown.addClass("markdown preserve-whitespace");
                embedFieldValueMarkdown
                        .html(Formatter.format(field.getValue()));

                embedFieldValue.appendChild(embedFieldValueMarkdown);
                embedField.appendChild(embedFieldValue);

                embedFields.appendChild(embedField);
            }

            embedText.appendChild(embedFields);
        }
    }

    private static void embedThumbnail(Document document, MessageEmbed embed) {
        String thumbnailUrl = embed.getThumbnail().getUrl();

        if (embed.getThumbnail() != null) {
            Element embedThumbnail = document.createElement("div");
            embedThumbnail.addClass("chatlog__embed-thumbnail-container");

            Element embedThumbnailLink = document.createElement("a");
            embedThumbnailLink.addClass("chatlog__embed-thumbnail-link");
            embedThumbnailLink.attr("href", thumbnailUrl);

            Element embedThumbnailImage = document.createElement("img");
            embedThumbnailImage.addClass("chatlog__embed-thumbnail");
            embedThumbnailImage.attr("src", thumbnailUrl);
            embedThumbnailImage.attr("alt", "Thumbnail");
            embedThumbnailImage.attr("loading", "lazy");

            embedThumbnailLink.appendChild(embedThumbnailImage);
            embedThumbnail.appendChild(embedThumbnailLink);

            embedContent.appendChild(embedThumbnail);
        }
    }

    private static void embedImage(Document document, MessageEmbed embed) {
        String imageURL = embed.getImage().getUrl();
        if (embed.getImage() != null) {
            Element embedImage = document.createElement("div");
            embedImage.addClass("chatlog__embed-image-container");

            Element embedImageLink = document.createElement("a");
            embedImageLink.addClass("chatlog__embed-image-link");
            embedImageLink.attr("href", imageURL);

            Element embedImageImage = document.createElement("img");
            embedImageImage.addClass("chatlog__embed-image");
            embedImageImage.attr("src", imageURL);
            embedImageImage.attr("alt", "Image");
            embedImageImage.attr("loading", "lazy");

            embedImageLink.appendChild(embedImageImage);
            embedImage.appendChild(embedImageLink);

            embedContentContainer.appendChild(embedImage);
        }
    }

    private static void embedFooter(Document document, MessageEmbed embed) {
        if (embed.getFooter() != null) {
            Element embedFooter = document.createElement("div");
            embedFooter.addClass("chatlog__embed-footer");

            if (embed.getFooter().getIconUrl() != null) {
                Element embedFooterIcon = document.createElement("img");
                embedFooterIcon.addClass("chatlog__embed-footer-icon");
                embedFooterIcon.attr("src", embed.getFooter().getIconUrl());
                embedFooterIcon.attr("alt", "Footer icon");
                embedFooterIcon.attr("loading", "lazy");

                embedFooter.appendChild(embedFooterIcon);
            }

            Element embedFooterText = document.createElement("span");
            embedFooterText.addClass("chatlog__embed-footer-text");
            embedFooterText.text(embed.getTimestamp() != null
                    ? embed.getFooter().getText() + " • " + embed.getTimestamp()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    : embed.getFooter().getText());

            embedFooter.appendChild(embedFooterText);

            embedContentContainer.appendChild(embedFooter);
        }
    }
}
