package AdvancedRedstone.TuxCraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import AdvancedRedstone.TuxCraft.blocks.tileEntity.ContainerBlockPlacer;
import AdvancedRedstone.TuxCraft.blocks.tileEntity.TileEntityBlockPlacer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBlockPlacer extends GuiContainer
{
    private static final ResourceLocation location = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityBlockPlacer tile;

    public GuiBlockPlacer(InventoryPlayer inventoryPlayer, TileEntityBlockPlacer tile)
    {
        super(new ContainerBlockPlacer(inventoryPlayer, tile));
        this.tile = tile;
    }
    
    protected void drawGuiContainerForegroundLayer(int i, int i2)
    {
        String s = this.tile.isInvNameLocalized() ? this.tile.getInvName() : I18n.func_135053_a(this.tile.getInvName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(I18n.func_135053_a("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(float i, int i2, int i3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(location);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
