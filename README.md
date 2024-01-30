This fork has created to solve compatibility issues with JDA 5
Since Version 3 there are two main classes:

For the original code (by ryzeon, updated for JDA 5) use: me.ryzeon.transcripts.DiscordHtmlTranscripts
For the new code use: me.bluept.transcripts.DiscordTranscripts

# Discord (JDA) HTML Transcripts
[![](https://jitpack.io/v/BluePT2022/discord-html-transcripts.svg)](https://jitpack.io/#BluePT2022/discord-html-transcripts)

Discord HTML Transcripts is a node.js module (recode on JDA) to generate nice looking HTML transcripts. Processes discord markdown like **bold**, *italics*, ~~strikethroughs~~, and more. Nicely formats attachments and embeds. Built in XSS protection, preventing users from inserting html tags. 

**This module is designed to work with [JDA](https://github.com/DV8FromTheWorld/JDA).**

HTML Template stolen from [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter).

## Installation

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.BluePT2022</groupId>
    <artifactId>discord-html-transcripts</artifactId>
    <version>Tag</version>
</dependency>
```

## Example Output
![output](https://img.derock.dev/5f5q0a.png)

## Usage
### Example usage using the built in message fetcher.
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();

transcript.createTranscript(textChannel);
```

### Or if you prefer, you can pass in your own messages.
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();

transcript.generateFromMessages(messages); // return to InputStream
```

