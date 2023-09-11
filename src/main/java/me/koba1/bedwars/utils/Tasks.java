package me.koba1.bedwars.utils;

import me.koba1.bedwars.Main;
import org.bukkit.Bukkit;

public class Tasks {

    private static Main m = Main.getPlugin(Main.class);

    public static void executeScheduled(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskTimer(m, runnable, 0L, HCF);
    }

    public static void executeLater(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskLater(m, runnable, HCF);
    }

    public static void executeAsync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(m, runnable);
    }

    public static void executeScheduledAsync(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, runnable, 0L, HCF);
    }

    public static void execute(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(m, runnable);
    }
}
