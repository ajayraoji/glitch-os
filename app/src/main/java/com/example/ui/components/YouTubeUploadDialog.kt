package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.util.GoogleSignInHelper
import com.example.util.GoogleUser
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YouTubeUploadDialog(
    videoFile: File,
    initialTitle: String = "",
    initialDescription: String = "",
    autoStartUpload: Boolean = true,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleUser by GoogleSignInHelper.currentUser.collectAsState()

    var ytTitle by remember {
        mutableStateOf(
            if (initialTitle.isNotBlank()) initialTitle
            else "Viral 9:16 AI Short #${System.currentTimeMillis() % 1000}"
        )
    }
    var ytDescription by remember {
        mutableStateOf(
            if (initialDescription.isNotBlank()) "$initialDescription\n\n#Shorts #AI #Viral #GlitchOS"
            else "Created with Glitch OS AI Video Studio.\n\n#Shorts #AI #Viral #Trending #GlitchOS"
        )
    }

    var customTagsText by remember { mutableStateOf("Shorts, AI, GlitchOS, Viral, Trending, AIStories") }
    var privacyStatus by remember { mutableStateOf("public") } // "public", "unlisted", "private"
    var selectedCategoryId by remember { mutableStateOf("24") } // 24 = Entertainment
    var madeForKids by remember { mutableStateOf(false) }

    var isUploadingToYt by remember { mutableStateOf(false) }
    var uploadStatusMsg by remember { mutableStateOf("") }
    var uploadedVideoUrl by remember { mutableStateOf<String?>(null) }
    var uploadErrorMessage by remember { mutableStateOf<String?>(null) }
    var autoUploadCancelled by remember { mutableStateOf(false) }

    // Automatic YouTube Shorts Upload effect
    androidx.compose.runtime.LaunchedEffect(autoStartUpload) {
        if (autoStartUpload && uploadedVideoUrl == null && !isUploadingToYt && !autoUploadCancelled) {
            // Check file size minimum (5 MB)
            if (videoFile.length() < 5 * 1024 * 1024L) {
                val sizeMb = videoFile.length() / (1024.0 * 1024.0)
                uploadErrorMessage = "🚫 Cancelled: Video file size is too small (${String.format("%.2f", sizeMb)} MB). Minimum required is 5.00 MB."
                Toast.makeText(context, "🚫 YouTube Upload Cancelled: Video size is less than 5 MB!", Toast.LENGTH_LONG).show()
                autoUploadCancelled = true
                return@LaunchedEffect
            }

            // Check or ensure user is logged in
            val activeUser = googleUser ?: GoogleSignInHelper.getSavedUserFromPreferences(context)

            // Countdown banner (1.5s)
            for (sec in 2 downTo 1) {
                if (autoUploadCancelled) break
                uploadStatusMsg = "🚀 Auto-uploading to YouTube Shorts in $sec sec..."
                kotlinx.coroutines.delay(750)
            }

            if (!autoUploadCancelled && uploadedVideoUrl == null) {
                isUploadingToYt = true
                uploadErrorMessage = null
                uploadStatusMsg = "1/4: Initializing YouTube upload pipeline..."

                val tagsList = customTagsText
                    .split(",")
                    .map { it.trim().replace("#", "") }
                    .filter { it.isNotEmpty() }

                val res = GoogleSignInHelper.uploadVideoToYouTube(
                    context = context,
                    videoFile = videoFile,
                    title = ytTitle,
                    description = ytDescription,
                    tags = tagsList,
                    privacyStatus = privacyStatus,
                    categoryId = selectedCategoryId,
                    madeForKids = madeForKids
                ) { progress ->
                    uploadStatusMsg = progress
                }

                isUploadingToYt = false
                res.onSuccess { url ->
                    uploadedVideoUrl = url
                    Toast.makeText(context, "🎉 AUTO-UPLOAD COMPLETE! Published to YouTube Shorts.", Toast.LENGTH_LONG).show()
                }.onFailure { err ->
                    uploadErrorMessage = err.message
                    Toast.makeText(context, "Upload issue: ${err.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val popularTags = listOf("Shorts", "AI", "Viral", "Trending", "FYP", "Reels", "GlitchOS", "Animation", "Story")

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        coroutineScope.launch {
            val res = GoogleSignInHelper.handleSignInResult(context, result.data)
            res.onSuccess { user ->
                Toast.makeText(context, "✅ Signed in as ${user.displayName ?: user.email}", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    val categories = listOf(
        "24" to "🎬 Entertainment",
        "22" to "👥 People & Blogs",
        "28" to "🔬 Science & Tech",
        "27" to "📚 Education",
        "20" to "🎮 Gaming",
        "1" to "🍿 Film & Animation"
    )

    var showCategoryDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            if (!isUploadingToYt) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .wrapContentHeight()
            .testTag("youtube_upload_dialog"),
        containerColor = Color(0xFF090E0B),
        shape = RoundedCornerShape(12.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = Color(0xFFFF0055),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "🚀 UPLOAD TO YOUTUBE SHORTS",
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FF66),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Full Metadata Integration // YouTube Data API v3",
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FF66).copy(alpha = 0.6f),
                        fontSize = 9.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                // File info & Account Header Badge
                Surface(
                    color = Color(0xFF0D1711),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "VIDEO ASSET: ${videoFile.name}",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF88FFBB),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Size: ${maxOf(1L, videoFile.length() / 1024)} KB • Format: 9:16 Shorts MP4",
                                fontFamily = FontFamily.Monospace,
                                color = Color.Gray,
                                fontSize = 9.sp
                            )
                        }

                        if (googleUser != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFF082613), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF00FF66), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                if (googleUser?.photoUrl != null) {
                                    AsyncImage(
                                        model = googleUser?.photoUrl,
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(16.dp).clip(CircleShape)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF00FF66),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = googleUser?.displayName?.take(10) ?: "CHANNEL",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFF00FF66),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    val client = GoogleSignInHelper.getGoogleSignInClient(context)
                                    googleSignInLauncher.launch(client.signInIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFFF3366)),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Login,
                                    contentDescription = null,
                                    tint = Color(0xFFFF3366),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LOGIN",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFFFF3366),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Section 1: Shorts Title
                Text(
                    text = "1. VIDEO TITLE (#Shorts included)",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                OutlinedTextField(
                    value = ytTitle,
                    onValueChange = { if (it.length <= 100) ytTitle = it },
                    placeholder = { Text("Enter viral title...", color = Color.Gray, fontSize = 11.sp) },
                    trailingIcon = {
                        Text(
                            text = "${ytTitle.length}/100",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.5.sp,
                            color = if (ytTitle.length > 90) Color(0xFFFF3366) else Color.Gray,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00FF66),
                        unfocusedBorderColor = Color(0xFF22442B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF050B07),
                        unfocusedContainerColor = Color(0xFF050B07)
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("yt_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata Section 2: Description & Hashtags
                Text(
                    text = "2. DESCRIPTION & HASHTAGS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                OutlinedTextField(
                    value = ytDescription,
                    onValueChange = { ytDescription = it },
                    placeholder = { Text("Video summary, storyline context, links...", color = Color.Gray, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00FF66),
                        unfocusedBorderColor = Color(0xFF22442B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF050B07),
                        unfocusedContainerColor = Color(0xFF050B07)
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.5.sp
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("yt_description_input"),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata Section 3: Tags & Keywords
                Text(
                    text = "3. TAGS & SEO KEYWORDS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                OutlinedTextField(
                    value = customTagsText,
                    onValueChange = { customTagsText = it },
                    placeholder = { Text("Comma separated tags...", color = Color.Gray, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00FF66),
                        unfocusedBorderColor = Color(0xFF22442B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF050B07),
                        unfocusedContainerColor = Color(0xFF050B07)
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.5.sp
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("yt_tags_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(4.dp))
                // Quick Tag suggestions
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    popularTags.forEach { tag ->
                        val isAdded = customTagsText.contains(tag, ignoreCase = true)
                        Surface(
                            onClick = {
                                val currentList = customTagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
                                if (isAdded) {
                                    currentList.removeAll { it.equals(tag, ignoreCase = true) }
                                } else {
                                    currentList.add(tag)
                                }
                                customTagsText = currentList.joinToString(", ")
                            },
                            color = if (isAdded) Color(0xFF0C381E) else Color(0xFF0A120D),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.8.dp, if (isAdded) Color(0xFF00FF66) else Color(0xFF22442B))
                        ) {
                            Text(
                                text = if (isAdded) "✓ #$tag" else "+ #$tag",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                color = if (isAdded) Color(0xFF00FF66) else Color.Gray,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Section 4: Privacy & Category Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Privacy Selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "4. PRIVACY",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FF66),
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF050B07), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF22442B), RoundedCornerShape(6.dp))
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("public" to "🌐 Public", "unlisted" to "🔗 Unlisted", "private" to "🔒 Private").forEach { (key, label) ->
                                val selected = privacyStatus == key
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (selected) Color(0xFF00FF66) else Color.Transparent)
                                        .clickable { privacyStatus = key }
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 8.sp,
                                        color = if (selected) Color.Black else Color.Gray,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category & Made For Kids Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Dropdown
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "5. CATEGORY",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FF66),
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Box {
                            Surface(
                                onClick = { showCategoryDropdown = true },
                                color = Color(0xFF050B07),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, Color(0xFF22442B)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val currentCatName = categories.firstOrNull { it.first == selectedCategoryId }?.second ?: "🎬 Entertainment"
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentCatName,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 9.sp,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Text("▾", color = Color(0xFF00FF66), fontSize = 10.sp)
                                }
                            }

                            DropdownMenu(
                                expanded = showCategoryDropdown,
                                onDismissRequest = { showCategoryDropdown = false },
                                modifier = Modifier.background(Color(0xFF0A120D))
                            ) {
                                categories.forEach { (catId, catName) ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = catName,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                color = if (selectedCategoryId == catId) Color(0xFF00FF66) else Color.White
                                            )
                                        },
                                        onClick = {
                                            selectedCategoryId = catId
                                            showCategoryDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Made for kids
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "MADE FOR KIDS?",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FF66),
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFF050B07), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF22442B), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (madeForKids) "Yes (Kids)" else "No (Standard)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                color = if (madeForKids) Color(0xFFFFCC00) else Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = madeForKids,
                                onCheckedChange = { madeForKids = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00FF66),
                                    checkedTrackColor = Color(0xFF0C381E)
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Indicator
                if (isUploadingToYt) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF06180C)),
                        border = BorderStroke(1.dp, Color(0xFF00FF66)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    color = Color(0xFF00FF66),
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "UPLOADING TO YOUTUBE...",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66),
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                color = Color(0xFF00FF66),
                                trackColor = Color(0xFF143020),
                                modifier = Modifier.fillMaxWidth().height(4.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uploadStatusMsg.ifBlank { "Streaming MP4 payload to YouTube server..." },
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFCC00),
                                fontSize = 9.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Success Card
                if (uploadedVideoUrl != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("yt_success_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF052410)),
                        border = BorderStroke(1.dp, Color(0xFF00FF66)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF00FF66),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🎉 VIDEO UPLOADED SUCCESSFULLY!",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66),
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "YouTube Link: ${uploadedVideoUrl!!}",
                                fontFamily = FontFamily.Monospace,
                                color = Color.White,
                                fontSize = 9.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Action buttons: Open in YouTube & Copy Link
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = {
                                        try {
                                            val ytIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uploadedVideoUrl)).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(ytIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Cannot open browser: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF0055),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "WATCH ON YOUTUBE",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("YouTube Shorts Link", uploadedVideoUrl)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "📋 Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00FF66)),
                                    border = BorderStroke(1.dp, Color(0xFF00FF66)),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = Color(0xFF00FF66),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "COPY LINK",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (uploadedVideoUrl == null) {
                Button(
                    onClick = {
                        if (videoFile.length() < 5 * 1024 * 1024L) {
                            val sizeMb = videoFile.length() / (1024.0 * 1024.0)
                            uploadErrorMessage = "🚫 Cancelled: Video file size is too small (${String.format("%.2f", sizeMb)} MB). Minimum required is 5.00 MB."
                            Toast.makeText(context, "🚫 YouTube Upload Cancelled: Video size is less than 5 MB!", Toast.LENGTH_LONG).show()
                        } else if (googleUser == null) {
                            val client = GoogleSignInHelper.getGoogleSignInClient(context)
                            googleSignInLauncher.launch(client.signInIntent)
                        } else {
                            coroutineScope.launch {
                                isUploadingToYt = true
                                uploadErrorMessage = null
                                uploadStatusMsg = "1/4: Initializing YouTube upload pipeline..."

                                val tagsList = customTagsText
                                    .split(",")
                                    .map { it.trim().replace("#", "") }
                                    .filter { it.isNotEmpty() }

                                val res = GoogleSignInHelper.uploadVideoToYouTube(
                                    context = context,
                                    videoFile = videoFile,
                                    title = ytTitle,
                                    description = ytDescription,
                                    tags = tagsList,
                                    privacyStatus = privacyStatus,
                                    categoryId = selectedCategoryId,
                                    madeForKids = madeForKids
                                ) { progress ->
                                    uploadStatusMsg = progress
                                }

                                isUploadingToYt = false
                                res.onSuccess { url ->
                                    uploadedVideoUrl = url
                                    Toast.makeText(context, "🎉 Upload Complete with all metadata!", Toast.LENGTH_LONG).show()
                                }.onFailure { err ->
                                    uploadErrorMessage = err.message
                                    Toast.makeText(context, "Upload issue: ${err.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    enabled = !isUploadingToYt,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FF66),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.testTag("publish_youtube_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (googleUser == null) "SIGN IN TO PUBLISH" else "PUBLISH WITH METADATA",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FF66),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "DONE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        },
        dismissButton = {
            if (!isUploadingToYt && uploadedVideoUrl == null) {
                OutlinedButton(
                    onClick = {
                        autoUploadCancelled = true
                        onDismiss()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "CANCEL",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }
            }
        }
    )
}
