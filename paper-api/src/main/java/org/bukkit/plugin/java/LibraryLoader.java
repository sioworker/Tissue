package org.bukkit.plugin.java;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builds the class loader for a plugin's bundled library jars.
 * <p>
 * Tissue does not download {@code libraries:} declared in {@code plugin.yml};
 * plugins have to shade their dependencies instead.
 *
 * @hidden
 */
@org.jetbrains.annotations.ApiStatus.Internal
public class LibraryLoader {

    private final Logger logger;
    public static java.util.function.BiFunction<URL[], ClassLoader, URLClassLoader> LIBRARY_LOADER_FACTORY; // Paper - rewrite reflection in libraries

    public LibraryLoader(@NotNull Logger logger) {
        this.logger = logger;
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc) {
        return this.createLoader(desc, null);
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc, java.util.@Nullable List<java.nio.file.Path> paperLibraryPaths) {
        if (!desc.getLibraries().isEmpty()) {
            throw new IllegalStateException(desc.getFullName() + " lists libraries in plugin.yml, which Tissue does not download; shade them into the plugin jar instead");
        }
        if (paperLibraryPaths == null) {
            return null;
        }

        List<URL> jarFiles = new ArrayList<>();
        for (java.nio.file.Path path : paperLibraryPaths) {
            File file = path.toFile();
            URL url;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException ex) {
                throw new AssertionError(ex);
            }

            jarFiles.add(url);
            logger.log(Level.INFO, "[{0}] Loaded library {1}", new Object[]
                {
                    java.util.Objects.requireNonNullElseGet(desc.getPrefix(), desc::getName), file // Paper - use configured log prefix
                });
        }

        // Paper start - rewrite reflection in libraries
        URLClassLoader loader;
        if (LIBRARY_LOADER_FACTORY == null) {
            loader = new URLClassLoader(jarFiles.toArray(new URL[jarFiles.size()]), getClass().getClassLoader());
        } else {
            loader = LIBRARY_LOADER_FACTORY.apply(jarFiles.toArray(new URL[jarFiles.size()]), getClass().getClassLoader());
        }
        // Paper end - rewrite reflection in libraries

        return loader;
    }
}
