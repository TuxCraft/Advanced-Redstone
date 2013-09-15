package AdvancedRedstone.TuxCraft.blocks.pipes;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.blocks.ModBlock;
import AdvancedRedstone.TuxCraft.client.ClientProxy;

public class BlockPipe extends ModBlock
{

    public BlockPipe(int id, String s)
    {
        super(id, Material.circuits, s, true);
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        Vector thisBlock = new Vector(world, x, y, z);

        boolean connectUp = Assets.getVecUp(thisBlock).getBlockID() == this.blockID;
        boolean connectDown = Assets.getVecDown(thisBlock).getBlockID() == this.blockID;
        boolean connectNorth = Assets.getVecNorth(thisBlock).getBlockID() == this.blockID;
        boolean connectSouth = Assets.getVecSouth(thisBlock).getBlockID() == this.blockID;
        boolean connectEast = Assets.getVecEast(thisBlock).getBlockID() == this.blockID;
        boolean connectWest = Assets.getVecWest(thisBlock).getBlockID() == this.blockID;
        
        float f1 = 0.25F;
        float f2 = 0.25F;
        float f3 = 0.25F;
        float f4 = 0.75F;
        float f5 = 0.75F;
        float f6 = 0.75F;
        
        if(connectUp)
        {
            f5 = 1.00F;
        }
        
        if(connectDown)
        {
            f2 = 0.00F;
        }
        
        if(connectNorth)
        {
            f3 = 0.00F;
        }
        
        if(connectSouth)
        {
            f6 = 1.00F;
        }
        
        if(connectEast)
        {
            f4 = 1.00F;
        }
        
        if(connectWest)
        {
            f1 = 0.00F;
        }
        
        this.setBlockBounds(f1, f2, f3, f4, f5, f6);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return ClientProxy.pipeBasicType;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }

}
