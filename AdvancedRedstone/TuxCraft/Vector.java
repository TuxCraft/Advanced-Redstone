package AdvancedRedstone.TuxCraft;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Vector
{
    private IBlockAccess world;
    private int   x;
    private int   y;
    private int   z;
    
    public Vector(IBlockAccess world, int x, int y, int z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock()
    {
        return Block.blocksList[getBlockID()];
    }

    public int getBlockID()
    {
        return world.getBlockId(x, y, z);
    }

    public int getMeta()
    {
        return world.getBlockMetadata(x, y, z);
    }

    public TileEntity getTile()
    {

        if (getBlock() != null && getBlock().hasTileEntity(getMeta()))
        {
            return world.getBlockTileEntity(x, y, z);
        }

        return null;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public IBlockAccess getIBlockAccess()
    {
        return world;
    }
    
    public World getWorld()
    {
        return (world instanceof World) ? ((World) world) : null;
    }

    @Override
    public String toString()
    {
        return x + " " + y + " " + z;
    }

    public boolean isIndirectlyPowered()
    {
        return (world instanceof World) ? ((World) world).isBlockIndirectlyGettingPowered(x, y, z) : false;
    }

    public boolean isAirBlock()
    {
        return world.isAirBlock(x, y, z);
    }
    
    public Vector plus(int xp, int yp, int zp)
    {
        return new Vector(world, x + xp, y + yp, z + zp);
    }
}
