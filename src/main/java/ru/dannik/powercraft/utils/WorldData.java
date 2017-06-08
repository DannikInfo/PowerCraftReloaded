package ru.dannik.powercraft.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import ru.dannik.powercraft.Main;

public class WorldData extends WorldSavedData{

	final static String key = Main.MODID;
	
	   public static WorldData forWorld(World world) {
	      MapStorage storage = world.perWorldStorage;
	      WorldData result = (WorldData)storage.loadData(WorldData.class, key);
	      if (result == null) {
	         result = new WorldData(key);
	         storage.setData(key, result);
	      }
	      return result;
	   }
	   
	   private NBTTagCompound data = new NBTTagCompound();

	   public WorldData(String tagName) {
	       super(tagName);
	   }

	   @Override
	   public void readFromNBT(NBTTagCompound compound) {
	  	 data = compound.getCompoundTag(key);
	   }

	   @Override
	   public void writeToNBT(NBTTagCompound compound) {
	       compound.setTag(key, data);
	   }

	   public NBTTagCompound getData() {
	       return data;
	   }
}
