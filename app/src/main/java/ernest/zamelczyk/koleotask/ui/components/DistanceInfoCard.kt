package ernest.zamelczyk.koleotask.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeOfTravel
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DistanceInfoCard(
    homeName: String,
    destinationName: String,
    distance: String,
    onRemoveHome: () -> Unit,
    onRemoveDestination: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (homeName.isNotBlank()) {
                        Icon(
                            modifier = Modifier.clickable(onClick = onRemoveHome),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                value = homeName,
                onValueChange = {}, enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (destinationName.isNotBlank()) {
                        Icon(
                            modifier = Modifier.clickable(onClick = onRemoveDestination),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                value = destinationName,
                onValueChange = {},
                enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ModeOfTravel,
                        contentDescription = null
                    )
                },
                value = distance,
                onValueChange = {},
                enabled = false
            )
        }
    }
}