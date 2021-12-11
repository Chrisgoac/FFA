package elpupas2015.ffa;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;
	public String rutaConfig;
	PluginDescriptionFile pdffile = getDescription();
	public String version = pdffile.getVersion();
	private FileConfiguration stats = null;
	private File statsFile = null;
	private FileConfiguration kit = null;
	private File kitFile = null;
	
	public static String pr = ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» ");
	
	public static ArrayList<Player> Insc = new ArrayList<Player>();
	public static ArrayList<Player> InCombat = new ArrayList<Player>();
	
	private ArrayList<String> players;
	
	public void addPlayer(Player p) {
		players.add(p.getName());
	}
	
	public void removePlayer(Player p) {
		players.remove(p.getName());
	}
	
	public boolean playerIsInList(Player p) {
		if(players.contains(p.getName())) {
			return true;
		}else {
			return false;
		}
	}
	
	public void onEnable() {
	    plugin = this;
		Bukkit.getConsoleSender().sendMessage("§7----------------------------------"); 
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§6               FFA");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§eCreated by: §cELPUPAS2015");
		Bukkit.getConsoleSender().sendMessage("§eVersion: §c"+version);
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§aThe plugin was succefully activated");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§7----------------------------------");
		ScoreboardAdmin sco = new ScoreboardAdmin(this);
		sco.createScoreboard(Integer.valueOf(getConfig().getString("Scoreboard.ticks")));
		players = new ArrayList<String>();
		rc();
		re();
		rC();
		registerStats();
		registerKit();
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7----------------------------------"); 
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§6               FFA");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§eCreated by: §cELPUPAS2015");
		Bukkit.getConsoleSender().sendMessage("§eVersion: §c"+version);
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§cThe plugin was succefully disabled");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§7----------------------------------");
	}
	
	public void rc() {
		this.getCommand("ffa").setExecutor(new FFACommand(this));
	}
	
	public void re() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Events(this), (this));
	}

	public void rC() {
		File config = new File(this.getDataFolder(),"config.yml");
		rutaConfig = config.getPath();
		if(!config.exists()) {
			this.getConfig().options().copyDefaults(true);
		}
		saveConfig();
	}
	public FileConfiguration getStats(){
        if(stats == null){
            reloadStats();
        }
        return stats;
    }
   
    public void reloadStats(){
        if(stats == null){
            statsFile = new File(getDataFolder(),"stats.yml");
        }
        stats = YamlConfiguration.loadConfiguration(statsFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("stats.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                stats.setDefaults(defConfig);
            }          
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
   
    public void saveStats(){
        try{
            stats.save(statsFile);           
        }catch(IOException e){
            e.printStackTrace();
        }
    }
   
    public void registerStats(){
        statsFile = new File(this.getDataFolder(),"stats.yml");
        if(!statsFile.exists()){
            this.getStats().options().copyDefaults(true);
            saveStats();
        }
    }
    
    public FileConfiguration getKit(){
        if(kit == null){
            reloadKit();
        }
        return kit;
    }
   
    public void reloadKit(){
        if(kit == null){
            kitFile = new File(getDataFolder(),"kit.yml");
        }
        kit = YamlConfiguration.loadConfiguration(kitFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("kit.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                kit.setDefaults(defConfig);
            }          
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
   
    public void saveKit(){
        try{
            kit.save(kitFile);           
        }catch(IOException e){
            e.printStackTrace();
        }
    }
   
    public void registerKit(){
        kitFile = new File(this.getDataFolder(),"kit.yml");
        if(!kitFile.exists()){
            this.getKit().options().copyDefaults(true);
            saveKit();
        }
    }
}
