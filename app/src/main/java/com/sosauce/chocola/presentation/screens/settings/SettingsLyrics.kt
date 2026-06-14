package com.sosauce.chocola.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.sosauce.chocola.R
import com.sosauce.chocola.data.datastore.rememberLyricsAlignment
import com.sosauce.chocola.data.datastore.rememberLyricsFontSize
import com.sosauce.chocola.presentation.screens.settings.compenents.SettingsDropdownMenu
import com.sosauce.chocola.presentation.screens.settings.compenents.SettingsWithTitle
import com.sosauce.chocola.utils.LyricsAlignment

@Composable
fun SettingsLyrics() {

    var lyricsAlignment by rememberLyricsAlignment()
    var lyricsFontSize by rememberLyricsFontSize()

    val lyricsAlignmentOptions = listOf(
        LyricsAlignment.START,
        LyricsAlignment.CENTERED,
        LyricsAlignment.END
    )

    Column {
        SettingsWithTitle(
            title = R.string.lyrics
        ) {
            SettingsDropdownMenu(
                value = lyricsAlignment,
                topDp = 24.dp,
                bottomDp = 4.dp,
                text = R.string.alignment
            ) { onClose ->
                lyricsAlignmentOptions.fastForEachIndexed { index, alignment ->
                    val selected = alignment == lyricsAlignment

                    val trailingIcon: @Composable (() -> Unit)? = if (selected) {
                        {
                            Icon(
                                painter = painterResource(R.drawable.check),
                                contentDescription = null
                            )
                        }
                    } else null

                    DropdownMenuItem(
                        selected = selected,
                        onClick = {
                            lyricsAlignment = alignment
                            onClose()
                        },
                        shapes = MenuDefaults.itemShape(index, lyricsAlignmentOptions.size),
                        text = { Text(alignment) },
                        trailingIcon = trailingIcon
                    )
                }
            }
            SettingsDropdownMenu(
                value = lyricsFontSize,
                topDp = 4.dp,
                bottomDp = 24.dp,
                text = R.string.font_size
            ) { onClose ->
                (20..40).forEachIndexed { index, size ->

                    val selected = size == lyricsFontSize

                    val trailingIcon: @Composable (() -> Unit)? = if (selected) {
                        {
                            Icon(
                                painter = painterResource(R.drawable.check),
                                contentDescription = null
                            )
                        }
                    } else null
                    DropdownMenuItem(
                        selected = selected,
                        onClick = {
                            lyricsFontSize = size
                            onClose()
                        },
                        shapes = MenuDefaults.itemShape(index, 20),
                        text = { Text(size.toString()) },
                        trailingIcon = trailingIcon
                    )
                }
            }
        }
    }
}