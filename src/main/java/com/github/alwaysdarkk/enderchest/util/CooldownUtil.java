package com.github.alwaysdarkk.enderchest.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class CooldownUtil {

    private final Table<String, String, Long> COOLDOWNS = HashBasedTable.create();

    public void create(String nickname, String identifier, long time, TimeUnit timeUnit) {
        COOLDOWNS.put(
                nickname,
                identifier,
                System.currentTimeMillis() + timeUnit.toMillis(time)
        );
    }

    public boolean isFinished(String nickname, String identifier) {
        if (!COOLDOWNS.contains(nickname, identifier)) return true;

        if (COOLDOWNS.get(nickname, identifier) <= System.currentTimeMillis()) {
            COOLDOWNS.remove(nickname, identifier);
            return true;
        }

        return false;
    }

    public long getMillisLeft(String nickname, String identifier) {
        return isFinished(nickname, identifier) ? 0L : COOLDOWNS.get(nickname, identifier) - System.currentTimeMillis();
    }
}