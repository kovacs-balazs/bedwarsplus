package me.koba1.bedwars.commands.subcmds.bedwars;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ShopCommand implements SubCommand {
    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public String getDescription() {
        return "Reload all configuration files";
    }

    @Override
    public String getSyntax() {
        return "/bedwars shop";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.admin.shop";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    private static Main m = Main.getPlugin(Main.class);

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p) {
            BedwarsPlayer.getPlayer(p).setSelectedShopCategory(ShopCategoryManager.getInstance().getQuickBuyCategory());
            p.openInventory(ShopCategoryManager.getInstance().getQuickBuyCategory().getInventory(p));
        }
    }
}
