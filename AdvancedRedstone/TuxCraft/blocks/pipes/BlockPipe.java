package AdvancedRedstone.TuxCraft.blocks.pipes;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.blocks.ModBlock;
import AdvancedRedstone.TuxCraft.client.ClientProxy;
import AdvancedRedstone.TuxCraft.entity.EntityPipeItem;

public class BlockPipe extends ModBlock implements IPipe
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

        boolean connectUp = Assets.canPipeConnectTo(Assets.getVecUp(thisBlock));
        boolean connectDown = Assets.canPipeConnectTo(Assets.getVecDown(thisBlock));
        boolean connectNorth = Assets.canPipeConnectTo(Assets.getVecNorth(thisBlock));
        boolean connectSouth = Assets.canPipeConnectTo(Assets.getVecSouth(thisBlock));
        boolean connectEast = Assets.canPipeConnectTo(Assets.getVecEast(thisBlock));
        boolean connectWest = Assets.canPipeConnectTo(Assets.getVecWest(thisBlock));

        float f1 = 0.25F;
        float f2 = 0.25F;
        float f3 = 0.25F;
        float f4 = 0.75F;
        float f5 = 0.75F;
        float f6 = 0.75F;

        if (connectUp)
        {
            f5 = 1.00F;
        }

        if (connectDown)
        {
            f2 = 0.00F;
        }

        if (connectNorth)
        {
            f3 = 0.00F;
        }

        if (connectSouth)
        {
            f6 = 1.00F;
        }

        if (connectEast)
        {
            f4 = 1.00F;
        }

        if (connectWest)
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
        return ClientProxy.typePipeBasic;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
    {
        Vector thisVec = new Vector(world, x, y, z);
        ItemStack stack = player.inventory.getCurrentItem();
        
        EntityPipeItem item = new EntityPipeItem(thisVec, stack);
        world.spawnEntityInWorld(item);
        
        return true;
    }

    @Override
    public boolean doPipeItemCollision(Vector vec, EntityPipeItem item)
    {
        return false;
    }

    @Override
    public void onItemPass(Vector vec, EntityPipeItem item)
    {   
    }

}
