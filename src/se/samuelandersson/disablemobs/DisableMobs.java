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

import java.util.logging.Logger;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import se.samuelandersson.disablemobs.helpers.EntityHelper;

/**
 * This plugin enables control over which mobs are allowed to spawn. It can be
 * configured to allow spawner eggs to be used, or completely disable spawning
 * of the mob type.
 * A command(/killmob) is also provided to allow players with permission to kill
 * all mobs of a certain type.
 * 
 * DisableMobs is currently updated with Bukkit 1.6.2-R0.1
 * 
 * @author Samuel Andersson
 * @since 2013-07-20
 * 
 */
public class DisableMobs extends JavaPlugin {

	private Logger log = Logger.getLogger("Minecraft");

	public DisableMobs() {
	}

	public MobPermission getMobPermission(EntityType type) {
		if (!EntityHelper.isMob(type)) return MobPermission.ENABLED;
		String setting = getConfig().getString("mobs." + EntityHelper.getEntityName(type), "true");
		if (setting.equalsIgnoreCase("false")) return MobPermission.DISABLED;
		if (setting.equalsIgnoreCase("egg")) return MobPermission.ALLOWED_WITH_EGG;
		return MobPermission.ENABLED;
	}

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		PluginCommand command = getCommand("killmob");
		if (command != null)
			command.setExecutor(new KillMobCommandExecutor(this));
		else
			log.severe("Couldn't retrieve plugin command \"killmob\". Command is not available.");
		new SpawnListener(this);

		log.info("[" + getDescription().getName() + "] is now enabled.");
	}

	@Override
	public void onDisable() {
		log.info("[" + getDescription().getName() + "] is now disabled.");
	}
}
