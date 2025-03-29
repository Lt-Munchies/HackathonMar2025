package com.devconnect.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.graphics.Color

object SyntaxHighlighter {
    private val keywords = setOf(
        "fun", "val", "var", "class", "interface", "object", "package", "import",
        "return", "if", "else", "when", "while", "for", "do", "break", "continue",
        "public", "private", "protected", "internal", "override", "abstract", "final",
        "companion", "const", "lateinit", "sealed", "suspend", "enum", "data"
    )

    private val colors = object {
        val keyword = Color(0xFF7B1FA2) // Purple
        val string = Color(0xFF689F38) // Green
        val number = Color(0xFF1976D2) // Blue
        val comment = Color(0xFF9E9E9E) // Gray
        val punctuation = Color(0xFF795548) // Brown
        val annotation = Color(0xFFF57C00) // Orange
        val default = Color(0xFF212121) // Dark Gray
    }

    fun highlight(code: String, language: String): AnnotatedString = buildAnnotatedString {
        // Basic tokenization and highlighting
        val tokens = code.split(Regex("(\\s+)"))
        var inString = false
        var inComment = false
        
        tokens.forEach { token ->
            when {
                token.startsWith("//") || inComment -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.comment)
                        )
                    )
                    inComment = token.contains("\n").not()
                }
                token.startsWith("\"") || inString -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.string)
                        )
                    )
                    inString = token.endsWith("\"").not()
                }
                token.matches(Regex("[0-9]+")) -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.number)
                        )
                    )
                }
                token in keywords -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.keyword)
                        )
                    )
                }
                token.startsWith("@") -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.annotation)
                        )
                    )
                }
                token.matches(Regex("[.,(){}\\[\\];:]")) -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.punctuation)
                        )
                    )
                }
                else -> {
                    append(
                        AnnotatedString(
                            text = token,
                            spanStyle = SpanStyle(color = colors.default)
                        )
                    )
                }
            }
            
            // Add any whitespace back
            if (token.endsWith(" ")) {
                append(" ")
            }
        }
    }
}
