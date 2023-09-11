package me.koba1.bedwars.configs.messages;

import lombok.Getter;
import me.koba1.bedwars.shopgui.ShopItem;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageValue {

    @Getter private String message;
    @Getter private String defaultMessage;
    @Getter private List<String> listMessage;
    @Getter private List<String> defaultListMessage;
    private final String section;
    @Getter private final String[] comments;

    public MessageValue(String section, String message, String... comments) {
        this.section = section;
        this.message = message;
        this.defaultMessage = message;
        this.comments = comments.clone();
    }

    public MessageValue(String section, List<String> listMessage, String... comments) {
        this.section = section;
        this.defaultListMessage = listMessage;
        this.listMessage = listMessage;
        this.comments = comments.clone();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMessageObject() {
        if(isString())
            return this.message;
        return this.listMessage;
    }

    public void setListMessage(List<String> list) {
        this.listMessage = list;
    }

    public boolean isString() {
        return this.listMessage == null;
    }

    private boolean hasSection() {
        return this.section != null;
    }

    public boolean hasComment() {
        return this.comments != null;
    }

    public String getSection() {
        return this.section;
    }

    public static class PlayerMessage {
        private BedwarsPlayer player;
        private String message;
        private List<String> listMessage;

        public PlayerMessage(String message) {
            this.message = message;
        }

        public PlayerMessage(BedwarsPlayer player, String message) {
            this.message = message;
            this.player = player;
        }

        public PlayerMessage(List<String> listMessage) {
            this.listMessage = listMessage;
        }

        public PlayerMessage(BedwarsPlayer player, List<String> listMessage) {
            this.player = player;
            this.listMessage = listMessage;
        }

        public PlayerMessage setPlayer(Player p) {
            return replace("%player%", p.getName());
        }

        public PlayerMessage setPlayer(String name) {
            return replace("%player%", name);
        }

        public PlayerMessage setBedwarsLevel(int level) {
            return replace("%bedwars_level%", level + "");
        }

        public PlayerMessage setBedwarsLevelFormatted(String format) {
            return replace("%bedwars_level_formatted%", format);
        }

        public PlayerMessage setRank(String rank) {
            return replace("%rank%", rank);
        }

        public PlayerMessage papi(Player p) {
            this.listMessage = Formatter.format(this.listMessage).papi(p).list();
            return this;
        }

        public PlayerMessage setCurrency(ShopItem.CostCurrency currency) {
            return replace("%currency%", Formatter.format(currency.name()).toFirstUpper().string());
        }

        public PlayerMessage setCost(int cost) {
            return replace("%cost%", cost + "");
        }

        public PlayerMessage setTeamColor(String color) {
            return replace("%team_color%", color);
        }

        public PlayerMessage setAmount(int amount) {
            return replace("%amount%", amount + "");
        }
        public PlayerMessage setItem(String item) {
            return replace("%item%", item + "");
        }

        private PlayerMessage replace(String target, String replacement) {
            if(isString())
                this.message = this.message.replace(target, replacement);
            else {
                this.listMessage = Formatter.format(this.listMessage).replace(target, replacement).list();
            }
            return this;
        }

        public PlayerMessage setTime(int time){
            return replace("%time%", time + "");
        }

        public PlayerMessage setTime(long diff){
            return replace("%time%", Formatter.getRemaining(diff,true));
        }

        public PlayerMessage setCurrentPlayer(int player){
            return replace("%current_player%", String.valueOf(player));
        }
        public PlayerMessage setMaxPlayer(int player){
            return replace("%max_player%", String.valueOf(player));
        }
        public PlayerMessage setEnemyColor(String color){
            return replace("%enemy_color%", color);
        }
        public PlayerMessage setKiller(String name){
            return replace("%killer%", name);
        }
        public PlayerMessage setKiller(Player p){
            return replace("%killer%", p.getName());
        }




        public void send() {
            if(this.player == null) return;
            Player player = Bukkit.getPlayer(this.player.getUUID());
            if(player != null)
                player.sendMessage(queue());
        }

        public String queue() {
            return Formatter.applyColor(this.message);
        }

        public List<String> queueList() {
            return Formatter.format(this.listMessage).applyColor().list();
        }

        public String[] queueArray() {
            return Formatter.format(this.listMessage).applyColor().list().toArray(new String[0]);
        }

        private boolean isString() {
            return this.listMessage == null;
        }
    }
}
