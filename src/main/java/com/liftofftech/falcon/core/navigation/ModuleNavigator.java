package com.liftofftech.falcon.core.navigation;

import com.liftofftech.falcon.core.config.FrameworkConfig;
import com.liftofftech.falcon.core.driver.ModuleType;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for constructing module-specific URLs.
 * Handles subdomain-based URL construction with environment awareness.
 */
public final class ModuleNavigator {

    private ModuleNavigator() {
        // utility class
    }

    /**
     * Builds a complete module URL with optional query parameters.
     * Automatically handles stage vs prod environments based on configuration.
     * 
     * @param module the module type (IMAGE, VIDEO, AUDIO, etc.)
     * @param queryParams optional query parameters (can be null or empty)
     * @return complete URL (e.g., "https://image.galaxy.ai/ai-image-generator?model=nano-banana-pro")
     */
    public static String buildModuleUrl(ModuleType module, Map<String, String> queryParams) {
        String subdomain = FrameworkConfig.moduleSubdomain(module);
        String baseDomain = FrameworkConfig.baseDomain();
        String basePath = FrameworkConfig.moduleBasePath(module);

        StringBuilder url = new StringBuilder("https://");
        url.append(subdomain).append(".").append(baseDomain);
        url.append(basePath);

        if (queryParams != null && !queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
            url.append("?").append(queryString);
        }

        return url.toString();
    }
}

