package de.s42.duplicatemob;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Duplicatemob implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("duplicatemob");

    public void onInitialize() {
        LOGGER.info("DuplicateMob initializing");
    }
}
