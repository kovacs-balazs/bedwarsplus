package me.koba1.bedwars.utils;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    private static final DecimalFormat HEALTH_FORMATTER;
    private static final long MINUTE;
    private static final long HOUR;
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING;
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS;
    public static SimpleDateFormat DATE_FORMAT;

    /**
     *
     * @param timer
     * @return `HH:`mm:ss
     */
    public static String formatMMSS(long timer) {
        return DurationFormatUtils.formatDuration(timer, ((timer >= Formatter.HOUR) ? "HH:" : "") + "mm:ss");
    }

    /**
     *
     * @param timer
     * @param format
     * @param remaining
     * @return `HH:`mm:ss
     */
    public static String getRemaining(long timer, boolean format, boolean remaining) {
        if (format && timer < Formatter.MINUTE) {
            return (remaining ? Formatter.REMAINING_SECONDS_TRAILING : Formatter.REMAINING_SECONDS).get().format(timer * 0.001) + 's';
        }
        return DurationFormatUtils.formatDuration(timer, ((timer >= Formatter.HOUR) ? "HH:" : "") + "mm:ss");
    }

    static {
        HEALTH_FORMATTER = new DecimalFormat("#.#");
        MINUTE = TimeUnit.MINUTES.toMillis(1L);
        HOUR = TimeUnit.HOURS.toMillis(1L);
        REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
        REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));
        DATE_FORMAT = new SimpleDateFormat("dd MMMM hh:mm");
    }

    public static Long parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        if (input.equals("0")) {
            return 0L;
        }
        long l = 0L;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char chr = input.charAt(i);
            if (Character.isDigit(chr)) {
                builder.append(chr);
            }
            else {
                String s;
                if (Character.isLetter(chr) && !(s = String.valueOf(builder)).isEmpty()) {
                    l += convert(Integer.parseInt(s), chr);
                    builder = new StringBuilder();
                }
            }
        }
        return (l == 0L || l == -1L) ? null : l;
    }

    private static long convert(int time, char timers) {
        switch (timers) {
            case 'y': {
                return time * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return time * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return time * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return time * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return time * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return time * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }

    public static String getRemaining(long time, boolean b) {
        return getRemaining(time, b, true);
    }

    public static String formatDetailed(long time) {
        return DurationFormatUtils.formatDurationWords(time, true, true);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return formatter.format(date);
    }

    public static String formatHealth(double input) {
        return Formatter.HEALTH_FORMATTER.format(input);
    }

    private static final Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]){6}>");
    public static String applyColor(String message){
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            final ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
            final String before = message.substring(0, matcher.start());
            final String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }



    public static FormatterUtils format(String string) {
        return new FormatterUtils(string);
    }

    public static FormatterUtils format(List<String> list) {
        return new FormatterUtils(list);
    }
}
