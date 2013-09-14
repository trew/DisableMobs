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

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

import se.samuelandersson.disablemobs.helpers.ChatHelper;
import se.samuelandersson.disablemobs.helpers.EntityHelper;
import se.samuelandersson.disablemobs.helpers.WorldHelper;

/**
 * This is the executor for the command "/disablemob".
 * 
 * @author Samuel Andersson
 * 
 */
public class DisableMobCommandExecutor implements TabCompleter, CommandExecutor {

	private final DisableMobs plugin;

	public DisableMobCommandExecutor(DisableMobs plugin) {
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

		MobPermission permission = MobPermission.fromString(args[1]);
		plugin.setMobPermission(type, permission);
		return true;
	}
}
