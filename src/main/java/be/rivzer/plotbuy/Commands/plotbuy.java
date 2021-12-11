package be.rivzer.plotbuy.Commands;

import be.rivzer.plotbuy.Config.Config;
import be.rivzer.plotbuy.Helpers.WorldGuardUtils;
import be.rivzer.plotbuy.Inventorys.SellGui;
import be.rivzer.plotbuy.Logger;
import be.rivzer.plotbuy.Main;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class plotbuy implements CommandExecutor {

    private Main plugin;

    public plotbuy(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("plotbuy").setExecutor(this);
        plugin.getCommand("plotbuy").setTabCompleter(new plotbuy_tab());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if(cmd.getName().equalsIgnoreCase("plotbuy")){
            Player p = (Player) sender;

            if(args.length == 0){
                if(p.hasPermission("plotbuy.help")){
                    p.sendMessage(Logger.color("&f---------------------"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy help &f- &3Bekijk het help menu"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy sell &f- &3Verkoop je plot waar je op staat"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy quicksell &f- &3Quicksell je plot"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy buy &f- &3Open de gui om plots te kopen"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy setprice &f- &3Zet een prijs op een plot"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy info &f- &3Krijg informatie over deze plugin"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&f---------------------"));
                }
                else{
                    p.sendMessage(Logger.color("&f---------------------"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy help &f- &3Bekijk het help menu"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy sell &f- &3Laat je aantal blokken ver weten aan je contacten"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy buy &f- &3Open de gui om plots te kopen"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy info &f- &3Krijg informatie over deze plugin"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&f---------------------"));
                }
            }
            else if(args[0].equalsIgnoreCase("help")){
                if(p.hasPermission("plotbuy.help")){
                    p.sendMessage(Logger.color("&f---------------------"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy help &f- &3Bekijk het help menu"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy sell &f- &3Verkoop je plot waar je op staat"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy quicksell &f- &3Quicksell je plot"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy buy &f- &3Open de gui om plots te kopen"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy setprice &f- &3Zet een prijs op een plot"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy info &f- &3Krijg informatie over deze plugin"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&f---------------------"));
                }
                else{
                    p.sendMessage(Logger.color("&f---------------------"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy help &f- &3Bekijk het help menu"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy sell &f- &3Laat je aantal blokken ver weten aan je contacten"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy buy &f- &3Open de gui om plots te kopen"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&b/plotbuy info &f- &3Krijg informatie over deze plugin"));
                    p.sendMessage("");
                    p.sendMessage(Logger.color("&f---------------------"));
                }
            }
            else if(args[0].equalsIgnoreCase("info")){
                p.sendMessage(Logger.color("&f---------------------"));
                p.sendMessage(Logger.color("&bKoop deze plugin hier &fhttps://discord.gg/qqxfX5EyzD"));
                p.sendMessage(Logger.color("&bDeze plugin is gemaak door &f&lRivzer"));
                p.sendMessage(Logger.color("&bVersion: &f&l1.1"));
                p.sendMessage(Logger.color("&f---------------------"));
                return true;
            }
            else if(args[0].equalsIgnoreCase("buy")){
                p.closeInventory();
                try {
                    SellGui.openSellGui(p, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(args[0].equalsIgnoreCase("sell")){

                if(args.length == 1){
                    p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeefBedragOp")));
                    return true;
                }

                ApplicableRegionSet regions = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());

                if(!WorldGuardUtils.plotExists(p.getLocation())){
                    p.sendMessage(Logger.color(Config.getCustomConfig3().getString("StaatNietOpPlot")));
                    return true;
                }

                for(ProtectedRegion r : regions){
                    String uuid = r.getOwners().getUniqueIds().toString().replace("[" , "").replace("]", "");

                    if(uuid == null) return true;

                    if(uuid.equalsIgnoreCase(String.valueOf(p.getUniqueId()))){
                        String plotName = r.getId();
                        List<String> list = Config.getCustomConfig2().getStringList("Plots");

                        if(list.contains(plotName) && Config.getCustomConfig2().getString("PlotsInfo" + plotName + ".Tekoop") == "true"){
                            p.sendMessage(Logger.color(Config.getCustomConfig3().getString("PlotStaatAlTekoop")));
                            return true;
                        }
                        else{
                            if(!list.contains(plotName)){
                                list.add(String.valueOf(plotName));
                            }
                            Config.getCustomConfig2().set("Plots", list);
                            Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Name", plotName);
                            Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Price", Integer.parseInt(args[1]));
                            Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Eigenaar", p.getName());
                            Config.getCustomConfig2().set("PlotsInfo." + plotName + ".UUID", p.getUniqueId().toString());
                            Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Tekoop", true);
                            Config.saveConfig2();

                            String message = Logger.color(Config.getCustomConfig3().getString("PlotVerkocht"));
                            message = message.replace("%bedrag%", args[1]);
                            p.sendMessage(message);
                        }
                        return true;
                    }
                    else {
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeenEigenaar")));
                        return true;
                    }
                }

            }
            else if(args[0].equalsIgnoreCase("setprice")){
                if(p.hasPermission("plotbuy.setprice")){
                    if(args.length == 1){
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeefPlotOp")));
                        return true;
                    }
                    else if(args.length == 2){
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeefBedragOp")));
                        return true;
                    }
                    else if(args.length == 3){
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("TekoopOfNiet")));
                        return true;
                    }
                    else if(args.length == 4){
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("LeegOfNiet")));
                        return true;
                    }

                    List<String> list = Config.getCustomConfig2().getStringList("Plots");

                    if(WorldGuardUtils.plotExistsName(p, args[1]) != null){
                        if(!list.contains(args[1])){
                            list.add(String.valueOf(args[1]));
                            Config.getCustomConfig2().set("Plots", list);
                        }

                        RegionContainer container = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionContainer();
                        RegionManager regions = container.get(p.getLocation().getWorld());
                        ProtectedRegion region = regions.getRegion(args[1]);
                        String uuid = region.getOwners().getUniqueIds().toString().replace("[" , "").replace("]", "");

                        Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Name", args[1]);

                        if(args[4].equalsIgnoreCase("true")){
                            region.getOwners().clear();
                            region.getMembers().clear();
                            Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".UUID", null);
                            Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Eigenaar", "Gemeente");
                        }
                        else{
                            if(region.getOwners().size() == 0){
                                Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Eigenaar", "Gemeente");
                            }
                            else {
                                if(Config.getCustomConfig2().getString("PlotsInfo." + args[1] + ".Eigenaar") == null){
                                    //LATER GOEDE USERNAM
                                    Player pp = plugin.getServer().getPlayer(UUID.fromString(uuid));
                                    if(pp == null){
                                        Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Eigenaar", uuid);
                                    }
                                    else{
                                        Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Eigenaar", pp.getName());
                                    }
                                }
                                if(Config.getCustomConfig2().getString("PlotsInfo." + args[1] + ".UUID") == null){
                                    Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".UUID", uuid);
                                }
                            }
                        }

                        if(args[3].equalsIgnoreCase("true")){
                            Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Tekoop", true);
                        }
                        else {
                            Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".Tekoop", false);
                        }

                        Config.getCustomConfig2().set("PlotsInfo." + args[1] + ".StandaartPrijs", Integer.parseInt(args[2]));

                        Config.saveConfig2();

                        String message = Logger.color(Config.getCustomConfig3().getString("PrijsGezet"));
                        message = message.replace("%plot%", args[1]);
                        message = message.replace("%bedrag%", args[2]);
                        p.sendMessage(message);
                    }
                    else{
                        String message = Logger.color(Config.getCustomConfig3().getString("PlotBestaatNiet"));
                        message = message.replace("%plot%", args[1]);
                        message = message.replace("%bedrag%", args[2]);
                        p.sendMessage(message);
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("quicksell")){
                ApplicableRegionSet regions = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());

                if(!WorldGuardUtils.plotExists(p.getLocation())){
                    p.sendMessage(Logger.color(Config.getCustomConfig3().getString("StaatNietOpPlot")));
                    return true;
                }

                for(ProtectedRegion r : regions){
                    String uuid = r.getOwners().getUniqueIds().toString().replace("[" , "").replace("]", "");

                    if(uuid == null) return true;

                    if(uuid.equalsIgnoreCase(String.valueOf(p.getUniqueId()))){
                        String plotName = r.getId();
                        List<String> list = Config.getCustomConfig2().getStringList("Plots");

                        if(Config.getCustomConfig2().getString("PlotsInfo." + plotName + ".StandaartPrijs") == null){
                            String message = Config.getCustomConfig3().getString("GeenStandaartPrijs");
                            message = message.replace("%plot%", plotName);
                            p.sendMessage(Logger.color(message));
                            return true;
                        }

                        int nuPrijs = Config.getCustomConfig2().getInt("PlotsInfo." + plotName + ".StandaartPrijs");
                        int hvlPrcTerug = Config.getCustomConfig1().getInt("KrijgHoeveelProcentTerugBijQuickSell");
                        double krijgPrijs = (nuPrijs * hvlPrcTerug)/100;

                        EconomyResponse res = Main.getEconomy().depositPlayer(p, krijgPrijs);
                        if(res.transactionSuccess()){
                            try {
                                WorldGuardUtils.geefPlotAanGemeente(p, plotName);

                                String message = Config.getCustomConfig3().getString("PlotVerkochtAanGemeente");
                                message = message.replace("%plot%", plotName);
                                message = message.replace("%bedrag%", String.valueOf(krijgPrijs));

                                p.sendMessage(Logger.color(message));

                                if(!list.contains(plotName)){
                                    list.add(plotName);
                                }

                                Config.getCustomConfig2().set("Plots", list);
                                Config.getCustomConfig2().set("PlotsInfo." + plotName + ".UUID", null);
                                Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Eigenaar", "Gemeente");
                                Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Price", null);
                                Config.getCustomConfig2().set("PlotsInfo." + plotName + ".Tekoop", true);
                                Config.saveConfig2();
                                return true;
                            } catch (InterruptedException e) {
                                p.sendMessage(Logger.color("&cEr ging iets fout bij het quicksellen van plot &7" + plotName));
                                e.printStackTrace();
                                return true;
                            }
                        }
                        else{
                            p.sendMessage(Logger.color(Config.getCustomConfig3().getString("TransactieFoutQuickSell")));
                            return true;
                        }
                    }
                    else {
                        p.sendMessage(Logger.color(Config.getCustomConfig3().getString("GeenEigenaar")));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
