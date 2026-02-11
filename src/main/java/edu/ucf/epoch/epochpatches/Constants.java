package edu.ucf.epoch.epochpatches;

import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Constants {
	/** The name of the world in the server. For season 3 it was "expedition3". */
	public static final String WORLD_NAME = "New World";
	
	public static final Path EPOCH_FOLDER = FMLPaths.GAMEDIR.get().resolve("epochpatches");
	
	static {
		if (Files.notExists(EPOCH_FOLDER)) {
			try {
				Files.createDirectory(EPOCH_FOLDER);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
