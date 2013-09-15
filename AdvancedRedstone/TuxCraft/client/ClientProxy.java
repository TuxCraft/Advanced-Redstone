package AdvancedRedstone.TuxCraft.client;

import AdvancedRedstone.TuxCraft.CommonProxy;
import AdvancedRedstone.TuxCraft.client.render.block.PipeBasicRenderer;
import AdvancedRedstone.TuxCraft.client.render.block.SidedBlockRenderer;
import AdvancedRedstone.TuxCraft.entity.EntityMovingBlock;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

    @Override
    public void load()
    {

    }

    public static int sidedBlockType;
    public static int pipeBasicType;

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityMovingBlock.class, new RenderMovingBlock());
        
        sidedBlockType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new SidedBlockRenderer());
        
        pipeBasicType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new PipeBasicRenderer());
    }

}
