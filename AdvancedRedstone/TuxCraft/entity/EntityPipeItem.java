package AdvancedRedstone.TuxCraft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.blocks.pipes.IPipe;

public class EntityPipeItem extends Entity
{

    public static double baseSpeed = 0.1;
    private double       speed     = baseSpeed;
    private double       targetX;
    private double       targetY;
    private double       targetZ;
    private ItemStack    stack;

    public EntityPipeItem(World world)
    {
        super(world);

        this.noClip = true;
    }

    public EntityPipeItem(Vector thisVec, ItemStack item)
    {
        super(thisVec.getWorld());

        double x = thisVec.getX() + 0.5;
        double y = thisVec.getY() + 0.25;
        double z = thisVec.getZ() + 0.5;

        this.stack = item;

        this.setSize(0.4F, 0.4F);
        this.yOffset = 0.0F;

        this.setPosition(x, y, z);

        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;

        this.noClip = true;
    }

    public void onUpdate()
    {
        Vector vec = Assets.getEntityVec(this);

        if (vec.getBlock() instanceof IPipe)
        {
            IPipe pipe = (IPipe) Assets.getEntityVec(this).getBlock();

            if (this.inCenter())
            {
                
                if (pipe.doPipeItemCollision(vec, this))
                {
                    return;
                }

                List<Vector> vecs = this.getPossibleDestinations();
                Vector destination = getFinalDestination(vecs);

                if (destination == null)
                {

                    this.ejectItem();
                    this.kill();

                    return;
                }

                this.targetX = destination.getX() + 0.5;
                this.targetY = destination.getY() + 0.25;
                this.targetZ = destination.getZ() + 0.5;
            }

            pipe.onItemPass(vec, this);
        }

        else if (vec.getTile() instanceof IInventory)
        {
            IInventory inv = (IInventory) vec.getTile();
            int side = -1;

            if (inv instanceof ISidedInventory)
            {
                side = getSide();
                Assets.print("sided: " + side);
            }

            if (insertStack(inv, this.stack, side) != null)
            {
                this.ejectItem();
            }

            this.kill();
        }

        else
        {
            this.ejectItem();
            this.kill();
        }

        moveItem();

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        
        if(this.speed > baseSpeed)
        {
            this.speed -= 0.01;
        }
    }

    private int getSide()
    {
        Vector tmp = Assets.getEntityVec(this);

        double tmpX = tmp.getX() + 0.5;
        double tmpY = tmp.getY() + 0.25;
        double tmpZ = tmp.getZ() + 0.5;

        int side = 1;

        if (this.motionX != 0 && this.posX > tmpX)
        {
            side = 5;
        }

        if (this.motionX != 0 && this.posX < tmpX)
        {
            side = 4;
        }

        if (this.motionY != 0 && this.posY > tmpY)
        {
            side = 1;
        }

        if (this.motionY != 0 && this.posY < tmpY)
        {
            side = 0;
        }

        if (this.motionZ != 0 && this.posZ > tmpZ)
        {
            side = 3;
        }

        if (this.motionZ != 0 && this.posZ < tmpZ)
        {
            side = 2;
        }

        return side;
    }

    public static ItemStack insertStack(IInventory inv, ItemStack stack, int side)
    {
        if (inv instanceof ISidedInventory && side > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory) inv;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int j = 0; j < aint.length && stack != null && stack.stackSize > 0; ++j)
            {
                stack = addStackToInventory(inv, stack, aint[j], side);
            }
        }
        else
        {
            int k = inv.getSizeInventory();

            for (int l = 0; l < k && stack != null && stack.stackSize > 0; ++l)
            {
                stack = addStackToInventory(inv, stack, l, side);
            }
        }

        if (stack != null && stack.stackSize == 0)
        {
            stack = null;
        }

        return stack;
    }

    private static boolean canInsertItemToInventory(IInventory inv, ItemStack stack, int currSlot, int side)
    {
        return !inv.isItemValidForSlot(currSlot, stack) ? false : !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(currSlot, stack, side);
    }

    private static ItemStack addStackToInventory(IInventory inv, ItemStack stack, int currSlot, int side)
    {
        ItemStack itemstack1 = inv.getStackInSlot(currSlot);

        boolean flag = false;

        if (itemstack1 == null)
        {
            inv.setInventorySlotContents(currSlot, stack);
            stack = null;
            flag = true;
        }

        if (stack != null)
        {
            int k = stack.getMaxStackSize() - itemstack1.stackSize;
            int l = Math.min(stack.stackSize, k);
            stack.stackSize -= l;
            itemstack1.stackSize += l;
            flag = l > 0;
        }

        if (flag)
        {
            if (inv instanceof TileEntityHopper)
            {
                ((TileEntityHopper) inv).setTransferCooldown(8);
                inv.onInventoryChanged();
            }

            inv.onInventoryChanged();
        }

        return stack;
    }

    private Vector getFinalDestination(List<Vector> vecs)
    {
        final int size = vecs.size();

        for (int i = 0; i < size; i++)
        {
            Vector tmp = vecs.get(rand.nextInt(vecs.size()));

            double tmpX = tmp.getX() + 0.5;
            double tmpY = tmp.getY() + 0.25;
            double tmpZ = tmp.getZ() + 0.5;

            boolean xAlign = (int) posX == (int) tmpX;
            boolean yAlign = (int) posY == (int) tmpY;
            boolean zAlign = (int) posZ == (int) tmpZ;

            int currDirX = motionX == 0 ? 0 : motionX > 0 ? 1 : -1;
            int newDirX = posX == tmpX ? 0 : tmpX > posX ? 1 : -1;
            int currDirY = motionY == 0 ? 0 : motionY > 0 ? 1 : -1;
            int newDirY = posY == tmpY ? 0 : tmpY > posY ? 1 : -1;
            int currDirZ = motionZ == 0 ? 0 : motionZ > 0 ? 1 : -1;
            int newDirZ = posZ == tmpZ ? 0 : tmpZ > posZ ? 1 : -1;

            boolean flagX = !xAlign && currDirX != 0 && newDirX != 0 && currDirX != newDirX;
            boolean flagY = !yAlign && currDirY != 0 && newDirY != 0 && currDirY != newDirY;
            boolean flagZ = !zAlign && currDirZ != 0 && newDirZ != 0 && currDirZ != newDirZ;

            if (flagX || flagY || flagZ)
            {
                vecs.remove(tmp);
            }

            else
            {
                return tmp;
            }
        }

        return null;
    }

    private boolean moveItem()
    {
        boolean xAlign = (int) posX == (int) targetX;
        boolean yAlign = (int) posY == (int) targetY;
        boolean zAlign = (int) posZ == (int) targetZ;

        if (!xAlign)
        {
            int currDir = motionX == 0 ? 0 : motionX > 0 ? 1 : -1;
            int newDir = posX == targetX ? 0 : targetX > posX ? 1 : -1;

            if (currDir != 0 && newDir != 0 && currDir != newDir)
            {
                return false;
            }

            else
            {
                motionX = speed * newDir;
                motionY = 0.0D;
                motionZ = 0.0D;
            }
        }

        if (!yAlign)
        {
            int currDir = motionY == 0 ? 0 : motionY > 0 ? 1 : -1;
            int newDir = posY == targetY ? 0 : targetY > posY ? 1 : -1;

            if (currDir != 0 && newDir != 0 && currDir != newDir)
            {
                return false;
            }

            else
            {
                motionX = 0.0D;
                motionY = speed * newDir;
                motionZ = 0.0D;
            }
        }

        if (!zAlign)
        {
            int currDir = motionZ == 0 ? 0 : motionZ > 0 ? 1 : -1;
            int newDir = posZ == targetZ ? 0 : targetZ > posZ ? 1 : -1;

            if (currDir != 0 && newDir != 0 && currDir != newDir)
            {
                return false;
            }

            else
            {
                motionX = 0.0D;
                motionY = 0.0D;
                motionZ = speed * newDir;
            }
        }

        return true;
    }

    private void ejectItem()
    {
        if (!this.worldObj.isRemote && this.stack != null)
        {
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.stack);
            this.worldObj.spawnEntityInWorld(entityitem);
        }

        this.kill();
    }

    private List<Vector> getPossibleDestinations()
    {
        Vector vec = Assets.getEntityVec(this);
        List<Vector> destinations = new ArrayList();

        List<Vector> adjBlocks = Assets.getAdjacentBlocks(Assets.getEntityVec(this));

        for (int i = 0; i < adjBlocks.size(); i++)
        {
            if (adjBlocks.get(i).getBlock() instanceof IPipe || adjBlocks.get(i).getTile() instanceof IInventory)
            {
                destinations.add(adjBlocks.get(i));
            }
        }

        return destinations;
    }

    private boolean inCenter()
    {
        if (Assets.round(posX, 2) - (int) posX == 0.5 && Assets.round(posY, 2) - (int) posY == 0.25 && Assets.round(posZ, 2) - (int) posZ == 0.5)
        {
            return true;
        }

        return false;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);

        nbtTag.setCompoundTag("stack", this.stack.writeToNBT(new NBTTagCompound()));

        nbtTag.setDouble("targetX", this.targetX);
        nbtTag.setDouble("targetY", this.targetY);
        nbtTag.setDouble("targetZ", this.targetZ);
        
        nbtTag.setDouble("speed", this.speed);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);

        this.stack = ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag("stack"));

        this.targetX = nbtTag.getDouble("targetX");
        this.targetY = nbtTag.getDouble("targetY");
        this.targetZ = nbtTag.getDouble("targetZ");
        
        this.speed = nbtTag.getDouble("speed");
    }

    @Override
    protected void doBlockCollisions()
    {

    }

    public void setSpeed(double d)
    {
        this.speed = d;
    }

}
