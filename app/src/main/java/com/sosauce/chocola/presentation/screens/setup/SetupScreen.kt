@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.chocola.presentation.screens.setup

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sosauce.chocola.presentation.screens.setup.components.SetupBottomBar
import com.sosauce.chocola.utils.hasMusicPermission
import kotlinx.coroutines.launch

@Composable
fun SetupScreen(
    onNavigateToApp: () -> Unit
) {

    var setupProgress by remember { mutableIntStateOf(0) }

    Scaffold { pv ->
        Crossfade(
            modifier = Modifier
                .padding(pv)
                .padding(horizontal = 10.dp),
            targetState = setupProgress
        ) { progress ->
            when(progress) {
                0 -> SetupPermissions { setupProgress = 1 }
                1 -> SetupFolders(onNavigateToApp)
            }
        }

    }
}