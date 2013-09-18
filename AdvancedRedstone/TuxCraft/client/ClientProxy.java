package AdvancedRedstone.TuxCraft.client;

import AdvancedRedstone.TuxCraft.CommonProxy;
import AdvancedRedstone.TuxCraft.client.render.block.RenderAdvancedPiston;
import AdvancedRedstone.TuxCraft.client.render.block.RenderPipeBasic;
import AdvancedRedstone.TuxCraft.client.render.block.RenderSidedBlock;
import AdvancedRedstone.TuxCraft.entity.EntityMovingBlock;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

    @Override
    public void load()
    {

    }

    public static int typeBlockSided;
    public static int typePipeBasic;
    public static int typeAdvancedPiston;

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityMovingBlock.class, new RenderMovingBlock());
         
        typeBlockSided = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderSidedBlock());
        
        typePipeBasic = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderPipeBasic());
        
        typeAdvancedPiston = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderAdvancedPiston());
    }

}
