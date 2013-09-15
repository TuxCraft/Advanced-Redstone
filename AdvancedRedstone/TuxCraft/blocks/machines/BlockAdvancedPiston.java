package AdvancedRedstone.TuxCraft.blocks.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.WorldBlock;
import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import AdvancedRedstone.TuxCraft.entity.EntityMovingBlock;

public class BlockAdvancedPiston extends BlockSided
{

    private boolean sticky;

    public BlockAdvancedPiston(int id, String s, boolean isSticky)
    {
        super(id, Material.piston, s);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setStepSound(soundStoneFootstep);
        this.setHardness(0.5F);
        this.sticky = isSticky;
    }

    /**public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {   
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
    }*/
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
    {
        if(player.inventory.getCurrentItem() == null)
        {
            Vector thisBlock = new Vector(world, x, y, z);
            Vector extTarget = Assets.getTarget(thisBlock);
            
            if(extTarget.getBlockID() == AdvancedRedstoneCore.advPistonExtension.blockID)
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
        
        List<Vector> targets = getVectorsInARow(thisBlock, 100);
        
        Vector extTarget = Assets.getTarget(thisBlock);
        WorldBlock extBlock = new WorldBlock(AdvancedRedstoneCore.advPistonExtension, thisBlock.getMeta());
        
        Assets.setVectorToAir(extTarget);
        EntityMovingBlock extMovingBlock = new EntityMovingBlock(world, extTarget, thisBlock, extBlock, 20, false);
        world.spawnEntityInWorld(extMovingBlock);
        
        if(this.sticky)
        {
            for(int i = 0; i < targets.size(); i++)
            {   
                Vector target = targets.get(i);
                WorldBlock block = Assets.getWorldBlock(target);
                
                if(!target.isAirBlock())
                {
                    EntityMovingBlock movingBlock = new EntityMovingBlock(world, target, target.plus(-dirArray[0], -dirArray[1], -dirArray[2]), block, 20, true);
                    world.spawnEntityInWorld(movingBlock);
                    
                    world.removeBlockTileEntity(target.getX(), target.getY(), target.getZ());
                    
                    Assets.setVectorToAir(target);
                }
            } 
        }
        
    }

    private void extendPiston(World world, int x, int y, int z)
    {
        Vector thisBlock = new Vector(world, x, y, z);
        int[] dirArray = Assets.getDirectionArray(thisBlock.getMeta());
        
        List<Vector> targets = getVectorsInARow(thisBlock, 48);
        
        Vector extTarget = Assets.getTarget(thisBlock);
        WorldBlock extBlock = new WorldBlock(AdvancedRedstoneCore.advPistonExtension, thisBlock.getMeta());
        
        EntityMovingBlock extMovingBlock = new EntityMovingBlock(world, thisBlock, extTarget, extBlock, 20, true);
        world.spawnEntityInWorld(extMovingBlock);
        
        for(int i = 0; i < targets.size(); i++)
        {   
            Vector target = targets.get(i);
            WorldBlock block = Assets.getWorldBlock(target);
            
            if(!target.isAirBlock())
            {
                EntityMovingBlock movingBlock = new EntityMovingBlock(world, target, target.plus(dirArray[0], dirArray[1], dirArray[2]), block, 20, true);
                world.spawnEntityInWorld(movingBlock);
                
                world.removeBlockTileEntity(target.getX(), target.getY(), target.getZ());
                
                Assets.setVectorToAir(target);
            }
        }
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
            
            if(vec.isAirBlock())
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

}
