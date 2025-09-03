@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.cutemusic.presentation.screens.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sosauce.cutemusic.R
import com.sosauce.cutemusic.data.datastore.rememberAppTheme
import com.sosauce.cutemusic.data.datastore.rememberShowBackButton
import com.sosauce.cutemusic.data.datastore.rememberShowShuffleButton
import com.sosauce.cutemusic.data.datastore.rememberShowXButton
import com.sosauce.cutemusic.data.datastore.rememberUseSystemFont
import com.sosauce.cutemusic.presentation.screens.settings.compenents.FontSelector
import com.sosauce.cutemusic.presentation.screens.settings.compenents.SettingsCards
import com.sosauce.cutemusic.presentation.screens.settings.compenents.SettingsWithTitle
import com.sosauce.cutemusic.presentation.screens.settings.compenents.ThemeSelector
import com.sosauce.cutemusic.presentation.shared_components.LazyRowWithScrollButton
import com.sosauce.cutemusic.presentation.shared_components.ScaffoldWithBackArrow
import com.sosauce.cutemusic.presentation.theme.nunitoFontFamily
import com.sosauce.cutemusic.utils.CuteTheme
import com.sosauce.cutemusic.utils.anyDarkColorScheme
import com.sosauce.cutemusic.utils.anyLightColorScheme

@Composable
fun SettingsLookAndFeel(
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    var theme by rememberAppTheme()
    var useSystemFont by rememberUseSystemFont()
    var showXButton by rememberShowXButton()
    var showShuffleButton by rememberShowShuffleButton()
    var showBackButton by rememberShowBackButton()
    val themeItems = listOf(
        ThemeItem(
            onClick = { theme = CuteTheme.SYSTEM },
            backgroundColor = if (isSystemInDarkTheme()) anyDarkColorScheme().background else anyLightColorScheme().background,
            text = stringResource(R.string.system),
            isSelected = theme == CuteTheme.SYSTEM,
            iconAndTint = Pair(
                painterResource(R.drawable.system_theme),
                if (isSystemInDarkTheme()) anyDarkColorScheme().onBackground else anyLightColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.DARK },
            backgroundColor = anyDarkColorScheme().background,
            text = stringResource(R.string.dark_mode),
            isSelected = theme == CuteTheme.DARK,
            iconAndTint = Pair(
                painterResource(R.drawable.dark_mode),
                anyDarkColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.LIGHT },
            backgroundColor = anyLightColorScheme().background,
            text = stringResource(R.string.light_mode),
            isSelected = theme == CuteTheme.LIGHT,
            iconAndTint = Pair(
                rememberVectorPainter(Icons.Outlined.LightMode),
                anyLightColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.AMOLED },
            backgroundColor = Color.Black,
            text = stringResource(R.string.amoled_mode),
            isSelected = theme == CuteTheme.AMOLED,
            iconAndTint = Pair(rememberVectorPainter(Icons.Rounded.Contrast), Color.White)
        )
    )
    val fontItems = listOf(
        FontItem(
            onClick = { useSystemFont = false },
            fontStyle = FontStyle.DEFAULT,
            borderColor = if (!useSystemFont) MaterialTheme.colorScheme.primary else Color.Transparent,
            text = {
                Text(
                    text = "Tt",
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
        ),
        FontItem(
            onClick = { useSystemFont = true },
            fontStyle = FontStyle.SYSTEM,
            borderColor = if (useSystemFont) MaterialTheme.colorScheme.primary else Color.Transparent,
            text = {
                Text(
                    text = "Tt",
                    fontWeight = FontWeight.Bold
                )
            }
        )
    )

    ScaffoldWithBackArrow(
        backArrowVisible = !scrollState.canScrollBackward,
        onNavigateUp = onNavigateUp
    ) { pv ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(pv)
        ) {
            SettingsWithTitle(
                title = R.string.theme
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    LazyRowWithScrollButton(
                        items = themeItems
                    ) { theme ->
                        ThemeSelector(theme)
                    }
                }
            }
            SettingsWithTitle(
                title = R.string.font
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    LazyRowWithScrollButton(
                        items = fontItems
                    ) { font ->
                        FontSelector(font)
                    }
                }
            }
            SettingsWithTitle(
                title = R.string.UI
            ) {
                SettingsCards(
                    checked = showBackButton,
                    onCheckedChange = { showBackButton = !showBackButton },
                    topDp = 24.dp,
                    bottomDp = 24.dp,
                    text = stringResource(R.string.show_back_button)
                )
            }
            SettingsWithTitle(
                title = R.string.cute_searchbar
            ) {
                SettingsCards(
                    checked = showXButton,
                    onCheckedChange = { showXButton = !showXButton },
                    topDp = 24.dp,
                    bottomDp = 4.dp,
                    text = stringResource(R.string.show_close_button)
                )
                SettingsCards(
                    checked = showShuffleButton,
                    onCheckedChange = { showShuffleButton = !showShuffleButton },
                    topDp = 4.dp,
                    bottomDp = 24.dp,
                    text = stringResource(R.string.show_shuffle_btn)
                )
            }
        }
    }
}

@Immutable
data class ThemeItem(
    val onClick: () -> Unit,
    val backgroundColor: Color,
    val text: String,
    val isSelected: Boolean,
    val iconAndTint: Pair<Painter, Color>
)

@Immutable
data class FontItem(
    val onClick: () -> Unit,
    val fontStyle: FontStyle,
    val borderColor: Color,
    val text: @Composable () -> Unit
)

enum class FontStyle {
    DEFAULT,
    SYSTEM
}