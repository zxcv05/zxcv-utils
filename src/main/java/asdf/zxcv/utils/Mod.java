package asdf.zxcv.utils;

import asdf.zxcv.utils.commands.Whoami;
import asdf.zxcv.utils.modules.WhoamiOnJoin;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod extends MeteorAddon {

    public static final Logger LOG = LoggerFactory.getLogger("zxcv-utils");
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        LOG.info("zxcv-utils init");

        Modules.get().add(new WhoamiOnJoin());
        Commands.add(new Whoami());
    }

    @Override
    public String getPackage() {
        return "asdf.zxcv.utils";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("zxcv05", "zxcv-utils");
    }
}
