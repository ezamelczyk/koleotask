package ernest.zamelczyk.koleotask.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ernest.zamelczyk.koleotask.utils.plus

@Composable
fun Stations(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    header: (@Composable () -> Unit)? = null,
    stations: List<StationItem>,
    onItemCheckedChange: (Long, Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding + PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        header?.let {
            stickyHeaderContentPaddingAware(listState = listState, key = 0) {
                header()
            }
        }
        items(items = stations, key = StationItem::id) {
            StationItem(
                modifier = Modifier.animateItemPlacement(),
                data = it,
                onCheckedChange = onItemCheckedChange
            )
        }
    }
}