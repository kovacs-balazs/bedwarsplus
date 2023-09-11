package me.koba1.bedwars.holograms;

import org.bukkit.Location;

public interface HologramFactory {

  Hologram createHologram(Location location, String hologramName);

}
