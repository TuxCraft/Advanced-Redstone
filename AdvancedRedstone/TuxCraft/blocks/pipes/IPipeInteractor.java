package AdvancedRedstone.TuxCraft.blocks.pipes;

import AdvancedRedstone.TuxCraft.Vector;

public interface IPipeInteractor
{
    public boolean connectsOnSide(Vector block, Vector pipe, int side);
}
