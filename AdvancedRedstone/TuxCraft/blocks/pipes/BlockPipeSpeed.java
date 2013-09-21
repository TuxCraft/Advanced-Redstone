package AdvancedRedstone.TuxCraft.blocks.pipes;

import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.entity.EntityPipeItem;


public class BlockPipeSpeed extends BlockPipe
{

    public BlockPipeSpeed(int id, String s)
    {
        super(id, s);
        
    }
    
    @Override
    public void onItemPass(Vector vec, EntityPipeItem item)
    {
        item.setSpeed(0.5);
    }

}
