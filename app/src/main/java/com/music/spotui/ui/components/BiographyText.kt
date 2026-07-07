package com.music.spotui.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.music.spotui.ui.theme.SpotifyGreen
import com.music.spotui.ui.theme.SpotifyMix

@Composable
fun BiographyText(
    html: String,
    onLinkClick: (uri: String, displayText: String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Gray,
    fontSize: TextUnit = 13.sp,
    fontFamily: FontFamily = SpotifyMix,
    linkColor: Color = SpotifyGreen,
) {
    val parsed = remember(html) { BiographyParser.parse(html) }

    if (parsed.links.isEmpty()) {
        Text(
            text = parsed.text,
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            modifier = modifier,
        )
        return
    }

    val annotatedString = remember(parsed) {
        buildAnnotatedString {
            append(parsed.text)
            for (link in parsed.links) {
                addStyle(
                    SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                    ),
                    link.start,
                    link.end,
                )
                addStringAnnotation("link", link.uri, link.start, link.end)
            }
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
        ),
        modifier = modifier,
        onClick = { offset ->
            annotatedString.getStringAnnotations("link", offset, offset)
                .firstOrNull()
                ?.let { annotation ->
                    val uri = annotation.item
                    val displayText = parsed.links
                        .firstOrNull { it.uri == uri }
                        ?.displayText
                        ?: ""
                    onLinkClick(uri, displayText)
                }
        },
    )
}
