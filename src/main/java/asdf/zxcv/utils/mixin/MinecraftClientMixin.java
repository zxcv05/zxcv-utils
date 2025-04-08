package asdf.zxcv.utils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import asdf.zxcv.utils.modules.WhoamiOnJoin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.world.ClientWorld;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
  
  @Inject(method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen$WorldEntryReason;)V", at = @At("TAIL"))
  private void onJoinWorld(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason reason, CallbackInfo ci) {
    WhoamiOnJoin.onJoinWorld();
  }

}
