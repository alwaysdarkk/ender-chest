package com.github.alwaysdarkk.enderchest.cache;

import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;

import java.util.Map;

public class EnderChestCache {

    @Delegate
    private final Map<String, EnderChest> enderChestMap = Maps.newHashMap();

}