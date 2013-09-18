package AdvancedRedstone.TuxCraft.blocks.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.WorldBlock;
import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import AdvancedRedstone.TuxCraft.client.ClientProxy;
import AdvancedRedstone.TuxCraft.entity.EntityMovingBlock;

public class BlockAdvancedPiston extends BlockSided
{

    public static int pistonStrength = 48;
    public static int time           = 20;
    private boolean   sticky;

    public BlockAdvancedPiston(int id, String s, boolean isSticky)
    {
        super(id, Material.piston, s);
        this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setStepSound(soundStoneFootstep);
        this.setHardness(0.5F);
        this.sticky = isSticky;
    }

    /**
     * public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
     * {
     * world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
     * }
     */

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
    {
        if (player.inventory.getCurrentItem() == null)
        {
            Vector thisBlock = new Vector(world, x, y, z);

            if (this.isPistonExtended(thisBlock))
            {
                this.retractPiston(world, x, y, z);
            }

            else
            {
                this.extendPiston(world, x, y, z);
            }
        }

        return true;
    }

    private void retractPiston(World world, int x, int y, int z)
    {
        Vector thisBlock = new Vector(world, x, y, z);
        int[] dirArray = Assets.getDirectionArray(thisBlock.getMeta());

        Vector extTarget = Assets.getTarget(thisBlock);

        List<Vector> targets = getVectorsInARow(extTarget, pistonStrength);

        if (this.sticky)
        {
            for (int i = 0; i < targets.size(); i++)
            {
                Vector target = targets.get(i);
                WorldBlock block = Assets.getWorldBlock(target);

                if (!target.isAirBlock())
                {
                    EntityMovingBlock movingBlock = new EntityMovingBlock(world, target, target.plus(-dirArray[0], -dirArray[1], -dirArray[2]), block, time, true);
                    world.spawnEntityInWorld(movingBlock);

                    world.removeBlockTileEntity(target.getX(), target.getY(), target.getZ());

                    if (target != extTarget)
                    {
                        Assets.setVectorToAir(target);
                    }
                }
            }
        }

        Assets.setVectorToAir(extTarget);
    }

    private void extendPiston(World world, int x, int y, int z)
    {
        Vector thisBlock = new Vector(world, x, y, z);
        int[] dirArray = Assets.getDirectionArray(thisBlock.getMeta());

        List<Vector> targets = getVectorsInARow(thisBlock, pistonStrength);

        if (targets.size() == pistonStrength)
        {
            return;
        }

        Vector extTarget = Assets.getTarget(thisBlock);
        WorldBlock extBlock = new WorldBlock(AdvancedRedstoneCore.advPistonExtension, thisBlock.getMeta());

        for (int i = 0; i < targets.size(); i++)
        {
            Vector target = targets.get(i);
            WorldBlock block = Assets.getWorldBlock(target);

            if (!target.isAirBlock())
            {
                EntityMovingBlock movingBlock = new EntityMovingBlock(world, target, target.plus(dirArray[0], dirArray[1], dirArray[2]), block, time, true);
                world.spawnEntityInWorld(movingBlock);

                world.removeBlockTileEntity(target.getX(), target.getY(), target.getZ());

                Assets.setVectorToAir(target);
            }
        }

        Assets.setWorldBlock(extBlock, extTarget);
    }

    public static List<Vector> getVectorsInARow(Vector originalVec, int length)
    {
        World world = originalVec.getWorld();

        List<Vector> vectorList = new ArrayList();

        int[] dirArray = Assets.getDirectionArray(originalVec.getMeta());

        int x = originalVec.getX();
        int y = originalVec.getY();
        int z = originalVec.getZ();

        for (int i = 0; i < length; i++)
        {
            x += dirArray[0];
            y += dirArray[1];
            z += dirArray[2];

            Vector vec = new Vector(world, x, y, z);

            if (vec.isAirBlock())
            {
                return vectorList;
            }

            vectorList.add(vec);
        }

        return vectorList;
    }

    public static int getOrientation(int par0)
    {
        return par0 & 7;
    }

    public int getRenderType()
    {
        return ClientProxy.typeAdvancedPiston;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * public void setBlockBoundsBasedOnState(Vector thisBlock)
     * {
     * int l = thisBlock.getMeta();
     * if (this.isPistonExtended(thisBlock))
     * {
     * float f = 0.25F;
     * switch (getOrientation(l))
     * {
     * case 0:
     * this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
     * break;
     * case 1:
     * this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
     * break;
     * case 2:
     * this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
     * break;
     * case 3:
     * this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
     * break;
     * case 4:
     * this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
     * break;
     * case 5:
     * this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
     * }
     * }
     * else
     * {
     * this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
     * }
     * }
     */

    public static boolean isPistonExtended(Vector thisBlock)
    {
        World world = thisBlock.getWorld();
        Vector extTarget = Assets.getTarget(thisBlock);

        if (extTarget.getBlockID() == AdvancedRedstoneCore.advPistonExtension.blockID)
        {
            return true;
        }

        return false;
    }

}
