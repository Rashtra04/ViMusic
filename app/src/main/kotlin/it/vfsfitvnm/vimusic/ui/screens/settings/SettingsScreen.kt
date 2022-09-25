package it.vfsfitvnm.vimusic.ui.screens.settings

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.route.*
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.ui.components.themed.Scaffold
import it.vfsfitvnm.vimusic.ui.components.themed.Switch
import it.vfsfitvnm.vimusic.ui.components.themed.ValueSelectorDialog
import it.vfsfitvnm.vimusic.ui.screens.globalRoutes
import it.vfsfitvnm.vimusic.ui.screens.settings.*
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import it.vfsfitvnm.vimusic.utils.*

@ExperimentalAnimationApi
@Composable
fun SettingsScreen() {
    val saveableStateHolder = rememberSaveableStateHolder()

    val (tabIndex, onTabChanged) = rememberSaveable {
        mutableStateOf(0)
    }

    RouteHandler(listenToGlobalEmitter = true) {
        globalRoutes()

        host {
            Scaffold(
                topIconButtonId = R.drawable.chevron_back,
                onTopIconButtonClick = pop,
                tabIndex = tabIndex,
                onTabChanged = onTabChanged,
                tabColumnContent = { Item ->
                    Item(0, "Appearance", R.drawable.color_palette)
                    Item(1, "Player", R.drawable.play)
                    Item(2, "Cache", R.drawable.server)
                    Item(3, "Other", R.drawable.shapes)
                    Item(4, "About", R.drawable.information)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(currentTabIndex) {
                    when (currentTabIndex) {
                        0 -> AppearanceSettings()
                        1 -> PlayerSettings()
                        2 -> CacheSettings()
                        3 -> OtherSettings()
                        4 -> About()
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : Enum<T>> EnumValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    crossinline onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = Enum<T>::name
) {
    ValueSelectorSettingsEntry(
        title = title,
        selectedValue = selectedValue,
        values = enumValues<T>().toList(),
        onValueSelected = onValueSelected,
        modifier = modifier,
        isEnabled = isEnabled,
        valueText = valueText
    )
}

@Composable
inline fun <T> ValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    values: List<T>,
    crossinline onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = { it.toString() }
) {
    var isShowingDialog by remember {
        mutableStateOf(false)
    }

    if (isShowingDialog) {
        ValueSelectorDialog(
            onDismiss = {
                isShowingDialog = false
            },
            title = title,
            selectedValue = selectedValue,
            values = values,
            onValueSelected = onValueSelected,
            valueText = valueText
        )
    }

    SettingsEntry(
        title = title,
        text = valueText(selectedValue),
        modifier = modifier,
        isEnabled = isEnabled,
        onClick = {
            isShowingDialog = true
        }
    )
}

@Composable
fun SwitchSettingEntry(
    title: String,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val (colorPalette, typography) = LocalAppearance.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onCheckedChange(!isChecked) },
                enabled = isEnabled
            )
            .alpha(if (isEnabled) 1f else 0.5f)
            .padding(start = 16.dp)
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicText(
                text = title,
                style = typography.xs.semiBold.copy(color = colorPalette.text),
            )

            BasicText(
                text = text,
                style = typography.xs.semiBold.copy(color = colorPalette.textSecondary),
            )
        }

        Switch(isChecked = isChecked)
    }
}

@Composable
fun SettingsEntry(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    val (colorPalette, typography) = LocalAppearance.current

    Column(
        modifier = modifier
            .clickable(
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
                enabled = isEnabled
            )
            .alpha(if (isEnabled) 1f else 0.5f)
            .padding(start = 16.dp)
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        BasicText(
            text = title,
            style = typography.xs.semiBold.copy(color = colorPalette.text),
        )

        BasicText(
            text = text,
            style = typography.xs.semiBold.copy(color = colorPalette.textSecondary),
        )
    }
}

@Composable
fun SettingsDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val (_, typography) = LocalAppearance.current

    BasicText(
        text = text,
        style = typography.xxs.secondary,
        modifier = modifier
            .padding(start = 16.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsEntryGroupText(
    title: String,
    modifier: Modifier = Modifier,
) {
    val (colorPalette, typography) = LocalAppearance.current

    BasicText(
        text = title.uppercase(),
        style = typography.xxs.semiBold.copy(colorPalette.accent),
        modifier = modifier
            .padding(start = 16.dp)
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun SettingsGroupSpacer(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .height(24.dp)
    )
}
