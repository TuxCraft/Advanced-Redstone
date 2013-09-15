package AdvancedRedstone.TuxCraft.entity;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import AdvancedRedstone.TuxCraft.Assets;
import AdvancedRedstone.TuxCraft.Vector;
import AdvancedRedstone.TuxCraft.WorldBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityMovingBlock extends Entity
{
    public int            blockID;
    public int            metadata;
    public boolean        shouldDropItem;
    public NBTTagCompound tileData;
    private double        targetX;
    private double        targetY;
    private double        targetZ;
    private double        time;
    private double        ticksExisted;
    private boolean       setBlock;

    public EntityMovingBlock(World world)
    {
        super(world);
        this.shouldDropItem = true;
        this.renderDistanceWeight = 0.0D;
    }

    public EntityMovingBlock(World world, Vector spawn, Vector target, WorldBlock block, double speed, boolean setBlock)
    {
        this(world, spawn.getX(), spawn.getY(), spawn.getZ(), block.getBlockID(), block.getMeta(), block.getTile(), target.getX(), target.getY(), target.getZ(), speed, setBlock);
    }

    public EntityMovingBlock(World world, double x, double y, double z, int id, int meta, TileEntity tile, double tx, double ty, double tz, double d, boolean set)
    {
        super(world);

        this.setBlock = set;
        this.shouldDropItem = true;
        this.blockID = id;
        this.metadata = meta;

        this.preventEntitySpawning = true;

        this.setSize(0.98F, 0.98F);
        this.yOffset = 0.1F;

        this.setPosition(x + 0.5, y + 0.5, z + 0.5);

        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

        this.prevPosX = x + 0.5;
        this.prevPosY = y + 0.5;
        this.prevPosZ = z + 0.5;

        if (tile != null)
        {
            this.tileData = new NBTTagCompound();
            tile.writeToNBT(this.tileData);
        }

        this.targetX = tx + 0.5;
        this.targetY = ty + 0.5;
        this.targetZ = tz + 0.5;

        this.time = d;
        this.ticksExisted = time;
    }

    protected boolean canTriggerWalking()
    {
        return true;
    }

    protected void entityInit()
    {
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public void onUpdate()
    {
        boolean xAlign = (int) this.posX == (int) this.targetX;
        boolean yAlign = (int) this.posY == (int) this.targetY;
        boolean zAlign = (int) this.posZ == (int) this.targetZ;

        this.ticksExisted--;

        if (!xAlign && this.targetX != 0)
        {
            double distance = Math.abs(this.prevPosX - this.targetX);
            double speed = distance / this.time;

            int direction = (this.posX < this.targetX) ? 1 : -1;

            this.motionX = speed * direction;
        }

        if (!yAlign && this.targetY != 0)
        {
            double distance = Math.abs(this.prevPosY - this.targetY);
            double speed = distance / this.time;

            int direction = (this.posY < this.targetY) ? 1 : -1;

            this.motionY = speed * direction;
        }

        if (!zAlign && this.targetZ != 0)
        {
            double distance = Math.abs(this.prevPosZ - this.targetZ);
            double speed = distance / this.time;

            int direction = (this.posZ < this.targetZ) ? 1 : -1;

            this.motionZ = speed * direction;
        }

        if (this.ticksExisted == 0)
        {
            if (this.setBlock)
            {
                Vector target = new Vector(this.worldObj, (int) this.targetX, (int) this.targetY, (int) this.targetZ);

                Assets.setWorldBlock(new WorldBlock(Block.blocksList[this.blockID], this.metadata), target);

                if (this.tileData != null && Block.blocksList[this.blockID].hasTileEntity(this.metadata))
                {
                    TileEntity tileentity = target.getTile();

                    if (tileentity != null)
                    {
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        tileentity.writeToNBT(nbttagcompound);
                        Iterator iterator = this.tileData.getTags().iterator();

                        while (iterator.hasNext())
                        {
                            NBTBase nbtbase = (NBTBase) iterator.next();

                            if (!nbtbase.getName().equals("x") && !nbtbase.getName().equals("y") && !nbtbase.getName().equals("z"))
                            {
                                nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy());
                            }
                        }

                        tileentity.readFromNBT(nbttagcompound);
                        tileentity.onInventoryChanged();
                    }
                }
            }
            
            this.kill();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    protected void fall(float par1)
    {

    }

    protected void writeEntityToNBT(NBTTagCompound nbtTag)
    {
        nbtTag.setByte("Tile", (byte) this.blockID);
        nbtTag.setInteger("TileID", this.blockID);
        nbtTag.setByte("Data", (byte) this.metadata);
        nbtTag.setBoolean("DropItem", this.shouldDropItem);

        nbtTag.setDouble("targetX", this.targetX);
        nbtTag.setDouble("targetY", this.targetY);
        nbtTag.setDouble("targetZ", this.targetZ);

        nbtTag.setDouble("time", this.time);
        nbtTag.setDouble("ticksExisted", this.ticksExisted);

        if (tileData != null)
        {
            nbtTag.setCompoundTag("TileEntityData", tileData);
        }
    }

    protected void readEntityFromNBT(NBTTagCompound nbtTag)
    {
        this.targetX = nbtTag.getDouble("targetX");
        this.targetY = nbtTag.getDouble("targetY");
        this.targetZ = nbtTag.getDouble("targetZ");

        this.time = nbtTag.getDouble("time");

        this.ticksExisted = nbtTag.getDouble("ticksExisted");

        if (nbtTag.hasKey("TileID"))
        {
            this.blockID = nbtTag.getInteger("TileID");
        }
        else
        {
            this.blockID = nbtTag.getByte("Tile") & 255;
        }

        this.metadata = nbtTag.getByte("Data") & 255;

        if (nbtTag.hasKey("DropItem"))
        {
            this.shouldDropItem = nbtTag.getBoolean("DropItem");
        }

        if (nbtTag.hasKey("TileEntityData"))
        {
            this.tileData = nbtTag.getCompoundTag("TileEntityData");
        }

        if (this.blockID == 0)
        {
            this.blockID = Block.sandStone.blockID;
        }
    }

    public void func_85029_a(CrashReportCategory crash)
    {
        super.func_85029_a(crash);
        crash.addCrashSection("Immitating block ID", Integer.valueOf(this.blockID));
        crash.addCrashSection("Immitating block data", Integer.valueOf(this.metadata));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World getWorld()
    {
        return this.worldObj;
    }

    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }
    
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }
    
    @Override
    protected void doBlockCollisions()
    {
        
    }
}
