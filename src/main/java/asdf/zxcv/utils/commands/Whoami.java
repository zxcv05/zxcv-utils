package asdf.zxcv.utils.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import asdf.zxcv.utils.modules.WhoamiOnJoin;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

public class Whoami extends Command {

    public Whoami() {
        super("whoami", "Send your current username and permission level");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            WhoamiOnJoin.sendChat();
            return SINGLE_SUCCESS;
        });
    }
}
