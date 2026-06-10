package com.andrei1058.spigot.sidebar.dist;

import com.andrei1058.spigot.sidebar.SidebarManager;

/**
 * Bootstrap class for the shaded distribution JAR.
 * Ensures the SidebarManager can be initialized at runtime.
 */
@SuppressWarnings("unused")
public final class DistBootstrap {

    private DistBootstrap() {}

    public static SidebarManager init() {
        return SidebarManager.init();
    }
}
