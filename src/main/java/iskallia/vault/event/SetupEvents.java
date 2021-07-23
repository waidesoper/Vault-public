package iskallia.vault.event;

import iskallia.vault.capabilities.PlayerArenaCap;
import iskallia.vault.init.*;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        ModScreens.register(event);
        ModScreens.registerOverlays();
        ModKeybinds.register(event);
        ModEntities.Renderers.register(event);
        MinecraftForge.EVENT_BUS.register(InputEvents.class);
        ModBlocks.registerTileEntityRenderers();
    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) {
        ModConfigs.register();
        ModNetwork.initialize();
		registerCapabilities();
    }


    // #Crimson_Fluff
    private static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(PlayerArenaCap.class, new Capability.IStorage<PlayerArenaCap>() {
            @Override
            public void readNBT(Capability<PlayerArenaCap> capability, PlayerArenaCap ppCapability, Direction direction, INBT inbt) {
                throw new UnsupportedOperationException();
            }

            @Override
            public INBT writeNBT(Capability<PlayerArenaCap> capability, PlayerArenaCap ppCapability, Direction direction) {
                throw new UnsupportedOperationException();
            }

        }, () -> {
            throw new UnsupportedOperationException();
        });
    }
}
