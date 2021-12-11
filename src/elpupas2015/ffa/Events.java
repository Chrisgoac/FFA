package elpupas2015.ffa;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import elpupas2015.ffa.ActionBar;

public class Events implements Listener {
	
	private Main plugin;
	public Events(Main pl) {
		this.plugin = pl;
	}
	
	@SuppressWarnings("unchecked")
    public void restoreInventory(Player p) throws IOException {
        //File f = new File(plugin.getDataFolder().getAbsolutePath(), p.getName() + ".yml");
        FileConfiguration c = plugin.getKit();
        List<ItemStack> list = (List<ItemStack>) c.getList("Inventory.armor", new ArrayList<ItemStack>());
        ItemStack[] content = list.toArray(new ItemStack[list.size()]);
        p.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("Inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		FileConfiguration config = plugin.getConfig();
		if(config.getBoolean("Config.join-ffa-on-join")) {
			if(config.contains("Config.Spawn.x")) {
				Main.Insc.add(p);
				try {
					restoreInventory(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				double x = Double.valueOf(config.getString("Config.Spawn.x"));
				double y = Double.valueOf(config.getString("Config.Spawn.y"));
				double z = Double.valueOf(config.getString("Config.Spawn.z"));;
				float yaw = Float.valueOf(config.getString("Config.Spawn.yaw"));
				float pitch = Float.valueOf(config.getString("Config.Spawn.pitch"));
				World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));

				Location l = new Location(world, x, y, z, yaw, pitch);
				p.teleport(l);
			}else {
				p.sendMessage(Main.pr + "§c¡Ha habido un error al entrar al FFA! Hay que colocar el spawn primero.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		FileConfiguration stats = plugin.getStats();
		if(!stats.contains("Players." + p.getName() + ".playerkills")) {
			stats.set("Players." + p.getName() + ".playerkills", 0);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".muertes")) {
			stats.set("Players." + p.getName() + ".muertes", 0);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".coins")) {
			stats.set("Players." + p.getName() + ".coins", 0);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".gp-upgrade")) {
			stats.set("Players." + p.getName() + ".gp-upgrade", false);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".ar-upgrade")) {
			stats.set("Players." + p.getName() + ".ar-upgrade", false);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".ar2-upgrade")) {
			stats.set("Players." + p.getName() + ".ar2-upgrade", false);
			plugin.saveStats();
		}
		if(!stats.contains("Players." + p.getName() + ".ob-upgrade")) {
			stats.set("Players." + p.getName() + ".ob-upgrade", false);
			plugin.saveStats();
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Main.Insc.contains(p)) {
			if(p.getFoodLevel() != 20) {
				p.setFoodLevel(20);	
			}
		}
	}
	
	/*public static void killLeaderBoard(Main pl,Player p) {
	FileConfiguration stats = pl.getStats();
	FileConfiguration config = pl.getConfig();

	Map<String, Integer> userKillsList = new HashMap<>();

	for (String user : stats.getConfigurationSection("Players").getKeys(false)) {
		userKillsList.put(user, stats.getInt("Players." + user + ".playerkills"));
	}

	String user = "";
	Integer userKills = 0;

	for (int i = 1; i < config.getInt("Top.cantidad") +1; i++) {
		for (String playerName : userKillsList.keySet()) {
			if (userKillsList.get(playerName) > userKills) {
				user = playerName;
				userKills = userKillsList.get(playerName);
			}


		}
		Location l = p.getLocation().add(0, -i, 0);
		ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomName("§a#" + i + " §7- §e" + user + " §7- §c" + userKills + " Kills.");
		as.setCustomNameVisible(true);
		as.setVisible(false);

		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
		String world = l.getWorld().getName();
		float yaw = l.getYaw();
		float pitch = l.getPitch();
		config.set("Config.Hologram.world", world);
		config.set("Config.Hologram.x", x);
		config.set("Config.Hologram.y", y);
		config.set("Config.Hologram.z", z);
		config.set("Config.Hologram.yaw", yaw);
		config.set("Config.Hologram.pitch", pitch);
		plugin.saveConfig();

		userKillsList.remove(user);
		user = "";
		userKills = 0;
	}

}*/
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		FileConfiguration stats = plugin.getStats();
		Main.Insc.remove(p);
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0);
		if(Main.InCombat.contains(p)) {
			if(stats.contains("Players." + p.getName()+".muertes")) {
				int cm = Integer.valueOf(stats.getString("Players." + p.getName() + ".muertes"));
				stats.set("Players." + p.getName() + ".muertes", cm+1);
				plugin.saveStats();
        	}else{
        		stats.set("Players." + p.getName() + ".muertes", 1);
        		plugin.saveStats();
        		return;
        	}
			if(stats.contains("Players." + p.getName()+".coins")) {
				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
				stats.set("Players." + p.getName() + ".coins", cs -10);
				plugin.saveStats();
        	}else{
        		stats.set("Players." + p.getName() + ".coins", -10);
        		plugin.saveStats();
        		return;
        	}
			for(Player all : Bukkit.getServer().getOnlinePlayers()) {
				all.sendMessage(Main.pr + "§c" + p.getName() + "§7 ha muerto (-10 Coins) §e(CombatLog)");
			}
		}
	}
	
	@EventHandler
	public void onFall(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(Main.Insc.contains(p)) {
				if(e.getCause() == DamageCause.FALL){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        FileConfiguration stats = plugin.getStats();
        int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
        if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Mejoras - Tienda."))) {
        	if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
        		if(e.getCurrentItem().getItemMeta().getDisplayName() !=null) {
        			if(Main.Insc.contains(p)) {
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lMEJORA: §7Golden apple al matar.")) {
        					if(stats.getBoolean("Players." + p.getName() + ".gp-upgrade")) {
        						p.sendMessage(Main.pr + "§c¡Ya has comprado esta mejora!");
        						e.setCancelled(true);
        					}else {
        						if(cs >= 1000) {
                					p.sendMessage(Main.pr + "§aHas comprado la mejora: §7Golden apple al matar.");
                					stats.set("Players." + p.getName() + ".coins", cs -1000);
                					stats.set("Players." + p.getName() + ".gp-upgrade", true);
                					e.setCancelled(true);
                					plugin.saveStats();
                				}else {
                					p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!");
                					e.setCancelled(true);
                				}
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lMEJORA: §7Inmune a las flechas. (Tier I)")) {
        					if(stats.getBoolean("Players." + p.getName() + ".ar-upgrade")) {
        						p.sendMessage(Main.pr + "§c¡Ya has comprado esta mejora!");
        						e.setCancelled(true);
        					}else {
        						if(cs >= 1000) {
                					p.sendMessage(Main.pr + "§aHas comprado la mejora: §7Inmune a las flechas.");
                					stats.set("Players." + p.getName() + ".coins", cs -1000);
                					stats.set("Players." + p.getName() + ".ar-upgrade", true);
                					e.setCancelled(true);
                					plugin.saveStats();
                					ItemStack bbow = new ItemStack(Material.BOW, 1);
                					ItemMeta bbmeta = bbow.getItemMeta();
                					bbmeta.setDisplayName("§9§lMEJORA: §7Inmune a las flechas. (Tier II)");
                					ArrayList<String> bblist = new ArrayList<String>();
                					bblist.add("§9Descripción: §7Al comprar esta mejora cada vez que te disparen");
                					bblist.add("§7tendrás un 50% de que la flecha no te haga daño.");
                					bblist.add("§9Precio: §72500 coins");
                					bblist.add("§9Calidad: §6Legendaria.");
                					if(stats.getBoolean("Players." + p.getName() + ".ar2-upgrade")) {
                						bblist.add("§9Disponible: §cno");
                					}else {
                						bblist.add("§9Disponible: §así");
                					}
                					bbmeta.setLore(bblist);
                					bbow.setItemMeta(bbmeta);
                					Inventory inv = e.getInventory();
                					inv.setItem(2, bbow);
                				}else {
                					p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!");
                					e.setCancelled(true);
                				}
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lMEJORA: §7Inmune a las flechas. (Tier II)")) {
        					if(stats.getBoolean("Players." + p.getName() + ".ar2-upgrade")) {
        						p.sendMessage(Main.pr + "§c¡Ya has comprado esta mejora!");
        						e.setCancelled(true);
        					}else {
        						if(cs >= 2500) {
                					p.sendMessage(Main.pr + "§aHas comprado la mejora: §7Inmune a las flechas.");
                					stats.set("Players." + p.getName() + ".coins", cs -2500);
                					stats.set("Players." + p.getName() + ".ar2-upgrade", true);
                					e.setCancelled(true);
                					plugin.saveStats();
                				}else {
                					p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!");
                					e.setCancelled(true);
                				}
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lITEM: §7Manzana de oro encantada.")) {
        					if(cs >= 200) {
        						p.sendMessage(Main.pr + "§aHas comprado el item: §7Manzana de oro encantada.");
            					stats.set("Players." + p.getName() + ".coins", cs -200);
            					p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
            					e.setCancelled(true);
        					}else {
        						p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!");
        						e.setCancelled(true);
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lITEM: §7Bloques de obsidiana.")) {
        					if(cs >= 50) {
        						p.sendMessage(Main.pr + "§aHas comprado el item: §7Bloques de obsidiana.");
            					stats.set("Players." + p.getName() + ".coins", cs -50);
            					p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 8));
            					e.setCancelled(true);
        					}else {
        						p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!"); 
        						e.setCancelled(true);
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lMEJORA: §7Bloques de obsidiana al matar.")) {
        					if(stats.getBoolean("Players." + p.getName() + ".ob-upgrade")) {
        						p.sendMessage(Main.pr + "§c¡Ya has comprado esta mejora!");
        						e.setCancelled(true);
        					}else {
        						if(cs >= 500) {
                					p.sendMessage(Main.pr + "§aHas comprado la mejora: §7Bloques de obsidiana al matar.");
                					stats.set("Players." + p.getName() + ".coins", cs -500);
                					stats.set("Players." + p.getName() + ".ob-upgrade", true);
                					e.setCancelled(true);
                					plugin.saveStats();
                				}else {
                					p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!"); 
                					e.setCancelled(true);
                				}
        					}
        				}
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lITEM: §7Enderpearl")) {
        					if(cs >= 100) {
        						p.sendMessage(Main.pr + "§aHas comprado el item: §7Enderpearl.");
            					stats.set("Players." + p.getName() + ".coins", cs -100);
            					p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            					e.setCancelled(true);
        					}else {
        						p.sendMessage(Main.pr + "§c¡No tienes suficientes coins!"); 
        						e.setCancelled(true);
        					}
        				}
        			}else {
        				p.sendMessage(Main.pr + "§c¡No estas dentro del FFA!");
        				e.setCancelled(true);
        			}
           		}
      		}
        }
    }
	
	@EventHandler
    public void onPlayerClickInventoryTopsKills(InventoryClickEvent e) {
        if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops - Kills."))) {
        	e.setCancelled(true);
        }
    }
	
	@EventHandler
    public void onPlayerClickInventoryTopsCoins(InventoryClickEvent e) {
        if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops - Coins."))) {
        	e.setCancelled(true);
        }
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerClickInventoryTops(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        FileConfiguration stats = plugin.getStats();
        if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops."))) {
        	if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
        		if(e.getCurrentItem().getItemMeta().getDisplayName() !=null) {
        			if(Main.Insc.contains(p)) {
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lTOP: §7Kills.")) {
        					Map<String, Integer> userKillsList = new HashMap<>();

        					for (String user : stats.getConfigurationSection("Players").getKeys(false)) {
        						userKillsList.put(user, stats.getInt("Players." + user + ".playerkills"));
        					}

        					String user = "";
        					Integer userKills = 0;
        					
        					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops - Kills."));
        					
        					for (int i = 1; i < 5+1 ; i++) {
        						for (String playerName : userKillsList.keySet()) {
        							if (userKillsList.get(playerName) > userKills) {
        								user = playerName;
        								userKills = userKillsList.get(playerName);
        							}
        						}
        						
        						ItemStack skull = new ItemStack(397,1,(short) 3);
    							SkullMeta meta = (SkullMeta) skull.getItemMeta();
        						meta.setOwner(user);
        						meta.setDisplayName("§9" + user);
        						ArrayList<String> lore = new ArrayList<>();
        						lore.add("§9Posición: §7#" + i);
        						lore.add("§9Kills: §7" + userKills);
        						meta.setLore(lore);
        						skull.setItemMeta(meta);
        						
        						inv.setItem(i-1, skull);
        						
        						userKillsList.remove(user);
        						user = "";
        						userKills = 0;
        					}
        					p.openInventory(inv);
        				}
        				e.setCancelled(true);
        			}else {
        				p.sendMessage(Main.pr + "§c¡No estas dentro del FFA!");
        				e.setCancelled(true);
        			}
           		}
      		}
        }
    }
	
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
    	Player p = e.getPlayer();
    	if(e.getPlayer().getItemInHand().getType() == Material.TNT) {
    		p.getInventory().removeItem(new ItemStack(Material.TNT, 1));
    		TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
    		tnt.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
    	}
    }
    
    private static final Set<Material> toDestroy = new HashSet<Material>();
    static {
      toDestroy.add(Material.OBSIDIAN);
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
    	if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
    		List<Block> destroyed = event.blockList();
    		Iterator<Block> it = destroyed.iterator();
    		while (it.hasNext()) {
    			Block block = (Block) it.next();
    			if (!toDestroy.contains(block.getType())) {
    				it.remove();
    			}
    		}
    	}
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerClickInventoryTopsCoins2(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        FileConfiguration stats = plugin.getStats();
        if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops."))) {
        	if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
        		if(e.getCurrentItem().getItemMeta().getDisplayName() !=null) {
        			if(Main.Insc.contains(p)) {
        				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§9§lTOP: §7Coins.")) {
        					Map<String, Integer> userCoinsList = new HashMap<>();

        					for (String user2 : stats.getConfigurationSection("Players").getKeys(false)) {
        						userCoinsList.put(user2, stats.getInt("Players." + user2 + ".coins"));
        					}

        					String user2 = "";
        					Integer userCoins = 0;
        					
        					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops - Coins."));
        					
        					for (int i = 1; i < 5+1 ; i++) {
        						for (String playerName : userCoinsList.keySet()) {
        							if (userCoinsList.get(playerName) > userCoins) {
        								user2 = playerName;
        								userCoins = userCoinsList.get(playerName);
        							}
        						}
        						
        						ItemStack skull2 = new ItemStack(397,1,(short) 3);
    							SkullMeta meta = (SkullMeta) skull2.getItemMeta();
        						meta.setDisplayName("§9" + user2);
        						ArrayList<String> lore = new ArrayList<>();
        						lore.add("§9Posición: §7#" + i);
        						lore.add("§9Coins: §7" + userCoins);
        						meta.setLore(lore);
        						skull2.setItemMeta(meta);
        						
        						inv.setItem(i-1, skull2);
        						
        						userCoinsList.remove(user2);
        						user2 = "";
        						userCoins = 0;
        					}
        					p.openInventory(inv);
        				}
        				e.setCancelled(true);
        			}else {
        				p.sendMessage(Main.pr + "§c¡No estas dentro del FFA!");
        				e.setCancelled(true);
        			}
           		}
      		}
        }
    }
	
	private void broadcastKillstreak(Player p, Sound sound) {
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
        	all.sendMessage(Main.pr + "§aEl jugador §e" + p.getName() + " §atiene un killstreak de §e" + p.getLevel() + "§a.");
            all.playSound(all.getLocation(), sound, 10.0F, 10.0F);
        }
    }
	
	public enum TitleType {
	    ACTION_BAR,
	     TITLE,
	     SUBTITLE
	}
	
	public static String getJsonString(String field, String value){
        return "{\""+field+"\":\"" + value + "\"}";
    }
	
	public static Class<?> getServerClass(String afterPackage){
        String servPack = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + servPack + "." + afterPackage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void sendPacket(Player player, Object packet){
        try {
            Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            Method sendPacket = connection.getClass().getMethod("sendPacket", getServerClass("Packet"));

            sendPacket.invoke(connection, packet);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void sendTitle(Player player, String title, TitleType type, int fadeIn, int stay, int fadeOut){
        try{
            Constructor<?> titleConst = getServerClass("PacketPlayOutTitle")
                    .getConstructor(getServerClass("PacketPlayOutTitle$EnumTitleAction"),
                            getServerClass("IChatBaseComponent"), int.class, int.class, int.class);

            Object titleType = getServerClass("PacketPlayOutTitle$EnumTitleAction").getField(type.toString()).get(null);
            Object message = getServerClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class)
                    .invoke(null, getJsonString("text", title));

            Object titlePacket = titleConst.newInstance(titleType, message, fadeIn, stay, fadeOut);

            sendPacket(player, titlePacket);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
	
	@EventHandler
	public void getCombatLog(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			if(e.getCause() == DamageCause.ENTITY_ATTACK) {
				Player p = (Player) e.getDamager();
				Player pl = (Player) e.getEntity();
				if(Main.InCombat.contains(p)) {
					return;
				}else {
					Main.InCombat.add(p);
					ActionBar ab5 = new ActionBar("§cEstás en combate por: 5 segundos.");
					ActionBar ab4 = new ActionBar("§cEstás en combate por: 4 segundos.");
					ActionBar ab3 = new ActionBar("§cEstás en combate por: 3 segundos.");
					ActionBar ab2 = new ActionBar("§cEstás en combate por: 2 segundos.");
					ActionBar ab1 = new ActionBar("§cEstás en combate por: 1 segundo.");
					ActionBar ab0 = new ActionBar("§cYa no estás en combate.");
					ab5.sendToPlayer(p);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab4.sendToPlayer(p), 20);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab3.sendToPlayer(p), 40);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab2.sendToPlayer(p), 60);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab1.sendToPlayer(p), 80);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab0.sendToPlayer(p), 100);
					Bukkit.getScheduler().runTaskLater(plugin, () -> Main.InCombat.remove(p), 100);
				}
				if(Main.InCombat.contains(pl)) {
					return;
				}else {
					Main.InCombat.add(pl);
					ActionBar ab5 = new ActionBar("§cEstás en combate por: 5 segundos.");
					ActionBar ab4 = new ActionBar("§cEstás en combate por: 4 segundos.");
					ActionBar ab3 = new ActionBar("§cEstás en combate por: 3 segundos.");
					ActionBar ab2 = new ActionBar("§cEstás en combate por: 2 segundos.");
					ActionBar ab1 = new ActionBar("§cEstás en combate por: 1 segundo.");
					ActionBar ab0 = new ActionBar("§cYa no estás en combate.");
					ab5.sendToPlayer(pl);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab4.sendToPlayer(pl), 20);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab3.sendToPlayer(pl), 40);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab2.sendToPlayer(pl), 60);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab1.sendToPlayer(pl), 80);
					Bukkit.getScheduler().runTaskLater(plugin, () -> ab0.sendToPlayer(pl), 100);
					Bukkit.getScheduler().runTaskLater(plugin, () -> Main.InCombat.remove(pl), 100);
				}
			}else {
				return;
			}
		}
	}
		
	@EventHandler
	public void onDeathRespawn(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
	    	Player pl = (Player) e.getEntity();
	    	if(pl.getHealth() - e.getFinalDamage() > 0 || pl.getHealth() - e.getFinalDamage() > 1) {
	    		if(e.getCause() == DamageCause.PROJECTILE) {
	    			if(!(e.getDamager() instanceof Arrow)) {
	    				return;
	    			}
	    			Arrow a = (Arrow) e.getDamager();
	    			FileConfiguration stats = plugin.getStats();
	    			if(a.getShooter() instanceof Player) {
	    				if(stats.getBoolean("Players." + pl.getName() + ".ar-upgrade")) {
				        	Random rand = new Random();
		                    int n = rand.nextInt(4) + 1;
		                    if(n == 2) {
		                    	e.setCancelled(true);
		                    }
		                    if(stats.getBoolean("Players." + pl.getName() + ".ar2-upgrade")) {
		                    	Random rand2 = new Random();
			                    int n2 = rand2.nextInt(4) + 1;
			                    if(n2 == 3) {
			                    	e.setCancelled(true);
			                    }
		                    }
				        }
	    			}
	    		}
		    	return; 
		    }
	    	FileConfiguration config = plugin.getConfig();
	    	if(config.contains("Config.Spawn")) {
	    		e.setCancelled(true);
				double x = Double.valueOf(config.getString("Config.Spawn.x"));
			    double y = Double.valueOf(config.getString("Config.Spawn.y"));
			    double z = Double.valueOf(config.getString("Config.Spawn.z"));;
			    float yaw = Float.valueOf(config.getString("Config.Spawn.yaw"));
			    float pitch = Float.valueOf(config.getString("Config.Spawn.pitch"));
			    World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));

			    Location l = new Location(world, x, y, z, yaw, pitch);
			    pl.teleport(l);
		        pl.setHealth(20);
	    	}
	    	FileConfiguration stats = plugin.getStats();
	        if(e.getCause() == DamageCause.PROJECTILE) {
	        	Arrow a = (Arrow) e.getDamager();
	            if(a.getShooter() instanceof Player) {
	                Player p = (Player) a.getShooter();
	                for(Player all : Bukkit.getServer().getOnlinePlayers()) {
	            		all.sendMessage(Main.pr + "§c" + pl.getName() + " §7ha muerto por el disparo de §a" + p.getName() + "§7.");
	            	}
	                if(p.hasPermission("ffa.multiply.10")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +50);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +50 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 50);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +50 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else if(p.hasPermission("ffa.multiply.6")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +30);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +30 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 30);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +30 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else if(p.hasPermission("ffa.multiply.3")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +15);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +15 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 15);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +15 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +5);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +5 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 5);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +5 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }
	            	
	            	
	            	if(stats.contains("Players." + pl.getName()+".coins")) {
	    				int cs = Integer.valueOf(stats.getString("Players." + pl.getName() + ".coins"));
	    				stats.set("Players." + pl.getName() + ".coins", cs -2);
	    				plugin.saveStats();
	            	}else{
	            		stats.set("Players." + pl.getName() + ".coins", 0);
	            		plugin.saveStats();
	            		return;
	            	}
	            	pl.getInventory().clear();
					pl.setHealth(20);
					
					
					Bukkit.getScheduler ().runTaskLater (plugin, () -> {
						try {
							restoreInventory(pl);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}, 20);
			        
			        pl.setLevel(0);
			        pl.setExp(0);
			        
			        //PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"Hola que tal xd\"}"), 20, 20, 20);
			        //((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
			        
			        sendTitle(pl, "§c§lHAS MUERTO", TitleType.TITLE, 2, 10, 2);
			        sendTitle(pl, "§7Te ha matado §e" + p.getName(), TitleType.TITLE, 2, 10, 2);
			        
			        if(stats.getBoolean("Players." + p.getName() + ".gp-upgrade")) {
			        	p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
			        }
			        if(stats.getBoolean("Players." + p.getName() + ".ob-upgrade")) {
			        	p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 8));
			        }
			        
			        p.setLevel(p.getLevel() + 1);
			        p.setExp(0);;

					if (p.getLevel() == 5) {
						broadcastKillstreak(p, Sound.NOTE_PLING);
					} else if (p.getLevel() == 10) {
						broadcastKillstreak(p, Sound.NOTE_PLING);
					} else if (p.getLevel() == 15) {
						broadcastKillstreak(p, Sound.ENDERMAN_TELEPORT);
					} else if (p.getLevel() == 20) {
						broadcastKillstreak(p, Sound.ENDERMAN_HIT);
					} else if (p.getLevel() == 25) {
						broadcastKillstreak(p, Sound.ENDERMAN_IDLE);
					} else if (p.getLevel() == 30) {
						broadcastKillstreak(p, Sound.ENDERDRAGON_DEATH);
					}
			        
			        if(config.getString("Config.anticlean").equalsIgnoreCase("true")) {
			        	if(!plugin.playerIsInList(p)) {
				        	plugin.addPlayer(p);
				        	Coldown c = new Coldown(plugin, 5, p);
							c.execution();
							p.sendMessage(Main.pr + "§7Modo anticlean §aactivado§7.");
							p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 3)));
				        }else {
				        	p.sendMessage(Main.pr + "§cNo se ha podido activar el modo anticlean porque ya esta activado.");
				        }
			        } 
	            }
	        }else {
	        	if(e.getDamager() instanceof Player) {
	        		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
	                    Player damager = (Player) e.getDamager();
	                    
	                    Random rand = new Random();
	                    int n = rand.nextInt(4) + 1;
	                    if(n == 1) {
	                    	all.sendMessage(Main.pr + "§a" + damager.getName() + " §7ha deborado a §c" + pl.getName() + "§7.");
	                    }
	                    if(n == 2) {
	                    	all.sendMessage(Main.pr + "§a" + damager.getName() + " §7ha matado al pobre §c" + pl.getName() + "§7.");
	                    }
	                    if(n == 3) {
	                    	all.sendMessage(Main.pr + "§c" + pl.getName() + " §7ha muerto a manos de §a" + damager.getName() + "§7.");
	                    }
	                    if(n == 4) {
	                    	all.sendMessage(Main.pr + "§c" + pl.getName() + " §7no resistió los ataques de §a" + damager.getName() + "§7.");
	                    }
	                    
	                }
	            	Player p = (Player) e.getDamager();
	            	if(p.hasPermission("ffa.multiply.10")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +50);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +50 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 50);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +50 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else if(p.hasPermission("ffa.multiply.6")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +30);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +30 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 30);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +30 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else if(p.hasPermission("ffa.multiply.3")) {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +15);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +15 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 15);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +15 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }else {
	                	if(stats.contains("Players." + p.getName()+".playerkills")) {
		    				int cantidadjugadores = Integer.valueOf(stats.getString("Players." + p.getName() + ".playerkills"));
		    				stats.set("Players." + p.getName() + ".playerkills", cantidadjugadores+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".playerkills", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + pl.getName()+".muertes")) {
		    				int cm = Integer.valueOf(stats.getString("Players." + pl.getName() + ".muertes"));
		    				stats.set("Players." + pl.getName() + ".muertes", cm+1);
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + pl.getName() + ".muertes", 1);
		            		plugin.saveStats();
		            		return;
		            	}
		            	if(stats.contains("Players." + p.getName()+".coins")) {
		    				int cs = Integer.valueOf(stats.getString("Players." + p.getName() + ".coins"));
		    				stats.set("Players." + p.getName() + ".coins", cs +5);
		    				p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +5 Coins)");
		    				plugin.saveStats();
		            	}else{
		            		stats.set("Players." + p.getName() + ".coins", 5);
		            		p.sendMessage(Main.pr + "§a¡Has matado a un jugador! §7(+1 Baja, +5 Coins)");
		            		plugin.saveStats();
		            		return;
		            	}
	                }
	            	pl.getInventory().clear();
					pl.setHealth(20);
					
					Bukkit.getScheduler ().runTaskLater (plugin, () -> {
						try {
							restoreInventory(pl);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}, 20);
			        
			        pl.setLevel(0);
			        pl.setExp(0);
			        
			        //PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"Hola que tal xd\"}"), 20, 20, 20);
			        //((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
			        
			        sendTitle(pl, "§c§lHAS MUERTO", TitleType.TITLE, 2, 10, 2);
			        sendTitle(pl, "§7Te ha matado §e" + p.getName(), TitleType.SUBTITLE, 2, 10, 2);
			        
			        if(stats.getBoolean("Players." + p.getName() + ".gp-upgrade")) {
			        	p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
			        }
			        if(stats.getBoolean("Players." + p.getName() + ".ob-upgrade")) {
			        	p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 8));
			        }
			        
			        p.setLevel(p.getLevel() + 1);
			        p.setExp(0);
			        
			        if (p.getLevel() == 5) {
						broadcastKillstreak(p, Sound.NOTE_PLING);
					} else if (p.getLevel() == 10) {
						broadcastKillstreak(p, Sound.NOTE_PLING);
					} else if (p.getLevel() == 15) {
						broadcastKillstreak(p, Sound.ENDERMAN_TELEPORT);
					} else if (p.getLevel() == 20) {
						broadcastKillstreak(p, Sound.ENDERMAN_HIT);
					} else if (p.getLevel() == 25) {
						broadcastKillstreak(p, Sound.ENDERMAN_IDLE);
					} else if (p.getLevel() == 30) {
						broadcastKillstreak(p, Sound.ENDERDRAGON_DEATH);
					}
			        
			        if(config.getString("Config.anticlean").equalsIgnoreCase("true")) {
			        	if(!plugin.playerIsInList(p)) {
				        	plugin.addPlayer(p);
				        	Coldown c = new Coldown(plugin, 5, p);
							c.execution();
							p.sendMessage(Main.pr + "§7Modo anticlean §aactivado§7.");
							p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 3)));
				        }else {
				        	p.sendMessage(Main.pr + "§cNo se ha podido activar el modo anticlean porque ya esta activado.");
				        }
			        }
			        
	        	}else {
	        		return;
	        	}
	        }
	    }else {
	    	return;
	    }
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(Main.Insc.contains(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Main.Insc.contains(p)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(Main.Insc.contains(p)) {
			Block block = e.getBlock();
			if(e.getBlock().getType() == Material.OBSIDIAN) {
				Bukkit.getScheduler ().runTaskLater (plugin, () -> block.setType(Material.AIR), 160);
			}else {
				e.setCancelled(true);
			}
		}
	}
	
}
