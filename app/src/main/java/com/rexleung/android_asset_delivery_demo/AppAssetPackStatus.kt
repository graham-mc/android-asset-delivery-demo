package com.rexleung.android_asset_delivery_demo

import com.google.android.play.core.assetpacks.model.AssetPackStatus

enum class AppAssetPackStatus {
    CANCELED,
    COMPLETED,
    DOWNLOADING,
    FAILED,
    NOT_INSTALLED,
    PENDING,
    TRANSFERRING,
    WAITING_FOR_WIFI,

    UNKNOWN,

    ;

    companion object {
        fun get(@AssetPackStatus status: Int): AppAssetPackStatus {
            return when (status) {
                AssetPackStatus.CANCELED -> {
                    CANCELED
                }

                AssetPackStatus.COMPLETED -> {
                    COMPLETED
                }

                AssetPackStatus.DOWNLOADING -> {
                    DOWNLOADING
                }

                AssetPackStatus.FAILED -> {
                    FAILED
                }

                AssetPackStatus.NOT_INSTALLED -> {
                    NOT_INSTALLED
                }

                AssetPackStatus.PENDING -> {
                    PENDING
                }

                AssetPackStatus.TRANSFERRING -> {
                    TRANSFERRING
                }

                AssetPackStatus.WAITING_FOR_WIFI -> {
                    WAITING_FOR_WIFI
                }

                else -> {
                    UNKNOWN
                }
            }
        }
    }
}
