package AdvancedRedstone.TuxCraft.blocks.tileEntity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.AdvancedRedstoneCore;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;

public class TileEntityBlockPlacer extends TileEntity implements IInventory
{

    private ItemStack[] contents = new ItemStack[9];
    private Random      rand     = new Random();
    protected String    customName;

    @Override
    public int getSizeInventory()
    {
        return 9;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.contents[i];
    }

    public ItemStack decrStackSize(int slot, int amount)
    {
        if (this.contents[slot] != null)
        {
            ItemStack itemstack;

            if (this.contents[slot].stackSize <= amount)
            {
                itemstack = this.contents[slot];
                this.contents[slot] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.contents[slot].splitStack(amount);

                if (this.contents[slot].stackSize == 0)
                {
                    this.contents[slot] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    public int getRandomStackFromInventory()
    {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.contents.length; ++k)
        {
            if (this.contents[k] != null && this.rand.nextInt(j++) == 0)
            {
                i = k;
            }
        }

        return i;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack)
    {
        if(this.blockType != null && this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID, this.blockType.tickRate(this.worldObj));
        
        this.contents[i] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();

    }

    public int addItem(ItemStack stack)
    {
        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] == null || this.contents[i].itemID == 0)
            {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }

    @Override
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.customName : "container." + AdvancedRedstoneCore.modid + ":blockPlacer";
    }

    public void setCustomName(String s)
    {
        this.customName = s;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return this.customName != null;
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items");
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.contents.length)
            {
                this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if (nbt.hasKey("CustomName"))
        {
            this.customName = nbt.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);

        if (this.isInvNameLocalized())
        {
            nbt.setString("CustomName", this.customName);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

}
