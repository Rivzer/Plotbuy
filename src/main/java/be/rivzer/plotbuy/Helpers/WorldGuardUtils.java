package be.rivzer.plotbuy.Helpers;

import be.rivzer.plotbuy.Config.Config;
import be.rivzer.plotbuy.Inventorys.SellGui;
import be.rivzer.plotbuy.Logger;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class WorldGuardUtils {
    private static WorldGuardPlugin worldGuard;

    public static void transferPlot(Player p, String regionName) throws InterruptedException {
        RegionContainer container = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionContainer();
        RegionManager regions = container.get(p.getLocation().getWorld());
        ProtectedRegion region = regions.getRegion(regionName);

        region.getOwners().clear();
        region.getMembers().clear();
        region.getOwners().addPlayer(p.getUniqueId());

        List<String> list = Config.getCustomConfig2().getStringList("Plots");
        if(!list.contains(regionName)){
            list.add(regionName);
        }
        Config.getCustomConfig2().set("Plots", list);
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".UUID", p.getUniqueId().toString());
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".Eigenaar", p.getName());
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".Tekoop", false);
        Config.saveConfig2();

        p.closeInventory();
        SellGui.openSellGui(p, 1);
    }

    public static void geefPlotAanGemeente(Player p, String regionName) throws InterruptedException {
        RegionContainer container = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionContainer();
        RegionManager regions = container.get(p.getLocation().getWorld());
        ProtectedRegion region = regions.getRegion(regionName);

        region.getOwners().clear();
        region.getMembers().clear();

        List<String> list = Config.getCustomConfig2().getStringList("Plots");
        if(!list.contains(regionName)){
            list.add(regionName);
        }
        Config.getCustomConfig2().set("Plots", list);
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".UUID", null);
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".Eigenaar", "Gemeente");
        Config.getCustomConfig2().set("PlotsInfo." + regionName + ".Tekoop", false);
        Config.saveConfig2();

        p.closeInventory();
    }

    public static ProtectedRegion plotExistsName(Player p, String regionName){
        RegionContainer container = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionContainer();
        RegionManager regions = container.get(p.getLocation().getWorld());
        ProtectedRegion region = regions.getRegion(regionName);
        return region;
    }

    public static boolean plotExists(Location plotLoc) {
        return worldGuard.getRegionManager(plotLoc.getWorld()).getApplicableRegions(plotLoc).getRegions().size() != 0;
    }

    public static String getRegionName(Location plotLoc) {
        return WorldGuardUtils.plotExists(plotLoc) ? ((ProtectedRegion)worldGuard.getRegionManager(plotLoc.getWorld()).getApplicableRegions(plotLoc).getRegions().iterator().next()).getId() : null;
    }

    static {
        Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin instanceof WorldGuardPlugin) {
            worldGuard = (WorldGuardPlugin)worldGuardPlugin;
        }
    }
}
