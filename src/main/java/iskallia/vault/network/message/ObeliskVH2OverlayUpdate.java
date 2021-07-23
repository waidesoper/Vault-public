package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ObeliskVH2OverlayUpdate {
    private int a = 0;
    private int b = 0;
    public ObeliskVH2OverlayUpdate() { }

    public ObeliskVH2OverlayUpdate(int found, int needed) { this.a = found; this.b = needed;}

    public static void write(ObeliskVH2OverlayUpdate message, PacketBuffer buffer) { buffer.writeInt(message.a); buffer.writeInt(message.b); }

    public static ObeliskVH2OverlayUpdate read(PacketBuffer buffer) {
        ObeliskVH2OverlayUpdate message = new ObeliskVH2OverlayUpdate();
        message.a = buffer.readInt();
        message.b = buffer.readInt();
        return message;
    }

    public static void onMessage(ObeliskVH2OverlayUpdate message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            VaultRaidOverlay.obelisksActivated = message.a;
            VaultRaidOverlay.obelisksNeeded = message.b;
        });

        context.setPacketHandled(true);
    }
}
