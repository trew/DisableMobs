/*
 * Copyright (c) 2013 Samuel Andersson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package se.samuelandersson.disablemobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import se.samuelandersson.disablemobs.helpers.ChatHelper;
import se.samuelandersson.disablemobs.helpers.EntityHelper;
import se.samuelandersson.disablemobs.helpers.WorldHelper;

/**
 * This is the executor for the command "/killmob".
 * 
 * @author Samuel Andersson
 * 
 */
public class KillMobCommandExecutor implements TabCompleter, CommandExecutor {

	private final DisableMobs plugin;
	private Random random = new Random(new Date().getTime());

	public KillMobCommandExecutor(DisableMobs plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!sender.hasPermission(command.getPermission())) return null;
		if (args.length == 0) return null;
		if (args.length == 1) {
			return EntityHelper.getMobsStartingWith(args[0]);
		}
		// if args.length > 1
		return WorldHelper.getWorldsStartingWith(args[args.length - 1]);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission(command.getPermission())) {
			sender.sendMessage(ChatHelper.colorize("You are not authorized to use this command!", ChatColor.RED));
			return true;
		}

		if (args.length < 1) return ChatHelper.sendUsage(sender, command);

		String mobName = args[0];
		EntityType type = EntityHelper.getTypeFromName(mobName);
		if (type == null || type == EntityType.UNKNOWN) {
			sender.sendMessage(ChatHelper.colorize("Unknown mob: " + mobName, ChatColor.RED));
			return true;
		}

		ArrayList<World> worlds = new ArrayList<World>(3);
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				String worldName = args[i];
				if (worldName.equals("all")) {
					worlds.clear();
					worlds.addAll(plugin.getServer().getWorlds());
					break;
				}
				World world = WorldHelper.getWorld(plugin.getServer(), worldName);
				if (world == null) {
					sender.sendMessage(ChatColor.RED + "World " + ChatColor.YELLOW + worldName
					      + ChatHelper.colorize(" does not exist. Possible worlds are: ", ChatColor.RED)
					      + WorldHelper.concatWorldNames(plugin.getServer().getWorlds()));
					return true;
				} else {
					worlds.add(world);
				}
			}
		} else {
			if (sender instanceof Player)
				worlds.add(((Player) sender).getWorld());
			else
				worlds.addAll(plugin.getServer().getWorlds());
		}

		int killedEntities = 0;
		for (World w : worlds) {
			killedEntities += killEntityType(w, type);
		}

		if (killedEntities == 0)
			sender.sendMessage("No " + ChatHelper.colorize(mobName, ChatColor.YELLOW) + " were found to be "
			      + getRandomKillWord(false) + " in " + WorldHelper.concatWorldNames(worlds) + ".");
		else {
			sender.sendMessage(String.format(getRandomKillWord(true) + ChatHelper.colorize(" %s %s", ChatColor.YELLOW) + " in "
			      + WorldHelper.concatWorldNames(worlds) + ".", killedEntities, mobName));
		}
		return true;
	}

	/**
	 * Returns a random word for destruction.
	 * 
	 * @param capitalFirstLetter true if the first letter is to be capitalized
	 * @return a random word for destruction.
	 */
	private String getRandomKillWord(boolean capitalFirstLetter) {
		List<String> killMessages = plugin.getConfig().getStringList("killwords");
		String word = killMessages.size() > 0 ? killMessages.get(random.nextInt(killMessages.size())) : "killed";
		if (!capitalFirstLetter) return word;
		return word.substring(0, 1).toUpperCase().concat(word.substring(1));
	}

	/**
	 * Kills all living entities of provided type
	 * 
	 * @param world the world which the entities should be killed in
	 * @param type the entity type to kill
	 * @return how many entities were killed
	 */
	private int killEntityType(World world, EntityType type) {
		int count = 0;
		for (LivingEntity entity : world.getLivingEntities()) {
			if (entity.getType().equals(type)) {
				entity.setHealth(0);
				count++;
			}
		}
		return count;
	}

}
