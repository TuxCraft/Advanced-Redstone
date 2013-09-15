package AdvancedRedstone.TuxCraft;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import AdvancedRedstone.TuxCraft.blocks.machines.BlockAdvancedPiston;
import AdvancedRedstone.TuxCraft.blocks.machines.BlockAdvancedPistonExtension;
import AdvancedRedstone.TuxCraft.blocks.machines.BlockBreaker;
import AdvancedRedstone.TuxCraft.blocks.machines.BlockPlacer;
import AdvancedRedstone.TuxCraft.blocks.materials.MaterialMachine;
import AdvancedRedstone.TuxCraft.blocks.pipes.BlockPipe;
import AdvancedRedstone.TuxCraft.blocks.tileEntity.TileEntityBlockPlacer;
import AdvancedRedstone.TuxCraft.entity.EntityMovingBlock;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = AdvancedRedstoneCore.modid, name = "Advanced Redstone", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class AdvancedRedstoneCore
{

    @Instance("AdvancedRedstone")
    public static AdvancedRedstoneCore instance;

    @SidedProxy(clientSide = "AdvancedRedstone.TuxCraft.client.ClientProxy", serverSide = "AdvancedRedstone.TuxCraft.CommonProxy")
    public static CommonProxy          proxy;

    public static final String         modid        = "AdvancedRedstone";

    public static int                  idBaseEntity = 0;

    public static List<Block>          blocksThatRotateWhenPlaced;

    public static Block                breaker;
    public static Block                placer;
    public static Block                advPiston;
    public static Block                advPistonSticky;
    public static Block                advPistonExtension;
    public static Block                pipeBasic;

    public static Material             machine;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {

        machine = new MaterialMachine(MapColor.stoneColor);

        breaker = new BlockBreaker(250, "blockBreaker");
        GameRegistry.registerBlock(breaker, "blockBreaker");

        placer = new BlockPlacer(251, "blockPlacer");
        GameRegistry.registerBlock(placer, "blockPlacer");
        GameRegistry.registerTileEntity(TileEntityBlockPlacer.class, "blockPlacer");

        advPiston = new BlockAdvancedPiston(252, "advPiston", false);
        GameRegistry.registerBlock(advPiston, "advPiston");
        
        advPistonSticky = new BlockAdvancedPiston(255, "advPistonSticky", true);
        GameRegistry.registerBlock(advPistonSticky, "advPistonSticky");

        advPistonExtension = new BlockAdvancedPistonExtension(253, "advPistonExtension", false);
        GameRegistry.registerBlock(advPistonExtension, "advPistonExtension");

        pipeBasic = new BlockPipe(254, "pipeBasic");
        GameRegistry.registerBlock(pipeBasic, "pipeBasic");

        EntityRegistry.registerModEntity(EntityMovingBlock.class, "movingBlock", idBaseEntity + 1, instance, 128, 1, true);

        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
    }

}
