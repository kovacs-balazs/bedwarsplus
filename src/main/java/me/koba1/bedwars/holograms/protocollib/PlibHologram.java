package me.koba1.bedwars.holograms.protocollib;

import me.koba1.bedwars.holograms.AbstractHologram;
import me.koba1.bedwars.holograms.HologramLine;
import org.bukkit.Location;

public class PlibHologram extends AbstractHologram {
  public PlibHologram(Location location, String name) {
    super(location, name);
  }

  @Override
  protected HologramLine createLine(Location location) {
    return new PlibHologramLine(location);
  }
}
