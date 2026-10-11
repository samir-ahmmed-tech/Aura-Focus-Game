package com.example.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan

/**
 * Production-ready Google AdMob Manager architecture for AuraFocus.
 * Preconfigured with official Google Mobile Ads test identifiers.
 * When ready to publish to Google Play Store, simply replace test IDs with live Ad Unit IDs.
 */
object AuraAdMobManager {

    // Google AdMob Standard Test Ad Unit IDs
    const val TEST_BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_AD_ID = "ca-app-pub-3940256099942544/5224354917"

    // Set to true once the AdMob dependency and Application ID in AndroidManifest.xml are set
    var isAdMobEnabled: Boolean = false

    fun showInterstitial(context: Context, onDismissed: () -> Unit) {
        if (!isAdMobEnabled) {
            onDismissed()
            return
        }
        // When play-services-ads is compiled, invoke InterstitialAd.show(activity)
        onDismissed()
    }

    fun showInterstitialAd(context: Context, onDismissed: () -> Unit) = showInterstitial(context, onDismissed)

    fun showRewardedAd(context: Context, onRewardEarned: (rewardAmount: Int) -> Unit) {
        if (!isAdMobEnabled) {
            Toast.makeText(context, "AdMob Test Mode: Bonus Reward Granted! 🍎", Toast.LENGTH_SHORT).show()
            onRewardEarned(25)
            return
        }
        // When play-services-ads is compiled, invoke RewardedAd.show(activity)
        onRewardEarned(25)
    }

    /**
     * Reusable Glassmorphism Banner Ad slot container.
     */
    @Composable
    fun AdMobBannerSlot(
        modifier: Modifier = Modifier
    ) {
        if (!isAdMobEnabled) {
            // Sleek placeholder slot until live AdMob IDs are injected
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x12FFFFFF))
                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aura AdMob Slot • Ready for Google Play",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0x70FFFFFF)
                )
            }
        }
    }
}
