package AdvancedRedstone.TuxCraft;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldBlock
{
    private Block      block;
    private TileEntity tile;
    private int        meta;

    public WorldBlock(Block block)
    {
        this(block, null, 0);
    }
    
    public WorldBlock(Block block, int meta)
    {
        this(block, null, meta);
    }
    
    public WorldBlock(Block block, TileEntity tile, int meta)
    {
        this.block = block;
        this.tile = tile;
        this.meta = meta;
    }

    public Block getBlock()
    {
        return block;
    }

    public int getBlockID()
    {
        return block.blockID;
    }

    public int getMeta()
    {
        if (meta != 0)
        {
            return meta;
        }

        return 0;
    }

    public TileEntity getTile()
    {

        if (tile != null)
        {
            return tile;
        }

        return null;
    }

    @Override
    public String toString()
    {
        return getBlock().getLocalizedName();
    }
}
