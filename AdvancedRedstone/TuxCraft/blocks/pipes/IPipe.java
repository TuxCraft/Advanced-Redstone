package AdvancedRedstone.TuxCraft.blocks.pipes;

import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.entity.EntityPipeItem;

public interface IPipe
{
    /**
     * Return true to cancel EntityPipeItem logic
     * @param vec
     * @param item
     * @return
     */
    public boolean doPipeItemCollision(Vector vec, EntityPipeItem item);
    
    public void onItemPass(Vector vec, EntityPipeItem item);
}
