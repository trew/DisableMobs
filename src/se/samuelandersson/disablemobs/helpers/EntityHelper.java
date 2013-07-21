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

import org.bukkit.entity.EntityType;

/**
 * Provides helper functions for Entities. Especially concerning names, since
 * Bukkit gives some weird names when using EntityType.getName().
 * EntityType.OCELOT.getName() returns "Ozelot", which is very confusing. This
 * class also helps with separating hostile and neutral mobs, and also helps
 * with sorting out what is a mob and what is an item i.e.
 * 
 * @author Samuel Andersson
 * @since 2013-07-20
 * 
 */
public class EntityHelper {

	private static final EntityHelper instance = new EntityHelper();

	private final List<EntityType> mobs;
	private final List<EntityType> hostile;
	private final List<EntityType> neutral;

	private EntityHelper() {
		hostile = new ArrayList<EntityType>();
		hostile.add(EntityType.BLAZE);
		hostile.add(EntityType.CAVE_SPIDER);
		hostile.add(EntityType.CREEPER);
		hostile.add(EntityType.ENDER_DRAGON);
		hostile.add(EntityType.ENDERMAN);
		hostile.add(EntityType.GHAST);
		hostile.add(EntityType.MAGMA_CUBE);
		hostile.add(EntityType.PIG_ZOMBIE);
		hostile.add(EntityType.SILVERFISH);
		hostile.add(EntityType.SKELETON);
		hostile.add(EntityType.SLIME);
		hostile.add(EntityType.SPIDER);
		hostile.add(EntityType.WITCH);
		hostile.add(EntityType.WITHER);
		hostile.add(EntityType.ZOMBIE);

		neutral = new ArrayList<EntityType>();
		neutral.add(EntityType.BAT);
		neutral.add(EntityType.CHICKEN);
		neutral.add(EntityType.COW);
		neutral.add(EntityType.HORSE);
		neutral.add(EntityType.IRON_GOLEM);
		neutral.add(EntityType.MUSHROOM_COW);
		neutral.add(EntityType.OCELOT);
		neutral.add(EntityType.PIG);
		neutral.add(EntityType.SHEEP);
		neutral.add(EntityType.SNOWMAN);
		neutral.add(EntityType.SQUID);
		neutral.add(EntityType.VILLAGER);
		neutral.add(EntityType.WOLF);

		mobs = new ArrayList<EntityType>(hostile.size() + neutral.size());
		mobs.addAll(hostile);
		mobs.addAll(neutral);
	}

	/**
	 * Tries to retrieve the name from the mob using two methods. The first one
	 * uses <code>EntityType.name()</code> , the other one matches against
	 * <code>EntityType.getName()</code> , which sometimes returns weird results,
	 * like i.e. "Ozelot". Matches against the first one is preferred.
	 * 
	 * @param name the name of the entity type
	 * @return An entity type matching the provided name, or
	 *         <code>EntityType.UNKNOWN</code> otherwise.
	 * @see EntityType
	 */
	public static EntityType getTypeFromName(String name) {
		name = name.toLowerCase();
		for (EntityType t : instance.mobs) {
			if (t.name().toLowerCase().equals(name)) return t;
		}
		for (EntityType t : instance.mobs) {
			if (t.getName().toLowerCase().equals(name)) return t;
		}
		return EntityType.UNKNOWN;
	}

	/**
	 * Returns the lowercase entity name of this entity type in the following
	 * manner:
	 * <p>
	 * <code>return type.name().toLowerCase();</code>
	 * </p>
	 * 
	 * @param type the type which we want the name of
	 * @return the name of the entity
	 */
	public static String getEntityName(EntityType type) {
		return type.name().toLowerCase();
	}

	/**
	 * Returns a list of mobs where their name starts with the provided message.
	 * This is primarily used for tab completion.
	 * 
	 * @param msg the beginning of the mobs' names.
	 * @return a list of mobs that starts with the provided message
	 */
	public static List<String> getMobsStartingWith(String msg) {
		ArrayList<String> l = new ArrayList<String>(instance.mobs.size());
		for (EntityType e : instance.mobs) {
			String entityName = getEntityName(e);
			if (entityName.startsWith(msg)) {
				l.add(entityName);
			}
		}
		return l;
	}

	/**
	 * Returns true if the entity type is a mob
	 * 
	 * @param type the type which we want to know if it is a mob or not
	 * @return true if the entity type is a mob
	 */
	public static boolean isMob(EntityType type) {
		return instance.mobs.contains(type);
	}

	/**
	 * Returns true if the entity type is a hostile mob
	 * 
	 * @param type the type which we want to know if it is a hostile mob or not
	 * @return true if the entity type is a hostile mob
	 */
	public static boolean isHostileMob(EntityType type) {
		return instance.hostile.contains(type);
	}

	/**
	 * Returns true if the entity type is a neutral mob
	 * 
	 * @param type the type which we want to know if it is a neutral mob or not
	 * @return true if the entity type is a neutral mob
	 */
	public static boolean isNeutralMob(EntityType type) {
		return instance.neutral.contains(type);
	}

}
