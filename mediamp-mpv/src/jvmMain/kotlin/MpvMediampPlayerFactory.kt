/*
 * Copyright (C) 2024 OpenAni and contributors.
 *
 * Use of this source code is governed by the GNU GENERAL PUBLIC LICENSE version 3 license, which can be found at the following link.
 *
 * https://github.com/open-ani/mediamp/blob/main/LICENSE
 */

package org.openani.mediamp.mpv

import org.openani.mediamp.MediampPlayerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class MpvMediampPlayerFactory : MediampPlayerFactory<MpvMediampPlayer> {
    override val forClass: KClass<MpvMediampPlayer> = MpvMediampPlayer::class

    override fun create(context: Any, parentCoroutineContext: CoroutineContext): MpvMediampPlayer {
        return MpvMediampPlayer(context, parentCoroutineContext)
    }
}