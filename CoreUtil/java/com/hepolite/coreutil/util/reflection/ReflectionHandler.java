package com.hepolite.coreutil.util.reflection;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.coreutil.util.reflection.reflected.RAxisAlignedBB;
import com.hepolite.coreutil.util.reflection.reflected.REntity;
import com.hepolite.coreutil.util.reflection.reflected.RItemStack;
import com.hepolite.coreutil.util.reflection.reflected.RMovingObjectPosition;
import com.hepolite.coreutil.util.reflection.reflected.RNBTTag;
import com.hepolite.coreutil.util.reflection.reflected.RVec3D;
import com.hepolite.coreutil.util.reflection.reflected.RWorld;

public final class ReflectionHandler
{
	private final String version;
	private final IConfig config;
	private final JavaPlugin plugin;

	public ReflectionHandler(final JavaPlugin plugin)
	{
		this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		this.config = ConfigFactory.create(plugin, "ReflectionMappings");
		this.plugin = plugin;

		initialize();
	}

	private void initialize()
	{
		plugin.getLogger().info("Loading reflection utilities for " + version + "...");

		initializeVec3D();
		initializeMovingObjectPosition();
		initializeAxisAlignedBB();
		initializeWorld();
		initializeNBT();
		initializeEntity();
		initializeItemStack();

		plugin.getLogger().info("Done setting up reflection utilities!");
	}
	private void initializeAxisAlignedBB()
	{
		/// @formatter:off
		RAxisAlignedBB.nmsClass = getNMSClass("AxisAlignedBB");
		RAxisAlignedBB.nmsMinX = getNMSField(RAxisAlignedBB.nmsClass, "minX");
		RAxisAlignedBB.nmsMinY = getNMSField(RAxisAlignedBB.nmsClass, "minY");
		RAxisAlignedBB.nmsMinZ = getNMSField(RAxisAlignedBB.nmsClass, "minZ");
		RAxisAlignedBB.nmsMaxX = getNMSField(RAxisAlignedBB.nmsClass, "maxX");
		RAxisAlignedBB.nmsMaxY = getNMSField(RAxisAlignedBB.nmsClass, "maxY");
		RAxisAlignedBB.nmsMaxZ = getNMSField(RAxisAlignedBB.nmsClass, "maxZ");
		/// @formatter:on
	}
	private void initializeEntity()
	{
		/// @formatter:off
		REntity.nmsClass = getNMSClass("Entity");
		REntity.nmsGetBoundingBox = getNMSMethod(REntity.nmsClass, "getBoundingBox");
		REntity.nmsSave = getNMSMethod(REntity.nmsClass, "save", RNBTTag.Compound.nmsClass.handle);
		REntity.nmsLoad = getNMSMethod(REntity.nmsClass, "load", RNBTTag.Compound.nmsClass.handle);
		REntity.cbClass = getCBClass("entity.Entity");
		REntity.cbGetHandle = getCBMethod(REntity.cbClass, "getHandle");
		/// @formatter:on
	}
	private void initializeItemStack()
	{
		/// @formatter:off
		RItemStack.nmsClass = getNMSClass("ItemStack");
		RItemStack.nmsSetTag = getNMSMethod(RItemStack.nmsClass, "setTag", RNBTTag.Compound.nmsClass.handle);
		RItemStack.nmsGetTag = getNMSMethod(RItemStack.nmsClass, "getTag");
		RItemStack.nmsHasTag = getNMSMethod(RItemStack.nmsClass, "hasTag");
		RItemStack.cbClass = getCBClass("inventory.ItemStack");
		RItemStack.cbAsCraftCopy = getCBMethod(RItemStack.cbClass, "asCraftCopy", ItemStack.class);
		RItemStack.cbHandle = getCBField(RItemStack.cbClass, "handle");
		/// @formatter:on
	}
	private void initializeNBT()
	{
		/// @formatter:off
		RNBTTag.Base.nmsClass = getNMSClass("NBTBase");
		RNBTTag.Byte.nmsClass = getNMSClass("NBTTagByte");
		RNBTTag.Byte.nmsAsByte = getNMSMethod(RNBTTag.Byte.nmsClass, "asByte");
		RNBTTag.ByteArray.nmsClass = getNMSClass("NBTTagByteArray");
		RNBTTag.ByteArray.nmsAsByteArray = getNMSMethod(RNBTTag.ByteArray.nmsClass, "asByteArray");
		RNBTTag.Compound.nmsClass = getNMSClass("NBTTagCompound");
		RNBTTag.Compound.nmsGetByte = getNMSMethod(RNBTTag.Compound.nmsClass, "getByte", String.class);
		RNBTTag.Compound.nmsGetByteArray = getNMSMethod(RNBTTag.Compound.nmsClass, "getByteArray", String.class);
		RNBTTag.Compound.nmsGetCompound = getNMSMethod(RNBTTag.Compound.nmsClass, "getCompound", String.class);
		RNBTTag.Compound.nmsGetDouble = getNMSMethod(RNBTTag.Compound.nmsClass, "getDouble", String.class);
		RNBTTag.Compound.nmsGetFloat = getNMSMethod(RNBTTag.Compound.nmsClass, "getFloat", String.class);
		RNBTTag.Compound.nmsGetInt = getNMSMethod(RNBTTag.Compound.nmsClass, "getInt", String.class);
		RNBTTag.Compound.nmsGetIntArray = getNMSMethod(RNBTTag.Compound.nmsClass, "getIntArray", String.class);
		RNBTTag.Compound.nmsGetKeys = getNMSMethod(RNBTTag.Compound.nmsClass, "getKeys");
		RNBTTag.Compound.nmsGetLong = getNMSMethod(RNBTTag.Compound.nmsClass, "getLong", String.class);
		RNBTTag.Compound.nmsGetShort = getNMSMethod(RNBTTag.Compound.nmsClass, "getShort", String.class);
		RNBTTag.Compound.nmsGetString = getNMSMethod(RNBTTag.Compound.nmsClass, "getString", String.class);
		RNBTTag.Compound.nmsGetTag = getNMSMethod(RNBTTag.Compound.nmsClass, "getTag", String.class);
		RNBTTag.Compound.nmsHasKey = getNMSMethod(RNBTTag.Compound.nmsClass, "hasKey", String.class);
		RNBTTag.Compound.nmsRemove = getNMSMethod(RNBTTag.Compound.nmsClass, "remove", String.class);
		RNBTTag.Compound.nmsSetByte = getNMSMethod(RNBTTag.Compound.nmsClass, "setByte", String.class, byte.class);
		RNBTTag.Compound.nmsSetByteArray = getNMSMethod(RNBTTag.Compound.nmsClass, "setByteArray", String.class, byte[].class);
		RNBTTag.Compound.nmsSetDouble = getNMSMethod(RNBTTag.Compound.nmsClass, "setDouble", String.class, double.class);
		RNBTTag.Compound.nmsSetFloat = getNMSMethod(RNBTTag.Compound.nmsClass, "setFloat", String.class, float.class);
		RNBTTag.Compound.nmsSetInt = getNMSMethod(RNBTTag.Compound.nmsClass, "setInt", String.class, int.class);
		RNBTTag.Compound.nmsSetIntArray = getNMSMethod(RNBTTag.Compound.nmsClass, "setIntArray", String.class, int[].class);
		RNBTTag.Compound.nmsSetLong = getNMSMethod(RNBTTag.Compound.nmsClass, "setLong", String.class, long.class);
		RNBTTag.Compound.nmsSetShort = getNMSMethod(RNBTTag.Compound.nmsClass, "setShort", String.class, short.class);
		RNBTTag.Compound.nmsSetString = getNMSMethod(RNBTTag.Compound.nmsClass, "setString", String.class, String.class);
		RNBTTag.Compound.nmsSetTag = getNMSMethod(RNBTTag.Compound.nmsClass, "setTag", String.class, RNBTTag.Base.nmsClass.handle);
		RNBTTag.Double.nmsClass = getNMSClass("NBTTagDouble");
		RNBTTag.Double.nmsAsDouble = getNMSMethod(RNBTTag.Double.nmsClass, "asDouble");
		RNBTTag.Float.nmsClass = getNMSClass("NBTTagFloat");
		RNBTTag.Float.nmsAsFloat = getNMSMethod(RNBTTag.Float.nmsClass, "asFloat");
		RNBTTag.Int.nmsClass = getNMSClass("NBTTagInt");
		RNBTTag.Int.nmsAsInt = getNMSMethod(RNBTTag.Int.nmsClass, "asInt");
		RNBTTag.IntArray.nmsClass = getNMSClass("NBTTagIntArray");
		RNBTTag.IntArray.nmsAsIntArray = getNMSMethod(RNBTTag.IntArray.nmsClass, "asIntArray");
		RNBTTag.List.nmsClass = getNMSClass("NBTTagList");
		RNBTTag.List.nmsAdd = getNMSMethod(RNBTTag.List.nmsClass, "add", RNBTTag.Base.nmsClass.handle);
		RNBTTag.List.nmsGet = getNMSMethod(RNBTTag.List.nmsClass, "get", int.class);
		RNBTTag.List.nmsRemove = getNMSMethod(RNBTTag.List.nmsClass, "remove", int.class);
		RNBTTag.List.nmsSize = getNMSMethod(RNBTTag.List.nmsClass, "size");
		RNBTTag.Long.nmsClass = getNMSClass("NBTTagLong");
		RNBTTag.Long.nmsAsLong = getNMSMethod(RNBTTag.Long.nmsClass, "asLong");
		RNBTTag.Short.nmsClass = getNMSClass("NBTTagShort");
		RNBTTag.Short.nmsAsShort = getNMSMethod(RNBTTag.Short.nmsClass, "asShort");
		RNBTTag.String.nmsClass = getNMSClass("NBTTagString");
		RNBTTag.String.nmsAsString = getNMSMethod(RNBTTag.String.nmsClass, "asString");
		/// @formatter:on
	}
	private void initializeMovingObjectPosition()
	{
		/// @formatter:off
		RMovingObjectPosition.nmsClass = getNMSClass("MovingObjectPosition");
		RMovingObjectPosition.nmsPos = getNMSField(RMovingObjectPosition.nmsClass, "pos");
		/// @formatter:on
	}
	private void initializeVec3D()
	{
		/// @formatter:off
		RVec3D.nmsClass = getNMSClass("Vec3D");
		RVec3D.nmsConstructor = getConstructor(RVec3D.nmsClass, double.class, double.class, double.class);
		RVec3D.nmsX = getNMSField(RVec3D.nmsClass, "x");
		RVec3D.nmsY = getNMSField(RVec3D.nmsClass, "y");
		RVec3D.nmsZ = getNMSField(RVec3D.nmsClass, "z");
		/// @formatter:on
	}
	private void initializeWorld()
	{
		/// @formatter:off
		RWorld.nmsClass = getNMSClass("World");
		RWorld.nmsRayTracePrimary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.handle, RVec3D.nmsClass.handle);
		RWorld.nmsRayTraceSecondary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.handle, RVec3D.nmsClass.handle, boolean.class);
		RWorld.nmsRayTraceTertiary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.handle, RVec3D.nmsClass.handle, boolean.class, boolean.class, boolean.class);
		RWorld.cbClass = getCBClass("World");
		RWorld.cbGetHandle = getCBMethod(RWorld.cbClass, "getHandle");
		/// @formatter:on
	}

	// ...

	/**
	 * Attempts to retrieve the net.minecraft.src class with the given name. If the class was not
	 * found, an empty reflected class will be returned.
	 * 
	 * @param name The name of the NMS class
	 * @return The reflected class
	 */
	private ReflectedClass getNMSClass(final String name)
	{
		final Property translation = new Property(version + "." + name, "class");
		final String nmsName = config.getString(translation);

		try
		{
			return new ReflectedClass(name, Class.forName("net.minecraft.server." + version + "." + nmsName));
		}
		catch (final ClassNotFoundException e)
		{
			plugin.getLogger().warning(
					String.format("NMS Class '%s' -> '%s' was not found. Is the mapping correct?", name, nmsName));
		}
		return new ReflectedClass(name, null);
	}
	/**
	 * Attempts to retrieve the net.minecraft.src method with the given name under the given
	 * reflected class. If the method is not found or the method signature does not match the
	 * method, an empty reflected method will be returned.
	 * 
	 * @param base The base class containing the method
	 * @param name The name of the method to retrieve
	 * @param args The method signature, collection of the signature classes
	 * @return The reflected method
	 */
	private ReflectedMethod getNMSMethod(final ReflectedClass base, final String name, final Class<?>... args)
	{
		final Property translation = new Property(version + "." + base.name + "." + "method", name);
		final String nmsName = config.getString(translation);

		try
		{
			return new ReflectedMethod(name, base.handle.getDeclaredMethod(nmsName, args));
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			plugin.getLogger().warning(String.format(
					"NMS Method '%s' -> '%s' with signature '%s' was not found in %s. Is the mapping and signature correct?",
					name, nmsName, signature(args), base));
		}
		return new ReflectedMethod(name, null);
	}
	/**
	 * Attempts to retrieve the net.minecraft.src field with the given name under the given
	 * reflected class. If the field is not found, an empty reflected field will be returned.
	 * 
	 * @param base The base class containing the field
	 * @param name The name of the field to retrieve
	 * @return The reflected field
	 */
	private ReflectedField getNMSField(final ReflectedClass base, final String name)
	{
		final Property translation = new Property(version + "." + base.name + "." + "field", name);
		final String nmsName = config.getString(translation);

		try
		{
			return new ReflectedField(name, base.handle.getDeclaredField(nmsName));
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			plugin.getLogger().warning(String.format(
					"NMS Field '%s' -> '%s' was not found in %s. Is the mapping correct?", name, nmsName, base));
		}
		return new ReflectedField(name, null);
	}

	/**
	 * Attempts to retrieve the org.bukkit.craftbukkit class with the given name. If the class was
	 * not found, an empty reflected class will be returned.
	 * 
	 * @param name The name of the CB class
	 * @return The reflected class
	 */
	private ReflectedClass getCBClass(final String path)
	{
		final String name = path.substring(path.lastIndexOf('.') + 1);
		final String fullPath = "org.bukkit.craftbukkit." + version + "." + path.replace(name, "Craft" + name);
		try
		{
			return new ReflectedClass(name, Class.forName(fullPath));
		}
		catch (final ClassNotFoundException e)
		{
			plugin.getLogger()
					.warning(String.format("CB Class '%s' under %s was not found. Is the path correct?", name, path));
		}
		return new ReflectedClass(name, null);
	}
	/**
	 * Attempts to retrieve the org.bukkit.craftbukkit method with the given name under the given
	 * reflected class. If the method is not found or the method signature does not match the
	 * method, an empty reflected method will be returned.
	 * 
	 * @param base The base class containing the method
	 * @param name The name of the method to retrieve
	 * @param args The method signature, collection of the signature classes
	 * @return The reflected method
	 */
	private ReflectedMethod getCBMethod(final ReflectedClass base, final String name, final Class<?>... args)
	{
		try
		{
			return new ReflectedMethod(name, base.handle.getDeclaredMethod(name, args));
		}
		catch (final Exception e)
		{
			plugin.getLogger().warning(String.format(
					"CB Method '%s' with signature '%s' was not found in %s. Is the name and signature correct?", name,
					signature(args), base));
		}
		return new ReflectedMethod(name, null);
	}
	/**
	 * Attempts to retrieve the org.bukkit.craftbukkit field with the given name under the given
	 * reflected class. If the field is not found, an empty reflected field will be returned.
	 * 
	 * @param base The base class containing the field
	 * @param name The name of the field to retrieve
	 * @return The reflected field
	 */
	private ReflectedField getCBField(final ReflectedClass base, final String name)
	{
		try
		{
			return new ReflectedField(name, base.handle.getDeclaredField(name));
		}
		catch (final Exception e)
		{
			plugin.getLogger().warning(String
					.format("CB Method '%s' was not found in %s. Is the name and the arguments correct?", name, base));
		}
		return new ReflectedField(name, null);
	}

	/**
	 * Attempts to retrieve the constructor with the given signature under the given reflected
	 * class. If the constructor is not found or the constructor signature does not match the
	 * constructor, an empty reflected constructor will be returned.
	 * 
	 * @param base The base class containing the method
	 * @param args The method signature, collection of the signature classes
	 * @return The reflected constructor
	 */
	private ReflectedConstructor getConstructor(final ReflectedClass base, final Class<?>... args)
	{
		try
		{
			return new ReflectedConstructor(base.handle.getConstructor(args));
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			plugin.getLogger()
					.warning(String.format(
							"Constructor with signature '%s' was not found in %s. Is the signature correct?",
							signature(args), base));
		}
		return new ReflectedConstructor(null);
	}

	// ...

	private String signature(final Class<?>... args)
	{
		final StringBuilder builder = new StringBuilder();
		for (final Class<?> cls : args)
		{
			if (builder.length() != 0)
				builder.append(", ");
			builder.append(cls.getName());
		}
		if (builder.length() == 0)
			builder.append("void");
		return builder.toString();
	}
}
