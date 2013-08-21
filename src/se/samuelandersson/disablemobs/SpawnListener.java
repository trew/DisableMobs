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

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import se.samuelandersson.disablemobs.helpers.EntityHelper;

public class SpawnListener implements Listener {

	private final DisableMobs plugin;

	public SpawnListener(DisableMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack itemStack = event.getItem();
		if (itemStack != null && itemStack.getData() instanceof SpawnEgg) {
			SpawnEgg data = (SpawnEgg) event.getItem().getData();
			EntityType type = data.getSpawnedType();
			MobPermission permission = plugin.getMobPermission(type);

			if (permission == MobPermission.DISABLED) {
				event.getPlayer().sendMessage(
				      plugin.getDescription().getName() + " prevented " + EntityHelper.getEntityName(type) + " to be spawned.");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		EntityType type = event.getEntityType();
		MobPermission permission = plugin.getMobPermission(type);

		if (permission == MobPermission.ENABLED) {
			// do nothing
		} else if (permission == MobPermission.DISABLED) {
			event.setCancelled(true);
		} else if (permission == MobPermission.ALLOWED_WITH_EGG) {
			if (event.getSpawnReason() != SpawnReason.SPAWNER_EGG) event.setCancelled(true);
		}
	}
}
