package com.rovits.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.ui.theme.RovitsAppTheme

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileInfoRowPreview() {
    RovitsAppTheme {
        ProfileInfoRow(label = "Ad Soyad", value = "Ali Sari")
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileInfoMailRowPreview() {
    RovitsAppTheme {
        ProfileInfoRow(label = "Mail", value = "william.henry.harrison@example-pet-store.com")
    }
}




