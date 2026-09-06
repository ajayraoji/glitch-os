package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.FirestoreService
import com.example.util.GoogleSignInHelper
import com.example.util.VideoAgentSettings
import kotlinx.coroutines.launch

val VIDEO_CATEGORIES_30 = listOf(
    "Tech & AI",
    "Gaming & Esports",
    "Fitness & Health",
    "Finance & Investing",
    "Motivation & Mindset",
    "AI & Future Technology",
    "Comedy & Memes",
    "Sci-Fi & Space",
    "Horror & Ghost Stories",
    "Cyberpunk & Futurism",
    "Daily Life Hacks",
    "Science & Astronomy",
    "Anime & Manga",
    "Crypto & Web3",
    "Success & Entrepreneurship",
    "Pop Culture & Movies",
    "Gadgets & Tech Reviews",
    "Health & Nutrition",
    "Travel & Adventure",
    "World History & Facts",
    "True Crime & Mystery",
    "Cars & Superbikes",
    "Fashion & Personal Style",
    "Food & Quick Cooking",
    "Music & EDM Beats",
    "Extreme Sports",
    "Photography & Visuals",
    "Startups & Business Case Studies",
    "Relationships & Psychology",
    "Paranormal & Unknown Legends"
)

val VIDEO_LENGTHS = listOf(15, 30, 45, 60)

val VIDEO_FORMATS = listOf(
    "Vertical (9:16) - YouTube Shorts",
    "Vertical (9:16) - Instagram Reels",
    "Vertical (9:16) - TikTok",
    "Horizontal (16:9) - Standard Video"
)

val VIDEO_LANGUAGES = listOf(
    "Hinglish (Hindi + English)",
    "Hindi",
    "English (US)",
    "English (UK)",
    "Spanish",
    "French",
    "German",
    "Japanese",
    "Korean",
    "Portuguese",
    "Arabic",
    "Russian"
)

val AUTO_LOOP_HOURS = (1..10).toList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoAgentSettingsDialog(
    currentSettings: VideoAgentSettings,
    onDismiss: () -> Unit,
    onSaveSettings: (VideoAgentSettings) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleUser by GoogleSignInHelper.currentUser.collectAsState()

    var selectedLength by remember { mutableIntStateOf(currentSettings.videoLengthSeconds) }
    var selectedFormat by remember { mutableStateOf(currentSettings.videoFormat) }
    var selectedLanguage by remember { mutableStateOf(currentSettings.videoLanguage) }
    var selectedCategory by remember { mutableStateOf(currentSettings.category) }
    var selectedMode by remember { mutableStateOf(currentSettings.mode) } // "Manual" or "Auto"
    var autoIntervalHours by remember { mutableIntStateOf(currentSettings.autoIntervalHours) }
    var isAutoLoopRunning by remember { mutableStateOf(currentSettings.isAutoLoopRunning) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var formatDropdownExpanded by remember { mutableStateOf(false) }
    var languageDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF070B08),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF00FF66),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚙️ VIDEO AGENT SETTINGS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 15.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Configure parameters for autonomous AI short generation & automatic background looping synced with Firestore.",
                    fontFamily = FontFamily.Monospace,
                    color = Color.LightGray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 1. VIDEO LENGTH SELECTOR (15, 30, 45, 60 sec)
                Text(
                    text = "1. VIDEO LENGTH (SECONDS):",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VIDEO_LENGTHS.forEach { sec ->
                        val isSelected = (selectedLength == sec)
                        Button(
                            onClick = { selectedLength = sec },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF00FF66) else Color(0xFF0B1710),
                                contentColor = if (isSelected) Color.Black else Color(0xFF00FF66)
                            ),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${sec}s",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. VIDEO FORMAT SELECTOR
                Text(
                    text = "2. VIDEO FORMAT & PLATFORM:",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = formatDropdownExpanded,
                    onExpandedChange = { formatDropdownExpanded = !formatDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedFormat,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formatDropdownExpanded) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.5.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color(0xFF00FF66).copy(alpha = 0.4f),
                            focusedContainerColor = Color(0xFF0A0F0C),
                            unfocusedContainerColor = Color(0xFF050806)
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = formatDropdownExpanded,
                        onDismissRequest = { formatDropdownExpanded = false },
                        modifier = Modifier.background(Color(0xFF0A0F0C))
                    ) {
                        VIDEO_FORMATS.forEach { fmt ->
                            DropdownMenuItem(
                                text = { Text(fmt, color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                                onClick = {
                                    selectedFormat = fmt
                                    formatDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. LANGUAGE SELECTOR
                Text(
                    text = "3. SCRIPT & VOICE LANGUAGE:",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = languageDropdownExpanded,
                    onExpandedChange = { languageDropdownExpanded = !languageDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedLanguage,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageDropdownExpanded) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.5.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color(0xFF00FF66).copy(alpha = 0.4f),
                            focusedContainerColor = Color(0xFF0A0F0C),
                            unfocusedContainerColor = Color(0xFF050806)
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = languageDropdownExpanded,
                        onDismissRequest = { languageDropdownExpanded = false },
                        modifier = Modifier.background(Color(0xFF0A0F0C))
                    ) {
                        VIDEO_LANGUAGES.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang, color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                                onClick = {
                                    selectedLanguage = lang
                                    languageDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. CATEGORIES DROPDOWN (30 CATEGORIES)
                Text(
                    text = "4. CONTENT CATEGORY (30 OPTIONS):",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.5.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color(0xFF00FF66).copy(alpha = 0.4f),
                            focusedContainerColor = Color(0xFF0A0F0C),
                            unfocusedContainerColor = Color(0xFF050806)
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false },
                        modifier = Modifier.background(Color(0xFF0A0F0C)).heightIn(max = 240.dp)
                    ) {
                        VIDEO_CATEGORIES_30.forEachIndexed { index, cat ->
                            DropdownMenuItem(
                                text = { Text("${index + 1}. $cat", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. GENERATION MODE (MANUAL / AUTO LOOP)
                Text(
                    text = "5. AUTOMATION MODE:",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Manual", "Auto Loop").forEach { m ->
                        val isSelected = (selectedMode == m)
                        OutlinedButton(
                            onClick = { selectedMode = m },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) Color(0xFF00FF66) else Color.Transparent,
                                contentColor = if (isSelected) Color.Black else Color(0xFF00FF66)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00FF66)),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (m == "Manual") "✋ MANUAL" else "🔄 AUTO LOOP",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // AUTO MODE INTERVAL SELECTOR (1 to 10 Hours)
                if (selectedMode == "Auto Loop") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF00FF66), RoundedCornerShape(4.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1710))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "⏰ AUTO LOOP TIME INTERVAL:",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00FF66),
                                fontSize = 11.sp
                            )
                            Text(
                                text = "AI will automatically generate & upload a video every X hours in background until stopped.",
                                fontFamily = FontFamily.Monospace,
                                color = Color.LightGray,
                                fontSize = 10.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                (1..5).forEach { hr ->
                                    val isSelected = (autoIntervalHours == hr)
                                    Button(
                                        onClick = { autoIntervalHours = hr },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) Color(0xFF00FF66) else Color(0xFF050A07),
                                            contentColor = if (isSelected) Color.Black else Color(0xFF00FF66)
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("${hr}h", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                (6..10).forEach { hr ->
                                    val isSelected = (autoIntervalHours == hr)
                                    Button(
                                        onClick = { autoIntervalHours = hr },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) Color(0xFF00FF66) else Color(0xFF050A07),
                                            contentColor = if (isSelected) Color.Black else Color(0xFF00FF66)
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("${hr}h", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Start/Stop Loop Switch
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isAutoLoopRunning) "🟢 LOOP STATUS: ACTIVE (${autoIntervalHours}h)" else "🔴 LOOP STATUS: STOPPED",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isAutoLoopRunning) Color(0xFF00FF66) else Color(0xFFFF3366),
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = isAutoLoopRunning,
                                    onCheckedChange = { isAutoLoopRunning = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.Black,
                                        checkedTrackColor = Color(0xFF00FF66),
                                        uncheckedThumbColor = Color(0xFFFF3366),
                                        uncheckedTrackColor = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = VideoAgentSettings(
                        videoLengthSeconds = selectedLength,
                        videoFormat = selectedFormat,
                        videoLanguage = selectedLanguage,
                        category = selectedCategory,
                        mode = selectedMode,
                        autoIntervalHours = autoIntervalHours,
                        isAutoLoopRunning = isAutoLoopRunning,
                        lastUpdatedTimestamp = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        FirestoreService.saveVideoAgentSettings(context, googleUser?.email, updated)
                        onSaveSettings(updated)
                        Toast.makeText(context, "✅ Video Agent Settings saved to Firestore!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("SAVE SETTINGS", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color.Gray, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            }
        }
    )
}
