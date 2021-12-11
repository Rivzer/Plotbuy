package be.rivzer.plotbuy.Config;

import be.rivzer.plotbuy.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    static Main plugin = Main.getPlugin(Main.class);

    public static File customConfigFile1;
    private static FileConfiguration customConfig1;

    public static FileConfiguration getCustomConfig1() {
        return customConfig1;
    }

    public static void createCustomConfig1() {
        customConfigFile1 = new File(plugin.getDataFolder(), "config.yml");
        if (!customConfigFile1.exists()) {
            customConfigFile1.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        customConfig1 = new YamlConfiguration();

        try {
            customConfig1.load(customConfigFile1);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig1() {
        File file = new File("plugins//PlotBuy//config.yml");
        try {
            Config.getCustomConfig1().save(file);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    public static File customConfigFile2;
    private static FileConfiguration customConfig2;

    public static FileConfiguration getCustomConfig2() {
        return customConfig2;
    }

    public static void createCustomConfig2() {
        customConfigFile2 = new File(plugin.getDataFolder(), "data.yml");
        if (!customConfigFile2.exists()) {
            customConfigFile2.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }

        customConfig2 = new YamlConfiguration();

        try {
            customConfig2.load(customConfigFile2);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig2() {
        File file = new File("plugins//PlotBuy//data.yml");
        try {
            Config.getCustomConfig2().save(file);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    public static File customConfigFile3;
    private static FileConfiguration customConfig3;

    public static FileConfiguration getCustomConfig3() {
        return customConfig3;
    }

    public static void createCustomConfig3() {
        customConfigFile3 = new File(plugin.getDataFolder(), "language.yml");
        if (!customConfigFile3.exists()) {
            customConfigFile3.getParentFile().mkdirs();
            plugin.saveResource("language.yml", false);
        }

        customConfig3 = new YamlConfiguration();

        try {
            customConfig3.load(customConfigFile3);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig3() {
        File file = new File("plugins//PlotBuy//language.yml");
        try {
            Config.getCustomConfig3().save(file);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

}
