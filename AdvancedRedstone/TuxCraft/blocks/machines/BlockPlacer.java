package AdvancedRedstone.TuxCraft.blocks.machines;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.WorldBlock;
import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import AdvancedRedstone.TuxCraft.blocks.tileEntity.TileEntityBlockPlacer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlacer extends BlockSided implements ITileEntityProvider
{

    public BlockPlacer(int id, String s)
    {
        super(id, Material.rock, s);
        this.setHardness(3.5F);
        this.setStepSound(soundStoneFootstep);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID)
    {
        Vector thisBlock = new Vector(world, x, y, z);

        if (thisBlock.isIndirectlyPowered())
        {
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
        }
    }

    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        Vector thisBlock = new Vector(world, x, y, z);
        Vector target = Assets.getTarget(thisBlock);

        TileEntityBlockPlacer tile = (TileEntityBlockPlacer) thisBlock.getTile();

        if (tile != null && target.isAirBlock())
        {
            int l = tile.getRandomStackFromInventory();

            ItemStack stack = tile.getStackInSlot(l < 0 ? 0 : l);

            if (stack != null && stack.getItem() instanceof ItemBlock)
            {
                Block block = Block.blocksList[((ItemBlock) stack.getItem()).getBlockID()];
                int side = 0;
                int meta = stack.getItemDamage();

                TileEntity tileentity = null;
                if (block.hasTileEntity(meta))
                    tileentity = block.createTileEntity(world, meta);

                WorldBlock blockToPlace = new WorldBlock(block, tileentity, meta);

                if (Assets.canPlaceBlockAt(target, block))
                {

                    for (int i = 6; i >= 0; i--)
                    {
                        if (Assets.canPlaceBlockOnSide(target, block, i))
                        {
                            side = i;

                            if (Assets.setWorldBlock(blockToPlace, target))
                            {

                                ItemStack stack2 = stack;
                                stack2.stackSize -= 1;
                                tile.setInventorySlotContents(l, stack2.stackSize == 0 ? null : stack2);
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityBlockPlacer();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
    {
        super.onBlockActivated(world, x, y, z, player, side, offsetX, offsetY, offsetZ);

        if (world.isRemote)
        {
            return true;
        }

        else
        {
            TileEntityBlockPlacer tile = (TileEntityBlockPlacer) world.getBlockTileEntity(x, y, z);

            if (tile != null)
            {
                player.openGui(AdvancedRedstoneCore.instance, 0, world, x, y, z);
                return true;
            }

            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory))
        {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0)
            {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }
}
