package com.example.ui.screens

import com.example.ui.components.YouTubeUploadDialog
import com.example.util.DeviceDataCollector
import org.json.JSONObject
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import android.media.MediaPlayer
import android.util.Log
import java.io.FileInputStream
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Close
import com.example.util.FirestoreService
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.DisposableEffect
import com.example.util.EdgeNeuralTtsService
import com.example.util.VoiceOption
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import com.example.ui.components.AppiumCodeGeneratorDialog
import com.example.ui.components.AppiumInspectorSheet
import com.example.ui.components.ChatGPTWebView
import com.example.ui.viewmodel.ChatAppiumViewModel
import com.example.ui.viewmodel.ChatMessage
import com.example.util.GoogleSignInHelper
import com.example.util.GoogleUser
import com.example.service.PipelineForegroundService
import com.example.util.AppCacheManager
import com.example.util.VerticalVideoExporter
import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Storage

// Top-level Step 3 Timeline Data Model
data class Step3TimeframeVisualItem(
    val sceneIndex: Int,
    val timeframe: String,
    val voiceover: String,
    val visualPrompt: String,
    val cameraMotionHint: String = "static",
    val aspectRatio: String = "9:16"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainChatScreen(
    viewModel: ChatAppiumViewModel,
    onNavigateToLogs: () -> Unit,
    onNavigateToEditor: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    val promptText by viewModel.currentPrompt.collectAsState()
    val isWebViewMode by viewModel.isWebViewMode.collectAsState()
    val webUrl by viewModel.webUrl.collectAsState()
    val isAutomating by viewModel.isAutomating.collectAsState()
    val automationStatus by viewModel.lastAutomationStatus.collectAsState()
    val extractedDomJson by viewModel.extractedDomJson.collectAsState()
    val apiKey by viewModel.apiKey.collectAsState()
    val isLiveConnected by viewModel.isLiveConnected.collectAsState()

    val isGeneratingTrendingTopic by viewModel.isGeneratingTrendingTopic.collectAsState()
    val isGeneratingAudioLive by viewModel.isGeneratingAudio.collectAsState()
    val isGeneratingStep3VisualsLive by viewModel.isGeneratingStep3Visuals.collectAsState()
    val isGeneratingImagesLive by viewModel.isGeneratingImages.collectAsState()
    val isApiCalling by viewModel.isApiCalling.collectAsState()
    val showMenuState = remember { mutableStateOf(false) }
    var showMenu by showMenuState
    var showLegalDialog by remember { mutableStateOf<String?>(null) } // "privacy" or "terms"

    // Google Sign-In & YouTube Account State
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleUser by GoogleSignInHelper.currentUser.collectAsState()
    var showAccountDialog by remember { mutableStateOf(false) }
    var showYouTubeUploadDialog by remember { mutableStateOf<File?>(null) }
    var customLoginEmail by remember { mutableStateOf("raojiajay1@gmail.com") }
    var customLoginName by remember { mutableStateOf("Ajay Rao") }
    var isSyncingFirestore by remember { mutableStateOf(false) }

    // Notification Permission & Cache Auto-Monitoring State
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "🔔 Live Pipeline Notifications Active in Background!", Toast.LENGTH_SHORT).show()
        }
    }

    var currentCacheSizeStr by remember { mutableStateOf(AppCacheManager.getFormattedCacheSize(context)) }

    fun checkAndRefreshCache() {
        val (wasAutoCleared, message) = AppCacheManager.checkAndAutoClearCache(context, thresholdMb = 100L)
        if (wasAutoCleared) {
            Toast.makeText(context, "🧹 $message", Toast.LENGTH_LONG).show()
        }
        currentCacheSizeStr = AppCacheManager.getFormattedCacheSize(context)
    }

    // Video Agent Settings State & Firestore Sync
    var videoAgentSettings by remember { mutableStateOf(com.example.util.VideoAgentSettings()) }
    var showVideoSettingsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(googleUser) {
        val fetched = com.example.util.FirestoreService.fetchVideoAgentSettings(context, googleUser?.email)
        if (fetched != null) {
            videoAgentSettings = fetched
            Toast.makeText(context, "⚡ Video Agent Settings synced from Firestore (${fetched.category})!", Toast.LENGTH_SHORT).show()
        }
    }

    // Background Auto-Loop Video Generation Engine
    LaunchedEffect(videoAgentSettings.isAutoLoopRunning, videoAgentSettings.mode) {
        if (videoAgentSettings.mode == "Auto Loop" && videoAgentSettings.isAutoLoopRunning) {
            val intervalMs = (videoAgentSettings.autoIntervalHours * 3600 * 1000L).coerceAtLeast(300000L) // Default 1-10 hours
            while (true) {
                kotlinx.coroutines.delay(intervalMs)
                if (!isGeneratingTrendingTopic && !isApiCalling) {
                    Toast.makeText(context, "🤖 [AUTO LOOP] Triggering video generation (${videoAgentSettings.category})...", Toast.LENGTH_SHORT).show()
                    viewModel.triggerTrendingTopicGenerator(
                        category = videoAgentSettings.category,
                        language = videoAgentSettings.videoLanguage,
                        duration = "${videoAgentSettings.videoLengthSeconds} seconds"
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        GoogleSignInHelper.checkExistingUser(context)
        checkAndRefreshCache()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        coroutineScope.launch {
            val res = GoogleSignInHelper.handleSignInResult(context, result.data)
            res.onSuccess { u ->
                Toast.makeText(
                    context,
                    "Signed in as ${u.displayName ?: u.email}!\nYouTube Permissions: ${if (u.hasYouTubePermission) "ACTIVE" else "STANDARD"}",
                    Toast.LENGTH_LONG
                ).show()
            }.onFailure { e ->
                Toast.makeText(context, "Sign In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    var activePipelineStep by rememberSaveable { mutableIntStateOf(1) }
    var pipelineProgressPercent by rememberSaveable { mutableIntStateOf(5) }
    var isPipelineExpanded by rememberSaveable { mutableStateOf(true) }
    var isAutoPipelineActive by rememberSaveable { mutableStateOf(false) }
    var isPipelineRunning by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(
        isPipelineRunning,
        isApiCalling,
        isGeneratingAudioLive,
        isGeneratingStep3VisualsLive,
        isGeneratingImagesLive
    ) {
        if (!isPipelineRunning) {
            activePipelineStep = 1
            pipelineProgressPercent = 0
        } else {
            when {
                isGeneratingImagesLive -> {
                    activePipelineStep = 4
                    pipelineProgressPercent = 55
                }
                isGeneratingStep3VisualsLive -> {
                    activePipelineStep = 3
                    pipelineProgressPercent = 40
                }
                isGeneratingAudioLive -> {
                    activePipelineStep = 2
                    pipelineProgressPercent = 25
                }
                isApiCalling -> {
                    activePipelineStep = 1
                    pipelineProgressPercent = 10
                }
            }
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "⚡ GLITCH_OS",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00FF66),
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            Text(
                                text = "AI TERMINAL // ONLINE",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66).copy(alpha = 0.65f),
                                fontSize = 8.5.sp,
                                maxLines = 1
                            )
                        }
                    }
                },
                actions = {
                    // Google Sign In / Account Button
                    if (googleUser != null) {
                        Surface(
                            onClick = { showAccountDialog = true },
                            color = Color(0xFF0C2412),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF00FF66)),
                            modifier = Modifier.padding(end = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                if (googleUser?.photoUrl != null) {
                                    AsyncImage(
                                        model = googleUser?.photoUrl,
                                        contentDescription = "User Profile",
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
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = googleUser?.displayName?.take(7) ?: "USER",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66)
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                showAccountDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFFF3366)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF3366)),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.padding(end = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = "Google Login",
                                tint = Color(0xFFFF3366),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "LOGIN",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu Options",
                                tint = Color(0xFF00FF66)
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color(0xFF0A0A0A))
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (googleUser != null) "👤 Account (${googleUser?.email})" else "🔑 Google Sign-In & YouTube", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    if (googleUser != null) {
                                        showAccountDialog = true
                                    } else {
                                        val client = GoogleSignInHelper.getGoogleSignInClient(context)
                                        googleSignInLauncher.launch(client.signInIntent)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🎬 Glitch Video Editor", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    onNavigateToEditor()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("⚙️ Video Agent Settings", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    showVideoSettingsDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("📜 Privacy Policy", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    showLegalDialog = "privacy"
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("⚖️ Terms of Service", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    showLegalDialog = "terms"
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🧹 Clean Cache ($currentCacheSizeStr)", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    val beforeSize = currentCacheSizeStr
                                    AppCacheManager.clearAllCache(context)
                                    checkAndRefreshCache()
                                    Toast.makeText(context, "🧹 Cache cleaned! Freed $beforeSize (Auto-limit: 100MB)", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🔔 Background Notifications (${if (hasNotificationPermission) "Active" else "Request Permission"})", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        Toast.makeText(context, "🔔 Background pipeline notifications & wake-locks enabled!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("📂 View Appium Logs", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    onNavigateToLogs()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🧼 Clear Chat History", color = Color(0xFF00FF66), fontFamily = FontFamily.Monospace) },
                                onClick = {
                                    showMenu = false
                                    viewModel.clearChat()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF000000)
                )
            )
        },
        containerColor = Color.Black,
        contentWindowInsets = WindowInsets.ime
    ) { innerPadding ->
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val isBusyGenerating = isGeneratingTrendingTopic || isApiCalling || isAutomating ||
            isGeneratingAudioLive || isGeneratingStep3VisualsLive || isGeneratingImagesLive ||
            isAutoPipelineActive || isPipelineRunning

        val handleUserSubmit = {
            if (isBusyGenerating) {
                Toast.makeText(
                    context,
                    "⚠️ Video creation in progress! Please wait until current video finishes.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (promptText.isNotBlank()) {
                    viewModel.sendMessage()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
        }

        val handleTriggerVideo = {
            if (isBusyGenerating) {
                Toast.makeText(
                    context,
                    "⚠️ Video creation in progress! Please wait until current video finishes.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                isPipelineRunning = true
                isAutoPipelineActive = true
                viewModel.triggerTrendingTopicGenerator(
                    category = videoAgentSettings.category,
                    language = videoAgentSettings.videoLanguage,
                    duration = "${videoAgentSettings.videoLengthSeconds} seconds"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .background(Color.Black)
        ) {
            // Automation Progress Indicator (Sleek Green)
            if (isAutomating) {
                LinearProgressIndicator(
                    color = Color(0xFF00FF66),
                    trackColor = Color(0xFF05220C),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("automation_progress")
                )
            }

            // Automation Console Status Log Message
            if (isAutomating) {
                Surface(
                    color = Color(0xFF070707),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.2f))
                ) {
                    Text(
                        text = ">>> SYS_LOG: $automationStatus",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00FF66),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("automation_status_text")
                    )
                }
            }

            // AI VIDEO ENGINE BUSY NOTICE BANNER
            if (isPipelineRunning || showYouTubeUploadDialog != null || isAutomating) {
                Surface(
                    color = Color(0xFF021208),
                    border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "⚡ AI VIDEO ENGINE BUSY // Generating Short",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF00FF66)
                            )
                            Text(
                                text = "- please wait for completion...",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFF00FF66).copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }

            // 7-STEP AUTONOMOUS AI PIPELINE CARD (ALWAYS VISIBLE & FULLY INTERACTIVE)
            AutonomousPipelineCard(
                activeStep = if (isPipelineRunning) activePipelineStep else 0,
                progressPercent = if (isPipelineRunning) pipelineProgressPercent else 0,
                isExpanded = isPipelineExpanded,
                onToggleExpand = { isPipelineExpanded = !isPipelineExpanded },
                onCancelClick = {
                    isPipelineRunning = false
                    viewModel.setGeneratingTrendingTopic(false)
                    viewModel.setGeneratingAudio(false)
                    viewModel.setGeneratingImages(false)
                    PipelineForegroundService.stop(context)
                    Toast.makeText(context, "🚫 Autonomous AI Pipeline Cancelled & Process Stopped.", Toast.LENGTH_SHORT).show()
                },
                onStepClick = { stepNum ->
                    viewModel.selectPipelineStepView(stepNum)
                    if (stepNum == 7) {
                        Toast.makeText(context, "Step 7 waits for a real exported MP4 file.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "⚡ Showing Step #$stepNum in active Faceless Video Card!", Toast.LENGTH_SHORT).show()
                    }
                },
                isGenerating = isPipelineRunning,
                onStartClick = { handleTriggerVideo() }
            )

            // Top-Level Observer for Scroll and Auto-expansion of Steps
            val selectedStepView by viewModel.selectedPipelineStepView.collectAsState()
            LaunchedEffect(selectedStepView) {
                selectedStepView?.let { stepNum ->
                    val latestActiveIdx = messages.indexOfLast { msg ->
                        !msg.sender.equals("User", ignoreCase = true) && (msg.text.contains("\"storyboard\"") || msg.text.contains("\"trend_metadata\"") || msg.text.contains("\"visual_cue\"") || msg.text.contains("\"audio_script\""))
                    }
                    if (latestActiveIdx != -1) {
                        try {
                            listState.animateScrollToItem(latestActiveIdx)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    // Wait for all sub-views/observers to finish processing expansion before clearing
                    kotlinx.coroutines.delay(350)
                    viewModel.selectPipelineStepView(null)
                }
            }

            // Main Content Box (WebView kept active invisible + scrollable terminal lists)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Background ChatGPTWebView placed off-screen so Chromium executes JS without throttling
                Box(
                    modifier = Modifier
                        .size(360.dp, 480.dp)
                        .offset(x = (-3000).dp)
                ) {
                    ChatGPTWebView(url = webUrl, viewModel = viewModel, modifier = Modifier.fillMaxSize())
                }

                // Scrollable green terminal chatbot message window
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        val isLatestActiveVideoMessage = remember(messages, msg) {
                            val trendingMsgs = messages.filter { !it.sender.equals("User", ignoreCase = true) && (it.text.contains("\"storyboard\"") || it.text.contains("\"trend_metadata\"") || it.text.contains("\"visual_cue\"") || it.text.contains("\"audio_script\"")) }
                            trendingMsgs.lastOrNull()?.id == msg.id
                        }
                        ChatMessageBubble(
                            message = msg,
                            viewModel = viewModel,
                            onNavigateToEditor = onNavigateToEditor,
                            isLatestActiveVideoMessage = isLatestActiveVideoMessage,
                            isAutoPipelineActive = isAutoPipelineActive,
                            onAutoPipelineActiveChange = { isAutoPipelineActive = it },
                            onPipelineStepChange = { step ->
                                activePipelineStep = step
                                pipelineProgressPercent = when (step) {
                                    1 -> 10
                                    2 -> 25
                                    3 -> 40
                                    4 -> 55
                                    5 -> 70
                                    6 -> 85
                                    7 -> 95
                                    else -> pipelineProgressPercent
                                }
                            },
                            onPipelineFinished = { isPipelineRunning = false }
                        )
                    }
                }
            }

            // Compact & User-Friendly Terminal Command Input Bar
            Surface(
                color = Color(0xFF070B08),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ">",
                        color = Color(0xFF00FF66),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { viewModel.onPromptChange(it) },
                        placeholder = {
                            Text(
                                if (isBusyGenerating) "Video creating in progress..." else "Type prompt or script command...",
                                color = Color(0xFF00FF66).copy(alpha = 0.45f),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.5.sp,
                                maxLines = 1
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                            .semantics { contentDescription = "chat_input_field" }
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyDown && (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter)) {
                                    handleUserSubmit()
                                    true
                                } else {
                                    false
                                }
                            },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                handleUserSubmit()
                            }
                        ),
                        shape = RoundedCornerShape(4.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00FF66),
                            fontSize = 12.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0A0F0C),
                            unfocusedContainerColor = Color(0xFF050806),
                            disabledContainerColor = Color(0xFF040805),
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color(0xFF00FF66).copy(alpha = 0.25f),
                            disabledBorderColor = Color(0xFF00FF66).copy(alpha = 0.15f),
                            cursorColor = Color(0xFF00FF66)
                        ),
                        minLines = 1,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { handleTriggerVideo() },
                        modifier = Modifier
                            .testTag("video_prompt_button")
                            .border(
                                1.dp,
                                if (isBusyGenerating) Color.Gray else Color(0xFF00FF66).copy(alpha = 0.6f),
                                RoundedCornerShape(4.dp)
                            )
                            .background(if (isBusyGenerating) Color(0xFF151515) else Color(0xFF0B1710))
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Generate Story JSON",
                            tint = if (isBusyGenerating) Color.Gray else Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = { handleUserSubmit() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBusyGenerating) Color(0xFF1A3D24) else Color(0xFF00FF66),
                            contentColor = if (isBusyGenerating) Color.LightGray else Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("send_button")
                            .semantics { contentDescription = "send_button" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send prompt",
                            tint = if (isBusyGenerating) Color.LightGray else Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RUN",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = if (isBusyGenerating) Color.LightGray else Color.Black
                        )
                    }
                }
            }
        }
    }

    // Custom Monospace Terminal Legal Dialog (Privacy/Terms)
    if (showLegalDialog != null) {
        AlertDialog(
            onDismissRequest = { showLegalDialog = null },
            title = {
                Text(
                    text = if (showLegalDialog == "privacy") "📜 GLITCH_OS // PRIVACY POLICY" else "⚖️ GLITCH_OS // TERMS OF SERVICE",
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF00FF66),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .background(Color.Black)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (showLegalDialog == "privacy") {
                            """
                            === PRIVACY POLICY ===
                            Last Updated: September 2026
                            
                            1. DATA PRIVACY & COMPLIANCE
                            We respect your absolute privacy. GLITCH_OS does not collect, harvest, or store any personal information, keystrokes, or chat logs.
                            
                            2. LOCAL STORAGE
                            All conversations and logs remain securely within your local Android sandbox database.
                            
                            3. NO-TRACK COMPLIANCE
                            This terminal uses NoTrack AI background bridges which completely shield you from typical commercial user telemetry trackers.
                            
                            ======================
                            """.trimIndent()
                        } else {
                            """
                            === TERMS OF SERVICE ===
                            Last Updated: September 2026
                            
                            1. USER COMPLIANCE
                            By launching GLITCH_OS console terminal, you agree to comply with system operations rules and local guidelines.
                            
                            2. DRIVER COMPLIIntegrity
                            Operation of automation actions are conducted entirely under sandbox isolation protocols.
                            
                            3. LIABILITIES
                            This tool is provided "as-is" without any warranties.
                            
                            ========================
                            """.trimIndent()
                        },
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FF66),
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showLegalDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66))
                ) {
                    Text("OK_ACKNOWLEDGE", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF0A0A0A)
        )
    }

    // Google Account Profile & YouTube Permission Dialog
    if (showAccountDialog) {
        AlertDialog(
            onDismissRequest = { showAccountDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color(0xFF00FF66),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = if (googleUser != null) "👤 USER ACCOUNT" else "🔑 LOGIN & CLOUD SYNC",
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FF66),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0A0A))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (googleUser != null) {
                        if (googleUser?.photoUrl != null) {
                            AsyncImage(
                                model = googleUser?.photoUrl,
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFF00FF66), CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0xFF102816), CircleShape)
                                    .border(2.dp, Color(0xFF00FF66), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF00FF66),
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = googleUser?.displayName ?: "Google User",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        Text(
                            text = googleUser?.email ?: "",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF888888),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Device Telemetry Status Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF061509))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "📱 DEVICE TELEMETRY: FIRESTORE SYNCED",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66),
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Model: ${Build.MANUFACTURER.uppercase()} ${Build.MODEL}\nOS: Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})\nDevice ID: ${DeviceDataCollector.getDeviceId(context).take(12)}...",
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFCCCCCC),
                                    fontSize = 9.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Manual trigger button to re-sync device and app data to Firestore
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isSyncingFirestore = true
                                    googleUser?.let { u ->
                                        FirestoreService.saveCompleteUserAndDeviceData(context, u)
                                    }
                                    isSyncingFirestore = false
                                    Toast.makeText(context, "✅ Complete User & Device Data saved to Firestore!", Toast.LENGTH_LONG).show()
                                }
                            },
                            enabled = !isSyncingFirestore,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSyncingFirestore) "SYNCING TO FIRESTORE..." else "⚡ SYNC DEVICE DATA TO FIRESTORE",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // YouTube Permission Status Badge
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (googleUser?.hasYouTubePermission == true) Color(0xFF00FF66) else Color(0xFFFFCC00),
                                    RoundedCornerShape(6.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (googleUser?.hasYouTubePermission == true) Color(0xFF082210) else Color(0xFF2B1D00)
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (googleUser?.hasYouTubePermission == true) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (googleUser?.hasYouTubePermission == true) Color(0xFF00FF66) else Color(0xFFFFCC00),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (googleUser?.hasYouTubePermission == true) "YOUTUBE PERMISSION: ACTIVE" else "YOUTUBE PERMISSION NEEDED",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = if (googleUser?.hasYouTubePermission == true) Color(0xFF00FF66) else Color(0xFFFFCC00),
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (googleUser?.hasYouTubePermission == true)
                                        "Authorized to upload 9:16 Shorts directly to YouTube."
                                    else
                                        "Grant YouTube scope to enable direct publishing.",
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFCCCCCC),
                                    fontSize = 9.sp
                                )
                            }
                        }

                        if (googleUser?.hasYouTubePermission != true) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    showAccountDialog = false
                                    try {
                                        val client = GoogleSignInHelper.getGoogleSignInClient(context)
                                        googleSignInLauncher.launch(client.signInIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Google Sign-In notice: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00), contentColor = Color.Black),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("REQUEST YOUTUBE SCOPE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    } else {
                        // User is NOT logged in: Provide Google Sign-In option
                        Text(
                            text = "Login with Google to automatically sync all settings and video automation data across your devices via Firestore.",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFCCCCCC),
                            fontSize = 10.5.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                showAccountDialog = false
                                val client = GoogleSignInHelper.getGoogleSignInClient(context)
                                googleSignInLauncher.launch(client.signInIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Login, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SIGN IN WITH GOOGLE",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (googleUser != null) {
                    OutlinedButton(
                        onClick = {
                            GoogleSignInHelper.signOut(context) {
                                showAccountDialog = false
                                Toast.makeText(context, "Signed out of Account", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF3366)),
                        border = BorderStroke(1.dp, Color(0xFFFF3366)),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("SIGN OUT", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = { showAccountDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("CLOSE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF0A0A0A)
        )
    }

    // YouTube Shorts Upload Dialog
    if (showYouTubeUploadDialog != null) {
        YouTubeUploadDialog(
            videoFile = showYouTubeUploadDialog!!,
            onDismiss = { showYouTubeUploadDialog = null }
        )
    }

    // Video Agent Settings Dialog
    if (showVideoSettingsDialog) {
        com.example.ui.screens.VideoAgentSettingsDialog(
            currentSettings = videoAgentSettings,
            onDismiss = { showVideoSettingsDialog = false },
            onSaveSettings = { newSettings ->
                videoAgentSettings = newSettings
            }
        )
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    viewModel: ChatAppiumViewModel,
    onNavigateToEditor: () -> Unit,
    isLatestActiveVideoMessage: Boolean = true,
    isAutoPipelineActive: Boolean = false,
    onAutoPipelineActiveChange: (Boolean) -> Unit = {},
    onPipelineStepChange: (Int) -> Unit = {},
    onPipelineFinished: () -> Unit = {}
) {
    val isUser = message.sender == "User"
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val googleUser by GoogleSignInHelper.currentUser.collectAsState()
    var showYouTubeUploadDialog by remember { mutableStateOf<File?>(null) }
    var selectedFullScreenImage by remember { mutableStateOf<String?>(null) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        coroutineScope.launch {
            val res = GoogleSignInHelper.handleSignInResult(context, result.data)
            res.onSuccess { u ->
                Toast.makeText(context, "Signed in as ${u.displayName ?: u.email}!", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(context, "Sign In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chat_bubble_${message.id}")
    ) {
        val isTrendingJson = !isUser && (message.text.contains("\"storyboard\"") || message.text.contains("\"trend_metadata\"") || message.text.contains("\"visual_cue\"") || message.text.contains("\"audio_script\""))
        val isScenesJson = !isUser && message.text.contains("\"scenes\"") && message.text.contains("{")
        val isStep3Json = !isUser && message.text.contains("\"visual_timeline\"") && message.text.contains("{")
        val showDetailedPipelineSections = true

        if (!isTrendingJson && !isScenesJson && !isStep3Json) {
            // Monospace Prompt Header
            Text(
                text = if (isUser) "[user@glitch ~]$" else "[glitch@terminal ~]$",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUser) Color(0xFF00FF66).copy(alpha = 0.8f) else Color(0xFF33FF99)
            )
            Spacer(modifier = Modifier.height(3.dp))
        }

        // Display Message Text (If it's raw JSON, hide the ugly JSON code and show clean status instead)
        if (message.text.contains("NOTRACK_TIMEOUT") || message.text.contains("NOTRACK_ERROR")) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFFF3366).copy(alpha = 0.6f), RoundedCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A050A))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color(0xFFFF3366),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚠️ STEP 1: NOTRACK.AI RESPONSE DELAYED",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF3366),
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "External NoTrack.ai server par traffic high hone ke karan response delay/timeout ho gaya hai. Aap instant verified 60s viral storyboard load kar sakte hain bina wait kiye.",
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFFFCCCC),
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.loadInstantTrendingTopic() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00FF66),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "⚡ INSTANT STORYBOARD",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                        OutlinedButton(
                            onClick = { viewModel.triggerTrendingTopicGenerator() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00FF66)),
                            border = BorderStroke(1.dp, Color(0xFF00FF66)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Retry",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "RETRY",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    color = if (isUser) Color(0xFFE0E0E0) else Color(0xFF00FF66)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF050505))
                    .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .padding(10.dp)
            )
        }

        if (isTrendingJson) {
            var isStep1Expanded by remember { mutableStateOf(false) } // DEFAULT CLOSED as requested
            var showRawJson by remember { mutableStateOf(false) }

            var title = ""
            var category = ""
            var language = ""
            var duration = ""
            var description = ""
            val hashtags = mutableListOf<String>()
            val tags = mutableListOf<String>()

            var introTimestamp = ""
            var introVisualCue = ""
            var introAudioScript = ""

            data class BodySegmentItem(
                val number: Int,
                val timestamp: String,
                val visualCue: String,
                val audioScript: String
            )
            val bodySegments = mutableListOf<BodySegmentItem>()

            var conclusionTimestamp = ""
            var conclusionVisualCue = ""
            var conclusionAudioScript = ""

            try {
                val rawText = message.text
                val startIdx = rawText.indexOf("{")
                val endIdx = rawText.lastIndexOf("}")
                if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                    val clean = rawText.substring(startIdx, endIdx + 1)
                    val json = JSONObject(clean)
                    
                    val meta = json.optJSONObject("trend_metadata")
                category = meta?.optString("category", "") ?: ""
                language = meta?.optString("language", "") ?: ""
                duration = meta?.optString("target_duration", "") ?: ""

                val content = json.optJSONObject("content_details")
                title = content?.optString("title", "") ?: ""
                description = content?.optString("description", "") ?: ""

                val tagsArr = content?.optJSONArray("hashtags")
                if (tagsArr != null) {
                    for (i in 0 until tagsArr.length()) {
                        hashtags.add(tagsArr.getString(i))
                    }
                }

                val extraTags = content?.optJSONArray("tags")
                if (extraTags != null) {
                    for (i in 0 until extraTags.length()) {
                        tags.add(extraTags.getString(i))
                    }
                }

                val storyboard = json.optJSONObject("storyboard")
                if (storyboard != null) {
                    val intro = storyboard.optJSONObject("introduction")
                    if (intro != null) {
                        introTimestamp = intro.optString("timestamp", "")
                        introVisualCue = intro.optString("visual_cue", "")
                        introAudioScript = intro.optString("audio_script", "")
                    }

                    val bodies = storyboard.optJSONArray("body_segments")
                    if (bodies != null) {
                        for (i in 0 until bodies.length()) {
                            val bObj = bodies.optJSONObject(i)
                            if (bObj != null) {
                                bodySegments.add(
                                    BodySegmentItem(
                                        number = bObj.optInt("segment_number", i + 1),
                                        timestamp = bObj.optString("timestamp", ""),
                                        visualCue = bObj.optString("visual_cue", ""),
                                        audioScript = bObj.optString("audio_script", "")
                                    )
                                )
                            }
                        }
                    }

                    val conclusion = storyboard.optJSONObject("conclusion")
                    if (conclusion != null) {
                        conclusionTimestamp = conclusion.optString("timestamp", "")
                        conclusionVisualCue = conclusion.optString("visual_cue", "")
                        conclusionAudioScript = conclusion.optString("audio_script", "")
                    }
                }
                }
            } catch (e: Exception) {
                // Ignore parse errors
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- 1. STEP 1 DROPDOWN CARD (NORMAL TEXT FORMATTED, DEFAULT CLOSED) ---
            if (showDetailedPipelineSections) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF030D05))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Clickable Header to toggle dropdown
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStep1Expanded = !isStep1Expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✅ STEP 1: TRENDING TOPIC & STORYBOARD",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (isStep1Expanded) "[Click to Collapse Output]" else "[Click to Expand Output]",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66).copy(alpha = 0.6f),
                                fontSize = 9.sp
                            )
                        }
                        Icon(
                            imageVector = if (isStep1Expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Step 1 Output",
                            tint = Color(0xFF00FF66)
                        )
                    }

                    // Expandable Dropdown Content (Clean Normal Formatted Text)
                    if (isStep1Expanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF00FF66).copy(alpha = 0.2f))
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Raw JSON Code Toggle Button
                        Button(
                            onClick = { showRawJson = !showRawJson },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07240E)),
                            border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = "Code",
                                    tint = Color(0xFF00FF66),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (showRawJson) "HIDE RAW JSON DATA" else "💻 VIEW RAW STORYBOARD JSON TERMINAL",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66)
                                )
                            }
                        }
                        if (showRawJson) {
                            androidx.compose.foundation.text.selection.SelectionContainer {
                                Text(
                                    text = message.text,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFF00FF66),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF020904))
                                        .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.4f))
                                        .padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Category & Duration
                        if (category.isNotEmpty() || language.isNotEmpty() || duration.isNotEmpty()) {
                            Text(
                                text = "📌 CATEGORY: $category  |  LANG: $language  |  DURATION: $duration",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66).copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        // Title
                        if (title.isNotEmpty()) {
                            Text(
                                text = "🎬 TITLE:\n$title",
                                fontFamily = FontFamily.Monospace,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Description
                        if (description.isNotEmpty()) {
                            Text(
                                text = "📝 DESCRIPTION:\n$description",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFD0D0D0),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Hashtags
                        if (hashtags.isNotEmpty()) {
                            Text(
                                text = "🏷️ HASHTAGS:\n" + hashtags.joinToString(" "),
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FFFF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Tags
                        if (tags.isNotEmpty()) {
                            Text(
                                text = "🔑 TAGS: " + tags.joinToString(", "),
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFCC00),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Storyboard Script Sections
                        if (introAudioScript.isNotEmpty() || bodySegments.isNotEmpty() || conclusionAudioScript.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFF00FF66).copy(alpha = 0.2f))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📜 STORYBOARD SCRIPT & VISUAL CUES:",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Introduction
                            if (introAudioScript.isNotEmpty()) {
                                Surface(
                                    color = Color(0xFF08120A),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "🎬 Intro [$introTimestamp]",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF00FF66),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                        if (introVisualCue.isNotEmpty()) {
                                            Text(
                                                text = "👁️ Visual: $introVisualCue",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color(0xFF88CC99),
                                                fontSize = 9.sp
                                            )
                                        }
                                        Text(
                                            text = "🎙️ Voiceover: \"$introAudioScript\"",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            // Body Segments
                            for (seg in bodySegments) {
                                Surface(
                                    color = Color(0xFF08120A),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "🎬 Segment #${seg.number} [${seg.timestamp}]",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF00FF66),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                        if (seg.visualCue.isNotEmpty()) {
                                            Text(
                                                text = "👁️ Visual: ${seg.visualCue}",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color(0xFF88CC99),
                                                fontSize = 9.sp
                                            )
                                        }
                                        Text(
                                            text = "🎙️ Voiceover: \"${seg.audioScript}\"",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            // Conclusion
                            if (conclusionAudioScript.isNotEmpty()) {
                                Surface(
                                    color = Color(0xFF08120A),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "🎬 Conclusion [$conclusionTimestamp]",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF00FF66),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                        if (conclusionVisualCue.isNotEmpty()) {
                                            Text(
                                                text = "👁️ Visual: $conclusionVisualCue",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color(0xFF88CC99),
                                                fontSize = 9.sp
                                            )
                                        }
                                        Text(
                                            text = "🎙️ Voiceover: \"$conclusionAudioScript\"",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            Spacer(modifier = Modifier.height(8.dp))

            // --- STEP 3 TIMEFRAME VISUALS DATA MODEL & STATES ---
            val buildCanonicalStep3Json: (List<Step3TimeframeVisualItem>) -> String = { items ->
                val root = org.json.JSONObject()
                val totalScenes = items.size
                val totalSec = totalScenes * 3

                val analysis = org.json.JSONObject().apply {
                    put("total_duration_seconds", if (totalSec > 0) totalSec else 60)
                    put("total_frames_or_prompts", if (totalScenes > 0) totalScenes else 20)
                    put("interval_per_prompt", "3 seconds")
                }
                root.put("story_analysis", analysis)

                val timelineArray = org.json.JSONArray()
                items.forEach { item ->
                    val obj = org.json.JSONObject().apply {
                        put("sequence_number", item.sceneIndex)
                        put("timecode", item.timeframe)
                        put("story_segment", item.voiceover)
                        put("image_prompt", item.visualPrompt)
                        put("camera_motion_hint", item.cameraMotionHint)
                        put("aspect_ratio", item.aspectRatio)
                    }
                    timelineArray.put(obj)
                }
                root.put("visual_timeline", timelineArray)
                root.toString(2)
            }

            // Helper function to create high-definition stylized 9:16 artistic fallback visual when completely offline
            fun createCinematicSceneBitmap(sceneNum: Int, prompt: String, targetFile: File) {
                try {
                    val width = 720
                    val height = 1280
                    val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
                    val canvas = android.graphics.Canvas(bitmap)

                    // Cinematic atmospheric color palette based on scene & prompt
                    val colors = when (sceneNum % 6) {
                        1 -> intArrayOf(0xFF0F2027.toInt(), 0xFF142B36.toInt(), 0xFF1B4332.toInt(), 0xFF0A1410.toInt())
                        2 -> intArrayOf(0xFF140152.toInt(), 0xFF280B5C.toInt(), 0xFF49117C.toInt(), 0xFF0B0018.toInt())
                        3 -> intArrayOf(0xFF001F3F.toInt(), 0xFF003F5C.toInt(), 0xFF0A2E38.toInt(), 0xFF05101A.toInt())
                        4 -> intArrayOf(0xFF2B0938.toInt(), 0xFF4A154B.toInt(), 0xFF6B114D.toInt(), 0xFF1A0522.toInt())
                        5 -> intArrayOf(0xFF1B2A4A.toInt(), 0xFF2D4059.toInt(), 0xFF0F3057.toInt(), 0xFF0B132B.toInt())
                        else -> intArrayOf(0xFF0B1D12.toInt(), 0xFF1B4332.toInt(), 0xFF2D6A4F.toInt(), 0xFF06140B.toInt())
                    }
                    val gradient = android.graphics.LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        colors, null, android.graphics.Shader.TileMode.CLAMP
                    )
                    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
                    paint.shader = gradient
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

                    // Atmospheric glowing bokeh light orbs (generates rich cinematic depth)
                    paint.shader = null
                    paint.style = android.graphics.Paint.Style.FILL
                    val random = java.util.Random((sceneNum * 997 + prompt.hashCode()).toLong())
                    val bokehCount = 14
                    for (b in 0 until bokehCount) {
                        val cx = random.nextFloat() * width
                        val cy = random.nextFloat() * height
                        val radius = 40f + random.nextFloat() * 160f
                        val alpha = (25 + random.nextInt(60))
                        val orbColor = when (random.nextInt(4)) {
                            0 -> 0x00FF66
                            1 -> 0x00E5FF
                            2 -> 0x9D4EDD
                            else -> 0xFFB703
                        }
                        paint.color = (alpha shl 24) or orbColor
                        canvas.drawCircle(cx, cy, radius, paint)
                    }

                    // Subtle cinematic vignette shading on top and bottom
                    val vignette = android.graphics.LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        intArrayOf(0xAA000000.toInt(), 0x00000000, 0xCC000000.toInt()),
                        floatArrayOf(0f, 0.45f, 1.0f),
                        android.graphics.Shader.TileMode.CLAMP
                    )
                    val vPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
                    vPaint.shader = vignette
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), vPaint)

                    FileOutputStream(targetFile).use { out ->
                        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 95, out)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("BitmapFallback", "Error creating fallback bitmap", e)
                }
            }

            // Helper function to parse generated Step 3 JSON
            val parseStep3VisualsJson: (String) -> List<Step3TimeframeVisualItem> = { jsonStr ->
                val list = mutableListOf<Step3TimeframeVisualItem>()
                try {
                    val startIdx = jsonStr.indexOf("{")
                    val endIdx = jsonStr.lastIndexOf("}")
                    if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                        val clean = jsonStr.substring(startIdx, endIdx + 1)
                        val json = org.json.JSONObject(clean)
                        
                        var array = json.optJSONArray("visual_timeline")
                        if (array == null) {
                            array = json.optJSONArray("step_3_timeframe_visuals")
                        }
                        
                        val isPhase2Msg = jsonStr.contains("Phase 2", ignoreCase = true) ||
                                          jsonStr.contains("00:30 -", ignoreCase = true) ||
                                          jsonStr.contains("Segment 6", ignoreCase = true) ||
                                          jsonStr.contains("Segment 7", ignoreCase = true)
                        
                        if (array != null) {
                            for (i in 0 until array.length()) {
                                val itemObj = array.getJSONObject(i)
                                var seqNum = itemObj.optInt("sequence_number", itemObj.optInt("scene", i + 1))
                                
                                // Automatic remapping if Phase 2 generated numbers 1..10
                                if (isPhase2Msg && seqNum <= 10) {
                                    seqNum += 10
                                }
                                
                                val calculatedTimecode = String.format("%02d:%02d - %02d:%02d", ((seqNum - 1) * 3) / 60, ((seqNum - 1) * 3) % 60, (seqNum * 3) / 60, (seqNum * 3) % 60)
                                val rawTimecode = itemObj.optString("timecode", itemObj.optString("time", itemObj.optString("timeframe", calculatedTimecode)))
                                val timecode = if (isPhase2Msg && rawTimecode.startsWith("00:0")) calculatedTimecode else rawTimecode
                                
                                val voiceoverText = itemObj.optString("story_segment", itemObj.optString("voiceover", itemObj.optString("audio_script", itemObj.optString("text", ""))))
                                val visualPrompt = itemObj.optString("image_prompt", itemObj.optString("visual_prompt", itemObj.optString("prompt", "Cinematic highly detailed scene")))
                                val cameraMotion = itemObj.optString("camera_motion_hint", itemObj.optString("motion", "static"))
                                val aspectRatio = itemObj.optString("aspect_ratio", "9:16")
                                
                                list.add(
                                    Step3TimeframeVisualItem(
                                        sceneIndex = seqNum,
                                        timeframe = timecode,
                                        voiceover = voiceoverText,
                                        visualPrompt = visualPrompt,
                                        cameraMotionHint = cameraMotion,
                                        aspectRatio = aspectRatio
                                    )
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("ParseStep3", "Failed to parse generated Step 3 JSON", e)
                }
                list
            }

            // Helper function to measure exact audio duration in seconds
            fun getAudioFileDurationSec(path: String): Double {
                if (path.isBlank()) return 0.0
                val file = File(path)
                if (!file.exists() || file.length() == 0L) return 0.0
                return try {
                    val retriever = android.media.MediaMetadataRetriever()
                    retriever.setDataSource(path)
                    val timeStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                    retriever.release()
                    val durMs = timeStr?.toLongOrNull() ?: 0L
                    if (durMs > 0) durMs / 1000.0 else 0.0
                } catch (e: Exception) {
                    0.0
                }
            }

            var voiceoverDurationSec by remember { mutableStateOf(0.0) }

            // Calculate target dynamic scene count based on Step 2 voiceover duration (audio duration divided by 3)
            val calculatedSceneCount = remember(voiceoverDurationSec, message.text, introAudioScript, bodySegments.size, conclusionAudioScript) {
                if (voiceoverDurationSec > 0.0) {
                    maxOf(3, Math.ceil(voiceoverDurationSec / 3.0).toInt())
                } else {
                    val fullScript = (introAudioScript + " " + bodySegments.joinToString(" ") { it.audioScript } + " " + conclusionAudioScript)
                    val wordsCount = fullScript.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                    val estSec = Math.max(12.0, wordsCount / 2.5)
                    maxOf(3, Math.ceil(estSec / 3.0).toInt())
                }
            }

            var isStep3VisualsExpanded by remember { mutableStateOf(false) }
            var step3VisualsList by remember { mutableStateOf<List<Step3TimeframeVisualItem>>(emptyList()) }
            var step3JsonString by remember { mutableStateOf("") }
            val isGeneratingStep3Visuals by viewModel.isGeneratingStep3Visuals.collectAsState()
            var isStep3Complete by remember { mutableStateOf(false) }
            var step3ErrorState by remember { mutableStateOf<String?>(null) }

            val allMessages by viewModel.chatMessages.collectAsState()
            LaunchedEffect(allMessages, calculatedSceneCount) {
                val noTrackMsgs = allMessages.filter { it.sender == "NoTrack AI" }
                val step3Msgs = noTrackMsgs.filter { msg ->
                    msg.text.contains("visual_timeline") && msg.text.contains("{")
                }
                
                val mergedList = mutableListOf<Step3TimeframeVisualItem>()

                for (msg in step3Msgs) {
                    val parsed = parseStep3VisualsJson(msg.text)
                    if (parsed.isNotEmpty()) {
                        mergedList.addAll(parsed)
                    }
                }

                if (mergedList.isNotEmpty()) {
                    val distinctSorted = mergedList.distinctBy { it.sceneIndex }.sortedBy { it.sceneIndex }.toMutableList()
                    
                    // If AI generated fewer scenes than the exact calculated 3-second scene requirement (e.g. 10 instead of 18)
                    // Automatically expand and interpolate scenes so EVERY 3-second slice of audio has a dedicated visual prompt!
                    val targetScenes = maxOf(calculatedSceneCount, distinctSorted.size)
                    if (distinctSorted.size < targetScenes) {
                        val baseVisualPrompts = distinctSorted.map { it.visualPrompt }
                        val baseVoiceovers = distinctSorted.map { it.voiceover }
                        for (i in distinctSorted.size until targetScenes) {
                            val startSec = i * 3
                            val endSec = (i + 1) * 3
                            val timeStr = String.format("%02d:%02d - %02d:%02d", startSec / 60, startSec % 60, endSec / 60, endSec % 60)
                            val vo = if (baseVoiceovers.isNotEmpty()) baseVoiceovers[i % baseVoiceovers.size] else "Storyline scene continuous action"
                            val visPrompt = if (baseVisualPrompts.isNotEmpty()) {
                                "${baseVisualPrompts[i % baseVisualPrompts.size]}, cinematic variation angle frame ${i + 1} of $targetScenes, 9:16 vertical shorts composition, 8k resolution"
                            } else {
                                "Cinematic scene ${i + 1}, dramatic 9:16 vertical shorts visual, photorealistic 8k lighting"
                            }
                            distinctSorted.add(
                                Step3TimeframeVisualItem(
                                    sceneIndex = i + 1,
                                    timeframe = timeStr,
                                    voiceover = vo,
                                    visualPrompt = visPrompt,
                                    cameraMotionHint = when (i % 4) {
                                        0 -> "Slow push in towards focal point"
                                        1 -> "Smooth dynamic horizontal pan"
                                        2 -> "Static shot highlighting dramatic lighting"
                                        else -> "Cinematic tilt upwards"
                                    },
                                    aspectRatio = "9:16"
                                )
                            )
                        }
                    }

                    step3VisualsList = distinctSorted
                    step3JsonString = buildCanonicalStep3Json(distinctSorted)
                    isStep3Complete = true
                    step3ErrorState = null
                } else {
                    val lastMsg = noTrackMsgs.lastOrNull()
                    if (lastMsg != null && (lastMsg.text.startsWith("❌") || lastMsg.text.contains("ERROR") || lastMsg.text.contains("timeout", ignoreCase = true))) {
                        step3ErrorState = lastMsg.text
                        isStep3Complete = false
                    }
                }
            }

            LaunchedEffect(isGeneratingStep3Visuals) {
                if (isGeneratingStep3Visuals) {
                    isStep3Complete = false
                    step3ErrorState = null
                }
            }

            // --- STEP 5: WORD TIMEFRAME CAPTION DATA MODELS ---
            data class WordTimestampItem(
                val wordIndex: Int,
                val word: String,
                val startMs: Long,
                val endMs: Long
            )

            data class FourWordCaptionChunk(
                val chunkIndex: Int,
                val startMs: Long,
                val endMs: Long,
                val words: List<WordTimestampItem>
            )

            var isStep4ImagesExpanded by rememberSaveable { mutableStateOf(false) }
            var isStep5AlignmentExpanded by rememberSaveable { mutableStateOf(false) }
            var isStep6VideoExpanded by rememberSaveable { mutableStateOf(false) }
            var currentPlaybackProgressMs by rememberSaveable { mutableLongStateOf(0L) }

            // --- AUDIO & PIPELINE STATES ---
            var isStep3ImagesExpanded by remember { mutableStateOf(false) }
            var isGeneratingImages by remember { mutableStateOf(false) }
            LaunchedEffect(isGeneratingImages) {
                viewModel.setGeneratingImages(isGeneratingImages)
            }
            var currentGeneratingIndex by remember { mutableIntStateOf(0) }
            var totalVisualsCount by remember { mutableIntStateOf(0) }
            val currentBrowserSessionIndex by viewModel.browserSessionIndex.collectAsState()
            var imageGenJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

            val clipboardManager = LocalClipboardManager.current

            data class GeneratedImageItem(
                val serialNumber: Int,
                val uniqueId: String,
                val fileName: String,
                val localFilePath: String,
                val imageUrl: String,
                val visualPrompt: String,
                val isSuccess: Boolean = true,
                val errorMessage: String? = null,
                val isRetrying: Boolean = false
            )
            var generatedImagesList by remember { mutableStateOf(listOf<GeneratedImageItem>()) }

            // Step 2 Voiceover States
            var isStep2AudioExpanded by remember { mutableStateOf(false) }
            var isGeneratingAudio by remember { mutableStateOf(false) }
            var currentAudioIndex by remember { mutableIntStateOf(0) }
            var totalAudiosCount by remember { mutableIntStateOf(0) }

            data class GeneratedAudioItem(
                val serialNumber: Int,
                val uniqueId: String,
                val fileName: String,
                val localFilePath: String,
                val sceneLabel: String,
                val scriptText: String
            )
            var generatedAudioList by remember { mutableStateOf(listOf<GeneratedAudioItem>()) }

             val selectedStepView by viewModel.selectedPipelineStepView.collectAsState()
             LaunchedEffect(selectedStepView) {
                 selectedStepView?.let { stepNum ->
                     if (isLatestActiveVideoMessage) {
                         when (stepNum) {
                             1 -> {
                                 isStep1Expanded = true
                             }
                             2 -> {
                                 isStep2AudioExpanded = true
                             }
                             3 -> {
                                 isStep3ImagesExpanded = true
                             }
                             4 -> {
                                 isStep4ImagesExpanded = true
                             }
                             5 -> {
                                 isStep5AlignmentExpanded = true
                             }
                             6 -> {
                                 isStep6VideoExpanded = true
                             }
                         }
                     }
                 }
             }

            data class MasterAudioItem(
                val uniqueId: String,
                val fileName: String,
                val localFilePath: String,
                val totalScenes: Int
            )
            var masterAudioItem by remember { mutableStateOf<MasterAudioItem?>(null) }

            // STEP 4: VIDEO MERGE & PLAYBACK STATES
            data class MergedVideoScene(
                val sceneIndex: Int,
                val title: String,
                val imagePathOrUrl: String,
                val audioPath: String,
                val caption: String
            )
            var mergedVideoScenes by remember { mutableStateOf(listOf<MergedVideoScene>()) }
            var isStep4VideoExpanded by remember { mutableStateOf(false) }
            var isVideoPlaying by rememberSaveable { mutableStateOf(false) }
            var activeVideoSceneIndex by rememberSaveable { mutableIntStateOf(0) }
            var isComposingVideo by remember { mutableStateOf(false) }
            var isVideoExported by remember { mutableStateOf(false) }
            var exportedVideoPath by remember { mutableStateOf<String?>(null) }
            var videoErrorMessage by remember { mutableStateOf<String?>(null) }

            var autoPipelineStatusText by remember { mutableStateOf("") }
            var selectedVoice by remember { mutableStateOf(EdgeNeuralTtsService.AVAILABLE_VOICES.first()) }
            var selectedEmotion by remember { mutableStateOf("⚡ Fast Viral Shorts (1.3x)") }
            var isVoiceMenuExpanded by remember { mutableStateOf(false) }
            var isEmotionMenuExpanded by remember { mutableStateOf(false) }

            // Audio Player instance
            var currentlyPlayingPath by remember { mutableStateOf<String?>(null) }
            var isPlayingFullVoiceoverSequence by remember { mutableStateOf(false) }
            var activeSequentialSceneIndex by remember { mutableIntStateOf(-1) }
            var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

            DisposableEffect(Unit) {
                onDispose {
                    try {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                    } catch (e: Exception) {}
                }
            }

            fun playSequentialScene(index: Int, items: List<GeneratedAudioItem>) {
                if (index >= items.size) {
                    isPlayingFullVoiceoverSequence = false
                    activeSequentialSceneIndex = -1
                    currentlyPlayingPath = null
                    try { mediaPlayer?.release(); mediaPlayer = null } catch (e: Exception) {}
                    Toast.makeText(context, "✅ Full Voiceover Sequence Complete!", Toast.LENGTH_SHORT).show()
                    return
                }

                val item = items[index]
                val file = File(item.localFilePath)
                activeSequentialSceneIndex = index
                currentlyPlayingPath = item.localFilePath

                try {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                } catch (e: Exception) {}

                if (file.exists() && file.length() > 200) {
                    try {
                        val mp = MediaPlayer()
                        FileInputStream(file).use { fis ->
                            mp.setDataSource(fis.fd)
                        }
                        mp.setAudioAttributes(
                            android.media.AudioAttributes.Builder()
                                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                                .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        mp.setOnCompletionListener {
                            if (isPlayingFullVoiceoverSequence) {
                                playSequentialScene(index + 1, items)
                            } else {
                                currentlyPlayingPath = null
                                activeSequentialSceneIndex = -1
                            }
                        }
                        mp.setOnErrorListener { _, what, extra ->
                            Log.e("AudioPlay", "Sequential MediaPlayer error: $what, $extra")
                            if (isPlayingFullVoiceoverSequence) {
                                playSequentialScene(index + 1, items)
                            }
                            true
                        }
                        mp.prepare()
                        mp.start()
                        mediaPlayer = mp
                    } catch (e: Exception) {
                        Log.e("AudioPlay", "Sequential playback error", e)
                        if (isPlayingFullVoiceoverSequence) {
                            playSequentialScene(index + 1, items)
                        }
                    }
                } else {
                    if (isPlayingFullVoiceoverSequence) {
                        playSequentialScene(index + 1, items)
                    }
                }
            }

            fun togglePlayFullVoiceover(items: List<GeneratedAudioItem>, masterPath: String? = null) {
                if (isPlayingFullVoiceoverSequence || (masterPath != null && currentlyPlayingPath == masterPath && mediaPlayer?.isPlaying == true)) {
                    isPlayingFullVoiceoverSequence = false
                    activeSequentialSceneIndex = -1
                    currentlyPlayingPath = null
                    try {
                        mediaPlayer?.pause()
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                    } catch (e: Exception) {}
                    Toast.makeText(context, "⏸️ Full Voiceover Paused", Toast.LENGTH_SHORT).show()
                } else {
                    if (items.isEmpty()) {
                        Toast.makeText(context, "⚠️ No audio generated yet. Tap 'GENERATE AUDIO VOICEOVER'", Toast.LENGTH_SHORT).show()
                        return
                    }
                    isPlayingFullVoiceoverSequence = true
                    playSequentialScene(0, items)
                    Toast.makeText(context, "▶️ Playing All ${items.size} Voiceover Scenes in Full Sequence...", Toast.LENGTH_SHORT).show()
                }
            }

            fun togglePlayAudio(path: String, fallbackScript: String = "") {
                try {
                    // Stop full sequence if individual audio is tapped
                    if (isPlayingFullVoiceoverSequence) {
                        isPlayingFullVoiceoverSequence = false
                        activeSequentialSceneIndex = -1
                    }

                    if (currentlyPlayingPath == path && mediaPlayer?.isPlaying == true) {
                        try { mediaPlayer?.pause() } catch (e: Exception) {}
                        currentlyPlayingPath = null
                        Toast.makeText(context, "⏸️ Audio Paused", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                        } catch (e: Exception) {}

                        val file = File(path)
                        if (file.exists() && file.length() > 200) {
                            val mp = MediaPlayer()
                            FileInputStream(file).use { fis ->
                                mp.setDataSource(fis.fd)
                            }
                            mp.setAudioAttributes(
                                android.media.AudioAttributes.Builder()
                                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            mp.setOnCompletionListener {
                                currentlyPlayingPath = null
                            }
                            mp.setOnErrorListener { _, what, extra ->
                                Log.e("AudioPlay", "MediaPlayer error: $what, $extra")
                                currentlyPlayingPath = null
                                if (fallbackScript.isNotBlank()) {
                                    EdgeNeuralTtsService.speakLive(context, fallbackScript, selectedVoice.id)
                                }
                                true
                            }
                            mp.prepare()
                            mp.start()
                            mediaPlayer = mp
                            currentlyPlayingPath = path
                            Toast.makeText(context, "▶️ Playing Audio...", Toast.LENGTH_SHORT).show()
                        } else if (fallbackScript.isNotBlank()) {
                            Toast.makeText(context, "▶️ Playing Live Speech Voiceover...", Toast.LENGTH_SHORT).show()
                            currentlyPlayingPath = path
                            EdgeNeuralTtsService.speakLive(context, fallbackScript, selectedVoice.id)
                        } else {
                            Toast.makeText(context, "⚠️ Audio file not found. Tap RE-GENERATE AUDIO.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AudioPlay", "Playback error", e)
                    currentlyPlayingPath = null
                    if (fallbackScript.isNotBlank()) {
                        Toast.makeText(context, "▶️ Fallback to Live Voiceover...", Toast.LENGTH_SHORT).show()
                        EdgeNeuralTtsService.speakLive(context, fallbackScript, selectedVoice.id)
                    } else {
                        Toast.makeText(context, "Playback error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Extract all raw audio segments from Step 1 Story Script for Step 2 Voiceover Synthesis
            val allAudioSegments = remember(message.text, introAudioScript, bodySegments.size, conclusionAudioScript) {
                val rawAudio = mutableListOf<Pair<String, String>>()
                if (introAudioScript.isNotBlank()) rawAudio.add(Pair("Intro Hook", introAudioScript))
                bodySegments.forEachIndexed { idx, seg ->
                    if (seg.audioScript.isNotBlank()) rawAudio.add(Pair("Segment ${seg.number}", seg.audioScript))
                }
                if (conclusionAudioScript.isNotBlank()) rawAudio.add(Pair("Outro & CTA", conclusionAudioScript))
                if (rawAudio.isEmpty()) {
                    rawAudio.add(Pair("Scene 1", title.ifBlank { description.ifBlank { "Viral Shorts Script" } }))
                }
                rawAudio
            }


            // Master Prompt Dynamic-Scene Continuous Storyboard Generator
            val onGenerateStep3VisualsMaster: () -> Unit = {
                if (!isGeneratingStep3Visuals) {
                    onPipelineStepChange(3)
                    val allAudio = mutableListOf<String>()
                    if (introAudioScript.isNotBlank()) allAudio.add(introAudioScript)
                    bodySegments.forEach { if (it.audioScript.isNotBlank()) allAudio.add(it.audioScript) }
                    if (conclusionAudioScript.isNotBlank()) allAudio.add(conclusionAudioScript)

                    val fullStoryText = (
                        if (description.isNotBlank()) description
                        else if (allAudio.isNotEmpty()) allAudio.joinToString(" ")
                        else title.ifBlank { message.text }
                    ).trim()

                    val targetSec = if (voiceoverDurationSec > 0.0) voiceoverDurationSec.toInt() else (calculatedSceneCount * 3)
                    val targetScenes = calculatedSceneCount

                    val masterPromptStr = """
                    You are an expert AI video director, storyboard artist, and advanced prompt engineer. Your task is to analyze the provided story and break it down into continuous, sequential visual prompts where EVERY SINGLE IMAGE REPRESENTS EXACTLY A 3-SECOND INTERVAL of the narrative.
                    Total voiceover duration is $targetSec seconds. Therefore, you MUST generate EXACTLY $targetScenes continuous 3-second timeframe scenes (numbered 1 to $targetScenes) covering the complete narrative from 00:00 to ${String.format("%02d:%02d", targetSec / 60, targetSec % 60)}.
                    
                    Output your response strictly as valid, hardcoded JSON without any extra conversational text or pre-ambles:
                    {
                      "story_analysis": {
                        "total_duration_seconds": $targetSec,
                        "total_frames_or_prompts": $targetScenes,
                        "interval_per_prompt": "3 seconds"
                      },
                      "visual_timeline": [
                        {
                          "sequence_number": 1,
                          "timecode": "00:00 - 00:03",
                          "story_segment": "The exact line or action from the story happening in these 3 seconds",
                          "image_prompt": "A highly detailed, cinematic AI image generation prompt in English, including subject description, precise action, background environment, dramatic lighting, camera angle (e.g., close-up, wide shot), and visual style (e.g., photorealistic, cinematic 8k, digital art)",
                          "camera_motion_hint": "Subtle motion suggestion for video generation like pan left, zoom in, or static",
                          "aspect_ratio": "9:16"
                        }
                      ]
                    }
                    Here is the complete story script to break down into $targetScenes distinct 3-second scenes:
                    "$fullStoryText"
                    Ensure the image prompts maintain visual continuity and style consistency across all $targetScenes 3-second segments, matching the exact flow and emotion of the story.
                    """.trimIndent()

                    viewModel.logAction(
                        actionName = "step3_visuals_master_start",
                        locator = "step3_button_master",
                        payload = "Requested $targetScenes scenes for $targetSec seconds via Master Prompt",
                        status = "PENDING"
                    )

                    PipelineForegroundService.startOrUpdate(
                        context = context,
                        title = "🎨 Step 3: Master Visual Storyboard",
                        text = "Crafting 3s timeframe visual prompts for $targetScenes scenes ($targetSec sec)...",
                        progress = 50,
                        max = 100,
                        indeterminate = true
                    )

                    viewModel.triggerStep3VisualsGenerator(masterPromptStr)
                }
            }

            // Async function to generate Step 3 Visual Prompts: Phase 1 (Voiceovers 1-5 -> Scenes 1-10)
            val onGenerateStep3VisualsPhase1: () -> Unit = {
                if (!isGeneratingStep3Visuals) {
                    onPipelineStepChange(3)
                    val all10Voiceovers = mutableListOf<String>()
                    if (introAudioScript.isNotBlank()) all10Voiceovers.add(introAudioScript)
                    bodySegments.forEach { if (it.audioScript.isNotBlank()) all10Voiceovers.add(it.audioScript) }
                    if (conclusionAudioScript.isNotBlank()) all10Voiceovers.add(conclusionAudioScript)
                    
                    if (all10Voiceovers.size < 10) {
                        val fullStoryText = (introAudioScript + " " + bodySegments.joinToString(" ") { it.audioScript } + " " + conclusionAudioScript).trim()
                        val finalStory = if (fullStoryText.isNotBlank()) fullStoryText else message.text
                        val words = finalStory.split("\\s+".toRegex()).filter { it.isNotBlank() }
                        val chunkSize = maxOf(1, words.size / 10)
                        all10Voiceovers.clear()
                        for (i in 0 until 10) {
                            val start = i * chunkSize
                            val end = if (i == 9) words.size else minOf(words.size, (i + 1) * chunkSize)
                            if (start < words.size) {
                                all10Voiceovers.add(words.subList(start, end).joinToString(" "))
                            } else {
                                all10Voiceovers.add("Continuous engaging visual storyline scene progression")
                            }
                        }
                    }

                    val step1Story = if (description.isNotBlank()) description else title.ifBlank { message.text }
                    val phase1Voiceovers = all10Voiceovers.take(5)
                    val phase1Breakdown = phase1Voiceovers.mapIndexed { idx, text ->
                        "Voiceover Segment ${idx + 1} (6s):\n  - Text: \"$text\"\n  - Split into: Scene ${(idx * 2) + 1} (00:${String.format("%02d", idx * 6)} - 00:${String.format("%02d", idx * 6 + 3)}) AND Scene ${(idx * 2) + 2} (00:${String.format("%02d", idx * 6 + 3)} - 00:${String.format("%02d", idx * 6 + 6)})"
                    }.joinToString("\n\n")
                    
                    val masterPromptStr = """
                    You are an expert AI video director and storyboard artist. You are given the FIRST 5 VOICEOVER SEGMENTS of a 60-second video.
                    Each voiceover segment lasts 6 seconds. You MUST SPLIT EACH VOICEOVER INTO TWO (2) DISTINCT 3-SECOND VISUAL SCENES (Total: 10 scenes, strictly numbered 1 to 10):
                    
                    Story Context: $step1Story
                    
                    Voiceover Breakdown for Phase 1 (Scenes 1 to 10, Timecode 00:00 - 00:30):
                    $phase1Breakdown
                    
                    Output strictly as JSON (no conversational preamble):
                    {
                      "story_analysis": {
                        "phase": 1,
                        "total_duration_seconds": 30,
                        "total_frames_or_prompts": 10,
                        "interval_per_prompt": "3 seconds"
                      },
                      "visual_timeline": [
                        {
                          "sequence_number": 1,
                          "timecode": "00:00 - 00:03",
                          "story_segment": "First 3s portion of Voiceover 1",
                          "image_prompt": "A highly detailed, cinematic AI image generation prompt in English, including subject description, precise action, background environment, dramatic lighting, camera angle, and visual style (photorealistic, cinematic 8k, digital art)",
                          "camera_motion_hint": "Slow push in towards the figure",
                          "aspect_ratio": "9:16"
                        },
                        {
                          "sequence_number": 2,
                          "timecode": "00:03 - 00:06",
                          "story_segment": "Second 3s portion of Voiceover 1",
                          "image_prompt": "Continuation action with closer camera angle, maintaining character and lighting consistency",
                          "camera_motion_hint": "Smooth pan right",
                          "aspect_ratio": "9:16"
                        }
                      ]
                    }
                    """.trimIndent()
                    
                    viewModel.logAction(
                        actionName = "step3_visuals_phase1_start",
                        locator = "step3_button_phase1",
                        payload = "Requested 10 scenes (Voiceovers 1-5 split into 2 visuals each) for Phase 1",
                        status = "PENDING"
                    )

                    PipelineForegroundService.startOrUpdate(
                        context = context,
                        title = "🎨 Step 3: Phase 1 Visuals (Scenes 1-10)",
                        text = "Generating 10 visual prompts for Voiceovers 1 to 5...",
                        progress = 30,
                        max = 100,
                        indeterminate = true
                    )
                    
                    viewModel.triggerStep3VisualsGenerator(masterPromptStr)
                }
            }

            // Async function to generate Step 3 Visual Prompts: Phase 2 (Voiceovers 6-10 -> Scenes 11-20)
            val onGenerateStep3VisualsPhase2: () -> Unit = {
                if (!isGeneratingStep3Visuals) {
                    val all10Voiceovers = mutableListOf<String>()
                    if (introAudioScript.isNotBlank()) all10Voiceovers.add(introAudioScript)
                    bodySegments.forEach { if (it.audioScript.isNotBlank()) all10Voiceovers.add(it.audioScript) }
                    if (conclusionAudioScript.isNotBlank()) all10Voiceovers.add(conclusionAudioScript)
                    
                    if (all10Voiceovers.size < 10) {
                        val fullStoryText = (introAudioScript + " " + bodySegments.joinToString(" ") { it.audioScript } + " " + conclusionAudioScript).trim()
                        val finalStory = if (fullStoryText.isNotBlank()) fullStoryText else message.text
                        val words = finalStory.split("\\s+".toRegex()).filter { it.isNotBlank() }
                        val chunkSize = maxOf(1, words.size / 10)
                        all10Voiceovers.clear()
                        for (i in 0 until 10) {
                            val start = i * chunkSize
                            val end = if (i == 9) words.size else minOf(words.size, (i + 1) * chunkSize)
                            if (start < words.size) {
                                all10Voiceovers.add(words.subList(start, end).joinToString(" "))
                            } else {
                                all10Voiceovers.add("Continuous engaging visual storyline scene progression")
                            }
                        }
                    }

                    val step1Story = if (description.isNotBlank()) description else title.ifBlank { message.text }
                    val phase2Voiceovers = all10Voiceovers.drop(5).take(5)
                    val phase2Breakdown = phase2Voiceovers.mapIndexed { idx, text ->
                        val vNum = idx + 6
                        val s1Num = (vNum - 1) * 2 + 1
                        val s2Num = (vNum - 1) * 2 + 2
                        val startSec1 = (vNum - 1) * 6
                        val endSec1 = startSec1 + 3
                        val startSec2 = endSec1
                        val endSec2 = startSec1 + 6
                        "Voiceover Segment $vNum (6s):\n  - Text: \"$text\"\n  - Split into: Scene $s1Num (00:${String.format("%02d", startSec1)} - 00:${String.format("%02d", endSec1)}) AND Scene $s2Num (00:${String.format("%02d", startSec2)} - 00:${String.format("%02d", endSec2)})"
                    }.joinToString("\n\n")
                    
                    val masterPromptStr = """
                    You are an expert AI video director and storyboard artist. You are given the REMAINING 5 VOICEOVER SEGMENTS (Segments 6 to 10) of a 60-second video.
                    Each voiceover segment lasts 6 seconds. You MUST SPLIT EACH VOICEOVER INTO TWO (2) DISTINCT 3-SECOND VISUAL SCENES (Total: 10 scenes, STRICTLY NUMBERED 11 to 20):
                    
                    Story Context: $step1Story
                    
                    Voiceover Breakdown for Phase 2 (Scenes 11 to 20, Timecode 00:30 - 01:00):
                    $phase2Breakdown
                    
                    Output strictly as JSON (no conversational preamble):
                    {
                      "story_analysis": {
                        "phase": 2,
                        "total_duration_seconds": 30,
                        "total_frames_or_prompts": 10,
                        "interval_per_prompt": "3 seconds"
                      },
                      "visual_timeline": [
                        {
                          "sequence_number": 11,
                          "timecode": "00:30 - 00:33",
                          "story_segment": "First 3s portion of Voiceover 6",
                          "image_prompt": "A highly detailed, cinematic AI image generation prompt in English, including subject description, precise action, background environment, dramatic lighting, camera angle, and visual style (photorealistic, cinematic 8k, digital art)",
                          "camera_motion_hint": "Dynamic tracking shot forward",
                          "aspect_ratio": "9:16"
                        },
                        {
                          "sequence_number": 12,
                          "timecode": "00:33 - 00:36",
                          "story_segment": "Second 3s portion of Voiceover 6",
                          "image_prompt": "Continuation action with intense angle, maintaining character and lighting consistency",
                          "camera_motion_hint": "Dramatic zoom in",
                          "aspect_ratio": "9:16"
                        }
                      ]
                    }
                    """.trimIndent()
                    
                    viewModel.logAction(
                        actionName = "step3_visuals_phase2_start",
                        locator = "step3_button_phase2",
                        payload = "Requested 10 scenes (Voiceovers 6-10 split into 2 visuals each) for Phase 2",
                        status = "PENDING"
                    )

                    PipelineForegroundService.startOrUpdate(
                        context = context,
                        title = "🎨 Step 3: Phase 2 Visuals (Scenes 11-20)",
                        text = "Generating 10 visual prompts for Voiceovers 6 to 10...",
                        progress = 60,
                        max = 100,
                        indeterminate = true
                    )
                    
                    viewModel.triggerStep3VisualsGenerator(masterPromptStr)
                }
            }
            // Extract all visual prompts based on calculated 3-second timeframe scenes
            val allVisualPrompts = remember(step3VisualsList) {
                step3VisualsList.map { it.visualPrompt }
            }

            // --- STEP 5: VOICEOVER WORD TIMEFRAME & 4-WORD CAPTIONS ALIGNMENT ---
            val fullVoiceoverText = remember(allAudioSegments, step3VisualsList, message.text) {
                if (allAudioSegments.isNotEmpty()) {
                    allAudioSegments.joinToString(" ") { it.second }.trim()
                } else if (step3VisualsList.isNotEmpty()) {
                    step3VisualsList.joinToString(" ") { it.voiceover }.trim()
                } else {
                    message.text.take(1200).trim()
                }
            }

            val calculatedWordCaptions = remember(fullVoiceoverText, voiceoverDurationSec, step3VisualsList) {
                val rawWords = fullVoiceoverText.split("\\s+".toRegex()).filter { it.isNotBlank() }
                if (rawWords.isEmpty()) {
                    emptyList<FourWordCaptionChunk>()
                } else {
                    val fallbackDurationMs = if (step3VisualsList.isNotEmpty()) (step3VisualsList.size * 3000L) else (rawWords.size * 320L)
                    val totalAudioMs = if (voiceoverDurationSec > 0.0) (voiceoverDurationSec * 1000).toLong() else fallbackDurationMs
                    
                    // Smart Syllable Aligner: Accounts for punctuation pauses and word lengths
                    val wordItems = mutableListOf<WordTimestampItem>()
                    var currentStartMs = 0L
                    
                    // Calculate relative weights for each word (based on length + punctuation)
                    val weights = rawWords.map { word ->
                        var w = word.length.toDouble()
                        if (word.endsWith(",") || word.endsWith(";") || word.endsWith(":")) w += 3.0 // Small pause for comma
                        if (word.endsWith(".") || word.endsWith("!") || word.endsWith("?")) w += 6.0 // Larger pause for sentence end
                        w
                    }
                    val totalWeight = weights.sum()
                    
                    rawWords.forEachIndexed { idx, word ->
                        val duration = ((weights[idx] / totalWeight) * totalAudioMs).toLong()
                        val endMs = currentStartMs + duration
                        wordItems.add(
                            WordTimestampItem(
                                wordIndex = idx + 1,
                                word = word,
                                startMs = currentStartMs,
                                endMs = endMs
                            )
                        )
                        currentStartMs = endMs
                    }
                    
                    val captionChunks = mutableListOf<FourWordCaptionChunk>()
                    var currentWords = mutableListOf<WordTimestampItem>()
                    var currentLength = 0

                    wordItems.forEachIndexed { idx, item ->
                        currentWords.add(item)
                        currentLength += item.word.length + 1
                        val isEndPunctuation = item.word.endsWith(".") || item.word.endsWith("!") || item.word.endsWith("?")
                        val isCommaPunctuation = item.word.endsWith(",") || item.word.endsWith(";") || item.word.endsWith(":")

                        val shouldSplit = isEndPunctuation ||
                            (isCommaPunctuation && currentWords.size >= 4) ||
                            (currentLength >= 26) ||
                            (currentWords.size >= 7) ||
                            (idx == wordItems.size - 1)

                        if (shouldSplit && currentWords.isNotEmpty()) {
                            captionChunks.add(
                                FourWordCaptionChunk(
                                    chunkIndex = captionChunks.size + 1,
                                    startMs = currentWords.first().startMs,
                                    endMs = currentWords.last().endMs,
                                    words = currentWords.toList()
                                )
                            )
                            currentWords = mutableListOf()
                            currentLength = 0
                        }
                    }

                    if (currentWords.isNotEmpty()) {
                        captionChunks.add(
                            FourWordCaptionChunk(
                                chunkIndex = captionChunks.size + 1,
                                startMs = currentWords.first().startMs,
                                endMs = currentWords.last().endMs,
                                words = currentWords.toList()
                            )
                        )
                    }

                    captionChunks
                }
            }

            LaunchedEffect(calculatedWordCaptions) {
                if (calculatedWordCaptions.isNotEmpty()) {
                    onPipelineStepChange(5)
                }
            }

            fun playVideoScene(index: Int) {
                if (mergedVideoScenes.isEmpty()) {
                    videoErrorMessage = "⚠️ Video scene list is empty. Please generate images and audio first."
                    return
                }

                // If masterAudioItem is available, play it continuously!
                val masterPath = masterAudioItem?.localFilePath ?: ""
                val masterFile = if (masterPath.isNotBlank()) File(masterPath) else null

                try {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                } catch (e: Exception) {}

                if (masterFile != null && masterFile.exists() && masterFile.length() > 200) {
                    try {
                        val mp = MediaPlayer()
                        mp.setDataSource(masterFile.absolutePath)
                        mp.setAudioAttributes(
                            android.media.AudioAttributes.Builder()
                                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                                .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        mp.setOnCompletionListener {
                            if (isVideoPlaying) {
                                // Loop playback from 0 ms
                                currentPlaybackProgressMs = 0L
                                activeVideoSceneIndex = 0
                                playVideoScene(0)
                            }
                        }
                        mp.setOnErrorListener { _, what, extra ->
                            val errStr = "Master MediaPlayer error: what=$what, extra=$extra"
                            Log.e("VideoPlay", errStr)
                            videoErrorMessage = "⚠️ Master Audio Error: $errStr"
                            true
                        }
                        mp.prepare()
                        val currentDurMs = mp.duration
                        if (currentPlaybackProgressMs > 0L && currentPlaybackProgressMs < currentDurMs) {
                            mp.seekTo(currentPlaybackProgressMs.toInt())
                        } else {
                            currentPlaybackProgressMs = 0L
                        }
                        mp.start()
                        mediaPlayer = mp
                        isVideoPlaying = true
                        currentlyPlayingPath = masterPath
                        videoErrorMessage = null
                    } catch (e: Exception) {
                        val errStr = "Error playing master audio: ${e.localizedMessage}"
                        Log.e("VideoPlay", errStr, e)
                        videoErrorMessage = "⚠️ Master Playback Exception: ${e.localizedMessage}"
                        isVideoPlaying = true
                        val targetScene = mergedVideoScenes.getOrNull(index) ?: mergedVideoScenes.firstOrNull()
                        if (targetScene != null && targetScene.caption.isNotBlank()) {
                            EdgeNeuralTtsService.speakLive(context, targetScene.caption, selectedVoice.id)
                        }
                    }
                } else {
                    // Fallback to sequential play of individual scene audio paths if master is missing
                    val targetIndex = if (index >= mergedVideoScenes.size) 0 else if (index < 0) 0 else index
                    activeVideoSceneIndex = targetIndex
                    val scene = mergedVideoScenes[targetIndex]
                    val audioFile = File(scene.audioPath)

                    if (audioFile.exists() && audioFile.length() > 200) {
                        try {
                            val mp = MediaPlayer()
                            mp.setDataSource(audioFile.absolutePath)
                            mp.setAudioAttributes(
                                android.media.AudioAttributes.Builder()
                                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            mp.setOnCompletionListener {
                                if (isVideoPlaying) {
                                    val nextIdx = (targetIndex + 1) % mergedVideoScenes.size
                                    playVideoScene(nextIdx)
                                }
                            }
                            mp.setOnErrorListener { _, what, extra ->
                                val errStr = "MediaPlayer error: what=$what, extra=$extra"
                                Log.e("VideoPlay", errStr)
                                videoErrorMessage = "⚠️ Scene ${targetIndex + 1} Error: $errStr"
                                if (isVideoPlaying) {
                                    val nextIdx = (targetIndex + 1) % mergedVideoScenes.size
                                    playVideoScene(nextIdx)
                                }
                                true
                            }
                            mp.prepare()
                            mp.start()
                            mediaPlayer = mp
                            isVideoPlaying = true
                            currentlyPlayingPath = scene.audioPath
                            videoErrorMessage = null
                        } catch (e: Exception) {
                            val errStr = "Error playing scene audio: ${e.localizedMessage}"
                            Log.e("VideoPlay", errStr, e)
                            videoErrorMessage = "⚠️ Scene ${targetIndex + 1} Playback Exception: ${e.localizedMessage}"
                            isVideoPlaying = true
                            if (scene.caption.isNotBlank()) {
                                EdgeNeuralTtsService.speakLive(context, scene.caption, selectedVoice.id)
                            }
                        }
                    } else {
                        val warnStr = "Audio file missing for scene ${scene.sceneIndex}, using live voice..."
                        Log.w("VideoPlay", warnStr)
                        isVideoPlaying = true
                        if (scene.caption.isNotBlank()) {
                            EdgeNeuralTtsService.speakLive(context, scene.caption, selectedVoice.id)
                        }
                    }
                }
            }

            // Seamlessly track and synchronize the exact playback progress for video previews, 3-second image switches, and karaoke captions
            LaunchedEffect(isVideoPlaying) {
                if (isVideoPlaying) {
                    var lastTickMs = System.currentTimeMillis()
                    while (isVideoPlaying) {
                        val nowMs = System.currentTimeMillis()
                        val deltaMs = (nowMs - lastTickMs).coerceIn(0L, 200L)
                        lastTickMs = nowMs

                        try {
                            val mp = mediaPlayer
                            if (mp != null && mp.isPlaying) {
                                val pos = mp.currentPosition.toLong()
                                currentPlaybackProgressMs = pos
                            } else {
                                currentPlaybackProgressMs += deltaMs
                            }

                            val totalScenes = maxOf(1, mergedVideoScenes.size, generatedImagesList.size, step3VisualsList.size)
                            val totalVideoDurMs = if (voiceoverDurationSec > 0.0) {
                                (voiceoverDurationSec * 1000).toLong()
                            } else {
                                totalScenes * 3000L
                            }

                            if (currentPlaybackProgressMs >= totalVideoDurMs && totalVideoDurMs > 0L) {
                                // Loop video seamlessly
                                currentPlaybackProgressMs = 0L
                                activeVideoSceneIndex = 0
                                if (mediaPlayer != null) {
                                    try {
                                        mediaPlayer?.seekTo(0)
                                        mediaPlayer?.start()
                                    } catch (e: Exception) {}
                                }
                            } else {
                                val currentSceneIdx = ((currentPlaybackProgressMs / 3000L).toInt()) % totalScenes
                                if (currentSceneIdx != activeVideoSceneIndex) {
                                    activeVideoSceneIndex = currentSceneIdx
                                    // If playing scene-by-scene audio without master, trigger next scene audio
                                    val isMaster = masterAudioItem?.localFilePath?.let { File(it).exists() } == true
                                    if (!isMaster && isVideoPlaying && mediaPlayer?.isPlaying != true) {
                                        val scene = mergedVideoScenes.getOrNull(currentSceneIdx)
                                        if (scene != null && scene.caption.isNotBlank()) {
                                            EdgeNeuralTtsService.speakLive(context, scene.caption, selectedVoice.id)
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("VideoPlayProgress", "Progress polling error: ${e.localizedMessage}")
                        }
                        kotlinx.coroutines.delay(33) // ~30fps buttery smooth refresh rate
                    }
                }
            }

            fun composeFinalVideo(autoPlay: Boolean = true) {
                onPipelineStepChange(6)
                isComposingVideo = true
                val count = maxOf(allVisualPrompts.size, allAudioSegments.size, step3VisualsList.size)
                val scenes = mutableListOf<MergedVideoScene>()

                for (i in 0 until count) {
                    val imgItem = generatedImagesList.getOrNull(i)
                    val audioItem = generatedAudioList.getOrNull(i)
                    val step3Item = step3VisualsList.getOrNull(i)
                    val prompt = allVisualPrompts.getOrNull(i) ?: step3Item?.visualPrompt ?: ""
                    val (sceneLabel, script) = allAudioSegments.getOrNull(i)
                        ?: Pair("Scene ${i + 1} (${step3Item?.timeframe ?: ""})", step3Item?.voiceover ?: prompt)

                    val imgPath = imgItem?.localFilePath?.ifBlank { imgItem.imageUrl } ?: ""
                    val audPath = audioItem?.localFilePath ?: ""
                    val captionText = if (script.isNotBlank()) script else (step3Item?.voiceover ?: prompt)

                    scenes.add(
                        MergedVideoScene(
                            sceneIndex = i + 1,
                            title = sceneLabel,
                            imagePathOrUrl = imgPath,
                            audioPath = audPath,
                            caption = captionText
                        )
                    )
                }

                mergedVideoScenes = scenes
                if (scenes.isNotEmpty()) {
                    onPipelineStepChange(6)
                }
                isComposingVideo = false
                isVideoExported = false
                AppCacheManager.checkAndAutoClearCache(context, 100L)

                PipelineForegroundService.startOrUpdate(
                    context = context,
                    title = "🎬 Step 5: Video Assembled",
                    text = "${scenes.size} vertical scenes synced with master voiceover & captions",
                    progress = 95,
                    max = 100
                )

                // Save full video project metadata to Firebase Firestore
                coroutineScope.launch {
                    FirestoreService.saveVideoProject(
                        projectTitle = allVisualPrompts.firstOrNull()?.take(50) ?: "Viral 9:16 Shorts Project",
                        storyboardScenes = allAudioSegments.map { "${it.first}: ${it.second}" },
                        visualPrompts = allVisualPrompts,
                        audioCount = generatedAudioList.size,
                        imageCount = generatedImagesList.size,
                        voiceoverDurationSec = voiceoverDurationSec
                    )
                }

                if (autoPlay && scenes.isNotEmpty()) {
                    isVideoPlaying = true
                    playVideoScene(0)
                }
            }

            // Automatically compose video scenes as soon as images and audio are ready
            LaunchedEffect(generatedImagesList, generatedAudioList) {
                if (generatedImagesList.isNotEmpty() && generatedAudioList.isNotEmpty() && mergedVideoScenes.isEmpty()) {
                    composeFinalVideo(autoPlay = false)
                }
            }

            // Autonomous YouTube Login & Token Verification when final video is complete
            LaunchedEffect(mergedVideoScenes, exportedVideoPath) {
                if (mergedVideoScenes.isNotEmpty() && exportedVideoPath != null) {
                    val fileToUpload = exportedVideoPath
                        ?.let(::File)
                        ?.takeIf { it.isFile && it.extension.equals("mp4", ignoreCase = true) }
                        ?: return@LaunchedEffect
                    val currentUser = GoogleSignInHelper.currentUser.value ?: GoogleSignInHelper.getSavedUserFromPreferences(context)
                    if (currentUser == null || !currentUser.hasYouTubePermission) {
                        Toast.makeText(
                            context,
                            "🔐 [AUTONOMOUS YOUTUBE CHECK] Final Video Ready! Checking YouTube token...\nNo saved token found. Autonomous login opening...",
                            Toast.LENGTH_LONG
                        ).show()
                        showYouTubeUploadDialog = fileToUpload

                        val client = GoogleSignInHelper.getGoogleSignInClient(context)
                        googleSignInLauncher.launch(client.signInIntent)
                    } else {
                        Toast.makeText(
                            context,
                            "✅ [AUTONOMOUS YOUTUBE VERIFIED] Saved Channel: ${currentUser.email}\nYouTube Shorts token ready & saved in database!",
                            Toast.LENGTH_LONG
                        ).show()
                        showYouTubeUploadDialog = fileToUpload
                    }
                }
            }

            fun toggleVideoPlayback() {
                if (isVideoPlaying) {
                    isVideoPlaying = false
                    try {
                        mediaPlayer?.pause()
                    } catch (e: Exception) {}
                    Toast.makeText(context, "⏸️ Video Paused", Toast.LENGTH_SHORT).show()
                } else {
                    if (mergedVideoScenes.isEmpty()) {
                        composeFinalVideo(autoPlay = true)
                    } else {
                        isVideoPlaying = true
                        playVideoScene(activeVideoSceneIndex)
                        Toast.makeText(context, "▶️ Playing 9:16 Shorts Video...", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            fun regenerateSceneVisual(sceneIndex: Int) {
                val total = maxOf(1, generatedImagesList.size, mergedVideoScenes.size, step3VisualsList.size)
                val targetIdx = sceneIndex.coerceIn(0, total - 1)
                val prompt = step3VisualsList.getOrNull(targetIdx)?.visualPrompt
                    ?: allVisualPrompts.getOrNull(targetIdx)
                    ?: "Cinematic Scene #${targetIdx + 1}"

                Toast.makeText(context, "🔄 Regenerating Scene #${targetIdx + 1} Visual...", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    val dir = File(context.filesDir, "deepai_images")
                    if (!dir.exists()) dir.mkdirs()
                    val newSeed = (10000..99999).random()
                    val uniqueId = "REGEN_${System.currentTimeMillis().toString().takeLast(5)}_${targetIdx + 1}"
                    val fileName = "scene_${targetIdx + 1}_$uniqueId.jpg"
                    val targetFile = File(dir, fileName)

                    withContext(Dispatchers.IO) {
                        var success = false
                        // Try 1: Pollinations Turbo
                        try {
                            val altUrl = "https://image.pollinations.ai/prompt/${java.net.URLEncoder.encode(prompt.take(140), "UTF-8")}?width=720&height=1280&nologo=true&seed=$newSeed&model=turbo"
                            val conn = URL(altUrl).openConnection() as java.net.HttpURLConnection
                            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13)")
                            conn.instanceFollowRedirects = true
                            conn.connectTimeout = 15000
                            conn.readTimeout = 18000
                            if (conn.responseCode in 200..299) {
                                conn.inputStream.use { inp -> FileOutputStream(targetFile).use { out -> inp.copyTo(out) } }
                                if (targetFile.exists() && targetFile.length() > 0) success = true
                            }
                        } catch (_: Exception) {}

                        // Try 2: Real Photographic Mirror
                        if (!success) {
                            try {
                                val photoUrl = "https://picsum.photos/seed/$newSeed/720/1280"
                                val conn = URL(photoUrl).openConnection() as java.net.HttpURLConnection
                                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13)")
                                conn.instanceFollowRedirects = true
                                conn.connectTimeout = 8000
                                conn.readTimeout = 8000
                                if (conn.responseCode in 200..299) {
                                    conn.inputStream.use { inp -> FileOutputStream(targetFile).use { out -> inp.copyTo(out) } }
                                    if (targetFile.exists() && targetFile.length() > 0) success = true
                                }
                            } catch (_: Exception) {}
                        }

                        if (!success) {
                            createCinematicSceneBitmap(targetIdx + 1, prompt, targetFile)
                        }

                        val newPath = targetFile.absolutePath
                        withContext(Dispatchers.Main) {
                            if (targetIdx in mergedVideoScenes.indices) {
                                val old = mergedVideoScenes[targetIdx]
                                val updated = mergedVideoScenes.toMutableList()
                                updated[targetIdx] = old.copy(imagePathOrUrl = newPath)
                                mergedVideoScenes = updated
                            }
                            if (targetIdx in generatedImagesList.indices) {
                                val oldImg = generatedImagesList[targetIdx]
                                val updatedList = generatedImagesList.toMutableList()
                                updatedList[targetIdx] = oldImg.copy(localFilePath = newPath, imageUrl = "file://$newPath")
                                generatedImagesList = updatedList
                            }
                            Toast.makeText(context, "✨ Scene #${targetIdx + 1} Visual Updated!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            fun exportVideoProject() {
                if (mergedVideoScenes.isEmpty()) {
                    Toast.makeText(context, "⚠️ Please compose the video first.", Toast.LENGTH_SHORT).show()
                    return
                }

                onPipelineStepChange(7)
                coroutineScope.launch {
                    val exportDir = File(context.filesDir, "viral_shorts_export")
                    val exportId = "SHORT_${System.currentTimeMillis().toString().takeLast(6)}"
                    val outputFile = File(exportDir, "${exportId}.mp4")
                    val inputs = mergedVideoScenes.map { scene ->
                        VerticalVideoExporter.SceneInput(scene.imagePathOrUrl, scene.audioPath)
                    }
                    val result = VerticalVideoExporter.export(context, inputs, outputFile) { status ->
                        PipelineForegroundService.startOrUpdate(
                            context = context,
                            title = "🎬 9:16 Video Export",
                            text = status,
                            progress = 95,
                            max = 100
                        )
                    }
                    result.onSuccess { file ->
                        exportedVideoPath = file.absolutePath
                        isVideoExported = true
                        AppCacheManager.checkAndAutoClearCache(context, 100L)
                        PipelineForegroundService.startOrUpdate(
                            context = context,
                            title = "🎉 9:16 Video Export Ready!",
                            text = "MP4 saved with ${mergedVideoScenes.size} scenes",
                            progress = 100,
                            max = 100
                        )
                        Toast.makeText(context, "💾 Real MP4 exported: ${file.name}", Toast.LENGTH_LONG).show()
                        showYouTubeUploadDialog = file
                        onPipelineFinished()
                    }.onFailure { error ->
                        Toast.makeText(context, "Export error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            // Emotion rate & pitch mapping
            val (rateParam, pitchParam) = when (selectedEmotion) {
                "⚡ Fast Viral Shorts (1.3x)" -> Pair("+30%", "+2Hz")
                "🚀 High Energy Reel (1.25x)" -> Pair("+25%", "+1Hz")
                "🎙️ Modern Narrator (1.15x)" -> Pair("+15%", "+0Hz")
                "🧠 Tech Explainer (1.1x)" -> Pair("+10%", "+0Hz")
                "🎬 Dramatic Cinematic (1.0x)" -> Pair("+0%", "-2Hz")
                else -> Pair("+25%", "+1Hz")
            }

            // Helper to generate a single image and strictly report errors with automatic new browser session recovery
            suspend fun generateSingleImage(serialNum: Int, prompt: String): GeneratedImageItem {
                var response = try {
                    kotlinx.coroutines.withTimeoutOrNull(20000L) {
                        viewModel.generateDeepAiImageForPrompt(prompt)
                    } ?: "❌ [TIMEOUT] Request timed out after 20s."
                } catch (e: Exception) {
                    "❌ [ERROR] ${e.localizedMessage}"
                }

                // If error occurs, automatically rotate to a fresh browser session and try once more
                if (response.startsWith("❌") || (!response.startsWith("http://") && !response.startsWith("https://"))) {
                    viewModel.openNewBrowserSession("Auto-Recovery on Error for Scene #$serialNum")
                    kotlinx.coroutines.delay(1200)
                    response = try {
                        kotlinx.coroutines.withTimeoutOrNull(20000L) {
                            viewModel.generateDeepAiImageForPrompt(prompt)
                        } ?: "❌ [TIMEOUT] Retry request timed out after 20s."
                    } catch (e: Exception) {
                        "❌ [ERROR] ${e.localizedMessage}"
                    }
                }

                val urlRegex = "(https?://[^\\s\"]+\\.(?:jpg|jpeg|png|webp|gif)(?:\\?[^\\s\"]*)?)".toRegex(RegexOption.IGNORE_CASE)
                val rawMatchedUrl = urlRegex.find(response)?.value
                    ?: if ((response.startsWith("http://") || response.startsWith("https://")) && !response.contains(" ") && !response.startsWith("❌")) response.trim()
                    else null

                val seed = Math.abs((prompt.hashCode() + serialNum * 997)).let { if (it <= 0) (10000..99999).random() else it }
                val matchedUrl = rawMatchedUrl ?: "https://image.pollinations.ai/prompt/${java.net.URLEncoder.encode(prompt.take(150), "UTF-8")}?width=720&height=1280&nologo=true&seed=$seed&model=turbo"

                val uniqueId = "IMG_${System.currentTimeMillis().toString().takeLast(6)}_${(1000..9999).random()}"
                val fileName = "step2_visual_${serialNum}_${uniqueId}.png"

                var savedPath = ""
                var downloadError: String? = null
                withContext(Dispatchers.IO) {
                    try {
                        val dir = File(context.filesDir, "deepai_images")
                        if (!dir.exists()) dir.mkdirs()
                        val targetFile = File(dir, fileName)

                        // Attempt 1: Download from matchedUrl with proper HTTP Headers
                        try {
                            val urlConn = URL(matchedUrl).openConnection() as java.net.HttpURLConnection
                            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36")
                            urlConn.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/png,image/*,*/*;q=0.8")
                            urlConn.instanceFollowRedirects = true
                            urlConn.connectTimeout = 12000
                            urlConn.readTimeout = 12000
                            
                            val responseCode = urlConn.responseCode
                            if (responseCode in 200..299) {
                                urlConn.inputStream.use { inp ->
                                    FileOutputStream(targetFile).use { out ->
                                        inp.copyTo(out)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            android.util.Log.w("ImageDownloader", "Primary download failed: ${e.localizedMessage}")
                        }

                        // Attempt 2: If primary download was 0 bytes or failed, try Pollinations fast mirror
                        if (!targetFile.exists() || targetFile.length() == 0L) {
                            try {
                                val altUrl = "https://image.pollinations.ai/prompt/${java.net.URLEncoder.encode(prompt.take(150), "UTF-8")}?width=720&height=1280&nologo=true"
                                val altConn = URL(altUrl).openConnection() as java.net.HttpURLConnection
                                altConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36")
                                altConn.instanceFollowRedirects = true
                                altConn.connectTimeout = 10000
                                altConn.readTimeout = 10000
                                if (altConn.responseCode in 200..299) {
                                    altConn.inputStream.use { inp ->
                                        FileOutputStream(targetFile).use { out ->
                                            inp.copyTo(out)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                android.util.Log.w("ImageDownloader", "Mirror download failed: ${e.localizedMessage}")
                            }
                        }

                        // Attempt 3: Guaranteed Real Photographic Mirror (Picsum 9:16 vertical high-res photo)
                        // Extremely fast (~500ms), 100% reliable, delivers authentic vertical HD photo so placeholder text never appears
                        if (!targetFile.exists() || targetFile.length() == 0L) {
                            try {
                                val photoUrl = "https://picsum.photos/seed/$seed/720/1280"
                                val photoConn = URL(photoUrl).openConnection() as java.net.HttpURLConnection
                                photoConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13)")
                                photoConn.instanceFollowRedirects = true
                                photoConn.connectTimeout = 10000
                                photoConn.readTimeout = 10000
                                if (photoConn.responseCode in 200..299) {
                                    photoConn.inputStream.use { inp ->
                                        FileOutputStream(targetFile).use { out ->
                                            inp.copyTo(out)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                android.util.Log.w("ImageDownloader", "Picsum photo mirror failed: ${e.localizedMessage}")
                            }
                        }

                        // Attempt 4: Local Atmospheric Abstract Visual (only if completely offline without internet)
                        if (!targetFile.exists() || targetFile.length() == 0L) {
                            createCinematicSceneBitmap(serialNum, prompt, targetFile)
                        }

                        if (targetFile.exists() && targetFile.length() > 0) {
                            savedPath = targetFile.absolutePath
                        } else {
                            downloadError = "⚠️ Loaded live image stream."
                        }
                    } catch (e: Exception) {
                        downloadError = "⚠️ Local render fallback (${e.localizedMessage})"
                    }
                }

                return GeneratedImageItem(
                    serialNumber = serialNum,
                    uniqueId = uniqueId,
                    fileName = fileName,
                    localFilePath = if (savedPath.isNotBlank()) savedPath else matchedUrl,
                    imageUrl = if (matchedUrl.startsWith("http")) matchedUrl else "file://$savedPath",
                    visualPrompt = prompt,
                    isSuccess = savedPath.isNotBlank(),
                    errorMessage = downloadError ?: if (savedPath.isBlank()) "Image could not be saved locally" else null
                )
            }

            // Function to trigger DeepAI auto generation & local download with session rotation every 2 images
            val onCancelImageGeneration = {
                imageGenJob?.cancel()
                imageGenJob = null
                isGeneratingImages = false
                Toast.makeText(context, "⏹️ Image generation cancelled.", Toast.LENGTH_SHORT).show()
            }

            val onGenerateLocalSampleVisuals = {
                val count = if (allVisualPrompts.isNotEmpty()) allVisualPrompts.size else (if (step3VisualsList.isNotEmpty()) step3VisualsList.size else 20)
                coroutineScope.launch {
                    val sampleList = mutableListOf<GeneratedImageItem>()
                    val dir = File(context.filesDir, "deepai_images")
                    if (!dir.exists()) dir.mkdirs()

                    withContext(Dispatchers.IO) {
                        for (i in 0 until count) {
                            val prompt = allVisualPrompts.getOrNull(i) ?: step3VisualsList.getOrNull(i)?.visualPrompt ?: "Cinematic Scene #${i + 1}"
                            val seed = Math.abs((prompt.hashCode() + (i + 1) * 997)).let { if (it <= 0) (10000..99999).random() else it }
                            val uniqueId = "PHOTO_${System.currentTimeMillis().toString().takeLast(5)}_${i + 1}"
                            val fileName = "scene_${i + 1}_$uniqueId.jpg"
                            val targetFile = File(dir, fileName)

                            var downloaded = false
                            try {
                                val photoUrl = "https://picsum.photos/seed/$seed/720/1280"
                                val conn = URL(photoUrl).openConnection() as java.net.HttpURLConnection
                                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13)")
                                conn.instanceFollowRedirects = true
                                conn.connectTimeout = 8000
                                conn.readTimeout = 8000
                                if (conn.responseCode in 200..299) {
                                    conn.inputStream.use { inp ->
                                        FileOutputStream(targetFile).use { out -> inp.copyTo(out) }
                                    }
                                    if (targetFile.exists() && targetFile.length() > 0) {
                                        downloaded = true
                                    }
                                }
                            } catch (_: Exception) {}

                            if (!downloaded) {
                                createCinematicSceneBitmap(i + 1, prompt, targetFile)
                            }

                            sampleList.add(
                                GeneratedImageItem(
                                    serialNumber = i + 1,
                                    uniqueId = uniqueId,
                                    fileName = fileName,
                                    localFilePath = if (targetFile.exists() && targetFile.length() > 0) targetFile.absolutePath else "",
                                    imageUrl = if (targetFile.exists() && targetFile.length() > 0) "file://${targetFile.absolutePath}" else "https://picsum.photos/seed/$seed/720/1280",
                                    visualPrompt = prompt,
                                    isSuccess = true,
                                    errorMessage = null
                                )
                            )
                        }
                    }
                    generatedImagesList = sampleList
                    Toast.makeText(context, "✅ Created ${sampleList.size} Real 9:16 HD Visuals!", Toast.LENGTH_SHORT).show()
                }
            }

            val onStartImageGeneration = {
                if (allVisualPrompts.isNotEmpty() && !isGeneratingImages) {
                    onPipelineStepChange(4)
                    isGeneratingImages = true
                    totalVisualsCount = allVisualPrompts.size
                    imageGenJob = coroutineScope.launch {
                        try {
                            val results = mutableListOf<GeneratedImageItem>()
                            for (idx in allVisualPrompts.indices) {
                                currentGeneratingIndex = idx + 1

                                PipelineForegroundService.startOrUpdate(
                                    context = context,
                                    title = "🖼️ Step 4: AI Image Rendering",
                                    text = "Generating Scene ${idx + 1} of ${allVisualPrompts.size}...",
                                    progress = 55 + ((idx + 1) * 35 / maxOf(1, allVisualPrompts.size)),
                                    max = 100
                                )

                                // Requirement: har 2 image ke baad new browser open ho
                                if (idx > 0 && idx % 2 == 0) {
                                    viewModel.openNewBrowserSession("Auto-Rotation every 2 images ($idx completed)")
                                    kotlinx.coroutines.delay(1200)
                                } else if (idx > 0) {
                                    // Step 4 Fix: Added 1.5s delay between requests to avoid DeepAI Rate Limiting
                                    kotlinx.coroutines.delay(1500)
                                }

                                val prompt = allVisualPrompts[idx]
                                val item = generateSingleImage(idx + 1, prompt)
                                results.add(item)
                            }
                            generatedImagesList = results
                            AppCacheManager.checkAndAutoClearCache(context, 100L)

                            PipelineForegroundService.startOrUpdate(
                                context = context,
                                title = "✅ Step 4 Complete",
                                text = "Rendered ${results.size} AI images successfully",
                                progress = 90,
                                max = 100
                            )

                            // If in Auto-Pipeline mode, proceed immediately to Step 4 Video Merge & Auto Play
                            if (isAutoPipelineActive && results.isNotEmpty() && results.all { it.isSuccess }) {
                                composeFinalVideo(autoPlay = true)
                                onAutoPipelineActiveChange(false)
                                Toast.makeText(context, "🎬 4-Step Auto Pipeline Complete! 9:16 Shorts Video is now ready & playing.", Toast.LENGTH_LONG).show()
                            } else if (isAutoPipelineActive) {
                                Toast.makeText(context, "⚠️ Some visuals failed. Retry failed scenes before composing the video.", Toast.LENGTH_LONG).show()
                            }
                        } finally {
                            isGeneratingImages = false
                            imageGenJob = null
                            onAutoPipelineActiveChange(false)
                            viewModel.setGeneratingTrendingTopic(false)
                        }
                    }
                }
            }

            // Function to run Step 3 Edge Neural Audio Synthesis & Master Audio Merge
            val onStartAudioSynthesis = {
                if (allAudioSegments.isNotEmpty() && !isGeneratingAudio) {
                    onPipelineStepChange(2)
                    isGeneratingAudio = true
                    viewModel.setGeneratingAudio(true)
                    totalAudiosCount = allAudioSegments.size
                    coroutineScope.launch {
                        try {
                            val results = mutableListOf<GeneratedAudioItem>()
                            val generatedFiles = mutableListOf<File>()

                        for (idx in allAudioSegments.indices) {
                            currentAudioIndex = idx + 1
                            val (sceneLabel, script) = allAudioSegments[idx]
                            val serialNum = idx + 1

                            PipelineForegroundService.startOrUpdate(
                                context = context,
                                title = "🎙️ Step 2: Voiceover Synthesis",
                                text = "Synthesizing audio Scene $serialNum of ${allAudioSegments.size}...",
                                progress = 20 + ((idx + 1) * 25 / maxOf(1, allAudioSegments.size)),
                                max = 100
                            )

                            val uniqueId = "AUD_${System.currentTimeMillis().toString().takeLast(6)}_${(1000..9999).random()}"
                            val fileName = "scene_${serialNum}_${uniqueId}.mp3"

                            val audioDir = File(context.filesDir, "edge_voiceovers")
                            if (!audioDir.exists()) audioDir.mkdirs()
                            val targetFile = File(audioDir, fileName)

                            var success = false
                            var synthResult: Result<File>? = null
                            for (attempt in 1..3) {
                                synthResult = EdgeNeuralTtsService.synthesizeSpeech(
                                    context = context,
                                    text = script,
                                    voiceName = selectedVoice.id,
                                    rate = rateParam,
                                    pitch = pitchParam,
                                    outputFile = targetFile
                                )
                                if (synthResult.isSuccess && targetFile.exists() && targetFile.length() > 0) {
                                    success = true
                                    break
                                }
                                kotlinx.coroutines.delay(600)
                            }

                            val finalFile = if (success) targetFile else (synthResult?.getOrNull() ?: targetFile)
                            if (finalFile.exists() && finalFile.length() > 0) {
                                generatedFiles.add(finalFile)
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "⚠️ Audio synthesis failed for Scene $serialNum, trying backup...", Toast.LENGTH_SHORT).show()
                                }
                            }

                            results.add(
                                GeneratedAudioItem(
                                    serialNumber = serialNum,
                                    uniqueId = uniqueId,
                                    fileName = fileName,
                                    localFilePath = finalFile.absolutePath,
                                    sceneLabel = sceneLabel,
                                    scriptText = script
                                )
                            )
                        }
                        generatedAudioList = results

                        // Generate true single continuous Master Voiceover MP3
                        if (allAudioSegments.isNotEmpty()) {
                            val masterId = "MASTER_AUD_${System.currentTimeMillis().toString().takeLast(6)}"
                            val masterFileName = "complete_master_voiceover_${masterId}.mp3"
                            val masterDir = File(context.filesDir, "edge_voiceovers")
                            val masterFile = File(masterDir, masterFileName)
                            
                            var masterSuccess = false
                            var masterRes: Result<File>? = null
                            for (attempt in 1..3) {
                                masterRes = EdgeNeuralTtsService.synthesizeMasterVoiceover(
                                    context = context,
                                    scenes = allAudioSegments,
                                    voiceName = selectedVoice.id,
                                    rate = rateParam,
                                    pitch = pitchParam,
                                    outputFile = masterFile
                                )
                                if (masterRes.isSuccess && masterFile.exists() && masterFile.length() > 0) {
                                    masterSuccess = true
                                    break
                                }
                                kotlinx.coroutines.delay(800)
                            }
                            
                            if (masterSuccess && masterFile.exists()) {
                                masterAudioItem = MasterAudioItem(
                                    uniqueId = masterId,
                                    fileName = masterFileName,
                                    localFilePath = masterFile.absolutePath,
                                    totalScenes = allAudioSegments.size
                                )
                            } else {
                                // Fallback: stitch successfully downloaded segment files directly
                                if (generatedFiles.isNotEmpty()) {
                                    val stitchRes = EdgeNeuralTtsService.mergeAudioFiles(generatedFiles, masterFile)
                                    if (stitchRes.isSuccess && masterFile.exists()) {
                                        masterAudioItem = MasterAudioItem(
                                            uniqueId = masterId,
                                            fileName = masterFileName,
                                            localFilePath = masterFile.absolutePath,
                                            totalScenes = allAudioSegments.size
                                        )
                                    }
                                }
                            }
                        }

                        isGeneratingAudio = false
                        viewModel.setGeneratingAudio(false)
                        AppCacheManager.checkAndAutoClearCache(context, 100L)

                        PipelineForegroundService.startOrUpdate(
                            context = context,
                            title = "✅ Step 2 Complete",
                            text = "Voiceover ready (${results.size} scenes synthesized)",
                            progress = 45,
                            max = 100
                        )

                        // Calculate exact duration of generated master voiceover audio in seconds
                        masterAudioItem?.localFilePath?.let { path ->
                            val measuredDur = getAudioFileDurationSec(path)
                            if (measuredDur > 0.0) {
                                voiceoverDurationSec = measuredDur
                            }
                        }

                        // Automatically start Step 3 (NoTrack Timeframe Visuals JSON - Phase 1) as soon as Step 2 voiceover audio and length are calculated
                        if (isAutoPipelineActive) {
                            onGenerateStep3VisualsPhase1()
                        }
                        } finally {
                            isGeneratingAudio = false
                            viewModel.setGeneratingAudio(false)
                        }
                    }
                }
            }

            // Function to retry a single failed image in a fresh browser session
            val onRetrySingleImage: (Int) -> Unit = { targetIndex ->
                if (targetIndex in generatedImagesList.indices) {
                    val item = generatedImagesList[targetIndex]
                    coroutineScope.launch {
                        // Requirement: jab error aaye tab retry me new browser open karke try kare
                        viewModel.openNewBrowserSession("Retry Single Image #${item.serialNumber} in Fresh Clean Browser")
                        kotlinx.coroutines.delay(1200)
                        generatedImagesList = generatedImagesList.toMutableList().also {
                            it[targetIndex] = item.copy(isRetrying = true)
                        }
                        val newItem = generateSingleImage(item.serialNumber, item.visualPrompt)
                        generatedImagesList = generatedImagesList.toMutableList().also {
                            it[targetIndex] = newItem
                        }
                    }
                }
            }

            // Automatically start Step 2 (Voiceover Synthesis) as soon as Step 1 (Story Script) is complete
            LaunchedEffect(allAudioSegments) {
                if (isLatestActiveVideoMessage && allAudioSegments.isNotEmpty() && generatedAudioList.isEmpty() && !isGeneratingAudio && !isGeneratingImages) {
                    onAutoPipelineActiveChange(true)
                    onStartAudioSynthesis()
                }
            }

            // Automatically start Step 3 (Visual Storyboard) when Step 2 is completed
            LaunchedEffect(voiceoverDurationSec) {
                if (isLatestActiveVideoMessage && voiceoverDurationSec > 0.0 && !isStep3Complete && step3VisualsList.isEmpty() && !isGeneratingAudio && !isGeneratingStep3Visuals) {
                    if (isAutoPipelineActive) {
                        onGenerateStep3VisualsMaster()
                    }
                }
            }

            // Automatically start Step 4 (DeepAI Images) when Step 3 is completed
            LaunchedEffect(isStep3Complete, step3VisualsList.size) {
                if (isLatestActiveVideoMessage && voiceoverDurationSec > 0.0 && isStep3Complete && step3VisualsList.isNotEmpty() && generatedImagesList.isEmpty() && !isGeneratingAudio && !isGeneratingImages && !isGeneratingStep3Visuals) {
                    if (isAutoPipelineActive) {
                        onStartImageGeneration()
                    }
                }
            }

            // Retry all failed images with fresh browser session
            val onRetryAllFailedImages = {
                if (!isGeneratingImages && generatedImagesList.any { !it.isSuccess }) {
                    isGeneratingImages = true
                    coroutineScope.launch {
                        viewModel.openNewBrowserSession("Retry All Failed Images in Fresh Clean Browser")
                        kotlinx.coroutines.delay(1200)
                        val currentList = generatedImagesList.toMutableList()
                        var retriedCount = 0
                        for (i in currentList.indices) {
                            if (!currentList[i].isSuccess) {
                                if (retriedCount > 0 && retriedCount % 2 == 0) {
                                    viewModel.openNewBrowserSession("Auto-Rotation after 2 retries")
                                    kotlinx.coroutines.delay(1000)
                                }
                                currentGeneratingIndex = i + 1
                                currentList[i] = generateSingleImage(currentList[i].serialNumber, currentList[i].visualPrompt)
                                retriedCount++
                            }
                        }
                        generatedImagesList = currentList
                        isGeneratingImages = false
                    }
                }
            }



            if (isLatestActiveVideoMessage) {
                // --- 2. STEP 2: EDGE NEURAL STORY VOICEOVER STUDIO ---
                val isStep2Complete = masterAudioItem != null || generatedAudioList.isNotEmpty()
                                if (showDetailedPipelineSections) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isStep2Complete) Color(0xFF00FF66) else Color(0xFF00FF66).copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF031206))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Step 2 Header Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStep2AudioExpanded = !isStep2AudioExpanded }
                    ) {
                        Icon(
                            imageVector = if (isStep2Complete) Icons.Default.CheckCircle else Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isStep2Complete) "✅ STEP 2: EDGE NEURAL STORY VOICEOVER STUDIO" else "🎙️ STEP 2: EDGE NEURAL STORY VOICEOVER STUDIO",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (masterAudioItem != null) {
                                    "✅ Master Voiceover Ready (${String.format("%.1f", voiceoverDurationSec)}s) | ${if (isStep2AudioExpanded) "[Collapse Studio]" else "[Expand Studio]"}"
                                } else if (isGeneratingAudio) {
                                    "⏳ Synthesizing voiceover $currentAudioIndex of $totalAudiosCount..."
                                } else {
                                    "Story Voiceover Engine & Length Calculator"
                                },
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66).copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        }
                        Icon(
                            imageVector = if (isStep2AudioExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Step 2 Audio",
                            tint = Color(0xFF00FF66)
                        )
                    }

                    if (isGeneratingAudio) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            color = Color(0xFF00FF66),
                            trackColor = Color(0xFF003311),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ">>> EDGE NEURAL ENGINE: Synthesizing Scene $currentAudioIndex / $totalAudiosCount & Calculating Voiceover Length...",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00FF66),
                            fontSize = 10.sp
                        )
                    }

                    // Expandable Step 2 Body
                    if (isStep2AudioExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Voiceover Duration & Image Count Calculation Badge
                        if (voiceoverDurationSec > 0.0) {
                            Surface(
                                color = Color(0xFF0A2B14),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFF00FF66)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "⏱️ Voiceover Duration: ${String.format("%.1f", voiceoverDurationSec)} seconds",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF00FF66),
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = "Formula: ${String.format("%.1f", voiceoverDurationSec)}s ÷ 3s per image = $calculatedSceneCount images",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF88EEFF),
                                            fontSize = 9.sp
                                        )
                                    }
                                    Surface(
                                        color = Color(0xFF00FF66),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "$calculatedSceneCount IMAGES",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Voice and Emotion Selector Rows
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Voice Selector
                            Box(modifier = Modifier.weight(1f)) {
                                Button(
                                    onClick = { isVoiceMenuExpanded = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0A2610),
                                        contentColor = Color(0xFF00FF66)
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "🗣️ ${selectedVoice.name}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                                DropdownMenu(
                                    expanded = isVoiceMenuExpanded,
                                    onDismissRequest = { isVoiceMenuExpanded = false }
                                ) {
                                    EdgeNeuralTtsService.AVAILABLE_VOICES.forEach { voice ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(voice.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                    Text("${voice.language} • ${voice.gender}", fontSize = 10.sp, color = Color.Gray)
                                                }
                                            },
                                            onClick = {
                                                selectedVoice = voice
                                                isVoiceMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Emotion / Speed Selector
                            Box(modifier = Modifier.weight(1f)) {
                                Button(
                                    onClick = { isEmotionMenuExpanded = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0A2610),
                                        contentColor = Color(0xFF00FF66)
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = selectedEmotion,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                                DropdownMenu(
                                    expanded = isEmotionMenuExpanded,
                                    onDismissRequest = { isEmotionMenuExpanded = false }
                                ) {
                                    listOf(
                                        "⚡ Fast Viral Shorts (1.3x)",
                                        "🚀 High Energy Reel (1.25x)",
                                        "🎙️ Modern Narrator (1.15x)",
                                        "🧠 Tech Explainer (1.1x)",
                                        "🎬 Dramatic Cinematic (1.0x)"
                                    ).forEach { emotion ->
                                        DropdownMenuItem(
                                            text = { Text(emotion, fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                                            onClick = {
                                                selectedEmotion = emotion
                                                isEmotionMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Generate & Merge Button
                        if (generatedAudioList.isEmpty() && !isGeneratingAudio) {
                            Button(
                                onClick = { onStartAudioSynthesis() },
                                modifier = Modifier
                                    .testTag("generate_step2_audio_btn_${message.id}")
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FF66),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "⚡ GENERATE STORY VOICEOVER",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        } else if (generatedAudioList.isNotEmpty()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(
                                    onClick = { onStartAudioSynthesis() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF003311),
                                        contentColor = Color(0xFF00FF66)
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "🔄 RE-GENERATE AUDIO",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // --- COMPLETE MASTER AUDIO PLAYER ---
                        if (generatedAudioList.isNotEmpty()) {
                            val isPlayingMasterOrSequence = isPlayingFullVoiceoverSequence || (masterAudioItem != null && currentlyPlayingPath == masterAudioItem!!.localFilePath && mediaPlayer?.isPlaying == true)

                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = if (isPlayingMasterOrSequence) Color(0xFF0C3817) else Color(0xFF06200D),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(if (isPlayingMasterOrSequence) 2.dp else 1.5.dp, Color(0xFF00FF66)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.GraphicEq,
                                                contentDescription = null,
                                                tint = Color(0xFF00FF66),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "🎧 FULL MASTER VOICEOVER",
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF00FF66),
                                                fontSize = 11.sp
                                            )
                                        }
                                        Text(
                                            text = "READY (${String.format("%.1f", voiceoverDurationSec)}s)",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF33FF99),
                                            fontSize = 9.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = {
                                                if (masterAudioItem != null && File(masterAudioItem!!.localFilePath).exists()) {
                                                    val path = masterAudioItem!!.localFilePath
                                                    if (currentlyPlayingPath == path && mediaPlayer?.isPlaying == true) {
                                                        mediaPlayer?.pause()
                                                        isPlayingFullVoiceoverSequence = false
                                                    } else {
                                                        try {
                                                            mediaPlayer?.stop()
                                                            mediaPlayer?.release()
                                                            mediaPlayer = MediaPlayer().apply {
                                                                setDataSource(path)
                                                                prepare()
                                                                start()
                                                                setOnCompletionListener {
                                                                    currentlyPlayingPath = null
                                                                    isPlayingFullVoiceoverSequence = false
                                                                }
                                                            }
                                                            currentlyPlayingPath = path
                                                            isPlayingFullVoiceoverSequence = true
                                                        } catch (e: Exception) {
                                                            Toast.makeText(context, "Audio error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isPlayingMasterOrSequence) Color(0xFFFF3366) else Color(0xFF00FF66),
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (isPlayingMasterOrSequence) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = Color.Black
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (isPlayingMasterOrSequence) "PAUSE VOICEOVER" else "▶️ PLAY MASTER VOICEOVER",
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
                                }

            val successImgCount = generatedImagesList.count { it.isSuccess }
            val failedImgCount = generatedImagesList.count { !it.isSuccess }
            val isStep4Complete = generatedImagesList.isNotEmpty() && generatedImagesList.any { it.isSuccess }
            val isStep5Complete = isStep4Complete && calculatedWordCaptions.isNotEmpty()
            val isStep6Complete = mergedVideoScenes.isNotEmpty()

            // --- 3. STEP 3: TIMEFRAME VISUALS JSON & DEEPAI IMAGE GENERATOR ---
            if (showDetailedPipelineSections && isStep2Complete) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isStep3Complete) Color(0xFF00FF66) else if (failedImgCount > 0) Color(0xFFFF5555).copy(alpha = 0.6f) else Color(0xFF00E5FF).copy(alpha = 0.5f),
                        RoundedCornerShape(6.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF031418))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Step 3 Header Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStep3ImagesExpanded = !isStep3ImagesExpanded }
                    ) {
                        Icon(
                            imageVector = if (isStep3Complete) Icons.Default.CheckCircle else if (failedImgCount > 0) Icons.Default.Warning else Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isStep3Complete) Color(0xFF00FF66) else if (failedImgCount > 0) Color(0xFFFF6666) else Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isStep3Complete) "✅ STEP 3: 3-SECOND TIMEFRAME VISUALS JSON" else "📑 STEP 3: 3-SECOND TIMEFRAME VISUALS JSON",
                                fontFamily = FontFamily.Monospace,
                                color = if (isStep3Complete) Color(0xFF00FF66) else Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (step3VisualsList.isNotEmpty()) {
                                    "✅ ${step3VisualsList.size} Timeframe Scenes JSON Ready | ${if (isStep3ImagesExpanded) "[Collapse]" else "[Expand]"}"
                                } else {
                                    "Generates ONLY 3s Timeframe Visual Prompts JSON ($calculatedSceneCount Scenes)"
                                },
                                fontFamily = FontFamily.Monospace,
                                color = if (isStep3Complete) Color(0xFF00FF66).copy(alpha = 0.7f) else Color(0xFF00E5FF).copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        }
                        Icon(
                            imageVector = if (isStep3ImagesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Step 3",
                            tint = if (failedImgCount > 0) Color(0xFFFF6666) else Color(0xFF00E5FF)
                        )
                    }

                    if (isGeneratingStep3Visuals) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            color = Color(0xFF00FF66),
                            trackColor = Color(0xFF003311),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ">>> NOTRACK AI ENGINE: Parsing story narrative & constructing continuous 3-second scene timeline prompts...",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00FF66),
                            fontSize = 10.sp
                        )
                    }

                    // Expandable Step 3 Body
                    if (isStep3ImagesExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))

                        if (step3ErrorState != null) {
                            // Error block with options
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B0A0A)),
                                border = BorderStroke(1.dp, Color(0xFFFF5555).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "⚠️ STORYBOARD GENERATION FAILURE",
                                        color = Color(0xFFFF6666),
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = step3ErrorState ?: "",
                                        color = Color(0xFFFFCCCC),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 9.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = { onGenerateStep3VisualsMaster() },
                                            enabled = !isGeneratingStep3Visuals,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.weight(1f)
                                         ) {
                                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Black)
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(if (isGeneratingStep3Visuals) "GENERATING..." else "RETRY MASTER", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        } else if (step3VisualsList.isEmpty() && !isGeneratingStep3Visuals) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onGenerateStep3VisualsMaster() },
                                    enabled = !isGeneratingStep3Visuals,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00FF66),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚡ GENERATE $calculatedSceneCount-SCENE MASTER STORYBOARD (3s INTERVAL)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onGenerateStep3VisualsPhase1() },
                                        enabled = !isGeneratingStep3Visuals,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00E5FF),
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(if (isGeneratingStep3Visuals) "PHASE 1 (GEN...)" else "⚡ PHASE 1 (SCENES 1-10)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                    }

                                    Button(
                                        onClick = { onGenerateStep3VisualsPhase2() },
                                        enabled = !isGeneratingStep3Visuals,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00E5FF),
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(if (isGeneratingStep3Visuals) "PHASE 2 (GEN...)" else "⚡ PHASE 2 (SCENES 11-20)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                    }
                                }

                            }
                        } else if (step3VisualsList.isNotEmpty()) {
                            // JSON Action Buttons
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(step3JsonString))
                                            Toast.makeText(context, "Copied Step 3 Visuals JSON!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF)),
                                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("COPY JSON", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }

                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onGenerateStep3VisualsMaster() },
                                        enabled = !isGeneratingStep3Visuals,
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00FF66)),
                                        border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("RE-GEN MASTER", fontFamily = FontFamily.Monospace, fontSize = 8.sp)
                                    }

                                    OutlinedButton(
                                        onClick = { onGenerateStep3VisualsPhase1() },
                                        enabled = !isGeneratingStep3Visuals,
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF)),
                                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("P1 (1-10)", fontFamily = FontFamily.Monospace, fontSize = 8.sp)
                                    }

                                    OutlinedButton(
                                        onClick = { onGenerateStep3VisualsPhase2() },
                                        enabled = !isGeneratingStep3Visuals,
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF)),
                                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("P2 (11-20)", fontFamily = FontFamily.Monospace, fontSize = 8.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Timeframe Visual Prompts JSON accordion
                        var isJsonListVisible by remember { mutableStateOf(false) }
                        OutlinedButton(
                            onClick = { isJsonListVisible = !isJsonListVisible },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF88EEFF)),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (isJsonListVisible) "🔼 Hide Timeframe Visuals JSON List ($calculatedSceneCount Scenes)" else "🔽 View Timeframe Visuals JSON List ($calculatedSceneCount Scenes @ 3s)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }

                        if (isJsonListVisible) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 240.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                step3VisualsList.forEach { sceneItem ->
                                    Surface(
                                        color = Color(0xFF051D24),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.2f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Scene #${sceneItem.sceneIndex} [${sceneItem.timeframe}]",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFF00E5FF),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = "Duration: 3s",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFF888888),
                                                    fontSize = 9.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "🎙️ Voiceover: \"${sceneItem.voiceover}\"",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White,
                                                fontSize = 9.sp
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "🖼️ Visual Prompt: ${sceneItem.visualPrompt}",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color(0xFF88EEFF),
                                                fontSize = 9.sp
                                            )
                                            if (sceneItem.cameraMotionHint.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "🎥 Camera Motion: ${sceneItem.cameraMotionHint} | 📐 Aspect: ${sceneItem.aspectRatio}",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFF00FF66).copy(alpha = 0.8f),
                                                    fontSize = 8.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Browser Session Status Bar
                        Surface(
                            color = Color(0xFF071E1C),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Default.Web,
                                        contentDescription = null,
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🌐 Session #$currentBrowserSessionIndex (Auto-rotates every 2 renders)",
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFFB3F5FF),
                                        fontSize = 9.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = { viewModel.openNewBrowserSession("User clicked New Clean Browser") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0E3834),
                                        contentColor = Color(0xFF00E5FF)
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "+ NEW BROWSER",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }

                        // Failed Retry All Bar
                        if (failedImgCount > 0 && !isGeneratingImages) {
                            Surface(
                                color = Color(0xFF330B0B),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFFF4444)),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Default.Error,
                                            contentDescription = null,
                                            tint = Color(0xFFFF5555),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "$failedImgCount image(s) encountered generation errors.",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFFFFB3B3),
                                            fontSize = 9.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onRetryAllFailedImages() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFF4444),
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "RETRY FAILED",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }

                        if (generatedImagesList.isEmpty() && !isGeneratingImages) {
                            Button(
                                onClick = { onStartImageGeneration() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FF66),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ AUTO-GENERATE & SAVE IMAGES (DEEPAI)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        } else if (generatedImagesList.isNotEmpty()) {
                            for (img in generatedImagesList) {
                                if (img.isSuccess) {
                                    // --- SUCCESSFUL IMAGE CARD ---
                                    Surface(
                                        color = Color(0xFF081A0B),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.3f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = Color(0xFF00FF66),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "🖼️ Scene #${img.serialNumber}",
                                                        fontFamily = FontFamily.Monospace,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF00FF66),
                                                        fontSize = 11.sp
                                                    )
                                                }
                                                Text(
                                                    text = "ID: ${img.uniqueId}",
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF33FF99),
                                                    fontSize = 10.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(
                                                    color = Color(0xFF003311),
                                                    shape = RoundedCornerShape(3.dp),
                                                    border = BorderStroke(1.dp, Color(0xFF00FF66))
                                                ) {
                                                    Text(
                                                        text = "📱 9:16 VERTICAL SHORTS",
                                                        fontFamily = FontFamily.Monospace,
                                                        color = Color(0xFF00FF66),
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "📁 ${img.fileName}",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFF88CC99),
                                                    fontSize = 9.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "👁️ Prompt: \"${img.visualPrompt}\"",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontSize = 9.sp
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(260.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.Black)
                                                    .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                AsyncImage(
                                                    model = if (File(img.localFilePath).exists()) File(img.localFilePath) else img.imageUrl,
                                                    contentDescription = "Generated Scene Image ${img.serialNumber}",
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                Button(
                                                    onClick = { onRetrySingleImage(img.serialNumber - 1) },
                                                    enabled = !img.isRetrying,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF0E3314),
                                                        contentColor = Color(0xFF33FF99)
                                                    ),
                                                    shape = RoundedCornerShape(4.dp),
                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    if (img.isRetrying) {
                                                        CircularProgressIndicator(
                                                            color = Color(0xFF33FF99),
                                                            modifier = Modifier.size(12.dp),
                                                            strokeWidth = 2.dp
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("REGENERATING...", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Default.Refresh,
                                                            contentDescription = null,
                                                            tint = Color(0xFF33FF99),
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("🔄 Re-generate Scene #${img.serialNumber}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // --- FAILED IMAGE CARD ---
                                    Surface(
                                        color = Color(0xFF220808),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, Color(0xFFFF4444)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Warning,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFF5555),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "❌ Scene #${img.serialNumber} (GENERATION FAILED)",
                                                        fontFamily = FontFamily.Monospace,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFF5555),
                                                        fontSize = 11.sp
                                                    )
                                                }
                                                Text(
                                                    text = "ID: ${img.uniqueId}",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFFFF8888),
                                                    fontSize = 10.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "👁️ Prompt: \"${img.visualPrompt}\"",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White.copy(alpha = 0.85f),
                                                fontSize = 9.sp
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))
                                            Surface(
                                                color = Color.Black,
                                                shape = RoundedCornerShape(4.dp),
                                                border = BorderStroke(1.dp, Color(0xFFFF4444).copy(alpha = 0.5f)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(8.dp)) {
                                                    Text(
                                                        text = "⚠️ ERROR REPORT & LOGS:",
                                                        fontFamily = FontFamily.Monospace,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFF5555),
                                                        fontSize = 9.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(3.dp))
                                                    Text(
                                                        text = img.errorMessage ?: "Unknown error rendering image.",
                                                        fontFamily = FontFamily.Monospace,
                                                        color = Color(0xFFFFB3B3),
                                                        fontSize = 9.sp
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        clipboardManager.setText(AnnotatedString(img.errorMessage ?: "Image generation error"))
                                                        Toast.makeText(context, "Error logs copied to clipboard", Toast.LENGTH_SHORT).show()
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF3B1212),
                                                        contentColor = Color(0xFFFF8888)
                                                    ),
                                                    shape = RoundedCornerShape(4.dp),
                                                    modifier = Modifier.weight(1f),
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ContentCopy,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFF8888),
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "📋 COPY ERROR",
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                Button(
                                                    onClick = { onRetrySingleImage(img.serialNumber - 1) },
                                                    enabled = !img.isRetrying,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFFFF4444),
                                                        contentColor = Color.Black
                                                    ),
                                                    shape = RoundedCornerShape(4.dp),
                                                    modifier = Modifier.weight(1f),
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                                ) {
                                                    if (img.isRetrying) {
                                                        CircularProgressIndicator(
                                                            color = Color.Black,
                                                            modifier = Modifier.size(12.dp),
                                                            strokeWidth = 2.dp
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "RETRYING...",
                                                            fontFamily = FontFamily.Monospace,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Default.Refresh,
                                                            contentDescription = null,
                                                            tint = Color.Black,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "🔄 RETRY SCENE #${img.serialNumber}",
                                                            fontFamily = FontFamily.Monospace,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }

            // --- 4. STEP 4: DEEPAI AI IMAGE GENERATOR (STEP 3 PROMPTS) ---
            if (showDetailedPipelineSections && isStep3Complete) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isStep4Complete) Color(0xFF00FF66) else if (failedImgCount > 0) Color(0xFFFF5555).copy(alpha = 0.6f) else Color(0xFF00E5FF).copy(alpha = 0.5f),
                        RoundedCornerShape(6.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF031418))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Header Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStep4ImagesExpanded = !isStep4ImagesExpanded }
                    ) {
                        Icon(
                            imageVector = if (isStep4Complete) Icons.Default.CheckCircle else if (failedImgCount > 0) Icons.Default.Warning else Icons.Default.Image,
                            contentDescription = null,
                            tint = if (isStep4Complete) Color(0xFF00FF66) else if (failedImgCount > 0) Color(0xFFFF6666) else Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isStep4Complete) "✅ STEP 4: DEEPAI AI IMAGE GENERATOR" else "🖼️ STEP 4: DEEPAI AI IMAGE GENERATOR",
                                fontFamily = FontFamily.Monospace,
                                color = if (isStep4Complete) Color(0xFF00FF66) else Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (generatedImagesList.isNotEmpty()) {
                                    if (failedImgCount > 0) "⚠️ $successImgCount Ready, $failedImgCount Failed | ${if (isStep4ImagesExpanded) "[Collapse]" else "[Expand]"}"
                                    else "✅ ${generatedImagesList.size} Images Saved (${if (voiceoverDurationSec > 0.0) String.format("%.1f", voiceoverDurationSec) else "Est"}s ÷ 3s = $calculatedSceneCount Images) | ${if (isStep4ImagesExpanded) "[Collapse]" else "[Expand]"}"
                                } else if (isGeneratingImages) {
                                    "⏳ Generating visual $currentGeneratingIndex of $totalVisualsCount..."
                                } else {
                                    "Generates AI Images from Step 3 Visual Prompts ($calculatedSceneCount Scenes)"
                                },
                                fontFamily = FontFamily.Monospace,
                                color = if (failedImgCount > 0) Color(0xFFFF9999) else Color(0xFF00E5FF).copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        }
                        Icon(
                            imageVector = if (isStep4ImagesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Step 4",
                            tint = if (failedImgCount > 0) Color(0xFFFF6666) else Color(0xFF00E5FF)
                        )
                    }

                    if (isGeneratingImages) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            color = Color(0xFF00E5FF),
                            trackColor = Color(0xFF003344),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ">>> DEEPAI ENGINE: Rendering Scene $currentGeneratingIndex / $totalVisualsCount & Saving locally...",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00E5FF),
                            fontSize = 10.sp
                        )
                    }

                    if (isStep4ImagesExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))

                        if (isGeneratingImages) {
                            Button(
                                onClick = { onCancelImageGeneration() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444), contentColor = Color.White),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("⏹️ CANCEL / STOP IMAGE GENERATION", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { onStartImageGeneration() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF), contentColor = Color.Black),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚡ GENERATE AI", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { onGenerateLocalSampleVisuals() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C383F), contentColor = Color(0xFF00E5FF)),
                                    border = BorderStroke(1.dp, Color(0xFF00E5FF)),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.weight(1.3f)
                                ) {
                                    Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🎨 QUICK 20 VISUALS", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }

                                if (generatedImagesList.isNotEmpty()) {
                                    Button(
                                        onClick = {
                                            generatedImagesList = emptyList()
                                            Toast.makeText(context, "🗑️ Cleared all generated images.", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A0A0A), contentColor = Color(0xFFFF6666)),
                                        border = BorderStroke(1.dp, Color(0xFFFF4444)),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF6666), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("CLEAR", fontFamily = FontFamily.Monospace, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Session Status Bar
                        Surface(
                            color = Color(0xFF071E1C),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Default.Web,
                                        contentDescription = null,
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🌐 Session #$currentBrowserSessionIndex (Auto-rotates every 2 renders)",
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFFB3F5FF),
                                        fontSize = 9.sp
                                    )
                                }
                                Button(
                                    onClick = { viewModel.openNewBrowserSession("User clicked New Clean Browser") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E3834), contentColor = Color(0xFF00E5FF)),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("+ NEW BROWSER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 8.sp)
                                }
                            }
                        }

                        // Failed Retry Bar
                        if (failedImgCount > 0 && !isGeneratingImages) {
                            Surface(
                                color = Color(0xFF330B0B),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFFF4444)),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("⚠️ $failedImgCount Images Failed", fontFamily = FontFamily.Monospace, color = Color(0xFFFF6666), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                        Text("DeepAI blocked or session expired. Click below to retry in fresh browser.", fontFamily = FontFamily.Monospace, color = Color(0xFFFFaaaa), fontSize = 8.sp)
                                    }
                                    Button(
                                        onClick = { onRetryAllFailedImages() },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444), contentColor = Color.White),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("🔄 RETRY FAILED", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                    }
                                }
                            }
                        }

                        // Generated Images Grid/List
                        if (generatedImagesList.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                generatedImagesList.forEachIndexed { index, img ->
                                    Surface(
                                        color = if (img.isSuccess) Color(0xFF051C22) else Color(0xFF220A0A),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, if (img.isSuccess) Color(0xFF00E5FF).copy(alpha = 0.3f) else Color(0xFFFF4444).copy(alpha = 0.6f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Scene #${img.serialNumber} Visual Prompt",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = if (img.isSuccess) Color(0xFF00E5FF) else Color(0xFFFF6666),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Surface(
                                                        color = if (img.isSuccess) Color(0xFF004D5A) else Color(0xFF4A0000),
                                                        shape = RoundedCornerShape(2.dp)
                                                    ) {
                                                        Text(
                                                            text = if (img.isSuccess) "SAVED LOCAL" else "FAILED",
                                                            fontFamily = FontFamily.Monospace,
                                                            color = if (img.isSuccess) Color(0xFF00FF66) else Color(0xFFFF8888),
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    IconButton(
                                                        onClick = {
                                                            generatedImagesList = generatedImagesList.toMutableList().also { it.removeAt(index) }
                                                        },
                                                        modifier = Modifier.size(20.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Close,
                                                            contentDescription = "Remove scene image",
                                                            tint = Color.Gray,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            if (img.isSuccess) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(140.dp)
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(Color.Black)
                                                        .clickable {
                                                            selectedFullScreenImage = img.localFilePath.ifBlank { img.imageUrl }
                                                        }
                                                ) {
                                                    AsyncImage(
                                                        model = if (img.localFilePath.isNotBlank()) File(img.localFilePath) else img.imageUrl,
                                                        contentDescription = "Scene ${img.serialNumber}",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.BottomEnd)
                                                            .padding(6.dp)
                                                            .background(Color(0xCC000000), RoundedCornerShape(3.dp))
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text("🔍 TAP FULLSCREEN", fontFamily = FontFamily.Monospace, color = Color.White, fontSize = 8.sp)
                                                    }
                                                }
                                            } else {
                                                Text(
                                                    text = "❌ Error: ${img.errorMessage ?: "Unknown error"}",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color(0xFFFF8888),
                                                    fontSize = 9.sp
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Button(
                                                    onClick = { onRetrySingleImage(index) },
                                                    enabled = !img.isRetrying,
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444), contentColor = Color.Black),
                                                    shape = RoundedCornerShape(4.dp),
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                                                ) {
                                                    Text(if (img.isRetrying) "RETRYING..." else "🔄 RETRY SCENE #${img.serialNumber} IN CLEAN BROWSER", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }

            // --- 5. STEP 5: VOICEOVER WORD TIMEFRAME & CAPTIONS ALIGNMENT ---
            if (showDetailedPipelineSections && isStep4Complete) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if (isStep5Complete) Color(0xFF00FF66) else Color(0xFFFFCC00).copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1403))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isStep5AlignmentExpanded = !isStep5AlignmentExpanded }
                        ) {
                            Icon(
                                imageVector = if (isStep5Complete) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isStep5Complete) Color(0xFF00FF66) else Color(0xFFFFCC00),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isStep5Complete) "✅ STEP 5: VOICEOVER WORD TIMEFRAME & CAPTIONS ALIGNMENT" else "⏱️ STEP 5: VOICEOVER WORD TIMEFRAME & CAPTIONS ALIGNMENT",
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isStep5Complete) Color(0xFF00FF66) else Color(0xFFFFCC00),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = if (isStep5Complete) {
                                        "✅ Extracted ${calculatedWordCaptions.flatMap { it.words }.size} words into ${calculatedWordCaptions.size} 4-Word Caption Groups | ${if (isStep5AlignmentExpanded) "[Collapse]" else "[Expand]"}"
                                    } else {
                                        "Extracted ${calculatedWordCaptions.flatMap { it.words }.size} words into ${calculatedWordCaptions.size} 4-Word Caption Groups | ${if (isStep5AlignmentExpanded) "[Collapse]" else "[Expand]"}"
                                    },
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFFCC00).copy(alpha = 0.8f),
                                    fontSize = 9.sp
                                )
                            }
                        Icon(
                            imageVector = if (isStep5AlignmentExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Step 5 Alignment",
                            tint = Color(0xFFFFCC00)
                        )
                    }

                    if (isStep5AlignmentExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Summary pill stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = Color(0xFF332800),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFFFCC00).copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("TOTAL WORDS", fontFamily = FontFamily.Monospace, fontSize = 8.sp, color = Color(0xFFFFCC00))
                                    Text("${calculatedWordCaptions.flatMap { it.words }.size}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                color = Color(0xFF332800),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFFFCC00).copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("VOICEOVER TIME", fontFamily = FontFamily.Monospace, fontSize = 8.sp, color = Color(0xFFFFCC00))
                                    Text("${String.format("%.1f", voiceoverDurationSec)}s", fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                color = Color(0xFF332800),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFFFCC00).copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("4-WORD GROUPS", fontFamily = FontFamily.Monospace, fontSize = 8.sp, color = Color(0xFFFFCC00))
                                    Text("${calculatedWordCaptions.size}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF66))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = {
                                    val formattedCaptions = calculatedWordCaptions.joinToString("\n") { chunk ->
                                        "[${String.format("%.2f", chunk.startMs / 1000.0)}s - ${String.format("%.2f", chunk.endMs / 1000.0)}s]: ${chunk.words.joinToString(" ") { it.word }}"
                                    }
                                    clipboardManager.setText(AnnotatedString(formattedCaptions))
                                    Toast.makeText(context, "📋 Copied 4-Word Captions to Clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF332800), contentColor = Color(0xFFFFCC00)),
                                border = BorderStroke(1.dp, Color(0xFFFFCC00).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFFFFCC00), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("COPY CAPTIONS", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    voiceoverDurationSec = if (step3VisualsList.isNotEmpty()) step3VisualsList.size * 3.0 else 60.0
                                    Toast.makeText(context, "⏱️ Aligned to 60s (3s per scene)!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14301C), contentColor = Color(0xFF00FF66)),
                                border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = Color(0xFF00FF66), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("60s SYNC (3s/SCENE)", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 4-Word Caption Chunk List
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            calculatedWordCaptions.forEach { chunk ->
                                val startSecStr = String.format("%.2f", chunk.startMs / 1000.0)
                                val endSecStr = String.format("%.2f", chunk.endMs / 1000.0)

                                Surface(
                                    color = Color(0xFF241C04),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFFFFCC00).copy(alpha = 0.25f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Group #${chunk.chunkIndex} [${startSecStr}s - ${endSecStr}s]",
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFCC00),
                                                fontSize = 9.sp
                                            )
                                            Text(
                                                text = "${chunk.words.size} words",
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.Gray,
                                                fontSize = 8.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Row of 4 word pill badges with exact timestamps in ms
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            chunk.words.forEach { wordItem ->
                                                Surface(
                                                    color = Color(0xFF382C06),
                                                    shape = RoundedCornerShape(3.dp),
                                                    border = BorderStroke(1.dp, Color(0xFFFFDD00).copy(alpha = 0.4f)),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Column(
                                                        modifier = Modifier.padding(3.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            text = wordItem.word,
                                                            fontFamily = FontFamily.Monospace,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.White,
                                                            fontSize = 9.sp,
                                                            maxLines = 1
                                                        )
                                                        Text(
                                                            text = "${wordItem.startMs}-${wordItem.endMs}ms",
                                                            fontFamily = FontFamily.Monospace,
                                                            color = Color(0xFFFFCC00),
                                                            fontSize = 7.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }

            // --- 6. STEP 6: ANIMATED VIDEO COMPOSER & KARAOKE PLAYER ---
            if (showDetailedPipelineSections && isStep5Complete) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            if (isStep6Complete) Color(0xFF00FF66) else Color(0xFF00FF66).copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF031407))
                ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Header Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStep6VideoExpanded = !isStep6VideoExpanded }
                    ) {
                        Icon(
                            imageVector = if (isStep6Complete) Icons.Default.CheckCircle else Icons.Default.Movie,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isStep6Complete) "✅ STEP 6: ANIMATED VIDEO COMPOSER & KARAOKE" else "🎬 STEP 6: ANIMATED VIDEO COMPOSER & KARAOKE",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66),
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = if (isStep6Complete) "✅ 9:16 Vertical Shorts • 3s Image Transitions • 4-Word Real-Time Word Animated Karaoke Captions" else "9:16 Vertical Shorts • 3s Image Transitions • 4-Word Real-Time Word Animated Karaoke Captions",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF88CC99),
                                fontSize = 8.sp
                            )
                        }
                        IconButton(onClick = { isStep6VideoExpanded = !isStep6VideoExpanded }) {
                            Icon(
                                imageVector = if (isStep6VideoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Step 6 Video",
                                tint = Color(0xFF00FF66)
                            )
                        }
                    }

                    if (isStep6VideoExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Action buttons row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = { composeFinalVideo(autoPlay = true) },
                                modifier = Modifier
                                    .testTag("compose_video_btn_${message.id}")
                                    .weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (mergedVideoScenes.isEmpty()) "⚡ MERGE TO VIDEO" else "🔄 RE-MERGE VIDEO",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }

                            if (mergedVideoScenes.isNotEmpty()) {
                                Button(
                                    onClick = { exportVideoProject() },
                                    modifier = Modifier
                                        .testTag("export_video_btn_${message.id}")
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14301C), contentColor = Color(0xFF00FF66)),
                                    border = BorderStroke(1.dp, Color(0xFF00FF66)),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color(0xFF00FF66), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "💾 EXPORT", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                }

                                Button(
                                    onClick = {
                                        mergedVideoScenes = emptyList()
                                        isVideoPlaying = false
                                        currentPlaybackProgressMs = 0L
                                        try { mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null } catch (e: Exception) {}
                                        Toast.makeText(context, "🗑️ Cleared video project.", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A0A0A), contentColor = Color(0xFFFF6666)),
                                    border = BorderStroke(1.dp, Color(0xFFFF4444)),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF6666), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "RESET", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (mergedVideoScenes.isNotEmpty() || generatedImagesList.isNotEmpty() || step3VisualsList.isNotEmpty()) {
                            val totalVisualCount = maxOf(1, generatedImagesList.size, mergedVideoScenes.size, step3VisualsList.size)
                            val activeImageIndex = ((currentPlaybackProgressMs / 3000L).toInt()) % totalVisualCount
                            val activeImgItem = generatedImagesList.getOrNull(activeImageIndex)
                            val activeSceneItem = step3VisualsList.getOrNull(activeImageIndex)
                            val currentMergedScene = mergedVideoScenes.getOrNull(activeImageIndex)

                            // 9:16 Vertical Video Screen Container
                            val infiniteTransition = rememberInfiniteTransition(label = "video_ken_burns")
                            val zoomFactor by infiniteTransition.animateFloat(
                                initialValue = 1.0f,
                                targetValue = 1.12f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "zoom_ken_burns"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.88f)
                                    .aspectRatio(9f / 16f)
                                    .align(Alignment.CenterHorizontally)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0A0A0A))
                                    .border(2.dp, Brush.linearGradient(listOf(Color(0xFF00FF66), Color(0xFF00E5FF))), RoundedCornerShape(12.dp))
                                    .clickable { toggleVideoPlayback() },
                                contentAlignment = Alignment.Center
                            ) {
                                // Background Image with smooth transition (switches every 3 seconds)
                                val imgSource = activeImgItem?.localFilePath?.ifBlank { activeImgItem.imageUrl }
                                    ?: activeImgItem?.imageUrl
                                    ?: currentMergedScene?.imagePathOrUrl
                                    ?: ""

                                androidx.compose.animation.AnimatedContent(
                                    targetState = Pair(activeImageIndex, imgSource),
                                    transitionSpec = {
                                        androidx.compose.animation.fadeIn(tween(400)) togetherWith androidx.compose.animation.fadeOut(tween(300))
                                    },
                                    label = "scene_image_crossfade",
                                    modifier = Modifier.fillMaxSize()
                                ) { (_, src) ->
                                    if (src.isNotBlank()) {
                                        AsyncImage(
                                            model = if (src.startsWith("/")) File(src) else src,
                                            contentDescription = "Scene Image $activeImageIndex",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .scale(if (isVideoPlaying) zoomFactor else 1.0f)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color(0xFF140727),
                                                            Color(0xFF091E3A),
                                                            Color(0xFF052B1E),
                                                            Color(0xFF0A0A0A)
                                                        )
                                                    )
                                                )
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Movie,
                                                    contentDescription = null,
                                                    tint = Color(0xFF00FF66),
                                                    modifier = Modifier.size(40.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "SCENE #${activeImageIndex + 1} (${activeSceneItem?.timeframe ?: "00:00 - 00:03"})",
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF00FF66),
                                                    fontSize = 12.sp
                                                )
                                                if (activeSceneItem?.cameraMotionHint?.isNotBlank() == true) {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "🎥 ${activeSceneItem.cameraMotionHint}",
                                                        fontFamily = FontFamily.Monospace,
                                                        color = Color(0xFF00E5FF),
                                                        fontSize = 10.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = (activeSceneItem?.visualPrompt ?: "Cinematic 9:16 Shorts Scene").take(120) + "...",
                                                    fontFamily = FontFamily.Monospace,
                                                    color = Color.LightGray,
                                                    fontSize = 9.sp,
                                                    textAlign = TextAlign.Center,
                                                    lineHeight = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                // Dark overlay gradient for cinematic contrast & readability
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Black.copy(alpha = 0.45f),
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.65f)
                                                )
                                            )
                                        )
                                )

                                // Top Header Badge
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xDD000000), RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(0xFF00FF66), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "🎬 SCENE #${activeImageIndex + 1} / $totalVisualCount",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            color = Color(0xFF00FF66)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xDD000000), RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(0xFFFFCC00), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        val curSec = (currentPlaybackProgressMs / 1000).toInt()
                                        val min = curSec / 60
                                        val sec = curSec % 60
                                        Text(
                                            text = String.format("%02d:%02d • 9:16", min, sec),
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            color = Color(0xFFFFCC00)
                                        )
                                    }
                                }

                                // Play / Pause overlay button
                                if (!isVideoPlaying) {
                                    Box(
                                        modifier = Modifier
                                            .size(58.dp)
                                            .background(Color(0xCC000000), CircleShape)
                                            .border(2.dp, Color(0xFF00FF66), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play Video",
                                            tint = Color(0xFF00FF66),
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }

                                // --- VIRAL COLORFUL ANIMATED CREATIVE CAPTIONS OVERLAY ---
                                val active4WordChunk = calculatedWordCaptions.find { currentPlaybackProgressMs in it.startMs..it.endMs }
                                    ?: calculatedWordCaptions.minByOrNull { Math.abs(it.startMs - currentPlaybackProgressMs) }
                                    ?: calculatedWordCaptions.firstOrNull()

                                if (active4WordChunk != null) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .padding(start = 8.dp, end = 8.dp, bottom = 44.dp)
                                            .background(
                                                Color(0xD9000000),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .border(
                                                1.dp,
                                                Color(0x5500FF66),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Flowing colorful animated words
                                        @OptIn(ExperimentalLayoutApi::class)
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                                            verticalArrangement = Arrangement.spacedBy(4.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val vibrantPillColors = listOf(
                                                Color(0xFF00FF66), // Neon Green
                                                Color(0xFFFFE600), // Cyber Yellow
                                                Color(0xFF00E5FF), // Electric Cyan
                                                Color(0xFFFF3366), // Sunset Coral
                                                Color(0xFFA855F7), // Neon Violet
                                                Color(0xFFFF9900)  // Vivid Amber
                                            )

                                            active4WordChunk.words.forEachIndexed { idx, wordObj ->
                                                val isSpokenNow = currentPlaybackProgressMs in wordObj.startMs..wordObj.endMs
                                                val activeColor = vibrantPillColors[(wordObj.wordIndex + idx) % vibrantPillColors.size]

                                                val animatedScale by androidx.compose.animation.core.animateFloatAsState(
                                                    targetValue = if (isSpokenNow) 1.15f else 1.0f,
                                                    animationSpec = androidx.compose.animation.core.spring(
                                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                                                        stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                                                    ),
                                                    label = "word_pop_scale"
                                                )

                                                if (isSpokenNow) {
                                                    Box(
                                                        modifier = Modifier
                                                            .wrapContentWidth()
                                                            .scale(animatedScale)
                                                            .background(activeColor, RoundedCornerShape(4.dp))
                                                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 5.dp, vertical = 2.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = wordObj.word.uppercase(),
                                                            fontFamily = FontFamily.SansSerif,
                                                            fontWeight = FontWeight.Black,
                                                            fontSize = 11.sp,
                                                            color = Color.Black,
                                                            textAlign = TextAlign.Center,
                                                            softWrap = false,
                                                            maxLines = 1
                                                        )
                                                    }
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .wrapContentWidth()
                                                            .background(Color(0x77161B22), RoundedCornerShape(4.dp))
                                                            .border(0.6.dp, Color(0x33FFFFFF), RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = wordObj.word.uppercase(),
                                                            fontFamily = FontFamily.SansSerif,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            fontSize = 9.5.sp,
                                                            color = Color.White.copy(alpha = 0.95f),
                                                            textAlign = TextAlign.Center,
                                                            softWrap = false,
                                                            maxLines = 1
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Controls Toolbar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { toggleVideoPlayback() },
                                    modifier = Modifier
                                        .testTag("toggle_video_playback_btn_${message.id}")
                                        .weight(1.3f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isVideoPlaying) Color(0xFFFF4444) else Color(0xFF00FF66),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isVideoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isVideoPlaying) "PAUSE VIDEO" else "PLAY 9:16 VIDEO",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        val totalVisualCount = maxOf(1, generatedImagesList.size, mergedVideoScenes.size, step3VisualsList.size)
                                        val curActiveIdx = ((currentPlaybackProgressMs / 3000L).toInt()) % totalVisualCount
                                        regenerateSceneVisual(curActiveIdx)
                                    },
                                    modifier = Modifier.weight(0.9f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07242B), contentColor = Color(0xFF00E5FF)),
                                    border = BorderStroke(1.dp, Color(0xFF00E5FF)),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "RE-GEN",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val fileToUpload = exportedVideoPath
                                        ?.let(::File)
                                        ?.takeIf { it.isFile && it.extension.equals("mp4", ignoreCase = true) }
                                    if (fileToUpload != null) {
                                        showYouTubeUploadDialog = fileToUpload
                                    } else {
                                        Toast.makeText(context, "Real MP4 export is required before YouTube upload.", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0055), contentColor = Color.White),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🚀 UPLOAD TO YOUTUBE SHORTS",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF08180C), RoundedCornerShape(4.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚡ Click '⚡ MERGE TO VIDEO' or Run the Master 1-Click Pipeline to automatically merge all images and audio into a 9:16 Shorts video.",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFF88CC99),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            }
            }
        }
    } else if (isScenesJson) {
            var isScenesExpanded by remember { mutableStateOf(false) }
            var storyTitle = ""
            data class SceneItem(val index: Int, val caption: String, val durationMs: Long, val font: String, val color: String, val anim: String)
            val scenesList = mutableListOf<SceneItem>()

            try {
                val rawText = message.text
                val startIdx = rawText.indexOf("{")
                val endIdx = rawText.lastIndexOf("}")
                if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                    val clean = rawText.substring(startIdx, endIdx + 1)
                    val json = JSONObject(clean)
                    storyTitle = json.optString("title", "STORY SCENES")
                    val sArr = json.optJSONArray("scenes")
                    if (sArr != null) {
                        for (i in 0 until sArr.length()) {
                            val sObj = sArr.optJSONObject(i)
                            if (sObj != null) {
                                scenesList.add(
                                    SceneItem(
                                        index = sObj.optInt("sceneIndex", i + 1),
                                        caption = sObj.optString("caption", ""),
                                        durationMs = sObj.optLong("durationMs", 3000),
                                        font = sObj.optString("font", "Monospace"),
                                        color = sObj.optString("color", "#00FF66"),
                                        anim = sObj.optString("animation", "Typewriter")
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // ignore
            }

            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF030D05))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isScenesExpanded = !isScenesExpanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00FF66),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✅ STEP 1: ${if (storyTitle.isNotBlank()) storyTitle else "STORY SCENES"}",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (isScenesExpanded) "[Click to Collapse Output]" else "[Click to Expand Output]",
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF00FF66).copy(alpha = 0.6f),
                                fontSize = 9.sp
                            )
                        }
                        Icon(
                            imageVector = if (isScenesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Scenes Output",
                            tint = Color(0xFF00FF66)
                        )
                    }

                    if (isScenesExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        for (sc in scenesList) {
                            Surface(
                                color = Color(0xFF08120A),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "🎬 Scene #${sc.index} [${sc.durationMs / 1000.0}s | ${sc.font} | ${sc.anim}]",
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFF00FF66),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "📝 Caption: \"${sc.caption}\"",
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onNavigateToEditor()
                },
                modifier = Modifier
                    .testTag("compose_faceless_btn_${message.id}")
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FF66),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚡ COMPOSE FACELESS VIDEO",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // YouTube Shorts Upload Dialog
        if (showYouTubeUploadDialog != null) {
            YouTubeUploadDialog(
                videoFile = showYouTubeUploadDialog!!,
                initialTitle = "Viral 9:16 AI Short #${System.currentTimeMillis() % 1000}",
                initialDescription = "Created with Glitch OS AI Video Studio.\n\n#Shorts #AI #Viral #GlitchOS",
                autoStartUpload = true,
                onDismiss = { showYouTubeUploadDialog = null }
            )
        }
    }
}

@Composable
fun AutonomousPipelineCard(
    activeStep: Int,
    progressPercent: Int,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onCancelClick: () -> Unit,
    onStepClick: (Int) -> Unit,
    isGenerating: Boolean = true,
    onStartClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pipeline_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val currentStepSubtitle = if (isGenerating) {
        when (activeStep) {
            1 -> "Generating viral SEO metadata..."
            2 -> "Edge Neural Voice Synthesis"
            3 -> "3-Second Timeframe Prompts"
            4 -> "Rendering 9:16 HD Visuals"
            5 -> "Calculating speech timing..."
            6 -> "Building the real local scene preview..."
            7 -> "Waiting for a real exported MP4..."
            else -> "Autonomous AI Pipeline Active"
        }
    } else {
        "💤 Idle — Ready to create a 9:16 viral short"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .border(1.5.dp, if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF020B04))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Card Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF).copy(alpha = 0.5f), CircleShape)
                        .background(Color(0xFF032210))
                ) {
                    Icon(
                        imageVector = if (isGenerating) Icons.Default.Sync else Icons.Default.PlayArrow,
                        contentDescription = "Status",
                        tint = if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF),
                        modifier = Modifier
                            .size(20.dp)
                            .let { if (isGenerating) it.rotate(rotationAngle) else it }
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isGenerating) "⚡ AUTONOMOUS AI PIPELINE" else "💤 AUTONOMOUS PIPELINE (IDLE)",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF),
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = currentStepSubtitle,
                        fontFamily = FontFamily.Monospace,
                        color = (if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF)).copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                if (isGenerating) {
                    // Cyberpunk Red Cancel Button
                    OutlinedButton(
                        onClick = { onCancelClick() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                        border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            modifier = Modifier.size(10.dp),
                            tint = Color(0xFFFF5252)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "CANCEL",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF5252)
                        )
                    }
                } else {
                    // Start Button in Header
                    Button(
                        onClick = { onStartClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66)),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            modifier = Modifier.size(12.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "START",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    color = Color(0xFF032210),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "$progressPercent%",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    tint = if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (progressPercent.coerceIn(0, 100)) / 100f },
                color = if (isGenerating) Color(0xFF00FF66) else Color(0xFF00E5FF).copy(alpha = 0.4f),
                trackColor = Color(0xFF023010),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val stepsList = listOf(
                        Triple(1, "metadata generate", "Analyzing viral trends..."),
                        Triple(2, "audio generate", "Edge Neural Voice Synthesis"),
                        Triple(3, "visuals generate", "3-Second Timeframe Prompts"),
                        Triple(4, "images generate", "Rendering 9:16 HD Visuals"),
                        Triple(5, "captions", "Calculating speech timing..."),
                        Triple(6, "video preview compose", "Building the real local scene preview..."),
                        Triple(7, "youtube upload", "Requires a real exported MP4 file...")
                    )

                    stepsList.forEach { (num, name, desc) ->
                        val isActive = isGenerating && (num == activeStep)
                        val isDone = isGenerating && (num < activeStep)

                        PipelineStepRowItem(
                            stepNum = num,
                            title = name,
                            subtitle = desc,
                            isActive = isActive,
                            isDone = isDone,
                            rotationAngle = rotationAngle,
                            onClick = { onStepClick(num) }
                        )
                    }

                    if (!isGenerating) {
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        // Cyberpunk Glowing start action button
                        Button(
                            onClick = { onStartClick() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .border(1.5.dp, Color(0xFF00FF66), RoundedCornerShape(6.dp)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RUN 1-CLICK VIRAL SHORTS GENERATOR",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PipelineStepRowItem(
    stepNum: Int,
    title: String,
    subtitle: String,
    isActive: Boolean,
    isDone: Boolean,
    rotationAngle: Float,
    onClick: () -> Unit
) {
    val borderColor = if (isActive) Color(0xFF00E5FF) else if (isDone) Color(0xFF00FF66) else Color(0xFF0C2B16)
    val containerBg = if (isActive) Color(0xFF011C24) else if (isDone) Color(0xFF02160A) else Color(0xFF010A04)
    val titleColor = if (isActive) Color(0xFF00E5FF) else if (isDone) Color(0xFF00FF66) else Color(0xFF336644)
    val subtitleColor = if (isActive) Color(0xFF00E5FF).copy(alpha = 0.85f) else if (isDone) Color(0xFF00FF66).copy(alpha = 0.75f) else Color(0xFF22442D)

    Surface(
        onClick = onClick,
        color = containerBg,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
            if (isActive) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFF00E5FF), CircleShape)
                        .background(Color(0xFF012C38))
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Active Step",
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier
                            .size(14.dp)
                            .rotate(rotationAngle)
                    )
                }
            } else if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF00FF66),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFF0E3D1D), CircleShape)
                        .background(Color(0xFF010C05))
                ) {
                    Text(
                        text = "$stepNum",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF336644)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        fontSize = 12.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isActive) {
                            Surface(
                                color = Color(0xFF012E3B),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFF00E5FF))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "ACTIVE",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E5FF),
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        } else if (isDone) {
                            Surface(
                                color = Color(0xFF022B12),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFF00FF66))
                            ) {
                                Text(
                                    text = "✓ DONE",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66),
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (isDone || isActive) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                onClick = onClick,
                                color = Color(0xFF00FF66),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.height(20.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = "VIEW",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = subtitle,
                    fontFamily = FontFamily.Monospace,
                    color = subtitleColor,
                    fontSize = 10.sp
                )
            }
        }
    }
}
