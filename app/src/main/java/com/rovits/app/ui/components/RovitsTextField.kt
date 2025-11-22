package com.rovits.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.ui.theme.BorderGray
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.theme.TextPrimary

@Composable
fun RovitsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TextPrimary,
            unfocusedBorderColor = BorderGray,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = TextPrimary
        ),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun RovitsTextFieldPreview() {
    RovitsAppTheme {
        var text by remember { mutableStateOf("") }
        RovitsTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Email or Username"
        )
    }
}

