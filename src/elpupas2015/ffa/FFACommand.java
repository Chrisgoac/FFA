package elpupas2015.ffa;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FFACommand implements CommandExecutor {

	private static Main plugin;
	public FFACommand(Main pl) {
		FFACommand.plugin = pl;
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
    
    public void saveInventory(Player p) throws IOException {
        //File f = new File(plugin.getDataFolder().getAbsolutePath(), p.getName() + ".yml");
        FileConfiguration c = plugin.getKit();
        c.set("Inventory.armor", p.getInventory().getArmorContents());
        c.set("Inventory.content", p.getInventory().getContents());
        plugin.saveKit();
    }

//    @SuppressWarnings("unchecked")
//    public void restoreInventory(Player p) throws IOException {
//        //File f = new File(plugin.getDataFolder().getAbsolutePath(), p.getName() + ".yml");
//        FileConfiguration c = plugin.getKit();
//        List<ItemStack> list = (List<ItemStack>) c.getList("Inventory.armor", new ArrayList<ItemStack>());
//        ItemStack[] content = list.toArray(new ItemStack[list.size()]);
//        p.getInventory().setArmorContents(content);
//        content = ((List<ItemStack>) c.get("Inventory.content")).toArray(new ItemStack[0]);
//        p.getInventory().setContents(content);
//    }
    
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
    
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			//Bukkit.getConsoleSender().sendMessage(Main.pr + "§c¡Solo los usuarios pueden ejecutar este comando!");
			if(args.length > 0) {
				FileConfiguration stats = plugin.getStats();
				if(args[0].equalsIgnoreCase("coins")) {
					CommandSender p = sender;
					if(args.length == 1) {
						p.sendMessage("§2Argumentos validos:");
						p.sendMessage("§7» §6/ffa coins add <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins set <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins take <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins look <jugador>");
					}else {
						if(args[1].equalsIgnoreCase("add")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int cs = Integer.valueOf(stats.getString("Players." + target.getName() + ".coins"));
											int num = Integer.parseInt(args[3]);
											int suma = Integer.valueOf(cs+num);
											stats.set("Players." + target.getName() + ".coins", suma);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + suma + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + suma + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins add <jugador> <cantidad>");
								}
						}else if(args[1].equalsIgnoreCase("set")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int num = Integer.parseInt(args[3]);
											stats.set("Players." + target.getName() + ".coins", num);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + num + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + num + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins set <jugador> <cantidad>");
								}
						}else if(args[1].equalsIgnoreCase("take")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int cs = Integer.valueOf(stats.getString("Players." + target.getName() + ".coins"));
											int num = Integer.parseInt(args[3]);
											int resta = Integer.valueOf(cs-num);
											stats.set("Players." + target.getName() + ".coins", resta);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + resta + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + resta + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins take <jugador> <cantidad>");
								}
						}
					}
				}
			}
					
			return false;
		}else {
			FileConfiguration config = plugin.getConfig();
			FileConfiguration stats = plugin.getStats();
			Player p = (Player) sender;
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("info")) {
					p.sendMessage("§e§m--------------------------");
					p.sendMessage("                     §9FFA");
					p.sendMessage("");
					p.sendMessage("§6Created by: §cELPUPAS2015");
					p.sendMessage("§6Version: §c" + plugin.version);
					p.sendMessage("");
					p.sendMessage("§e§m--------------------------");
					return true;
				}
				else if(args[0].equalsIgnoreCase("setspawn")) {
					if(p.hasPermission("ffa.setspawn")) {
						Location l = p.getLocation();
						double x = l.getX();
						double y = l.getY();
						double z = l.getZ();
						String world = l.getWorld().getName();
						float yaw = l.getYaw();
						float pitch = l.getPitch();
						config.set("Config.Spawn.world", world);
						config.set("Config.Spawn.x", x);
						config.set("Config.Spawn.y", y);
						config.set("Config.Spawn.z", z);
						config.set("Config.Spawn.yaw", yaw);
						config.set("Config.Spawn.pitch", pitch);
						plugin.saveConfig();
						p.sendMessage(Main.pr + "§a¡El spawn se ha colocado correctamente!");
						return true;
					}else {
						p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("join")) {
					if(Main.Insc.contains(p)) {
						p.sendMessage(Main.pr + "§c¡Ya estas detro del FFA!");
					}else {
						if(config.contains("Config.Spawn.x")) {
							double x = Double.valueOf(config.getString("Config.Spawn.x"));
							double y = Double.valueOf(config.getString("Config.Spawn.y"));
							double z = Double.valueOf(config.getString("Config.Spawn.z"));
							float yaw = Float.valueOf(config.getString("Config.Spawn.yaw"));
							float pitch = Float.valueOf(config.getString("Config.Spawn.pitch"));
							World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));
							Location l = new Location(world, x, y, z, yaw, pitch);
							p.teleport(l);

							p.getInventory().clear();
							p.setHealth(20);
							
							try {
								restoreInventory(p);
							} catch (IOException e) {
								p.sendMessage(Main.pr + "§c¡Ha habido un error al unirte al FFA");
							}
							
							Main.Insc.add(p);
					        
							p.sendMessage(Main.pr + "§a¡Te has unido correctamente al FFA!");
							return true;	
						}else {
							p.sendMessage(Main.pr + "§c¡El spawn no ha sido colocado todavia!");
						}	
					}
					
				}
				else if(args[0].equalsIgnoreCase("leave")) {
					if(p.hasPermission("ffa.leave")) {
						if(Main.Insc.contains(p)) {
							if(config.contains("Config.Spawn.x")) {
								Main.Insc.remove(p);
								p.sendMessage(Main.pr + "§a¡Has salido del FFA!");
								double x = Double.valueOf(config.getString("Config.LeaveSpawn.x"));
								double y = Double.valueOf(config.getString("Config.LeaveSpawn.y"));
								double z = Double.valueOf(config.getString("Config.LeaveSpawn.z"));
								float yaw = Float.valueOf(config.getString("Config.LeaveSpawn.yaw"));
								float pitch = Float.valueOf(config.getString("Config.LeaveSpawn.pitch"));
								World world = plugin.getServer().getWorld(config.getString("Config.LeaveSpawn.world"));
								Location l = new Location(world, x, y, z, yaw, pitch);
								p.teleport(l);
								
								p.getInventory().clear();
								p.setHealth(20);
							}	
						}else {
							p.sendMessage(Main.pr + "§c¡No estas dentro del FFA!");
						}
						return true;
					}else {
						p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("createkit")) {
					if(p.hasPermission("ffa.createkit")) {
						try {
							saveInventory(p);
						} catch (IOException e) {
							p.sendMessage(Main.pr + "§c¡No se ha podido guardar el kit!");
						}
					}else {
						p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
					}
				}
				else if(args[0].equalsIgnoreCase("setleavespawn")) {
					if(p.hasPermission("ffa.setleavespawn")) {
						Location l = p.getLocation();
						double x = l.getX();
						double y = l.getY();
						double z = l.getZ();
						String world = l.getWorld().getName();
						float yaw = l.getYaw();
						float pitch = l.getPitch();
						config.set("Config.LeaveSpawn.world", world);
						config.set("Config.LeaveSpawn.x", x);
						config.set("Config.LeaveSpawn.y", y);
						config.set("Config.LeaveSpawn.z", z);
						config.set("Config.LeaveSpawn.yaw", yaw);
						config.set("Config.LeaveSpawn.pitch", pitch);
						plugin.saveConfig();
						p.sendMessage(Main.pr + "§a¡El spawn al salir se ha colocado correctamente!");
						return true;
					}else {
						p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("reload")) {
					if(p.hasPermission("ffa.reload")) {
						p.sendMessage(Main.pr + "§a¡El plugin se ha recargado correctamente!");
						plugin.reloadConfig();
						plugin.reloadStats();
						return true;
					}else {
						p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("tops")) {
					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Tops."));
					
					ItemStack ds = new ItemStack(Material.DIAMOND_SWORD, 1);
					ItemMeta ds_meta = ds.getItemMeta();
					ds_meta.setDisplayName("§9§lTOP: §7Kills.");
					ds.setItemMeta(ds_meta);
					inv.setItem(2, ds);
					
					ItemStack cs = new ItemStack(Material.APPLE, 1);
					ItemMeta cs_meta = cs.getItemMeta();
					cs_meta.setDisplayName("§9§lTOP: §7Coins.");
					cs.setItemMeta(cs_meta);
					inv.setItem(6, cs);
				        
					p.openInventory(inv);
					return true;
				}
				else if(args[0].equalsIgnoreCase("mejoras") || args[0].equalsIgnoreCase("tienda")) {
					p.sendMessage(Main.pr + "§aAbriendo la tienda.");
					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&9&lFFA &8» &7Mejoras - Tienda."));
					
					ItemStack gp = new ItemStack(Material.GOLDEN_APPLE, 1);
					ItemMeta meta = gp.getItemMeta();
					meta.setDisplayName("§9§lMEJORA: §7Golden apple al matar.");
					ArrayList<String> list = new ArrayList<String>();
					list.add("§9Descripción: §7Al comprar esta mejora cada vez que mates a");
					list.add("§7un jugador, se te otorgará una manzana de oro.");
					list.add("§9Precio: §71000 coins");
					list.add("§9Calidad: §5Épica.");
					if(stats.getBoolean("Players." + p.getName() + ".gp-upgrade")) {
						list.add("§9Disponible: §cno");
					}else {
						list.add("§9Disponible: §así");
					}
					meta.setLore(list);
					gp.setItemMeta(meta);
					inv.setItem(0, gp);
					
					ItemStack bow = new ItemStack(Material.BOW, 1);
					ItemMeta bmeta = bow.getItemMeta();
					bmeta.setDisplayName("§9§lMEJORA: §7Inmune a las flechas. (Tier I)");
					ArrayList<String> blist = new ArrayList<String>();
					blist.add("§9Descripción: §7Al comprar esta mejora cada vez que te disparen");
					blist.add("§7tendrás un 25% de que la flecha no te haga daño.");
					blist.add("§9Precio: §71000 coins");
					blist.add("§9Calidad: §5Épica.");
					if(stats.getBoolean("Players." + p.getName() + ".ar-upgrade")) {
						blist.add("§9Disponible: §cno");
					}else {
						blist.add("§9Disponible: §así");
					}
					bmeta.setLore(blist);
					bow.setItemMeta(bmeta);
					
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
					
					if(stats.getBoolean("Players." + p.getName() + ".ar-upgrade")) {
						inv.setItem(2, bbow);
					}else {
						inv.setItem(2, bow);
					}
					
					
					ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
					ItemMeta gmeta = gapple.getItemMeta();
					gmeta.setDisplayName("§9§lITEM: §7Manzana de oro encantada.");
					ArrayList<String> glist = new ArrayList<String>();
					glist.add("§9Descripción: §7Al comprar este item recibiras una");
					glist.add("§7manzana de oro encantada. (1 solo uso)");
					glist.add("§9Precio: §7200 coins");
					glist.add("§9Calidad: §eNo común");
					gmeta.setLore(glist);
					gapple.setItemMeta(gmeta);
					inv.setItem(1, gapple);
					
					ItemStack bloque = new ItemStack(Material.OBSIDIAN, 1);
					ItemMeta bbbmeta = bloque.getItemMeta();
					bbbmeta.setDisplayName("§9§lITEM: §7Bloques de obsidiana.");
					ArrayList<String> vglist = new ArrayList<String>();
					vglist.add("§9Descripción: §7Al comprar este item recibiras 8 bloques");
					vglist.add("§7de obisidana para colocarlos por el mapa.");
					vglist.add("§9Precio: §750 coins");
					vglist.add("§9Calidad: §7Común.");
					bbbmeta.setLore(vglist);
					bloque.setItemMeta(bbbmeta);
					inv.setItem(4, bloque);
					
					ItemStack bbloque = new ItemStack(Material.OBSIDIAN, 1);
					ItemMeta bbbbmeta = bbloque.getItemMeta();
					bbbbmeta.setDisplayName("§9§lMEJORA: §7Bloques de obsidiana al matar.");
					ArrayList<String> bvglist = new ArrayList<String>();
					bvglist.add("§9Descripción: §7Al comprar esta mejora recibiras 8 bloques");
					bvglist.add("§7cada vez que mates a un jugador.");
					bvglist.add("§9Precio: §7500 coins");
					bvglist.add("§9Calidad: §5Épica.");
					if(stats.getBoolean("Players." + p.getName() + ".ob-upgrade")) {
						bvglist.add("§9Disponible: §cno");
					}else {
						bvglist.add("§9Disponible: §así");
					}
					bbbbmeta.setLore(bvglist);
					bbloque.setItemMeta(bbbbmeta);
					inv.setItem(3, bbloque);
					
					ItemStack vbloque = new ItemStack(Material.ENDER_PEARL, 1);
					ItemMeta vbbbmeta = vbloque.getItemMeta();
					vbbbmeta.setDisplayName("§9§lITEM: §7Enderpearl");
					ArrayList<String> vvglist = new ArrayList<String>();
					vvglist.add("§9Descripción: §7Al comprar este item recibiras 1 enderpearl");
					vvglist.add("§7para usarla por todo el mapa. (1 solo uso)");
					vvglist.add("§9Precio: §7100 coins");
					vvglist.add("§9Calidad: §eNo común.");
					vbbbmeta.setLore(vvglist);
					vbloque.setItemMeta(vbbbmeta);
					inv.setItem(5, vbloque);
				    
					p.openInventory(inv);
					return true;
				}
				else if(args[0].equalsIgnoreCase("coins")) {
					if(args.length == 1) {
						p.sendMessage("§2Argumentos validos:");
						p.sendMessage("§7» §6/ffa coins add <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins set <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins take <jugador> <cantidad>");
						p.sendMessage("§7» §6/ffa coins look <jugador>");
					}else {
						if(args[1].equalsIgnoreCase("add")) {
							if(p.hasPermission("ffa.coins")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int cs = Integer.valueOf(stats.getString("Players." + target.getName() + ".coins"));
											int num = Integer.parseInt(args[3]);
											int suma = Integer.valueOf(cs+num);
											stats.set("Players." + target.getName() + ".coins", suma);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + suma + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + suma + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins add <jugador> <cantidad>");
								}
							}else {
								p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
							}
						}else if(args[1].equalsIgnoreCase("set")) {
							if(p.hasPermission("ffa.coins")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int num = Integer.parseInt(args[3]);
											stats.set("Players." + target.getName() + ".coins", num);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + num + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + num + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins set <jugador> <cantidad>");
								}
							}else {
								p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
							}
						}else if(args[1].equalsIgnoreCase("take")) {
							if(p.hasPermission("ffa.coins")) {
								if(args.length == 4) {
									Player target = Bukkit.getPlayer(args[2]);
									if(target == null) {
										p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
									}else {
										if(isInt(args[3])) {
											int cs = Integer.valueOf(stats.getString("Players." + target.getName() + ".coins"));
											int num = Integer.parseInt(args[3]);
											int resta = Integer.valueOf(cs-num);
											stats.set("Players." + target.getName() + ".coins", resta);
						    				plugin.saveStats();
						    				p.sendMessage(Main.pr + "§aHas cambiado las coins de §7" + target.getName() + "§a, nueva cantidad: §7" + resta + "§a.");
						    				target.sendMessage(Main.pr + "§aUn administrador ha cambiado tus coins, nueva cantidad: §7" + resta + "§a.");
										}else {
											p.sendMessage(Main.pr + "§cEl segundo argumento tiene que ser un número.");
										}
									}
								}else {
									p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins take <jugador> <cantidad>");
								}
							}else {
								p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
							}
						}else if(args[1].equalsIgnoreCase("look")) {
							if(args.length == 3) {
								Player target = Bukkit.getPlayer(args[2]);
								if(target == null) {
									p.sendMessage(Main.pr + "§c¡Ese jugador no esta conectado!");
								}else {
									int cs = Integer.valueOf(stats.getString("Players." + target.getName() + ".coins"));
									p.sendMessage(Main.pr + "§aEl jugador §7" + target.getName() + " §atiene §7" + cs + " §acoins.");
								}
							}else {
								p.sendMessage(Main.pr + "§cFormato incorrecto, usa §7-/ffa coins look <jugador>");
							}
						}else {
							p.sendMessage("§2Argumentos validos:");
							p.sendMessage("§7» §6/ffa coins add <jugador> <cantidad>");
							p.sendMessage("§7» §6/ffa coins set <jugador> <cantidad>");
							p.sendMessage("§7» §6/ffa coins take <jugador> <cantidad>");
							p.sendMessage("§7» §6/ffa coins look <jugador>");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("anticlean")) {
					if(args.length == 1) {
						if(p.hasPermission("ffa.anticlean")) {
							p.sendMessage(Main.pr + "§eModo anticlean:");
							TextComponent msg = new TextComponent(Main.pr + "§a[ACTIVAR] ");
							TextComponent add = new TextComponent("§c [DESACTIVAR]");
							msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Click aqui para §aactivar §7el modo anticlean.").create()));
							msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ffa anticlean on"));
							add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Click aqui para §cdesactivar §7el modo anticlean.").create()));
							add.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ffa anticlean off"));
							msg.addExtra(add);
							p.spigot().sendMessage(msg);
						}else {
							p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
							return true;
						}
					}else {
						if(p.hasPermission("ffa.anticlean")) {
							if(args[1].equalsIgnoreCase("on")) {
								p.sendMessage(Main.pr + "§7Has §aactivado §7el modo anticlean.");
								config.set("Config.anticlean", "true");
								for(Player all : Bukkit.getServer().getOnlinePlayers()) {
									all.sendTitle("§c§lANTICLEAN", "§7El anticlean ahora está §aactivado§7.");
								}
								plugin.saveConfig();
							}
							else if(args[1].equalsIgnoreCase("off")) {
								p.sendMessage(Main.pr + "§7Has §cdesactivado §7el modo anticlean.");
								config.set("Config.anticlean", "false");
								for(Player all : Bukkit.getServer().getOnlinePlayers()) {
									all.sendTitle("§c§lANTICLEAN", "§7El anticlean ahora está §cdesactivado§7.");
								}
								plugin.saveConfig();
							}else {
								p.sendMessage(Main.pr + "§c¡Este comando no existe!");
							}
						}else {
							p.sendMessage(Main.pr + "§c¡No tienes permisos para ejecutar este comando!");
							return true;
						}
					}
				}
				else {
					p.sendMessage(Main.pr + "§c¡Este comando no existe!");
				}
			}else {
				p.sendMessage("");
				p.sendMessage("§e§m--------------------------");
				p.sendMessage("                   §9FFA");
				p.sendMessage("");
				p.sendMessage("§2Player commands:");
				p.sendMessage("");
				p.sendMessage("§7» §6/ffa join");
				p.sendMessage("§7» §6/ffa tienda");
				p.sendMessage("§7» §6/ffa tops");
				p.sendMessage("§7» §6/ffa info");
				p.sendMessage("");
				p.sendMessage("§2Admin commands:");
				p.sendMessage("");
				p.sendMessage("§7» §6/ffa setspawn");
				p.sendMessage("§7» §6/ffa setleavespawn");
				p.sendMessage("§7» §6/ffa reload");
				p.sendMessage("§7» §6/ffa leave");
				p.sendMessage("§7» §6/ffa anticlean");
				p.sendMessage("§7» §6/ffa coins");
				p.sendMessage("");
				p.sendMessage("§e§m--------------------------");
				return true;
			}
		}
		return false;
	}
}
