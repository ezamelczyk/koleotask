package ernest.zamelczyk.koleotask.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

data class StationItem(
    val id: Long,
    val name: String,
    val city: String,
    val selected: Boolean,
    val selectable: Boolean
)

@Composable
fun StationItem(
    modifier: Modifier = Modifier,
    data: StationItem,
    onCheckedChange: (Long, Boolean) -> Unit
) {
    val color by animateColorAsState(
        targetValue = when {
            data.selected -> MaterialTheme.colorScheme.primaryContainer
            data.selectable -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceDim
        }
    )
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .toggleable(
                value = data.selected,
                enabled = data.selectable,
                onValueChange = { onCheckedChange(data.id, it) }
            ),
        colors = CardDefaults.elevatedCardColors(containerColor = color)
    ) {
        Text(
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
            text = data.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
            text = data.city,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}