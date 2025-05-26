package com.sosauce.cutemusic.ui.screens.settings.compenents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sosauce.cutemusic.ui.shared_components.CuteText

@Composable
fun SettingsCategoryCard(
    icon: Painter,
    name: String,
    description: String,
    topDp: Dp,
    bottomDp: Dp,
    onNavigate: () -> Unit
) {
    Card(
        onClick = onNavigate,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(
            topStart = topDp,
            topEnd = topDp,
            bottomStart = bottomDp,
            bottomEnd = bottomDp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null
            )
            Spacer(Modifier.width(15.dp))
            Column {
                CuteText(name)
                CuteText(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}