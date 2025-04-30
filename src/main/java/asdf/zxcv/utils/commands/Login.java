package asdf.zxcv.utils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import asdf.zxcv.utils.Mod;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.accounts.types.CrackedAccount;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandSource;

public class Login extends Command {

    public Login() {
      super("login", "Will reconnect with specific username");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(argument("name", StringArgumentType.string()).executes(ctx -> {
        String name = StringArgumentType.getString(ctx, "name");
        new CrackedAccount(name).login();

        ServerInfo serverInfo = Mod.mc.getNetworkHandler().getServerInfo();
        Mod.mc.world.disconnect();
        ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), Mod.mc, 
            ServerAddress.parse(serverInfo.address), serverInfo, false, null);

        return SINGLE_SUCCESS;
      }));
    }
}
