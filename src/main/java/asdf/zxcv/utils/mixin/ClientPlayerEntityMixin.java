package asdf.zxcv.utils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import asdf.zxcv.utils.modules.WhoamiOnJoin;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
  
  @Inject(method = "setClientPermissionLevel(I)V", at = @At("TAIL"))
  public void onInit(int level, CallbackInfo ci) {
    WhoamiOnJoin.onPlayerSetPermission();
  }

}
