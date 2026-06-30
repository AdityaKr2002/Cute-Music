@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.chocola.presentation.screens.setup

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sosauce.chocola.R
import com.sosauce.chocola.data.datastore.rememberMinTrackDuration
import com.sosauce.chocola.presentation.screens.settings.compenents.FoldersView
import com.sosauce.chocola.presentation.screens.settings.compenents.SliderSettingsCards
import com.sosauce.chocola.presentation.shared_components.Spacer

@Composable
fun SetupFolders(
    onNext: () -> Unit
) {

    var minTrackDuration by rememberMinTrackDuration()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(156.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialShapes.Cookie12Sided.toShape()
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.hide_filled),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(15.dp)

        Text(
            text = "Library setup",
            style = MaterialTheme.typography.headlineSmallEmphasized.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "Choose folders to scan and set a minimum track duration.",
            style = MaterialTheme.typography.bodyMediumEmphasized.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )
        FoldersView()
        Spacer(25.dp)
        SliderSettingsCards(
            value = minTrackDuration,
            onValueChange = { minTrackDuration = it },
            topDp = 24.dp,
            bottomDp = 24.dp,
            text = stringResource(R.string.min_track_length_text)
        )

        Spacer(20.dp)

        Button(
            onClick = onNext,
            shapes = ButtonDefaults.shapes(),
        ) {
            Text(
                text = "Let's a meow!"
            )
        }
    }
}