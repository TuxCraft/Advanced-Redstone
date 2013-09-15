package AdvancedRedstone.TuxCraft.blocks;

import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class ModBlock extends Block
{
    
    protected String name;
    protected boolean useFastIcon; 
    
    @SideOnly(Side.CLIENT)
    protected Icon fastGraphics;

    public ModBlock(int id, Material material, String s, boolean b)
    {
        super(id, material);
        this.name = s;
        this.useFastIcon = b;
        this.setUnlocalizedName(AdvancedRedstoneCore.modid + ":" + s);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        this.blockIcon = register.registerIcon(AdvancedRedstoneCore.modid + ":" + this.name);
        this.fastGraphics = register.registerIcon(AdvancedRedstoneCore.modid + ":" + this.name + "_fast");
    }
    
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return (!Minecraft.getMinecraft().gameSettings.fancyGraphics && useFastIcon) ? this.fastGraphics : this.blockIcon;
    }

}
