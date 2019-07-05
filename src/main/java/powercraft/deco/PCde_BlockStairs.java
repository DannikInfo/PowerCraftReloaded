package powercraft.deco;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.mod_PowerCraft;

@PC_BlockInfo(name = "Stairs", tileEntity = PCde_TileEntityStairs.class, canPlacedRotated = true)
public class PCde_BlockStairs extends PC_Block {

	public PCde_BlockStairs(int id) {
		super(Material.rock, "ironplate", "fence");
		setHardness(1.5F);
		setResistance(30.0F);
		setStepSound(Block.soundTypeMetal);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack is) {
		int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}

	/**
	 * Get fences that are shown for stairs.
	 * 
	 * @param world world
	 * @param pos   block pos
	 * @return bool{X+, X-, Z+, Z-}
	 */
	private boolean[] getFencesShownStairsAbsolute(World world, PC_VecI pos) {
		boolean fences[] = { false, false, false, false };

		PC_Direction dir = getRotation(PC_Utils.getMD(world, pos)).mirror();

		if (dir == PC_Direction.FRONT) {
			fences[0] = fences[1] = true;
		} else if (dir == PC_Direction.RIGHT) {
			fences[2] = fences[3] = true;
		} else if (dir == PC_Direction.BACK) {
			fences[0] = fences[1] = true;
		} else if (dir == PC_Direction.LEFT) {
			fences[2] = fences[3] = true;
		}

		fences[0] &= isFallBlock(world, pos.offset(1, 0, 0)) && isFallBlock(world, pos.offset(1, -1, 0));
		fences[1] &= isFallBlock(world, pos.offset(-1, 0, 0)) && isFallBlock(world, pos.offset(-1, -1, 0));
		fences[2] &= isFallBlock(world, pos.offset(0, 0, 1)) && isFallBlock(world, pos.offset(0, -1, 1));
		fences[3] &= isFallBlock(world, pos.offset(0, 0, -1)) && isFallBlock(world, pos.offset(0, -1, -1));
		return fences;
	}

	/**
	 * Get which stair sides should be shown (left,right)
	 * 
	 * @param world
	 * @param pos
	 * @return left, right
	 */
	public boolean[] getFencesShownStairsRelative(World world, PC_VecI pos) {
		boolean fences[] = getFencesShownStairsAbsolute(world, pos);
		boolean rel[] = { false, false };

		PC_Direction dir = getRotation(PC_Utils.getMD(world, pos)).mirror();

		if (dir == PC_Direction.FRONT) {
			rel[0] = fences[0];
			rel[1] = fences[1];
		} else if (dir == PC_Direction.RIGHT) {
			rel[0] = fences[2];
			rel[1] = fences[3];
		} else if (dir == PC_Direction.BACK) {
			rel[0] = fences[1];
			rel[1] = fences[0];
		} else if (dir == PC_Direction.LEFT) {
			rel[0] = fences[3];
			rel[1] = fences[2];
		}

		return rel;
	}

	private static boolean isFallBlock(World world, PC_VecI pos) {
		Block block = PC_Utils.getBID(world, pos);
		if (block == Blocks.air || block == null) {
			return true;
		}

		if (block == Blocks.ladder || block == Blocks.vine) {
			return false;
		}

		if (block.getCollisionBoundingBoxFromPool(world, pos.x, pos.y, pos.z) == null) {
			return true;
		}
		if (block.getMaterial().isLiquid() || !block.getMaterial().isSolid()) {
			return true;
		}
		if (PC_MSGRegistry.hasFlag(world, pos, "BELT")) {
			return true;
		}
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	public void addACollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist,
			Entity entity) {
		AxisAlignedBB axisalignedbb1 = super.getCollisionBoundingBoxFromPool(world, x, y, z);

		if (axisalignedbb1 != null && axisalignedbb.intersectsWith(axisalignedbb1)) {
			arraylist.add(axisalignedbb1);
		}
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist,
			Entity entity) {

		PC_Direction dir = getRotation(PC_Utils.getMD(world, x, y, z)).mirror();

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		if (dir == PC_Direction.FRONT) {
			setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.RIGHT) {
			setBlockBounds(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.BACK) {
			setBlockBounds(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.LEFT) {
			setBlockBounds(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}

		// X+, X-, Z+, Z-
		boolean[] fences = getFencesShownStairsAbsolute(world, new PC_VecI(x, y, z));

		if (fences[0]) {
			setBlockBounds(1 - 0.0625F, 0, 0, 1, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[1]) {
			setBlockBounds(0, 0, 0, 0.0625F, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[2]) {
			setBlockBounds(0, 0, 1 - 0.0625F, 1, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[3]) {
			setBlockBounds(0, 0, 0, 1, 1.8F, 0.0625F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean renderInventoryBlock(int metadata, Object renderer) {

		PCde_ModelStairs model = new PCde_ModelStairs();

		float f = 1.0F;

		Minecraft.getMinecraft().getTextureManager()
		.bindTexture(new ResourceLocation(mod_PowerCraft.MODID, PC_TextureRegistry.getPowerCraftImageDir()
				+ PC_TextureRegistry.getTextureName(PCde_App.instance, "block_deco.png")));

		PC_Renderer.glPushMatrix();
		PC_Renderer.glRotatef(180 + 90, 0, 1, 0);
		PC_Renderer.glScalef(f, -f, -f);

		model.setStairsFences(false, true);

		model.render();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();

		return true;

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}

	@Override
	public PC_VecI moveBlockTryToPlaceAt(World world, int x, int y, int z, PC_Direction dir, float xHit, float yHit,
			float zHit, ItemStack itemStack, EntityPlayer entityPlayer) {

		Item item = itemStack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			PC_Direction rot = getRotation(PC_Utils.getMD(world, x, y, z));
			PC_Direction pRot = PC_Direction
					.getFromPlayerDir(MathHelper.floor_double(((entityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3);
			PC_VecI offset = pRot.getOffset();
			if (rot == pRot) {
				if (block == PCde_App.stairs || block == PCde_App.platform) {
					offset.y++;
				}
			} else if (rot == pRot.mirror()) {
				if (block == PCde_App.stairs && PC_KeyRegistry.isPlacingReversed(entityPlayer)) {
					offset.y--;
				}
			}
			return offset;
		}
		return null;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		setBlockBounds(0, 0, 0, 1, 1.5f, 1);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}
}
