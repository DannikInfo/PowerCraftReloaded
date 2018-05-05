package powercraft.transport;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Splitter", tileEntity=PCtr_TileEntitySplitter.class)
public class PCtr_BlockSplitter extends PC_Block {

	public static int color[] = {0x49C0FF, 0xFF4C7B, 0xFF8849, 0xE8FF42, 0x4CFF7F, 0x5149FF};
	
	public PCtr_BlockSplitter(int id) {
		super(PCtr_MaterialElevator.getMaterial(), "splitter0", "splitter1", "splitter2", "splitter3", "splitter4", "splitter5");
		setHardness(0.5F);
        setResistance(8.0F);
        setStepSound(Block.soundTypeMetal);
        setCreativeTab(CreativeTabs.tabTransport);
        setBlockName("PCtr_BlockSplitter");
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        PCtr_TileEntitySplitter tes = (PCtr_TileEntitySplitter) world.getTileEntity(i, j, k);
        PC_Direction redir = tes.calculateItemDirection(entity);

        PC_VecI pos_leading_to = pos.offset(redir.getOffset());
        
        if (entity instanceof EntityItem && PCtr_BeltHelper.storeEntityItemAt(world, pos_leading_to, (EntityItem) entity, redir))
        {
            return;
        }
        
        boolean leadsToNowhere = PCtr_BeltHelper.isBlocked(world, pos_leading_to);

        if (!leadsToNowhere)
        {
            PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
        }

        if(redir.getMCSide()<4){
        	entity.motionY=0;
        	entity.onGround=true;
        }
        
        leadsToNowhere = leadsToNowhere && PCtr_BeltHelper.isBeyondStorageBorder(world, redir, pos, entity, PCtr_BeltHelper.STORAGE_BORDER_LONG);
        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, redir, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                PCtr_BeltHelper.HORIZONTAL_BOOST);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (PCtr_BeltHelper.blockActivated(world, i, j, k, entityplayer))
        {
            return true;
        }
        else
        {
            PC_GresRegistry.openGres("Splitter", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l){
        return true;
    }

}
