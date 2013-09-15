package AdvancedRedstone.TuxCraft.blocks.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialMachine extends Material
{

    public MaterialMachine(MapColor color)
    {
        super(color);
        this.setRequiresTool();
    }
    
}
