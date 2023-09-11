package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.scoreboard.PlayerScoreboard;
import me.koba1.bedwars.shopgui.ShopCategories;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.shopgui.shopplayer.PlayerHotbar;
import me.koba1.bedwars.shopgui.shopplayer.PlayerQuickBuy;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsPlayerState;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsTeams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BedwarsPlayer {

    @Getter private String name;
    private UUID uuid;
    @Getter @Setter private PlayerStatistics statistics;
    @Getter @Setter private MessageLanguage language;
    @Setter private ShopCategories selectedShopCategory;
    @Getter @Setter private Party currentParty;
    @Getter @Setter private PlayerQuickBuy quickbuyManager;
    @Getter @Setter private PlayerHotbar hotbarManager;

    @Getter @Setter private BedwarsPlayerState bedwarsPlayerState;
    @Getter @Setter private BedwarsArenaManager currentGame;
    @Getter @Setter private BedwarsTeams currentTeam;
    @Getter @Setter private PlayerScoreboard scoreboard;

    public BedwarsPlayer(String name, UUID uuid, MessageLanguage language) {
        this.name = name;
        this.uuid = uuid;
        this.statistics = new PlayerStatistics(this);
        this.language = language;
        this.selectedShopCategory = null;
        this.currentParty = null;
        this.quickbuyManager = new PlayerQuickBuy(this);
        this.hotbarManager = new PlayerHotbar(this);
        this.bedwarsPlayerState = BedwarsPlayerState.OFFLINE;
        this.currentGame = null;

        Main.getBedwarsPlayers().put(uuid, this);
        System.out.println("Created " + name);
    }

    public void join(Player player) {
        this.scoreboard = new PlayerScoreboard(player);
    }

    public static BedwarsPlayer getPlayer(Player p) {
        if (!Main.getBedwarsPlayers().containsKey(p.getUniqueId())) {
            Main.getDataStorage().loadPlayer(p);
        }
        return Main.getBedwarsPlayers().get(p.getUniqueId());
    }

    public static BedwarsPlayer getPlayer(UUID uuid) {
        if (!Main.getBedwarsPlayers().containsKey(uuid)) {
            //Main.getDataStorage().loadPlayer(p);
        }
        return Main.getBedwarsPlayers().get(uuid);
    }

    public boolean isInParty() {
        return this.getCurrentParty() != null;
    }
    public boolean isPartyLeader() {
        return this.getCurrentParty() != null && (this.getCurrentParty().getPartyLeader() == this);
    }
    public void chat(Messages messages) {
        Player player = Bukkit.getPlayer(this.uuid);
        if(player != null)
            player.sendMessage(messages.language(player).queue());
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        if(player != null)
            player.sendMessage(message);
    }

    public ShopCategories getSelectedShopCategory() {
        return this.selectedShopCategory == null ? ShopCategoryManager.getInstance().getQuickBuyCategory() : this.selectedShopCategory;
    }

    public boolean equals(BedwarsPlayer bw) {
        if(this.uuid.toString().equalsIgnoreCase(bw.getUUID().toString()))
            return true;
        return false;
    }

    public boolean isInGame() {
        return currentGame != null;
    }

    public boolean equals(Player player) {
        if(this.uuid.toString().equalsIgnoreCase(BedwarsPlayer.getPlayer(player).getUUID().toString()))
            return true;
        return false;
    }

    public UUID getUUID() {
        return this.uuid;
    }
}
