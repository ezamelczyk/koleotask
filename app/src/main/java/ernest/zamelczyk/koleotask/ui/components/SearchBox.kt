package ernest.zamelczyk.koleotask.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    query: String,
    searchResults: List<StationItem>,
    onQueryChanged: (String) -> Unit,
    onItemClick: (Long) -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    var searchExpanded by remember { mutableStateOf(false) }
    DockedSearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChanged,
        onSearch = { searchExpanded = false },
        active = searchExpanded,
        onActiveChange = { searchExpanded = it },
        placeholder = { Text("Wyszukaj stację") },
        leadingIcon = if (searchExpanded) {
            {
                Icon(
                    modifier = Modifier.clickable { searchExpanded = false },
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        } else leadingIcon,
        trailingIcon = trailingIcon,
    ) {
        Stations(
            stations = searchResults,
            onItemClick = {
                onItemClick(it)
                searchExpanded = false
            }
        )
    }
}