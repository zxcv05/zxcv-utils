package asdf.zxcv.utils.modules;

import asdf.zxcv.utils.Mod;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Text;

public class WhoamiOnJoin extends Module {

    private static boolean shouldChat = false;

    public WhoamiOnJoin() {
        super(
            Categories.Misc,
            "whoami-on-join",
            "Will show your username and permission level in chat after joining a server"
        );
    }

    public static void onJoinWorld() {
        shouldChat = Modules.get().isActive(WhoamiOnJoin.class);
    }

    public static void onPlayerSetPermission() {
        if (!shouldChat) return;
        shouldChat = false;
        sendChat();
    }

    public static void sendChat() {
        if (Mod.mc.world == null || Mod.mc.player == null) return;

        ChatUtils.sendMsg("zxcv-utils", Text.literal("Logged in as " + Mod.mc.getSession().getUsername()));
        ChatUtils.sendMsg("zxcv-utils", Text.literal("Permission level: " + Mod.mc.player.getPermissionLevel()));
    }
}
