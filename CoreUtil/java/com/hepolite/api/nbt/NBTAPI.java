package com.hepolite.api.nbt;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutil.util.reflection.reflected.REntity;
import com.hepolite.coreutil.util.reflection.reflected.RItemStack;
import com.hepolite.coreutil.util.reflection.reflected.RNBTTag;

public final class NBTAPI
{
	/**
	 * Attempts to create a new item stack that is compatible with the NBT API right away. If
	 * something goes wrong, a normal item stack is returned instead.
	 * 
	 * @param material The item material
	 * @param amount The item amount
	 * @param meta The item metadata
	 * @return The itemstack
	 */
	public static final ItemStack getItemStack(final Material material, final int amount, final int meta)
	{
		final ItemStack itemStack = new ItemStack(material, amount, (short) meta);
		final ItemStack out = (ItemStack) RItemStack.cbAsCraftCopy.invoke(RItemStack.cbClass.handle, itemStack);
		return out == null ? itemStack : out;
	}

	/**
	 * Checks if the provided item stack has an NBTTag associated with it
	 * 
	 * @param itemStack The item stack to check
	 * @return True iff there is an NBTTag associated with the item
	 */
	public static final boolean hasTag(final ItemStack itemStack)
	{
		final Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		return (boolean) RItemStack.nmsHasTag.invoke(nmsItemStack);
	}
	/**
	 * Attempts to retrieve the NBTTag that is attached to the given item
	 * 
	 * @param itemStack The item that is under scrutiny
	 * @return The tag if it exists, an empty tag otherwise
	 */
	public static final NBTTag getTag(final ItemStack itemStack)
	{
		final Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		return new NBTTag(RItemStack.nmsGetTag.invoke(nmsItemStack));
	}
	/**
	 * Attempts to assign the given NBTTag to the given item. Returns the same item as was passed
	 * in.
	 * 
	 * @param itemStack The item stack that will have the tag attached to it
	 * @param tag The tag to attach to the item stack
	 * @return The same item stack as was passed in
	 */
	public static final ItemStack setTag(final ItemStack itemStack, final NBTTag tag)
	{
		final Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		RItemStack.nmsSetTag.invoke(nmsItemStack, tag.getNBTTagCompound());
		return itemStack;
	}

	/**
	 * Attempts to retrieve the NBTTag that is attached to the given entity
	 * 
	 * @param itemStack The entity that is under scrutiny
	 * @return The tag if it exists, an empty tag otherwise
	 */
	public static final NBTTag getTag(final Entity entity)
	{
		final Object nmsEntity = REntity.cbGetHandle.invoke(entity);
		return new NBTTag(REntity.nmsLoad.invoke(nmsEntity));
	}
	/**
	 * Attempts to assign the given NBTTag to the given entity. Returns the same entity as was
	 * passed in.
	 * 
	 * @param entity The entity that will have the tag attached to it
	 * @param tag The tag to attach to the entity
	 * @return The same entity as was passed in
	 */
	public static final Entity setTag(final Entity entity, final NBTTag tag)
	{
		final Object nmsEntity = REntity.cbGetHandle.invoke(entity);
		REntity.nmsSave.invoke(nmsEntity, tag.getNBTTagCompound());
		return entity;
	}

	// ...

	/**
	 * Attempts to retrieve the type of the NBTTag that is passed in.
	 * 
	 * @param tag The nms NBTTag that is under scrutiny
	 * @return The type of the data, or INVALID if invalid
	 */
	public static final NBTTagType getField(final Object tag)
	{
		final Class<?> nbtClass = tag.getClass();
		if (nbtClass.isAssignableFrom(RNBTTag.Byte.nmsClass.handle))
			return NBTTagType.BYTE;
		else if (nbtClass.isAssignableFrom(RNBTTag.ByteArray.nmsClass.handle))
			return NBTTagType.BYTE_ARRAY;
		else if (nbtClass.isAssignableFrom(RNBTTag.Double.nmsClass.handle))
			return NBTTagType.DOUBLE;
		else if (nbtClass.isAssignableFrom(RNBTTag.Float.nmsClass.handle))
			return NBTTagType.FLOAT;
		else if (nbtClass.isAssignableFrom(RNBTTag.Int.nmsClass.handle))
			return NBTTagType.INT;
		else if (nbtClass.isAssignableFrom(RNBTTag.IntArray.nmsClass.handle))
			return NBTTagType.INT_ARRAY;
		else if (nbtClass.isAssignableFrom(RNBTTag.List.nmsClass.handle))
			return NBTTagType.LIST;
		else if (nbtClass.isAssignableFrom(RNBTTag.Long.nmsClass.handle))
			return NBTTagType.LONG;
		else if (nbtClass.isAssignableFrom(RNBTTag.Short.nmsClass.handle))
			return NBTTagType.SHORT;
		else if (nbtClass.isAssignableFrom(RNBTTag.String.nmsClass.handle))
			return NBTTagType.STRING;
		else if (nbtClass.isAssignableFrom(RNBTTag.Compound.nmsClass.handle))
			return NBTTagType.TAG;
		else
			return NBTTagType.INVALID;
	}
}
