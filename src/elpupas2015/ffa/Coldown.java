package elpupas2015.ffa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class Coldown {
	int taskID;
	int time;
	private Player p;
	
	private Main plugin;
	
	public Coldown(Main plugin, int time, Player p) {
		this.plugin = plugin;
		this.time = time;
		this.p = p;
	}
	
	public void execution() {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskID = sh.scheduleSyncRepeatingTask(plugin,new Runnable() {
			public void run() {
				if(time == 0) {
					Bukkit.getScheduler().cancelTask(taskID);
					p.sendMessage(Main.pr + "§7Modo anticlean §cdesactivado§7.");
					plugin.removePlayer(p);
					return;
				}else {
					time--;
				}
			}
		},0L,20);
	}
}
