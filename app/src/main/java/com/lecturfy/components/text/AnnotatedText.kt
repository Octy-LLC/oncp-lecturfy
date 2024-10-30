package com.lecturfy.components.text

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class AnnotatedTextPart {
    data class TextPart(val text: String) : AnnotatedTextPart()
    data class LinkPart(val text: String, val url: String) : AnnotatedTextPart()
}

@Composable
fun AnnotatedText(
    parts: List<AnnotatedTextPart>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: Dp = Dp.Unspecified,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
) {
    val context = LocalContext.current
    val lineHeightSp = lineHeight.takeIf { it != Dp.Unspecified }?.let {
        with(LocalDensity.current) { it.toSp() }
    }

    val annotatedString = buildAnnotatedString {
        parts.forEach { part ->
            when (part) {
                is AnnotatedTextPart.TextPart -> {
                    append(part.text)
                }

                is AnnotatedTextPart.LinkPart -> {
                    val start = length
                    append(part.text)
                    addStyle(
                        style = SpanStyle(
                            color = color,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = start,
                        end = length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = part.url,
                        start = start,
                        end = length
                    )
                }
            }
        }
    }
    if (parts.any { it is AnnotatedTextPart.LinkPart }) {
        ClickableText(
            text = annotatedString,
            modifier = modifier,
            style = TextStyle(
                textAlign = textAlign,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color,
                lineHeight = lineHeightSp ?: fontSize // fallback if lineHeight is unspecified
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            }
        )
    } else {
        Text(
            text = annotatedString,
            modifier = modifier,
            style = TextStyle(
                textAlign = textAlign,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color,
                lineHeight = lineHeightSp ?: fontSize // fallback if lineHeight is unspecified
            ),
        )
    }
}
