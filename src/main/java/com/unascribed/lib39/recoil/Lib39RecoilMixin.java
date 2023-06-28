package com.unascribed.lib39.recoil;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.core.api.AutoMixin;

public class Lib39RecoilMixin extends AutoMixin {

    @Override
    protected boolean getConfigValue(String key) {
        return ("platform "+ P39.meta().target()).equals(key);
    }

}
