/*
 * Copyright (C) 2024 OpenAni and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the Apache-2.0 license, which can be found at the following link.
 *
 * https://github.com/open-ani/mediamp/blob/main/LICENSE
 */

package org.openani.mediamp.backend.mpv

import kotlin.contracts.contract

sealed class Platform {
    abstract val name: String // don't change, it's actually an ID
    abstract val arch: Arch

    val nameAndArch get() = "$name ${arch.displayName}"
    final override fun toString(): String = nameAndArch

    ///////////////////////////////////////////////////////////////////////////
    // mobile
    ///////////////////////////////////////////////////////////////////////////

    sealed class Mobile : Platform()

    data class Android(
        override val arch: Arch,
    ) : Mobile() {
        override val name: String get() = "Android"
    }

    data object Ios : Mobile() {
        override val name: String get() = "iOS"
        override val arch: Arch get() = Arch.AARCH64
    }


    ///////////////////////////////////////////////////////////////////////////
    // desktop
    ///////////////////////////////////////////////////////////////////////////

    sealed class Desktop(
        override val name: String
    ) : Platform()

    data class Windows(
        override val arch: Arch
    ) : Desktop("Windows")

    data class MacOS(
        override val arch: Arch
    ) : Desktop("macOS")

    data class Linux(
        override val arch: Arch
    ) : Desktop("Linux")
}


@Suppress("ObjectPropertyName")
private val _currentPlatform = runCatching { currentPlatformImpl() } // throw only on get

/**
 * 获取当前的平台. 在 Linux 上使用时会抛出 [UnsupportedOperationException].
 *
 * CI 会跑 Ubuntu test (比较快), 所以在 test 环境需要谨慎使用此 API.
 */

fun currentPlatform(): Platform = _currentPlatform.getOrThrow()


fun currentPlatformDesktop(): Platform.Desktop {
    val platform = currentPlatform()
    check(platform is Platform.Desktop)
    return platform
}

enum class ArchFamily {
    X86,
    AARCH,
}

// It's actually ABI
enum class Arch(
    val displayName: String, // Don't change, used by the server
    val family: ArchFamily,
    val addressSizeBits: Int,
) {
    /**
     * macOS, Windows, Android
     */
    X86_64("x86_64", ArchFamily.X86, 64),

    /**
     * macOS
     */
    AARCH64("aarch64", ArchFamily.AARCH, 64),

    /**
     * AArch32 的一个细分 ABI. 只有很久的手机或电视才会用.
     */
    ARMV7A("armeabi-v7a", ArchFamily.AARCH, 32),

    /**
     * AArch64 的一个细分 ABI. 目前绝大多数手机都是这个.
     */
    ARMV8A("arm64-v8a", ArchFamily.AARCH, 64),
}

internal expect fun currentPlatformImpl(): Platform