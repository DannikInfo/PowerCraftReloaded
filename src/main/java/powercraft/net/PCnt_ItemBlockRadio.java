package powercraft.net;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCnt_ItemBlockRadio extends PC_ItemBlock {
	
	Block block;

	/**
	 * @param i ID
	 */
	public PCnt_ItemBlockRadio(Block block) {
		super(block);
		this.block = block;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return super.getUnlocalizedName() + "." + (itemstack.getItemDamage() == 0 ? "tx" : "rx");
	}

	@Override
	public IIcon getIconFromDamage(int i) {
		return block.getIcon(i, 0);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
	
	
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		return arrayList;
	}

	//@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName() + ".tx", "Redstone Radio Transmitter"));
		names.add(new LangEntry(getUnlocalizedName() + ".rx", "Redstone Radio Receiver"));
        return names;
	}
	
}
