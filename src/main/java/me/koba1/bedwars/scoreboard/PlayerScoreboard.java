package me.koba1.bedwars.scoreboard;

import me.koba1.bedwars.configs.Config;
import me.koba1.bedwars.scoreboard.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerScoreboard {

    private final Player player;
    private long titleChanger;
    private FastBoard fastBoard;
    private int titleIndex;
    private String title;

    public PlayerScoreboard(Player player) {
        this.player = player;
        this.titleChanger = System.currentTimeMillis();
        this.titleIndex = 0;
        this.fastBoard = new FastBoard(player);
        this.title = "Title";
    }

    private void tickTitle() {
        if (Config.SCOREBOARD_SIDEBAR_TITLE_TICK_REFRESH.integer() != 0) {
            if (this.titleChanger < System.currentTimeMillis()) {
                this.titleChanger = System.currentTimeMillis() + Math.round(((double) (Config.SCOREBOARD_SIDEBAR_TITLE_TICK_REFRESH.integer() / 20)) * 1000L);
                this.fastBoard.updateTitle(this.getTitle());
            }
        }
        else {
            if (!this.fastBoard.getTitle().equals(this.title)) {
                this.fastBoard.updateTitle(this.title);
            }
        }
    }

    public String getTitle() {
        List<String> lines = BedwarsScoreboard.getInstance().getTitleLines(this.player);
        if(this.titleIndex == lines.size()) {
            this.titleIndex = 0;
            return lines.get(0);
        }

        String s = lines.get(this.titleIndex);
        this.titleIndex++;
        return s;
    }


    public void update() {
        if(player == null) {
            this.fastBoard.delete();
            return;
        }
        List<String> lines = BedwarsScoreboard.getInstance().getScoreboard(this.player);
        if (lines == null || lines.isEmpty()) {
            if (!this.fastBoard.isDeleted()) {
                this.fastBoard.delete();
            }
            return;
        }
        if (this.fastBoard.isDeleted()) {
            this.fastBoard = new FastBoard(this.player);
        }
        this.tickTitle();
        this.fastBoard.updateLines(lines);
    }
}
