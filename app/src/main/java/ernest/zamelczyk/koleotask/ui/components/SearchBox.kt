package ernest.zamelczyk.koleotask.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    query: String,
    searchResults: List<StationItem>,
    onQueryChanged: (String) -> Unit,
    onItemCheckedChange: (Long, Boolean) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        var searchExpanded by remember { mutableStateOf(false) }
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = query,
            onQueryChange = onQueryChanged,
            onSearch = { searchExpanded = false },
            active = searchExpanded,
            onActiveChange = { searchExpanded = it },
            placeholder = { Text("Wyszukaj stację") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        searchExpanded = false
                        onQueryChanged("")
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            },
        ) {
            Stations(
                stations = searchResults,
                onItemCheckedChange = onItemCheckedChange
            )
        }
    }
}