package com.rovits.app.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.rovits.app.R
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.theme.TextSecondary
import com.rovits.app.ui.theme.TextPrimary

@Composable
fun TermsPrivacyText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val termsText = stringResource(id = R.string.terms_of_use)
    val privacyText = stringResource(id = R.string.privacy_policy)
    val fullText = stringResource(id = R.string.terms_privacy_text)

    val annotatedString = buildAnnotatedString {
        // Find and style the terms and privacy text
        val termsStart = fullText.indexOf(termsText)
        val privacyStart = fullText.indexOf(privacyText)

        if (termsStart >= 0 && privacyStart >= 0) {
            // Text before terms
            withStyle(
                style = SpanStyle(
                    color = TextSecondary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) {
                append(fullText.substring(0, termsStart))
            }

            // Terms of Use - clickable
            pushStringAnnotation(tag = "TERMS", annotation = "terms")
            withStyle(
                style = SpanStyle(
                    color = TextPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(termsText)
            }
            pop()

            // Text between terms and privacy
            withStyle(
                style = SpanStyle(
                    color = TextSecondary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) {
                append(fullText.substring(termsStart + termsText.length, privacyStart))
            }

            // Privacy Policy - clickable
            pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
            withStyle(
                style = SpanStyle(
                    color = TextPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(privacyText)
            }
            pop()

            // Text after privacy
            if (privacyStart + privacyText.length < fullText.length) {
                withStyle(
                    style = SpanStyle(
                        color = TextSecondary,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append(fullText.substring(privacyStart + privacyText.length))
                }
            }
        } else {
            // Fallback if strings not found
            withStyle(
                style = SpanStyle(
                    color = TextSecondary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) {
                append(fullText)
            }
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let {
                    onTermsClick()
                }

            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let {
                    onPrivacyClick()
                }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TermsPrivacyTextPreview() {
    RovitsAppTheme {
        TermsPrivacyText(
            onTermsClick = {},
            onPrivacyClick = {}
        )
    }
}

