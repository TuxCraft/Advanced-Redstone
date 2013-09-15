package AdvancedRedstone.TuxCraft.blocks.machines;

import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdvancedPistonExtension extends BlockSided
{

    private boolean sticky;
    
    public BlockAdvancedPistonExtension(int id, String s, boolean isSticky)
    {
        super(id, Material.piston, s);
        //this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setStepSound(soundStoneFootstep);
        this.setHardness(0.5F);
        this.sticky = isSticky;
    }

}
