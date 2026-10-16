package com.distinctroutes.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val BlueLightColorScheme = lightColorScheme(
    primary = BlueLightPrimary,
    onPrimary = BlueLightOnPrimary,
    primaryContainer = BlueLightPrimaryContainer,
    onPrimaryContainer = BlueLightOnPrimaryContainer,
    secondary = BlueLightSecondary,
    onSecondary = BlueLightOnSecondary,
    secondaryContainer = BlueLightSecondaryContainer,
    onSecondaryContainer = BlueLightOnSecondaryContainer,
    tertiary = BlueLightTertiary,
    onTertiary = BlueLightOnTertiary,
    tertiaryContainer = BlueLightTertiaryContainer,
    onTertiaryContainer = BlueLightOnTertiaryContainer,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor,
    onErrorContainer = OnErrorContainerColor,
    background = BlueLightBackground,
    onBackground = BlueLightOnBackground,
    surface = BlueLightSurface,
    onSurface = BlueLightOnSurface,
    surfaceVariant = BlueLightSurfaceVariant,
    onSurfaceVariant = BlueLightOnSurfaceVariant,
    outline = BlueLightOutline,
    outlineVariant = BlueLightOutlineVariant,
    surfaceContainerLowest = BlueLightSurfaceContainerLowest,
    surfaceContainerLow = BlueLightSurfaceContainerLow,
    surfaceContainer = BlueLightSurfaceContainer,
    surfaceContainerHigh = BlueLightSurfaceContainerHigh,
    surfaceContainerHighest = BlueLightSurfaceContainerHighest
)

val BlueDarkColorScheme = darkColorScheme(
    primary = BlueDarkPrimary,
    onPrimary = BlueDarkOnPrimary,
    primaryContainer = BlueDarkPrimaryContainer,
    onPrimaryContainer = BlueDarkOnPrimaryContainer,
    secondary = BlueDarkSecondary,
    onSecondary = BlueDarkOnSecondary,
    secondaryContainer = BlueDarkSecondaryContainer,
    onSecondaryContainer = BlueDarkOnSecondaryContainer,
    tertiary = BlueDarkTertiary,
    onTertiary = BlueDarkOnTertiary,
    tertiaryContainer = BlueDarkTertiaryContainer,
    onTertiaryContainer = BlueDarkOnTertiaryContainer,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor,
    onErrorContainer = OnErrorContainerColor,
    background = BlueDarkBackground,
    onBackground = BlueDarkOnBackground,
    surface = BlueDarkSurface,
    onSurface = BlueDarkOnSurface,
    surfaceVariant = BlueDarkSurfaceVariant,
    onSurfaceVariant = BlueDarkOnSurfaceVariant,
    outline = BlueDarkOutline,
    outlineVariant = BlueDarkOutlineVariant,
    surfaceContainerLowest = BlueDarkSurfaceContainerLowest,
    surfaceContainerLow = BlueDarkSurfaceContainerLow,
    surfaceContainer = BlueDarkSurfaceContainer,
    surfaceContainerHigh = BlueDarkSurfaceContainerHigh,
    surfaceContainerHighest = BlueDarkSurfaceContainerHighest
)

val GreenLightColorScheme = lightColorScheme(
    primary = GreenLightPrimary,
    onPrimary = GreenLightOnPrimary,
    primaryContainer = GreenLightPrimaryContainer,
    onPrimaryContainer = GreenLightOnPrimaryContainer,
    secondary = GreenLightSecondary,
    onSecondary = GreenLightOnSecondary,
    secondaryContainer = GreenLightSecondaryContainer,
    onSecondaryContainer = GreenLightOnSecondaryContainer,
    tertiary = GreenLightTertiary,
    onTertiary = GreenLightOnTertiary,
    tertiaryContainer = GreenLightTertiaryContainer,
    onTertiaryContainer = GreenLightOnTertiaryContainer,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor,
    onErrorContainer = OnErrorContainerColor,
    background = GreenLightBackground,
    onBackground = GreenLightOnBackground,
    surface = GreenLightSurface,
    onSurface = GreenLightOnSurface,
    surfaceVariant = GreenLightSurfaceVariant,
    onSurfaceVariant = GreenLightOnSurfaceVariant,
    outline = GreenLightOutline,
    outlineVariant = GreenLightOutlineVariant,
    surfaceContainerLowest = GreenLightSurfaceContainerLowest,
    surfaceContainerLow = GreenLightSurfaceContainerLow,
    surfaceContainer = GreenLightSurfaceContainer,
    surfaceContainerHigh = GreenLightSurfaceContainerHigh,
    surfaceContainerHighest = GreenLightSurfaceContainerHighest
)

val GreenDarkColorScheme = darkColorScheme(
    primary = GreenDarkPrimary,
    onPrimary = GreenDarkOnPrimary,
    primaryContainer = GreenDarkPrimaryContainer,
    onPrimaryContainer = GreenDarkOnPrimaryContainer,
    secondary = GreenDarkSecondary,
    onSecondary = GreenDarkOnSecondary,
    secondaryContainer = GreenDarkSecondaryContainer,
    onSecondaryContainer = GreenDarkOnSecondaryContainer,
    tertiary = GreenDarkTertiary,
    onTertiary = GreenDarkOnTertiary,
    tertiaryContainer = GreenDarkTertiaryContainer,
    onTertiaryContainer = GreenDarkOnTertiaryContainer,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor,
    onErrorContainer = OnErrorContainerColor,
    background = GreenDarkBackground,
    onBackground = GreenDarkOnBackground,
    surface = GreenDarkSurface,
    onSurface = GreenDarkOnSurface,
    surfaceVariant = GreenDarkSurfaceVariant,
    onSurfaceVariant = GreenDarkOnSurfaceVariant,
    outline = GreenDarkOutline,
    outlineVariant = GreenDarkOutlineVariant,
    surfaceContainerLowest = GreenDarkSurfaceContainerLowest,
    surfaceContainerLow = GreenDarkSurfaceContainerLow,
    surfaceContainer = GreenDarkSurfaceContainer,
    surfaceContainerHigh = GreenDarkSurfaceContainerHigh,
    surfaceContainerHighest = GreenDarkSurfaceContainerHighest
)

@Composable
fun DistinctRoutesTheme(
    colourMode: String = "blue",
    themeMode: String = "light",
    darkTheme: Boolean = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    },
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        colourMode == "green" -> if (darkTheme) GreenDarkColorScheme else GreenLightColorScheme
        else -> if (darkTheme) BlueDarkColorScheme else BlueLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}