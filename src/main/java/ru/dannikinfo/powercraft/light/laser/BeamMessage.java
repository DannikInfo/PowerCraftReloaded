package ru.dannikinfo.powercraft.light.laser;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.dannikinfo.powercraft.api.network.AbstractMessage.AbstractServerMessage;
import ru.dannikinfo.powercraft.api.utils.BaseUtils;

public class BeamMessage extends AbstractServerMessage<BeamMessage>{
	
	private int count, dir, x, y, z, r, g, b, death;
	
	public BeamMessage() {}	

	public BeamMessage(int count, int dir, int x, int y, int z, int r, int g, int b, int death) {
		this.count = count;
		this.dir = dir;
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.death = death;
	}
	
	@Override
	protected void read(PacketBuffer buffer){
		count = buffer.readInt();
		dir = buffer.readInt();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		r = buffer.readInt();
		g = buffer.readInt();
		b = buffer.readInt();
		death = buffer.readInt();
	}

	@Override
	protected void write(PacketBuffer buffer) {
		buffer.writeInt(count);
		buffer.writeInt(dir);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(r);
		buffer.writeInt(g);
		buffer.writeInt(b);
		buffer.writeInt(death);
	}

	@Override
	public void process(EntityPlayer player, Side side) {	
		BaseUtils.traceBeamMessageRe(player.getEntityWorld(), count, dir, x, y, z, player, r, g, b, death);
	}

}
