package xyz.mcutils.backend.common;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class EnvironmentUtils {
    /**
     * Is the app running in a production environment?
     */
    @Getter
    private static final boolean production;
    static { // Are we running on production?
        String env = System.getenv("ENVIRONMENT");
        production = env != null && (env.equals("production"));
    }
}