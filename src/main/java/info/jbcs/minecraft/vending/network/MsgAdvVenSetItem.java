package info.jbcs.minecraft.vending.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import info.jbcs.minecraft.vending.ContainerAdvancedVendingMachine;
import info.jbcs.minecraft.vending.TileEntityVendingMachine;
import info.jbcs.minecraft.vending.Vending;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.swing.text.html.parser.Entity;

public class MsgAdvVenSetItem extends Message {
    private int                 id, count, damage;

    public MsgAdvVenSetItem() { }

    @SuppressWarnings("unchecked")
    public MsgAdvVenSetItem(int id, int count, int damage)
    {
        this.id = id;
        this.count = count;
        this.damage = damage;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
        count = buf.readInt();
        damage = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
        buf.writeInt(count);
        buf.writeInt(damage);
    }

    public static class Handler implements IMessageHandler<MsgAdvVenSetItem, IMessage> {

        @Override
        public IMessage onMessage(MsgAdvVenSetItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            Container con = player.openContainer;
            if (con == null || !(con instanceof ContainerAdvancedVendingMachine))
                return null;
            ContainerAdvancedVendingMachine container = (ContainerAdvancedVendingMachine) con;

            container.entity.setBoughtItem(message.id == 0 ? null : new ItemStack(Item.getItemById(message.id), message.count, message.damage));

            return null;
        }
    }
}
