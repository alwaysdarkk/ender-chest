package com.github.alwaysdarkk.enderchest.data;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.Inventory;

@Data
@Builder
public class EnderChest {

    private final String owner;
    private Inventory inventory;

}