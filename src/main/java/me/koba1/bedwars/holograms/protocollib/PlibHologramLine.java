package me.koba1.bedwars.holograms.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.koba1.bedwars.holograms.HologramLine;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlibHologramLine implements HologramLine {

  private final UUID entityUid;
  private final int entityId;
  private Location location;
  private String text;

  public PlibHologramLine(Location location) {
    this.location = location;
    // Could be safer but this is probably fine
    this.entityId = ThreadLocalRandom.current().nextInt();
    this.entityUid = UUID.randomUUID();
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void showTo(Player player) {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.sendServerPacket(player, createAddPacket());
    protocolManager.sendServerPacket(player, createDataPacket());
  }

  @Override
  public void hideFrom(Player player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, createRemovePacket());
  }

  @Override
  public void teleport(Location location) {
    this.location = location;
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createMovePacket());
  }

  @Override
  public void setText(String text) {
    this.text = text;
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createDataPacket());
  }

  private PacketContainer createAddPacket() {
    PacketType type = PacketType.Play.Server.SPAWN_ENTITY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    StructureModifier<Integer> intMod = packet.getIntegers();
    StructureModifier<EntityType> typeMod = packet.getEntityTypeModifier();
    StructureModifier<UUID> uuidMod = packet.getUUIDs();
    StructureModifier<Double> doubleMod = packet.getDoubles();

    // Write id of entity
    intMod.write(0, this.entityId);

    // Write type of entity
    typeMod.write(0, EntityType.ARMOR_STAND);

    // Write entities UUID
    uuidMod.write(0, this.entityUid);

    // Write position
    doubleMod.write(0, location.getX());
    doubleMod.write(1, location.getY());
    doubleMod.write(2, location.getZ());

    return packet;
  }

  private PacketContainer createDataPacket() {
    PacketType type = PacketType.Play.Server.ENTITY_METADATA;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, this.entityId);

    WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer(true);
    WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);

    List<WrappedDataValue> dataValues = new ArrayList<>();

    Byte flags = 0x20;
/*    List<WrappedDataWatcher.WrappedDataWatcherObject> watchers = new ArrayList<>();
    WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(0, byteSerializer);
    WrappedDataWatcher.WrappedDataWatcherObject object1 = new WrappedDataWatcher.WrappedDataWatcherObject(0, chatSerializer);
    WrappedDataWatcher.WrappedDataWatcherObject object2 = new WrappedDataWatcher.WrappedDataWatcherObject(0, boolSerializer);
    WrappedDataWatcher.WrappedDataWatcherObject object3 = new WrappedDataWatcher.WrappedDataWatcherObject(0, byteSerializer);
    watchers.add(object);
    watchers.add(object1);
    watchers.add(object2);
    watchers.add(object3);

    dataValues.add(new WrappedDataValue(0, byteSerializer, flags));

    Optional<?> optChat = Optional.of(WrappedChatComponent.fromChatMessage(this.text.replace("&", "§"))[0].getHandle());
    dataValues.add(new WrappedDataValue(2, chatSerializer, optChat));

    Boolean nameVisible = true;
    dataValues.add(new WrappedDataValue(3, boolSerializer, nameVisible));

    Byte armorStandTypeFlags = 0x10;
    dataValues.add(new WrappedDataValue(15, byteSerializer, armorStandTypeFlags));

    packet.getDataValueCollectionModifier().write(0, dataValues);*/
    WrappedDataWatcher metadata = new WrappedDataWatcher();
    Optional<?> optChat = Optional.of(WrappedChatComponent.fromChatMessage(this.text.replace("&", "§"))[0].getHandle());
    metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
    metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optChat);
    metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
    metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));
    //metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optChat);
    //packet.getDataWatcherModifier().write(0);
   // packet.getDataWatcherModifier().write(0, metadata);
    packet.getWatchableCollectionModifier().write(0, metadata.getWatchableObjects());
    return packet;
  }

  private PacketContainer createMovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_TELEPORT;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, entityId);

    StructureModifier<Double> doubleMod = packet.getDoubles();
    doubleMod.write(0, this.location.getX());
    doubleMod.write(1, this.location.getY());
    doubleMod.write(2, this.location.getZ());

    return packet;
  }

  private PacketContainer createRemovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_DESTROY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntLists().write(0, IntList.of(this.entityId));

    return packet;
  }

}
