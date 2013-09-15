package AdvancedRedstone.TuxCraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.blocks.tileEntity.ContainerBlockPlacer;
import AdvancedRedstone.TuxCraft.blocks.tileEntity.TileEntityBlockPlacer;
import AdvancedRedstone.TuxCraft.client.gui.GuiBlockPlacer;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBlockPlacer)
        {
            return new ContainerBlockPlacer(player.inventory, (TileEntityBlockPlacer) tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBlockPlacer)
        {
            return new GuiBlockPlacer(player.inventory, (TileEntityBlockPlacer) tileEntity);
        }
        return null;

    }
}
