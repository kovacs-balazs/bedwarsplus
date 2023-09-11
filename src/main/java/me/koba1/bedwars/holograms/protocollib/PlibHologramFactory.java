package me.koba1.bedwars.holograms.protocollib;

import me.koba1.bedwars.holograms.Hologram;
import me.koba1.bedwars.holograms.HologramFactory;
import org.bukkit.Location;

public class PlibHologramFactory implements HologramFactory {
  @Override
  public Hologram createHologram(Location location, String hologramName) {
    return new PlibHologram(location, hologramName);
  }
}
