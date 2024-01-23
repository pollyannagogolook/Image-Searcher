package com.pollyannawu.gogolook.compose.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        letterSpacing = (.5).sp,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        letterSpacing = (.5).sp,
        fontSize = 14.sp
    ),
)