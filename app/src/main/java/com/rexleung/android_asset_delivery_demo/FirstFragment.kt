package com.rexleung.android_asset_delivery_demo

import android.net.Uri
import android.os.Bundle
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
        println(text)
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
    }

    private fun setupView() {
        setupLocalAsset()
        setupInstallTime()
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

    private fun setupAssetPack() {
        if (assetPackManager == null) {
            assetPackManager = AssetPackManagerFactory.getInstance(this.requireContext())
        }
        assetPackManager?.registerListener(packStateListener)
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
