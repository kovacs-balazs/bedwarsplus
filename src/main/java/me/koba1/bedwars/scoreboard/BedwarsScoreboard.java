package me.koba1.bedwars.scoreboard;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.Config;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.FormatterUtils;
import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsTeams;
import org.bukkit.entity.Player;

import javax.sound.sampled.Line;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BedwarsScoreboard {

    private Main m;
    @Getter
    private static BedwarsScoreboard instance;


    public BedwarsScoreboard(Main m) {
        this.m = m;
        instance = this;
    }

    public List<String> getScoreboard(BedwarsArenaManager manager) {
        return new ArrayList<>();
    }

    public List<String> getLobbyScoreboard(Player player) {
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        List<String> lines = Messages.LOBBY.language(player).queueList();
        lines = Formatter.format(lines)
                .replace("%level%", bwPlayer.getStatistics().getBedwarsLevel() + "")
                .replace("%serverIp%", Config.SERVER_IP.string())
                .replace("%wins%", bwPlayer.getStatistics().getWins() + "")
                .replace("%kills%", (bwPlayer.getStatistics().getKills() + bwPlayer.getStatistics().getFinalKills()) + "").list();
        return lines;
    }

    public List<String> getScoreboard(Player player) {
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        if (bwPlayer == null) return new ArrayList<>();

        if (bwPlayer.isInGame()) {
            return getLobbyScoreboard(player);
        }

        BedwarsArenaManager manager = bwPlayer.getCurrentGame();

        SimpleDateFormat formatter = new SimpleDateFormat(Messages.FORMAT_SB_DATE.language(player).queue());
        long unixTime = System.currentTimeMillis();
        String teamLine = Messages.FORMAT_SB_TEAM_GENERIC.language(player).queue();
        String alive = Messages.FORMAT_SB_TEAM_ALIVE.language(player).queue();
        String eliminated = Messages.FORMAT_SB_TEAM_ELIMINATED.language(player).queue();
        String you = Messages.FORMAT_SB_YOU.language(player).queue();
        List<String> lines = new ArrayList<>();
        String group = Formatter.format(manager.getArena().getMode().getName()).toFirstUpper().string();

        switch (manager.getState()) {
            case PREPARING:
                lines = Messages.DEFAULT_WAITING.language(player).queueList();
                lines = replace(player, manager, lines)
                        .applyColor()
                        .papi(player)
                        .list();
                break;
            case STARTING:
                int diff = (int) ((manager.getPrepareTime() - unixTime) / 1000);
                lines = Messages.DEFAULT_STARTING.language(player).queueList();
                lines = replace(player, manager, lines)
                        .replace("%time%", diff + "")
                        .applyColor()
                        .papi(player)
                        .list();
                break;
            case RUNNING:
                switch (manager.getArena().getMode()) {
                    case MODE_SOLO -> lines = Messages.DEFAULT_PLAYING.language(player).queueList();
                    case MODE_DOUBLES -> lines = Messages.DOUBLES_PLAYING.language(player).queueList();
                    case MODE_3v3v3v3 -> lines = Messages.TRIPLES_PLAYING.language(player).queueList();
                    case MODE_4v4v4v4 -> lines = Messages.QUADS_PLAYING.language(player).queueList();
                }
                String nextEvent = "Diamond I";
                int nextEventTime = 20;
                FormatterUtils linesUtils = replace(player, manager, lines)
                        .replace("%nextEvent%", nextEvent)
                        .replace("%time%", nextEventTime + "")
                        .replace("%kills%", "0")
                        .replace("%final_kills%", "0")
                        .replace("%beds%", "0");

                for (BedwarsTeams value : BedwarsTeams.values()) {
                    if (!value.isTeam()) continue;
                    String formattedLine = Formatter.format(teamLine)
                            .replace("%team_color%", value.getColor())
                            .replace("%team_name%", Formatter.format(value.name()).toFirstUpper().string())
                            .replace("%team_letter%", value.name().substring(0, 1).toUpperCase())
                            .replace("%team_status%",
                                    manager.isTeamAlive(value)
                                            ? alive
                                            : eliminated
                            ).string();
                    if (bwPlayer.getCurrentTeam() == value) {
                        formattedLine += you;
                    }
                    linesUtils.replaceFirst("%team%", formattedLine);
                }

                linesUtils.applyColor().papi(player);
                lines = linesUtils.list();
                break;
        }

        return new ArrayList<>();
    }

    private FormatterUtils replace(Player player, BedwarsArenaManager manager, List<String> lines) {
        SimpleDateFormat formatter = new SimpleDateFormat(Messages.FORMAT_SB_DATE.language(player).queue());
        return Formatter.format(lines)
                .replace("%serverIp%", Config.SERVER_IP.string())
                .replace("%group%", Formatter.format(manager.getArena().getMode().getName()).toFirstUpper().string())
                .replace("%map%", manager.getArena().getDisplayName())
                .replace("%on%", manager.getAllPlayersInArena().size() + "")
                .replace("%max%", manager.getArena().getMaxPlayers() + "")
                .replace("%date%", formatter.format(new Date()));
    }

    public List<String> getTitleLines(Player player) {
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        if (bwPlayer == null) return new ArrayList<>();

        BedwarsArenaManager manager = bwPlayer.getCurrentGame();

        switch (manager.getState()) {
            case PREPARING:
                return Arrays.stream(Messages.DEFAULT_WAITING.language(player).queueList().get(0).split(",")).toList();
            case STARTING:
                return Arrays.stream(Messages.DEFAULT_STARTING.language(player).queueList().get(0).split(",")).toList();
            case RUNNING:
                switch (manager.getArena().getMode()) {
                    case MODE_SOLO:
                        return Arrays.stream(Messages.DEFAULT_PLAYING.language(player).queueList().get(0).split(",")).toList();
                    case MODE_DOUBLES:
                        return Arrays.stream(Messages.DOUBLES_PLAYING.language(player).queueList().get(0).split(",")).toList();
                    case MODE_3v3v3v3:
                        return Arrays.stream(Messages.TRIPLES_PLAYING.language(player).queueList().get(0).split(",")).toList();
                    case MODE_4v4v4v4:
                        return Arrays.stream(Messages.QUADS_PLAYING.language(player).queueList().get(0).split(",")).toList();
                }
        }

        return new ArrayList<>();
    }
}
