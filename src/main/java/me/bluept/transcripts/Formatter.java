package me.bluept.transcripts;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    private static final Pattern STRONG = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern EM = Pattern.compile("\\*(.+?)\\*");
    private static final Pattern S = Pattern.compile("~~(.+?)~~");
    private static final Pattern U = Pattern.compile("__(.+?)__");
    private static final Pattern CODE = Pattern.compile("```(.+?)```");
    private static final Pattern CODE_1 = Pattern.compile("`(.+?)`");
    private static final Pattern NEW_LINE = Pattern.compile("\\n");

    public static String formatBytes(long bytes) {
        int unit = 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String toHex(Color color) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(color.getRGB() & 0xffffff));
        while(hex.length() < 6){
            hex.insert(0, "0");
        }
        return hex.toString();
    }

    public static String format(String originalText) {
        originalText = formatStrong(originalText);
        originalText = formatEm(originalText);
        originalText = formatStrikethrough(originalText);
        originalText = formatUnderline(originalText);
        originalText = formatCodeBlock(originalText);
        originalText = formatInlineCode(originalText);
        originalText = formatNewLine(originalText);

        return originalText;
    }

    private static String formatStrong(String text) {
        return formatPattern(text, STRONG, "<strong>", "</strong>");
    }

    private static String formatEm(String text) {
        return formatPattern(text, EM, "<em>", "</em>");
    }

    private static String formatStrikethrough(String text) {
        return formatPattern(text, S, "<s>", "</s>");
    }

    private static String formatUnderline(String text) {
        return formatPattern(text, U, "<u>", "</u>");
    }

    private static String formatCodeBlock(String text) {
        Matcher matcher = CODE.matcher(text);
        boolean findCode = false;
        while (matcher.find()) {
            String group = matcher.group();
            text = text.replace(group,
                    "<div class=\"pre pre--multiline nohighlight\">"
                            + group.replace("```", "").substring(3, group.length() - 3) + "</div>");
            findCode = true;
        }
        return findCode ? text : formatInlineCode(text);
    }

    private static String formatInlineCode(String text) {
        return formatPattern(text, CODE_1, "<span class=\"pre pre--inline\">", "</span>");
    }

    private static String formatNewLine(String text) {
        return text.replaceAll(NEW_LINE.pattern(), "<br />");
    }

    private static String formatPattern(String text, Pattern pattern, String openTag, String closeTag) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            text = text.replace(group, openTag + group.replaceAll(pattern.pattern().substring(1, pattern.pattern().length() - 1), "") + closeTag);
        }
        return text;
    }
}
