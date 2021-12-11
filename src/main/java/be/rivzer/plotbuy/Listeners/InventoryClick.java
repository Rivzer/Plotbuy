package be.rivzer.plotbuy.Listeners;

import be.rivzer.plotbuy.Config.Config;
import be.rivzer.plotbuy.Helpers.WorldGuardUtils;
import be.rivzer.plotbuy.Inventorys.SellGui;
import be.rivzer.plotbuy.Logger;
import be.rivzer.plotbuy.Main;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) throws InterruptedException {
        if(e.getClickedInventory() == null) return;

        if(e.getClickedInventory().getName().startsWith(Logger.color("&bPlot Verkoop &7- &bPage: &3"))){
            e.setCancelled(true);

            ItemStack is = e.getCurrentItem();
            if(is == null) return;
            if(is.getItemMeta() == null) return;
            if(is.getItemMeta().getDisplayName() == null) return;

            final String itemName = ChatColor.stripColor(is.getItemMeta().getDisplayName());
            Player p = (Player) e.getWhoClicked();

            if(is.getType().equals(Material.SKULL_ITEM)) {
                if(e.getAction() == InventoryAction.PICKUP_ALL){
                    String uuid = "";

                    if(Config.getCustomConfig2().getString("PlotsInfo." + itemName + ".UUID") != null){
                        uuid = Config.getCustomConfig2().getString("PlotsInfo." + itemName + ".UUID");
                    }
                    else {
                        uuid = "NULL";
                    }
                    
                    if(uuid.equalsIgnoreCase(String.valueOf(p.getUniqueId()))){
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("EigenPlotNietKopen")));
                        return;
                    }
                    else {
                        int prijs = Config.getCustomConfig2().getInt("PlotsInfo." + itemName + ".Price");
                        if(prijs <= 0){
                            prijs = Config.getCustomConfig2().getInt("PlotsInfo." + itemName + ".StandaartPrijs");
                        }

                        if(prijs <= 0){
                            p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeenPrijs")));
                            return;
                        }
                        if(Main.getEconomy().getBalance(p) < prijs){
                            String message = Config.getCustomConfig3().getString("NietGenoegGeld");
                            message = message.replace("%plot%", itemName);
                            p.sendMessage(Logger.color(message));
                            return;
                        }
                        else{
                            if(WorldGuardUtils.plotExistsName(p, itemName) == null){
                                p.sendMessage(Logger.color(Config.getCustomConfig3().getString("DitPlotBestaatNiet")));
                                return;
                            }

                            EconomyResponse r = Main.getEconomy().withdrawPlayer(p, prijs);
                            EconomyResponse pr = Main.getEconomy().depositPlayer(Config.getCustomConfig2().getString("PlotsInfo." + itemName + ".Eigenaar"), prijs);
                            if(r.transactionSuccess()){
                                if(pr.transactionSuccess()){
                                    String message = Config.getCustomConfig3().getString("PlotGekocht");
                                    message = message.replace("%plot%", itemName);
                                    p.sendMessage(Logger.color(message));

                                    WorldGuardUtils.transferPlot(p, itemName);

                                    p.closeInventory();
                                    return;
                                }
                                else {
                                    String message = Config.getCustomConfig3().getString("ErGingIetsFoutVerzenden");
                                    message = message.replace("%plot%", itemName);
                                    p.sendMessage(Logger.color(message));

                                    EconomyResponse f = Main.getEconomy().depositPlayer(p, prijs);
                                    if(f.transactionSuccess()){
                                        String message2 = Config.getCustomConfig3().getString("GeldTerugOntvangen");
                                        message2 = message2.replace("%plot%", itemName);
                                        message2 = message2.replace("%prijs%", String.valueOf(prijs));
                                        p.sendMessage(Logger.color(message2));
                                    }
                                    return;
                                }
                            }
                            else{
                                String message = Config.getCustomConfig3().getString("ErGingIetsFoutEraf");
                                message = message.replace("%plot%", itemName);
                                p.sendMessage(Logger.color(message));
                                return;
                            }
                        }
                    }
                }
            }
            else if(is.getType().equals(Material.BARRIER)){
                p.closeInventory();
            }
            else if(is.getType().equals(Material.ARROW)){
                String getPage = e.getClickedInventory().getName().replace(Logger.color("&bPlot Verkoop &7- &bPage: &3"), "");
                int page = Integer.parseInt(getPage);

                if(is.getItemMeta().getDisplayName().equalsIgnoreCase(Logger.color("&3Next Page"))){
                    page++;
                    SellGui.openSellGui(p, page);
                }
                else if(is.getItemMeta().getDisplayName().equalsIgnoreCase(Logger.color("&3Prev Page"))){
                    if(page == 1 ) return;
                    page--;
                    SellGui.openSellGui(p, page);
                }
            }
        }
    }
}
