package me.dragonsteam.bungeestaffs.commands.types;

import me.dragonsteam.bungeestaffs.bStaffs;
import me.dragonsteam.bungeestaffs.loaders.Chats;
import me.dragonsteam.bungeestaffs.loaders.Comms;
import me.dragonsteam.bungeestaffs.loaders.Lang;
import me.dragonsteam.bungeestaffs.utils.CommandType;
import me.dragonsteam.bungeestaffs.utils.defaults.ChatUtils;
import me.dragonsteam.bungeestaffs.utils.defaults.ConfigFile;
import me.dragonsteam.bungeestaffs.utils.defaults.ToggleUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 09/11/2021 - 17:51.
 */
public class ToggleChatCMD extends Command implements TabExecutor {

    public ToggleChatCMD() {
        super("togglechat", "bstaffs.togglechat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Return if sender is not a player.
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 1) {
            Chats chats = null;
            for (String s : Chats.getChatsHashMap().keySet()) {
                if (s.equalsIgnoreCase(args[0])) chats = Chats.getChatByInput(s);
            }

            if (chats == null) {
                player.sendMessage(new TextComponent(Lang.CHAT_NOT_FOUND.toString()));
                return;
            }

            ConfigFile file = bStaffs.INSTANCE.getChatsFile();
            ChatUtils.setDefaultIfNotSet(file.getConfiguration(), "TOGGLE-CHAT-PERMISSION", "bstaffs.togglechat");
            file.save();

            if (!hasPerm(player, bStaffs.INSTANCE.getChatsFile().getString("TOGGLE-CHAT-PERMISSION"))) {
                player.sendMessage(new TextComponent(Lang.NO_PERMISSION.toString()));
                return;
            }

            ToggleUtils.togglePlayerChat(player, chats);
            player.sendMessage(new TextComponent(Lang.CHAT_TOGGLED.toString()
                    .replace("<chat>", chats.getInput())
                    .replace("<value>", ToggleUtils.isToggledChat(player, chats) ? Lang.BOOLEAN_FALSE.toString() : Lang.BOOLEAN_TRUE.toString())
            ));
            return;
        }

        player.sendMessage(ChatUtils.translate("&cUsage: &f/togglechat <input>"));
    }

    private boolean hasPerm(ProxiedPlayer player, String permission) {
        if (permission.equalsIgnoreCase("")) return true;
        return player.hasPermission(permission);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            Chats.getChatsHashMap().keySet().forEach(chat -> {
                if (chat.startsWith(args[0]))
                    arguments.add(chat);
            });
            return arguments;
        }

        return Collections.emptyList();
    }

}
