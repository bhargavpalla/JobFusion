package com.example.jobfusion.ui.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

object AuthDimens {
    val ScreenPadding = 20.dp
    val SectionSpacing = 16.dp
    val FieldSpacing = 14.dp
    val FieldRadius = 14.dp
    val TabRadius = 16.dp
    val ButtonRadius = 14.dp
}

object AuthColors {
    val Brand = Color(0xFF1E459B)
    val TabBackground = Color(0xFFEAEFF6)
    val Border = Color(0xFFD9E0EB)
    val TextPrimary = Color(0xFF111827)
    val TextSecondary = Color(0xFF4B5563)
    val Success = Color(0xFF1A7F37)
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AuthColors.TextPrimary
        )
        if (subtitle.isNotBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = AuthColors.TextSecondary
            )
        }
    }
}

@Composable
fun StatusSection(errorMessage: String?, successMessage: String?) {
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    } else if (successMessage != null) {
        Text(
            text = successMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = AuthColors.Success
        )
    }
}

@Composable
fun FooterAction(prefix: String, action: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prefix,
            style = MaterialTheme.typography.titleMedium,
            color = AuthColors.TextSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(onClick = onClick) {
            Text(
                text = action,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    errorText: String? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = AuthColors.TextPrimary
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = singleLine,
            minLines = minLines,
            isError = errorText != null,
            shape = RoundedCornerShape(AuthDimens.FieldRadius),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            supportingText = {
                if (errorText != null) {
                    Text(text = errorText, color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium, color = AuthColors.TextPrimary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("........") },
            singleLine = true,
            isError = errorText != null,
            shape = RoundedCornerShape(AuthDimens.FieldRadius),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (visible) "Hide password" else "Show password"
                    )
                }
            },
            supportingText = {
                if (errorText != null) {
                    Text(text = errorText, color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}

@Composable
fun ToggleTab(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                color = AuthColors.TextPrimary
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(AuthColors.TabBackground, RoundedCornerShape(AuthDimens.TabRadius))
                .padding(4.dp)
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(AuthDimens.TabRadius)
                        )
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = if (isSelected) AuthColors.Border else Color.Transparent,
                            shape = RoundedCornerShape(AuthDimens.TabRadius)
                        )
                        .clickable { onSelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) AuthColors.Brand else AuthColors.TextSecondary
                    )
                }
                if (index != options.lastIndex) Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun PrimaryButton(text: String, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(AuthDimens.ButtonRadius)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun <T> DropdownField(
    label: String,
    options: List<T>,
    selectedValue: T,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleSmall, color = AuthColors.TextPrimary)
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AuthColors.Border, RoundedCornerShape(AuthDimens.FieldRadius))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = optionLabel(selectedValue),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AuthColors.TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = AuthColors.TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionLabel(option),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
