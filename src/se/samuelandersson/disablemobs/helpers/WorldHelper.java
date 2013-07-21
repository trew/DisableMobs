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

package se.samuelandersson.disablemobs.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;

public class WorldHelper {

	private WorldHelper() {
	}

	/**
	 * Translates a world name to a world. Returns null if none found
	 * 
	 * @param name the name of the world, i.e. "overworld"
	 * @return a world matching the world name
	 */
	public static World getWorld(Server server, String name) {
		if (name.equalsIgnoreCase("overworld")) return server.getWorld("world");
		if (name.equalsIgnoreCase("nether")) return server.getWorld("world_nether");
		if (name.equalsIgnoreCase("the_end")) return server.getWorld("world_the_end");
		if (name.equalsIgnoreCase("theend")) return server.getWorld("world_the_end");
		if (name.equalsIgnoreCase("end")) return server.getWorld("world_the_end");
		return server.getWorld(name);
	}

	public static String getWorldName(World world) {
		if (world.getName().equals("world")) return "overworld";
		if (world.getName().equals("world_nether")) return "nether";
		if (world.getName().equals("world_the_end")) return "the_end";
		return world.getName();
	}

	public static List<String> getWorldsStartingWith(String msg) {
		ArrayList<String> values = new ArrayList<String>();
		if (msg.startsWith("a"))
			values.add("all");
		else if (msg.startsWith("n"))
			values.add("nether");
		else if (msg.startsWith("t"))
			values.add("the_end");
		else if (msg.startsWith("o"))
			values.add("overworld");
		else {
			values.add("all");
			values.add("nether");
			values.add("overworld");
			values.add("the_end");
		}
		return values;
	}

	public static String concatWorldNames(List<World> worlds) {
		if (worlds.size() > 2) {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < worlds.size() - 1; i++) {
				String worldName = ChatHelper.colorize(getWorldName(worlds.get(i)), ChatColor.YELLOW);
				b.append(worldName).append(", ");
			}
			b.append("and ").append(ChatColor.YELLOW).append(getWorldName(worlds.get(worlds.size() - 1))).append(ChatColor.RESET);
			return b.toString();
		} else if (worlds.size() == 2) {
			String world1 = ChatHelper.colorize(getWorldName(worlds.get(0)), ChatColor.YELLOW);
			String world2 = ChatHelper.colorize(getWorldName(worlds.get(1)), ChatColor.YELLOW);
			return world1 + " and " + world2;
		} else if (worlds.size() == 1) {
			return ChatHelper.colorize(getWorldName(worlds.get(0)), ChatColor.YELLOW);
		}
		return "";
	}

}
