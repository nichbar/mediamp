/*
 * Copyright (C) 2024 OpenAni and contributors.
 *
 * Use of this source code is governed by the GNU GENERAL PUBLIC LICENSE version 3 license, which can be found at the following link.
 *
 * https://github.com/open-ani/mediamp/blob/main/LICENSE
 */

package org.openani.mediamp.compose

import org.openani.mediamp.MediampPlayer
import java.util.ServiceLoader

public object MediampPlayerSurfaceProviderLoader {
    private var factories = ServiceLoader.load(MediampPlayerSurfaceProvider::class.java).toList()

    /**
     * Register a [MediampPlayerSurfaceProvider] implementation.
     */
    public fun register(factory: MediampPlayerSurfaceProvider<*>) {
        factories = (factories + factory).distinctBy { it.forClass }
    }

    public fun first(): MediampPlayerSurfaceProvider<*> = factories.firstOrNull()
        ?: throw IllegalStateException("No MediampPlayerFactory implementation found on the classpath.")

    public fun <T : MediampPlayer> getByInstance(mediampPlayer: T): MediampPlayerSurfaceProvider<T> {
        val res = factories.find {
            it.forClass.isInstance(mediampPlayer)
        } ?: throw IllegalStateException("No MediampPlayerFactory implementation found for $mediampPlayer.")

        @Suppress("UNCHECKED_CAST")
        return res as MediampPlayerSurfaceProvider<T>
    }
}
