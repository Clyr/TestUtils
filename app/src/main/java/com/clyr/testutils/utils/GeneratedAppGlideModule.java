package com.clyr.testutils.utils;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 *
 * @author lzy of clyr
 * @date 2022/06/17
 */

@GlideModule
public class GeneratedAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
