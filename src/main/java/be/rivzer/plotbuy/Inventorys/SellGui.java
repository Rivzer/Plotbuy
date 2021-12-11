package be.rivzer.plotbuy.Inventorys;

import be.rivzer.plotbuy.Config.Config;
import be.rivzer.plotbuy.Logger;
import be.rivzer.plotbuy.Main;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.naming.InterruptedNamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellGui {

    private static Main plugin = (Main) Main.getPlugin(Main.class);

    public static void openSellGui(Player p, int page) throws InterruptedException {
        Inventory inv = Bukkit.createInventory(p, 45, Logger.color("&bPlot Verkoop &7- &bPage: &3" + page));

        List<String> itemlijst2 = Config.getCustomConfig2().getStringList("Plots");
        String[] items2 = (String[])itemlijst2.toArray(new String[0]);
        String[] var12 = items2;

        int pageRangeMin = page*27-27;
        if (pageRangeMin < 0) pageRangeMin = 0;
        int pageRangeMax = page*27;
        int counter = 0;

        for(int i = pageRangeMin; i < pageRangeMax; i++){
            try {
                String naam = var12[i];
                if(Config.getCustomConfig2().getString("PlotsInfo." + naam + ".Tekoop") == "true"){
                    setItemSkull(inv, Material.SKULL_ITEM, counter, naam, p);

                    counter++;
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        int o = 26;
        for (int i = 0; i < 9; i++){
            o++;
            setGlass(inv, Material.STAINED_GLASS_PANE, o, " ");
        }

        setItem(inv, Material.BARRIER, 40, "&4&lClose");

        if(page > 1){
            setItem(inv, Material.ARROW, 36, "&3Prev Page");
        }
        if(page < 99){
            setItem(inv, Material.ARROW, 44, "&3Next Page");
        }

        p.openInventory(inv);
    }

    public static void setItem(Inventory inv, Material mat, Integer slotnum, String name){
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Logger.color(name));
        item.setItemMeta(meta);

        inv.setItem(slotnum, item);
    }

    public static void setGlass(Inventory inv, Material mat, Integer slotnum, String name){
        ItemStack item = new ItemStack(mat, 1, (short) 0, (byte) 9);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Logger.color(name));
        item.setItemMeta(meta);

        inv.setItem(slotnum, item);
    }

    public static void setItemSkull(Inventory inv, Material mat, Integer slotnum, String naam, Player p){
        String plotNaam = Config.getCustomConfig2().getString("PlotsInfo." + naam + ".Name");
        String Eigenaarnaam = "";

        if(Config.getCustomConfig2().getString("PlotsInfo." + naam + ".Eigenaar") != null){
            Eigenaarnaam = Config.getCustomConfig2().getString("PlotsInfo." + naam + ".Eigenaar");
        }
        int prijs;
        String uuid;

        if(Eigenaarnaam.equalsIgnoreCase("Gemeente")){
            String skullNaam = Config.getCustomConfig1().getString("GemeenteVerkoopSkullNaam");
            prijs = Config.getCustomConfig2().getInt("PlotsInfo." + plotNaam + ".StandaartPrijs");
            uuid = Config.getCustomConfig1().getString("GemeenteVerkoopSkullUUID");

            ItemStack head = new ItemStack(mat, 1, (short) 3);
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(Logger.color("&f&l" + plotNaam));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Logger.color("&6Eigenaar: &7" + Eigenaarnaam));
            lore.add(Logger.color("&6Plot Naam: &7" + plotNaam));
            lore.add(Logger.color("&6Prijs: &7€" + prijs));

            meta.setLore(lore);
            head.setItemMeta(meta);

            NBTItem nbti = new NBTItem(head);
            NBTCompound skull = nbti.addCompound("SkullOwner");
            skull.setString("Name", skullNaam);
            skull.setString("Id", uuid);
            nbti.setString("mtcustom", plotNaam);

            head = nbti.getItem();

            inv.setItem(slotnum, head);
        }
        else {
            prijs = Config.getCustomConfig2().getInt("PlotsInfo." + naam + ".Price");
            if(prijs <= 0){
                prijs = Config.getCustomConfig2().getInt("PlotsInfo." + naam + ".StandaartPrijs");
            }
            uuid = Config.getCustomConfig2().getString("PlotsInfo." + plotNaam + ".UUID");

            if(Eigenaarnaam.length() > 1){
                Player pp = plugin.getServer().getPlayer(UUID.fromString(uuid));
                if(pp != null){
                    Eigenaarnaam = pp.getName();
                }
            }

            ItemStack head = new ItemStack(mat, 1, (short) 3);
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(Logger.color("&f&l" + plotNaam));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Logger.color("&6Eigenaar: &7" + Eigenaarnaam));
            lore.add(Logger.color("&6Plot Naam: &7" + plotNaam));
            lore.add(Logger.color("&6Prijs: &7€" + prijs));
            if(uuid.equalsIgnoreCase(String.valueOf(p.getUniqueId()))){
                lore.add(Logger.color("&cDit plot is van u"));
            }

            meta.setLore(lore);
            head.setItemMeta(meta);

            NBTItem nbti = new NBTItem(head);
            NBTCompound skull = nbti.addCompound("SkullOwner");
            skull.setString("Name", Eigenaarnaam);
            skull.setString("Id", uuid);
            nbti.setString("mtcustom", plotNaam);

            head = nbti.getItem();

            inv.setItem(slotnum, head);
        }
    }

}
