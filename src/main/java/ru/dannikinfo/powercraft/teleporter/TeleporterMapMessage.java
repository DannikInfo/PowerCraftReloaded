package ru.dannikinfo.powercraft.teleporter;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.dannikinfo.powercraft.api.network.AbstractMessage;
import ru.dannikinfo.powercraft.transport.gui.GuiEjectBelt;
import ru.dannikinfo.powercraft.transport.tile.TileEntityEjectionBelt;

public class TeleporterMapMessage extends AbstractMessage<TeleporterMapMessage>{
	
	private int a, s, stacks, items, x, y, z;
	
	public TeleporterMapMessage() {}	

	public TeleporterMapMessage(int a, int s, int stacks, int items, int x, int y, int z) {
		this.a = a;
		this.s = s;
		this.stacks = stacks;
		this.items = items;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void read(PacketBuffer buffer){
		a = buffer.readInt();
		s = buffer.readInt();
		stacks = buffer.readInt();
		items = buffer.readInt();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
	}

	@Override
	protected void write(PacketBuffer buffer) {
		buffer.writeInt(a);
		buffer.writeInt(s);
		buffer.writeInt(stacks);
		buffer.writeInt(items);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void process(EntityPlayer player, Side side) {
		TileEntityEjectionBelt tile = (TileEntityEjectionBelt) player.getEntityWorld().getTileEntity(x, y, z);
		if(side.isServer()){
			if(items != 0 && stacks != 0){
				tile.setActionType(a);
				tile.setItemSelectMode(s);
				tile.setNumItemsEjected(items);
				tile.setNumStacksEjected(stacks);
				tile.guiOpen = false;
			}
		}else{
			GuiEjectBelt.a = a;
			GuiEjectBelt.s = s;
			GuiEjectBelt.items = items;
			GuiEjectBelt.stacks = stacks;
		}
		
	}

}
