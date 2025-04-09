package asdf.zxcv.utils.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Piss extends Module {
  public Piss() {
    super(
      Categories.Combat,
      "piss",
      "Piss fireballs on your enemies"
    );
  }

  private final SettingGroup sg = settings.getDefaultGroup();

  private final Setting<String> name = sg.add(new StringSetting.Builder()
    .name("Entity Name")
    .description("Will name the fireball this")
    .defaultValue("@asdf.zxcv loves you <3")
    .build());
    
  private final Setting<Integer> speed = sg.add(new IntSetting.Builder()
    .name("Speed")
    .description("Entities will spawn with this speed")
    .defaultValue(10)
    .sliderRange(1, 10)
    .build());

  private final Setting<Integer> power = sg.add(new IntSetting.Builder()
    .name("Power")
    .description("This is the fireball's explosion power")
    .defaultValue(15)
    .sliderRange(1, 127)
    .build());

  private final Setting<Integer> delay = sg.add(new IntSetting.Builder()
    .name("Delay")
    .description("Delay between spawns (in ticks)")
    .defaultValue(2)
    .build());

  public enum Mode { Auto, Creative, Command };
  private final Setting<Mode> mode = sg.add(new EnumSetting.Builder<Mode>()
    .name("Mode")
    .description("How to spawn entities")
    .defaultValue(Mode.Auto)
    .build());  

  private final Setting<Boolean> disableOnDisconnect = sg.add(new BoolSetting.Builder()
    .name("Disable on Disconnect")
    .description("When disconnecting, disable this module")
    .defaultValue(true)
    .build());

  private int tick;

  @EventHandler
  private void onGameLeft(GameLeftEvent event) {
    if (disableOnDisconnect.get()) toggle();
  }

  @EventHandler
  private void onTick(TickEvent.Post event) {
    if (!mc.player.getAbilities().creativeMode) {
      error("Creative mode required");
      toggle();
      return;
    }

    tick++;
    if (tick > delay.get()) {
      tick = 0;

      switch (mode.get()) {

        case Auto:
          if (mc.player.getAbilities().creativeMode) {
            spawnCreative();
          } else if (mc.player.getPermissionLevel() >= 2) {
            spawnCommand();
          } else {
            error("You are neither OP nor in Creative mode, cannot piss :(");
            toggle();
          }

          break;

        case Creative:
          if (!mc.player.getAbilities().creativeMode) {
            error("Creative mode required when using Mode = Creative");
            toggle();
            return;
          }
          
          spawnCreative();
          break;

        case Command:
          if (mc.player.getPermissionLevel() < 2) {
            error("Must be OP when using Mode = Command");
            toggle();
            return;
          }

          spawnCommand();
          break;
      }
    }
  }

  private NbtList getNbtSpeed() {
    Vec3d rot = mc.cameraEntity.getRotationVector();
    double y = Math.min(0.5, rot.y);

    NbtList NbtSpeed = new NbtList();
    NbtSpeed.add(NbtDouble.of(speed.get() * rot.x));
    NbtSpeed.add(NbtDouble.of(speed.get() * y));
    NbtSpeed.add(NbtDouble.of(speed.get() * rot.z));
    // NbtSpeed.add(NbtDouble.of(-speed.get()));
    return NbtSpeed;
  }

  private void spawnCreative() {
    NbtCompound entityData = new NbtCompound();

    NbtList NbtSpeed = getNbtSpeed();
    
    NbtList NbtPos = new NbtList();
    Vec3d pos = mc.player.getPos();
    Vec3d rot = mc.cameraEntity.getRotationVector();
    NbtPos.add(NbtDouble.of(pos.x + rot.x * 3));
    NbtPos.add(NbtDouble.of(pos.y + rot.y - 1));
    NbtPos.add(NbtDouble.of(pos.z + rot.z * 3));
    
    entityData.putString("id", "minecraft:fireball");
    entityData.put("Motion", NbtSpeed);
    entityData.put("Pos", NbtPos);
    entityData.putInt("ExplosionPower", power.get());

    ItemStack spawnEgg = new ItemStack(Items.ENDER_DRAGON_SPAWN_EGG);
    spawnEgg.applyChanges(ComponentChanges.builder()
      .add(DataComponentTypes.CUSTOM_NAME, Text.literal(name.get()))
      .add(DataComponentTypes.ITEM_NAME, Text.literal(name.get()))
      .add(DataComponentTypes.ENTITY_DATA, NbtComponent.of(entityData))
      .build());

    BlockHitResult fakeBlockHit = new BlockHitResult(
      mc.player.getPos().add(0, 1, 0),
      Direction.UP,
      new BlockPos(mc.player.getBlockPos().add(0, 1, 0)),
      false);

    mc.interactionManager.clickCreativeStack(spawnEgg, 36 + mc.player.getInventory().selectedSlot);
    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, fakeBlockHit);
  }

  private void spawnCommand() {
    NbtList NbtSpeed = getNbtSpeed();

    String command = "/summon minecraft:fireball ~ ~-2 ~ {";
    command += "\"CustomName\":\"{\\\"text\\\":\\\"" + name.get() + "\\\"}\",";
    command += "\"ExplosionPower\":" + power.get() + ",";
    command += "\"CustomNameVisible\":true,";
    command += "\"Motion\":" + NbtSpeed.toString() + "}";
    ChatUtils.sendPlayerMsg(command);
  }
}
