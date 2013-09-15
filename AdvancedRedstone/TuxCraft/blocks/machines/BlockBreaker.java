package AdvancedRedstone.TuxCraft.blocks.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBreaker extends BlockSided
{

    public BlockBreaker(int id, String s)
    {
        super(id, Material.rock, s);
        this.setHardness(3.5F);
        this.setStepSound(soundStoneFootstep);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID)
    {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
    }

    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        Vector thisBlock = new Vector(world, x, y, z);
        Vector target = Assets.getTarget(world, x, y, z);

        boolean f = target.getBlock() != null && thisBlock != null;
        boolean f1 = thisBlock.isIndirectlyPowered();
        boolean f2 = false;
        boolean f3 = false;

        if (f)
        {
            f2 = target.getBlock().blockMaterial.isToolNotRequired() || new ItemStack(Item.pickaxeIron).canHarvestBlock(target.getBlock());
            f3 = target.getBlock().blockHardness != -1.0F;
        }

        if (f && f1 && f2 && f3)
        {

            if (target.getBlock() != null)
            {
                ArrayList<ItemStack> items = target.getBlock().getBlockDropped(world, x, y, z, target.getMeta(), 0);

                if (!world.isRemote)
                {
                    List adjacentBlocks = Assets.getAdjacentBlocks(thisBlock);

                    for (int i = 0; i < adjacentBlocks.size(); i++)
                    {
                        Vector currentVector = ((Vector) adjacentBlocks.get(i));
                        Block currentBlock = currentVector.getBlock();

                        if (currentBlock != null && currentBlock.hasTileEntity(currentVector.getMeta()))
                        {
                            if (currentVector.getTile() instanceof IInventory)
                            {
                                IInventory inventory = (IInventory) currentVector.getTile();

                                if (!world.isRemote)
                                {
                                    boolean flag2 = false;

                                    if (flag2 == false)
                                    {
                                        for (int iii = 0; iii < inventory.getSizeInventory(); iii++)
                                        {
                                            for (int ii = 0; ii < items.size(); ii++)
                                            {
                                                if (!items.isEmpty())
                                                {
                                                    ItemStack stack = items.get(ii);

                                                    if (inventory.getStackInSlot(iii) != null && inventory.getStackInSlot(iii).isItemEqual(stack) && inventory.getStackInSlot(iii).stackSize != inventory.getStackInSlot(iii).getMaxStackSize())
                                                    {
                                                        inventory.getStackInSlot(iii).stackSize = inventory.getStackInSlot(iii).stackSize + stack.stackSize;
                                                        items.remove(ii);
                                                        flag2 = true;

                                                        world.destroyBlock(target.getX(), target.getY(), target.getZ(), false);
                                                    }

                                                    else if (inventory.getStackInSlot(iii) == null)
                                                    {
                                                        inventory.setInventorySlotContents(iii, stack);
                                                        items.remove(ii);
                                                        flag2 = true;

                                                        world.destroyBlock(target.getX(), target.getY(), target.getZ(), false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                world.destroyBlock(target.getX(), target.getY(), target.getZ(), true);
            }
        }
    }
}
