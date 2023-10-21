package com.rexleung.android_asset_delivery_demo

import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.play.core.assetpacks.*
import com.rexleung.android_asset_delivery_demo.databinding.FragmentFirstBinding

/**
 * Created by rexchung on 21/10/2023.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var assetPackManager: AssetPackManager? = null

    private val packStateListener = AssetPackStateUpdateListener { assetPackState ->
        val text = "\n" + "${assetPackState.name()} status:${AppAssetPackStatus.get(assetPackState.status())} error:${assetPackState.errorCode()} total:${assetPackState.bytesDownloaded()}/${assetPackState.totalBytesToDownload()}"
        _binding?.vDownloadStatus?.append(text)

        if (AppAssetPackStatus.get(assetPackState.status()) == AppAssetPackStatus.COMPLETED) {
            when (assetPackState.name()) {
                AppAssetPackConstant.packNameFastFollow -> {
                    setupFastFollow()
                }

                AppAssetPackConstant.packNameOnDemand -> {
                    setupOnDemand()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAssetPack()
        setupView()
        setupLog()
    }

    private fun setupView() {
        setupLocalAsset()
        setupInstallTime()
        setupFastFollow()
        setupOnDemand()

        _binding?.vCheckPack?.setOnClickListener {
            getPackStatus()
        }
        _binding?.vDownload?.setOnClickListener {
            downloadPack()
        }
    }

    private fun setupLog() {
        _binding?.vPackStatus?.movementMethod = ScrollingMovementMethod()
        _binding?.vDownloadStatus?.movementMethod = ScrollingMovementMethod()
    }

    private fun setupLocalAsset() {
        with(_binding?.vLocal) {
            val uri = Uri.parse("asset:///app_asset.mp4")
            val item = MediaItem.fromUri(uri)
            val player = ExoPlayer.Builder(requireContext()).build()
            this?.player = player

            player.setMediaItem(item)
            player.prepare()
            player.play()
        }
    }

    private fun setupInstallTime() {
        with(_binding?.vInstallTime) {
            val uri = Uri.parse("asset:///install_time.mp4")
            val item = MediaItem.fromUri(uri)
            val player = ExoPlayer.Builder(requireContext()).build()
            this?.player = player

            player.setMediaItem(item)
            player.prepare()
            player.play()
        }
    }

    private fun setupFastFollow() {
        with(_binding?.vFastFollow) {
            val uri = Uri.parse("${getPackLocationPath(AppAssetPackConstant.packNameFastFollow)}/fast_follow.mp4")
            val item = MediaItem.fromUri(uri)
            val player = ExoPlayer.Builder(requireContext()).build()
            this?.player = player

            player.setMediaItem(item)
            player.prepare()
            player.play()
        }
    }

    private fun setupOnDemand() {
        with(_binding?.vOnDemand) {
            val uri = Uri.parse("${getPackLocationPath(AppAssetPackConstant.packNameOnDemand)}/on_demand.mp4")
            val item = MediaItem.fromUri(uri)
            val player = ExoPlayer.Builder(requireContext()).build()
            this?.player = player

            player.setMediaItem(item)
            player.prepare()
            player.play()
        }
    }

    private fun setupAssetPack() {
        if (assetPackManager == null) {
            assetPackManager = AssetPackManagerFactory.getInstance(this.requireContext())
        }
        assetPackManager?.registerListener(packStateListener)
    }

    /**
     * Action
     */
    private fun getPackStatus() {
        try {
            assetPackManager?.getPackStates(
                listOf(
                    AppAssetPackConstant.packNameFastFollow,
                    AppAssetPackConstant.packNameOnDemand,
                ),
            )?.addOnCompleteListener { task ->
                /**
                 *  Display Log
                 */
                try {
                    var text = ""
                    task.result?.packStates()?.forEach {
                        text += "\n ${it.key} status:${AppAssetPackStatus.get(it.value.status())} error:${it.value.errorCode()} total:${it.value.bytesDownloaded()}/${it.value.totalBytesToDownload()}"
                    }

                    _binding?.vPackStatus?.append(text)
                } catch (e: Exception) {
                    _binding?.vPackStatus?.append(e.message)
                }
            }
        } catch (e: Exception) {
            _binding?.vPackStatus?.append(e.message)
        }
    }

    private fun downloadPack() {
        try {
            assetPackManager?.fetch(
                listOf(
                    AppAssetPackConstant.packNameFastFollow,
                    AppAssetPackConstant.packNameOnDemand,
                ),
            )
        } catch (e: Exception) {
            _binding?.vDownloadStatus?.append(e.message)
        }
    }

    private fun getPackLocationPath(namePack: String): String {
        return try {
            val location = assetPackManager?.getPackLocation(namePack)
            location?.assetsPath() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        assetPackManager?.unregisterListener(packStateListener)
        _binding = null
    }
}
