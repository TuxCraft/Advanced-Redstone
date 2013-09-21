package AdvancedRedstone.TuxCraft.blocks.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.blocks.BlockSided;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdvancedPistonExtension extends BlockSided
{

    private boolean sticky;
    
    public BlockAdvancedPistonExtension(int id, String s, boolean isSticky)
    {
        super(id, Material.piston, s);
        this.setCreativeTab(null);
        this.setStepSound(soundStoneFootstep);
        this.setHardness(0.5F);
        this.sticky = isSticky;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        this.blockIcon = register.registerIcon(AdvancedRedstoneCore.modid + ":" + "blockBreaker" + "_back");
        this.frontIcon = register.registerIcon(AdvancedRedstoneCore.modid + ":" + "advPiston" + "_front");
        this.sideIcon  = register.registerIcon(AdvancedRedstoneCore.modid + ":" + "blockBreaker" + "_side");
    }

}
