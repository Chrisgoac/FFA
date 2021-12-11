package elpupas2015.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardAdmin {
	
	private Main plugin;
	int taskID;
	public ScoreboardAdmin(Main pl) {
		this.plugin = pl;
	}
	
	public void createScoreboard(int ticks) {
		BukkitScheduler sch = Bukkit.getServer().getScheduler();
		taskID = sch.scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = plugin.getConfig();
				for(Player all : Bukkit.getOnlinePlayers()) {
					scoreLobby(all, config);
				}
			}
		}, 0, ticks);
	}
	
	public void scoreLobby(Player p, FileConfiguration config){
        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard b = m.getNewScoreboard();
        Objective o = b.registerNewObjective("ffa", "dummy");
        
        FileConfiguration stats = plugin.getStats();;
        
        o.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("Scoreboard.titulo")));

        Score blackLine = o.getScore("§r§f§m--------------------------");
        blackLine.setScore(10);

        Score user = o.getScore("§cUsuario: §7" + p.getName());
        user.setScore(9);

        Score online = o.getScore("§cOnline: §7" + Bukkit.getServer().getOnlinePlayers().size());
        online.setScore(8);
        
        Score anticlean = o.getScore("§cAnticlean: §7" + getAnticlean(config));
        anticlean.setScore(7);
        
        Score ks = o.getScore("§cKillstreak: §7" + p.getLevel());
        ks.setScore(6);
        
        int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
        Score coins2 = o.getScore("§cCoins: §7" + cs);
        coins2.setScore(5);
        
        int cp = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
        Score bajas = o.getScore("§cBajas: §7" + cp);
        bajas.setScore(4);
        
        int cm = Integer.valueOf(stats.getString("Players." + p.getName() + ".muertes"));
        Score muertes2 = o.getScore("§cMuertes: §7" + cm);
        muertes2.setScore(3);
        
        Score blackLine2 = o.getScore("§f§m--------------------------");
        blackLine2.setScore(2);

        Score ip = o.getScore(ChatColor.translateAlternateColorCodes('&', config.getString("Scoreboard.ip")));
        ip.setScore(1);

        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        p.setScoreboard(b);
    }
	
	public String getAnticlean(FileConfiguration config) {
		if(config.getString("Config.anticlean").equalsIgnoreCase("true")) {
        	return "activado";
        }else {
        	return "desactivado";
        }
	}
}
