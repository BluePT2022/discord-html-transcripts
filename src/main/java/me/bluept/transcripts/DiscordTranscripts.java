package me.bluept.transcripts;

import lombok.Getter;
import me.bluept.transcripts.transcripts.TranscriptBuilder;
import me.ryzeon.transcripts.DiscordHtmlTranscripts;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

public class DiscordTranscripts {
    @Getter
    private static final DiscordHtmlTranscripts instance = new DiscordHtmlTranscripts();

    public void createTranscript(TextChannel channel) throws IOException {
        createTranscript(channel, null);
    }

    public void createTranscript(TextChannel channel, String fileName) throws IOException {
        TranscriptBuilder transcriptBuilder = new TranscriptBuilder();
        InputStream file = transcriptBuilder.generateFromMessages(channel.getIterableHistory().stream().collect(Collectors.toList()));
        FileUpload upload = FileUpload.fromData(file, (fileName != null) ? fileName : "transcript.html");
        channel.sendFiles(upload).queue();
    }
}
