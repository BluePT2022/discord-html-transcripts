package me.bluept.transcripts.transcripts.helpers;

import me.bluept.transcripts.Formatter;
import net.dv8tion.jda.api.entities.Message;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

public class AttachmentsHelper {
    private static Element attachmentsDiv;
    private static final List<String>
            imageFormats = Arrays.asList("png", "jpg", "jpeg", "gif"),
            videoFormats = Arrays.asList("mp4", "webm", "mkv", "avi", "mov", "flv", "wmv", "mpg", "mpeg"),
            audioFormats = Arrays.asList("mp3", "wav", "ogg", "flac");

    public static Element buildAttachmentsElement(Document document, Message.Attachment attachment) {
        Element attachmentsDiv = document.createElement("div");
        attachmentsDiv.addClass("chatlog__attachment");

        String type = attachment.getFileExtension();
        if (imageFormats.contains(type)) {
            images(document, attachment);
        } else if (videoFormats.contains(type)) {
            video(document, attachment);
        } else if (audioFormats.contains(type)) {
            audio(document, attachment);
        } else {
            generic(document, attachment);
        }

        return attachmentsDiv;
    }

    private static void images(Document document, Message.Attachment attachment) {
        Element attachmentLink = document.createElement("a");

        Element attachmentImage = document.createElement("img");
        attachmentImage.addClass("chatlog__attachment-media");
        attachmentImage.attr("src", attachment.getUrl());
        attachmentImage.attr("alt", "Image attachment");
        attachmentImage.attr("loading", "lazy");
        attachmentImage.attr("title",
                "Image: " + attachment.getFileName() + Formatter.formatBytes(attachment.getSize()));

        attachmentLink.appendChild(attachmentImage);
        attachmentsDiv.appendChild(attachmentLink);
    }

    private static void video(Document document, Message.Attachment attachment) {
        Element attachmentVideo = document.createElement("video");
        attachmentVideo.addClass("chatlog__attachment-media");
        attachmentVideo.attr("src", attachment.getUrl());
        attachmentVideo.attr("alt", "Video attachment");
        attachmentVideo.attr("controls", true);
        attachmentVideo.attr("title",
                "Video: " + attachment.getFileName() + Formatter.formatBytes(attachment.getSize()));

        attachmentsDiv.appendChild(attachmentVideo);
    }

    private static void audio(Document document, Message.Attachment attachment) {
        Element attachmentAudio = document.createElement("audio");
        attachmentAudio.addClass("chatlog__attachment-media");
        attachmentAudio.attr("src", attachment.getUrl());
        attachmentAudio.attr("alt", "Audio attachment");
        attachmentAudio.attr("controls", true);
        attachmentAudio.attr("title",
                "Audio: " + attachment.getFileName() + Formatter.formatBytes(attachment.getSize()));

        attachmentsDiv.appendChild(attachmentAudio);
    }

    private static void generic(Document document, Message.Attachment attachment) {
        Element attachmentGeneric = document.createElement("div");
        attachmentGeneric.addClass("chatlog__attachment-generic");

        Element attachmentGenericIcon = document.createElement("svg");
        attachmentGenericIcon.addClass("chatlog__attachment-generic-icon");

        Element attachmentGenericIconUse = document.createElement("use");
        attachmentGenericIconUse.attr("xlink:href", "#icon-attachment");

        attachmentGenericIcon.appendChild(attachmentGenericIconUse);
        attachmentGeneric.appendChild(attachmentGenericIcon);

        Element attachmentGenericName = document.createElement("div");
        attachmentGenericName.addClass("chatlog__attachment-generic-name");

        Element attachmentGenericNameLink = document.createElement("a");
        attachmentGenericNameLink.attr("href", attachment.getUrl());
        attachmentGenericNameLink.text(attachment.getFileName());

        attachmentGenericName.appendChild(attachmentGenericNameLink);
        attachmentGeneric.appendChild(attachmentGenericName);

        Element attachmentGenericSize = document.createElement("div");
        attachmentGenericSize.addClass("chatlog__attachment-generic-size");

        attachmentGenericSize.text(Formatter.formatBytes(attachment.getSize()));
        attachmentGeneric.appendChild(attachmentGenericSize);

        attachmentsDiv.appendChild(attachmentGeneric);
    }
}
