package AdvancedRedstone.TuxCraft.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.blocks.pipes.BlockPipe;
import AdvancedRedstone.TuxCraft.client.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderPipeBasic implements ISimpleBlockRenderingHandler
{

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        GL11.glPushMatrix();

        metadata = 1;
        
        Tessellator tessellator = Tessellator.instance;

        int j;
        float f1;
        float f2;
        float f3;

        renderer.setRenderBoundsFromBlock(block);
        int k;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        GL11.glPopMatrix();

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        renderer.setRenderBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        renderer.renderStandardBlock(block, x, y, z);
        
        Vector thisBlock = new Vector(world, x, y, z);
        
        boolean connectUp = Assets.canPipeConnectTo(Assets.getVecUp(thisBlock));
        boolean connectDown = Assets.canPipeConnectTo(Assets.getVecDown(thisBlock));
        boolean connectNorth = Assets.canPipeConnectTo(Assets.getVecNorth(thisBlock));
        boolean connectSouth = Assets.canPipeConnectTo(Assets.getVecSouth(thisBlock));
        boolean connectEast = Assets.canPipeConnectTo(Assets.getVecEast(thisBlock));
        boolean connectWest = Assets.canPipeConnectTo(Assets.getVecWest(thisBlock));
        
        if(connectUp)
        {
            renderer.setRenderBounds(0.25D, 0.75D, 0.25D, 0.75D, 1.00D, 0.75D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        if(connectDown)
        {
            renderer.setRenderBounds(0.25D, 0.00D, 0.25D, 0.75D, 0.25D, 0.75D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        if(connectNorth)
        {
            renderer.setRenderBounds(0.25D, 0.25D, 0.00D, 0.75D, 0.75D, 0.25D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        if(connectEast)
        {
            renderer.setRenderBounds(0.75D, 0.25D, 0.25D, 1.00D, 0.75D, 0.75D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        if(connectWest)
        {
            renderer.setRenderBounds(0.00D, 0.25D, 0.25D, 0.25D, 0.75D, 0.75D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        if(connectSouth)
        {
            renderer.setRenderBounds(0.25D, 0.25D, 0.75D, 0.75D, 0.75D, 1.0D);
            renderer.renderStandardBlock(block, x, y, z);
        }
        
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.typePipeBasic;
    }

}
