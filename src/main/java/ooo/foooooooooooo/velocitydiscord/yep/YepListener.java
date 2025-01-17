package ooo.foooooooooooo.velocitydiscord.yep;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import ooo.foooooooooooo.velocitydiscord.VelocityDiscord;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class YepListener {
    private final Logger logger;

    public YepListener(Logger logger) {
        this.logger = logger;
        logger.info("YepListener created");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(VelocityDiscord.YepIdentifier)) {
            return;
        }

        String data = new String(event.getData(), StandardCharsets.UTF_8);

        // username:type:message
        var parts = data.split(":");

        if (parts.length != 3) {
            logger.warning("Invalid yep message: " + data);
            return;
        }

        MessageType type = null;
        try {
            type = MessageType.valueOf(parts[1]);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid yep message type: " + parts[0]);
        }

        var player = parts[0];
        var message = parts[2];
        var discord = VelocityDiscord.getDiscord();

        if (type == null) return;

        switch (type) {
            case DEATH -> discord.playerDeath(player, DeathMessage.fromString(message));
            case ADVANCEMENT -> discord.playerAdvancement(player, AdvancementMessage.fromString(message));
            default -> logger.warning("Invalid yep message type: " + parts[0]);
        }
    }
}
