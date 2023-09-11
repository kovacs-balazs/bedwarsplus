package me.koba1.bedwars.utils.objects.gameenums;

import lombok.Getter;

public enum BedwarsModes {

    MODE_SOLO(1),
    MODE_DOUBLES(2),
    MODE_3v3v3v3(3),
    MODE_4v4v4v4(4);

    @Getter private int teamSize;

    BedwarsModes(int teamSize) {
        this.teamSize = teamSize;
    }

    public static BedwarsModes getModeByName(String name) {
        for (BedwarsModes value : values()) {
            if(value.name().equalsIgnoreCase(name) || value.name().replace("MODE_", "").equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    public String getName() {
        return name().replace("MODE_", "");
    }
}
