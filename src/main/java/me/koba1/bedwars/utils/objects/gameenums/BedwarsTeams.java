package me.koba1.bedwars.utils.objects.gameenums;

import lombok.Getter;

public enum BedwarsTeams {
    RED("§c"),
    BLUE("§9"),
    GREEN("§a"),
    YELLOW("§e"),
    AQUA("§b"),
    WHITE("§f"),
    PINK("§d"),
    GRAY("§8"),
    NONE,
    SPECTATOR;


    private String colorCode;
    @Getter
    private boolean isTeam;

    BedwarsTeams(String colorCode) {
        this.colorCode = colorCode;
        this.isTeam = true;
    }

    BedwarsTeams() {
        this.colorCode = "§f";
        this.isTeam = false;
    }

    public static BedwarsTeams getTeamByName(String name) {
        for (BedwarsTeams value : values()) {
            if(value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return NONE;
    }
    public String getColor() {
        return this.colorCode;
    }
}
