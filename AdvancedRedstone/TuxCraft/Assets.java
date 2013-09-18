package AdvancedRedstone.TuxCraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.blocks.pipes.BlockPipe;
import AdvancedRedstone.TuxCraft.blocks.pipes.IPipe;
import AdvancedRedstone.TuxCraft.blocks.pipes.IPipeInteractor;

public class Assets
{
    /**
     * This class contains useful static methods that can
     * be easily accessed and used in any other class. It
     * contains methods dealing with the World, Blocks,
     * Item/Stacks, Entities, and console Prints.
     */

    // ========== World ==========

    /**
     * Shortcut so you don't have to manually
     * make a new WorldBlock
     * @param block
     * @param vec
     * @return setBlockSuccess
     */
    public static boolean setBlock(Block block, Vector vec)
    {
        return setWorldBlock(new WorldBlock(block), vec);
    }

    /**
     * Places a WorldBlock in the world, returns
     * false if there is a block in the way
     * @param blockToSet
     * @return placementValid
     */
    public static boolean setWorldBlock(WorldBlock blockToSet, Vector vec)
    {
        World world = vec.getWorld();

        if (world.isAirBlock(vec.getX(), vec.getY(), vec.getZ()))
        {
            world.setBlock(vec.getX(), vec.getY(), vec.getZ(), blockToSet.getBlockID());
            world.setBlockMetadataWithNotify(vec.getX(), vec.getY(), vec.getZ(), blockToSet.getMeta(), 2);
            world.notifyBlocksOfNeighborChange(vec.getX(), vec.getY(), vec.getZ(), blockToSet.getBlockID());
            world.scheduleBlockUpdate(vec.getX(), vec.getY(), vec.getZ(), blockToSet.getBlockID(), blockToSet.getBlock().tickRate(vec.getWorld()));

            if (blockToSet.getTile() != null)
            {
                TileEntity tile = blockToSet.getTile();
                world.setBlockTileEntity(vec.getX(), vec.getY(), vec.getZ(), tile);
            }

            return true;
        }

        return false;
    }

    /**
     * Sets a vector to air
     * @param vec
     * @return success
     */
    public static boolean setVectorToAir(Vector vec)
    {
        World world = vec.getWorld();

        return world.setBlockToAir(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Shortcut world method using vectors
     * @param vec
     * @param meta
     * @param i
     */
    public static void setVectorMetadata(Vector vec, int meta, int i)
    {
        vec.getWorld().setBlockMetadataWithNotify(vec.getX(), vec.getY(), vec.getZ(), meta, i);
    }

    /**
     * Spawns static particles around an entity
     * @param particleToSpawn
     * @param entityToSpawnAround
     * @param size
     * @param freq
     * @param yOffset
     */
    public static void spawnParticles(String s, Entity entity, int size, int freq, double yOffset)
    {

        for (int i = 0; i <= freq; i++)
        {

            double parX = Math.random() * size - size / 2;
            double parY = Math.random() * size - size / 2;
            double parZ = Math.random() * size - size / 2;

            entity.worldObj.spawnParticle(s, entity.posX + parX, entity.posY + parY + yOffset, entity.posZ + parZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public static WorldBlock getWorldBlock(Vector vec)
    {
        return new WorldBlock(vec.getBlock(), vec.getTile(), vec.getMeta());
    }

    // Meta: 1
    public static Vector getVecUp(Vector vec)
    {
        return vec.plus(0, 1, 0);
    }

    // Meta: 0
    public static Vector getVecDown(Vector vec)
    {
        return vec.plus(0, -1, 0);
    }

    // Meta: 2
    public static Vector getVecNorth(Vector vec)
    {
        return vec.plus(0, 0, -1);
    }

    // Meta: 3
    public static Vector getVecSouth(Vector vec)
    {
        return vec.plus(0, 0, 1);
    }

    // Meta: 5
    public static Vector getVecEast(Vector vec)
    {
        return vec.plus(1, 0, 0);
    }

    // Meta: 4
    public static Vector getVecWest(Vector vec)
    {
        return vec.plus(-1, 0, 0);
    }

    // ========== Blocks ==========

    /**
     * Get's all the adjacent non-air blocks
     * @param targetBlock
     * @return adjacentBlockList
     */
    public static List getAdjacentBlocks(Vector vec)
    {
        List<Vector> adjBlocks = new ArrayList();

        Vector vec1 = new Vector(vec.getWorld(), vec.getX() + 1, vec.getY(), vec.getZ());
        if (vec1 != null)
        {
            adjBlocks.add(vec1);
        }

        Vector vec2 = new Vector(vec.getWorld(), vec.getX() - 1, vec.getY(), vec.getZ());
        if (vec2 != null)
        {
            adjBlocks.add(vec2);
        }

        Vector vec3 = new Vector(vec.getWorld(), vec.getX(), vec.getY() + 1, vec.getZ());
        if (vec3 != null)
        {
            adjBlocks.add(vec3);
        }

        Vector vec4 = new Vector(vec.getWorld(), vec.getX(), vec.getY() - 1, vec.getZ());
        if (vec4 != null)
        {
            adjBlocks.add(vec4);
        }

        Vector vec5 = new Vector(vec.getWorld(), vec.getX(), vec.getY(), vec.getZ() + 1);
        if (vec5 != null)
        {
            adjBlocks.add(vec5);
        }

        Vector vec6 = new Vector(vec.getWorld(), vec.getX(), vec.getY(), vec.getZ() - 1);
        if (vec6 != null)
        {
            adjBlocks.add(vec6);
        }

        return adjBlocks;
    }

    /**
     * Shortcut method
     * Gets the BlockSided target block dependent on metadata
     * Used only on blocks that extend BlockSided
     * @param blockSided
     * @return target
     */
    public static Vector getTarget(Vector vec)
    {
        return getTarget(vec.getIBlockAccess(), vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Gets the BlockSided target block dependent on metadata
     * Used only on blocks that extend BlockSided
     * @param world
     * @param x
     * @param y
     * @param z
     * @return targetBlock
     */
    public static Vector getTarget(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);

        return getTarget(world, meta, x, y, z);
    }

    /**
     * Gets the BlockSided target block dependent on metadata
     * Used only on blocks that extend BlockSided
     * @param world
     * @param meta
     * @param x
     * @param y
     * @param z
     * @return targetBlock
     */
    public static Vector getTarget(IBlockAccess world, int meta, int x, int y, int z)
    {
        int[] dirArray = Assets.getDirectionArray(meta);

        int targetX = x + dirArray[0];
        int targetY = y + dirArray[1];
        int targetZ = z + dirArray[2];

        return new Vector(world, targetX, targetY, targetZ);
    }

    /**
     * Shortcut method, see original for
     * full description
     * @param originalVec
     * @param length
     * @return vectorsInARow
     */
    public static List<Vector> getVectorsInARow(Vector originalVec, int length)
    {
        return getVectorsInARow(originalVec, originalVec.getMeta(), length);
    }

    /**
     * Get's all of the vectors in a row
     * direction uses the BlockSided format
     * @param originalVec
     * @param meta
     * @param length
     * @return vectorsInARow
     */
    public static List<Vector> getVectorsInARow(Vector originalVec, int direction, int length)
    {
        World world = originalVec.getWorld();

        List<Vector> vectorList = new ArrayList();

        int[] dirArray = Assets.getDirectionArray(direction);

        int x = originalVec.getX();
        int y = originalVec.getY();
        int z = originalVec.getZ();

        for (int i = 0; i < length; i++)
        {
            x += dirArray[0];
            y += dirArray[1];
            z += dirArray[2];

            Vector vec = new Vector(world, x, y, z);
            vectorList.add(vec);
        }

        return vectorList;
    }

    /**
     * Get's the direction array from
     * a number. Goes in the standard x, y, z
     * format
     * @param direction
     * @return dirArray
     */
    public static int[] getDirectionArray(int direction)
    {
        int x = 0;
        int y = 0;
        int z = 0;

        switch (direction)
            {
                case 0:
                    y -= 1;
                    break;
                case 1:
                    y += 1;
                    break;
                case 2:
                    z -= 1;
                    break;
                case 3:
                    z += 1;
                    break;
                case 4:
                    x -= 1;
                    break;
                case 5:
                    x += 1;
                    break;
                default:
                    break;
            }

        return new int[] { x, y, z };
    }

    /**
     * Shortcut method for canPlaceBlockAt
     * @param vec
     * @param block
     * @return
     */
    public static boolean canPlaceBlockAt(Vector vec, Block block)
    {
        return block.canPlaceBlockAt(vec.getWorld(), vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Shortcut method for canPlaceBlockOnSide
     * @param vec
     * @param block
     * @param side
     * @return
     */
    public static boolean canPlaceBlockOnSide(Vector vec, Block block, int side)
    {
        return block.canPlaceBlockOnSide(vec.getWorld(), vec.getX(), vec.getY(), vec.getZ(), side);
    }

    /**
     * Shortcut method for onBlockPlaced
     * Vec is the vector of the block being placed
     * Vec1 is the vector of the block it's being placed on
     * @param vec
     * @param vec1
     * @param side
     * @return
     */
    public static int onBlockPlaced(Vector vec, Vector vec1, int side)
    {
        return vec.getBlock().onBlockPlaced(vec.getWorld(), vec.getX(), vec.getY(), vec.getZ(), side, vec1.getX(), vec1.getY(), vec1.getZ(), vec.getMeta());
    }

    // ========== Items ==========

    // ========== Entity ==========

    public static Vector getEntityVec(Entity entity)
    {
        return new Vector(entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }

    public static void moveEntityTowards(Entity entity, double targetX, double targetY, double targetZ, double time)
    {
        boolean xAlign = (int) entity.posX == (int) targetX;
        boolean yAlign = (int) entity.posY == (int) targetY;
        boolean zAlign = (int) entity.posZ == (int) targetZ;

        if (!xAlign && targetX != 0)
        {
            double distance = Math.abs(entity.prevPosX - targetX);
            double speed = distance / time;

            int direction = (entity.posX < targetX) ? 1 : -1;

            entity.motionX = speed * direction;
        }

        if (!yAlign && targetY != 0)
        {
            double distance = Math.abs(entity.prevPosY - targetY);
            double speed = distance / time;

            int direction = (entity.posY < targetY) ? 1 : -1;

            entity.motionY = speed * direction;
        }

        if (!zAlign && targetZ != 0)
        {
            double distance = Math.abs(entity.prevPosZ - targetZ);
            double speed = distance / time;

            int direction = (entity.posZ < targetZ) ? 1 : -1;

            entity.motionZ = speed * direction;
        }
    }

    // ========== Prints ==========

    /**
     * Shortcut, easier to type & includes modid
     * @param printedString
     */
    public static void print(Object object)
    {
        System.out.println("[" + AdvancedRedstoneCore.modid + "]" + " " + object);
    }

    /**
     * Shortcut to print a list
     * @param printedList
     */
    public static void printList(List list)
    {
        String s = "";

        for (int i = 0; i < list.size(); i++)
        {
            s = s + list.get(i).toString();

            if (i != list.size() - 1)
            {
                s = s + " -- ";
            }
        }

        print(s);
    }

    public static void printArray(Object[] obj)
    {
        String s = "";

        for (int i = 0; i < obj.length; i++)
        {
            s = s + obj[i];

            if (i != obj.length - 1)
            {
                s = s + " -- ";
            }
        }

        print(s);
    }
    
    public static void printArray(int[] obj)
    {
        String s = "";

        for (int i = 0; i < obj.length; i++)
        {
            s = s + obj[i];

            if (i != obj.length - 1)
            {
                s = s + " -- ";
            }
        }

        print(s);
    }

    /**
     * Shortcut to print coordinates
     * @param x
     * @param y
     * @param z
     */
    public static void printCoords(Object x, Object y, Object z)
    {
        String s = " x: " + x + "  y: " + y + "  z: " + z;

        print(s);
    }

    // ========== Misc ==========

    public static boolean canPipeConnectTo(Vector vec)
    {
        if (vec.getBlock() instanceof BlockPipe)
        {
            return true;
        }

        if (vec.getTile() != null && vec.getTile() instanceof IInventory)
        {
            return true;
        }

        if (vec.getBlock() instanceof IPipeInteractor)
        {
            return true;
        }

        if (vec.getBlock() instanceof IPipe)
        {
            return true;
        }

        return false;
    }

    public static int getGreatest(int[] arr)
    {
        int greatest = 0;

        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] > greatest)
            {
                greatest = arr[i];
            }
        }

        return greatest;
    }

    public static double getGreatest(double[] arr)
    {
        double greatest = 0;

        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] > greatest)
            {
                greatest = arr[i];
            }
        }

        return greatest;
    }

    public static double round(double value, int places)
    {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
