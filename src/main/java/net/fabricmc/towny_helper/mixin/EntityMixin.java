package net.fabricmc.towny_helper.mixin;

import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.service.Service;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fabricmc.towny_helper.service.Service.*;

@Mixin(Entity.class)
public class EntityMixin {


    @Inject(at = @At("RETURN"), method = "setYaw", cancellable = true)
    public void angleYaw(float yaw, CallbackInfo callbackInfo) {
        compassTextureRefresh();
        Service.ticker();
    }


    public void compassTextureRefresh() {
        if (MainMod.isIsLooking()) {
            if (MinecraftClient.getInstance().player == null) return;
            Vec3d pos = MinecraftClient.getInstance().player.getPos();

            double cDistance = Service.distanceCompute((int) pos.x, (int) pos.z, MainMod.getLookingX(), MainMod.getLookingZ());
            MainMod.setDistance((int) cDistance);
            if (cDistance == 0) {
                MainMod.setCompassTextureUID(17);
                return;
            }
            double degrees = Math.toDegrees(Math.asin(Math.abs(MainMod.getLookingX() - (int) pos.x) / cDistance));
            float nYaw = MinecraftClient.getInstance().player.getYaw() % 360;
            if (nYaw < 0) nYaw -= -360;
//                        if (degrees < 0) degrees-= -360;
            degrees = getQuarterDegrees(pos, degrees, nYaw);
            if (degrees < 0) degrees -= -360;
            MainMod.setCompassTextureUID(degreeToCompassTextureConverter((float) degrees));
            MainMod.degrees = (float) (360 - degrees);
        }
    }

}
