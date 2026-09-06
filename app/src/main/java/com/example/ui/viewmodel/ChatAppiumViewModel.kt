package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.AppiumLogEntity
import com.example.data.model.AppiumElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "User" or "ChatGPT"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatAppiumViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val logDao = db.appiumLogDao()

    val logsState: StateFlow<List<AppiumLogEntity>> = logDao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage(
            sender = "NoTrack AI",
            text = """
                === GLITCH_OS v2.6 // AI TERMINAL ONLINE ===
                📡 SYSTEM STATUS: SECURED & OPERATIONAL
                🔗 FIRESTORE SYNC: CO-PILOT TUNNEL ACTIVE
                
                Welcome, Operator. The fully autonomous AI short video generation pipeline is ready.
                
                👉 TO BEGIN:
                - Click the green 🎬 Video icon next to the RUN button (bottom right) to automatically generate a viral storyboard.
                - Or, type your custom script / story theme prompt and click RUN.
                
                ⚙️ SETTINGS & TOOLS:
                - Open the top-right menu (⋮) to configure "Video Agent Settings" (Length, Language, Category, Auto-Loop intervals synced to Firestore), inspect Appium logs, or clear system cache.
                ============================================
            """.trimIndent()
        )
    ))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _currentPrompt = MutableStateFlow("")
    val currentPrompt: StateFlow<String> = _currentPrompt.asStateFlow()

    private val _webUrl = MutableStateFlow("https://notrack.ai/chat")
    val webUrl: StateFlow<String> = _webUrl.asStateFlow()

    private val _isWebViewMode = MutableStateFlow(false)
    val isWebViewMode: StateFlow<Boolean> = _isWebViewMode.asStateFlow()

    private val _isAutomating = MutableStateFlow(false)
    val isAutomating: StateFlow<Boolean> = _isAutomating.asStateFlow()

    private val _lastAutomationStatus = MutableStateFlow("Initializing automatic Appium & DOM JSON pipeline...")
    val lastAutomationStatus: StateFlow<String> = _lastAutomationStatus.asStateFlow()

    private val _extractedDomJson = MutableStateFlow("")
    val extractedDomJson: StateFlow<String> = _extractedDomJson.asStateFlow()

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    // NoTrack AI custom background execution bridge
    private val _noTrackPromptEvent = MutableStateFlow<String?>(null)
    val noTrackPromptEvent: StateFlow<String?> = _noTrackPromptEvent.asStateFlow()

    fun consumeNoTrackPromptEvent() {
        _noTrackPromptEvent.value = null
    }

    private var onNoTrackResponseCallback: ((String) -> Unit)? = null

    fun registerNoTrackResponseCallback(callback: (String) -> Unit) {
        onNoTrackResponseCallback = callback
    }

    fun onNoTrackResponseScraped(text: String) {
        onNoTrackResponseCallback?.invoke(text)
    }

    // Step 2: DeepAI Chat Image Generation Bridge & Browser Session Rotation
    private val _deepAiImagePromptEvent = MutableStateFlow<String?>(null)
    val deepAiImagePromptEvent: StateFlow<String?> = _deepAiImagePromptEvent.asStateFlow()

    private val _browserSessionIndex = MutableStateFlow(1)
    val browserSessionIndex: StateFlow<Int> = _browserSessionIndex.asStateFlow()

    private val _browserResetTrigger = MutableStateFlow<String?>(null)
    val browserResetTrigger: StateFlow<String?> = _browserResetTrigger.asStateFlow()

    fun openNewBrowserSession(reason: String = "Manual / Automatic Session Rotation") {
        _browserSessionIndex.value += 1
        _browserResetTrigger.value = "Session #${_browserSessionIndex.value} - $reason"
    }

    fun consumeBrowserResetTrigger() {
        _browserResetTrigger.value = null
    }

    fun consumeDeepAiImagePromptEvent() {
        _deepAiImagePromptEvent.value = null
    }

    private var onDeepAiResponseCallback: ((String) -> Unit)? = null

    fun registerDeepAiResponseCallback(callback: (String) -> Unit) {
        onDeepAiResponseCallback = callback
    }

    fun onDeepAiResponseScraped(text: String) {
        onDeepAiResponseCallback?.invoke(text)
    }

    suspend fun generateDeepAiImageForPrompt(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKeyVal = _apiKey.value.trim()

        // Ensure prompt is formatted specifically for 9:16 Vertical YouTube Shorts / Reels
        val verticalPrompt = if (prompt.contains("9:16", ignoreCase = true) || prompt.contains("vertical", ignoreCase = true)) {
            prompt
        } else {
            "$prompt, 9:16 vertical portrait aspect ratio, full height vertical mobile composition for YouTube Shorts, 8k resolution, cinematic lighting, ultra-detailed photorealistic"
        }

        // 1. Direct Official DeepAI Engine (if API Key provided)
        if (apiKeyVal.isNotEmpty() && !apiKeyVal.startsWith("quickstart-") && (_apiProvider.value.contains("DeepAI") || apiKeyVal.length > 20)) {
            try {
                val url = java.net.URL("https://api.deepai.org/api/text2img")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("api-key", apiKeyVal)
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.doOutput = true
                conn.connectTimeout = 15000
                conn.readTimeout = 20000

                val postData = "text=" + java.net.URLEncoder.encode(verticalPrompt, "UTF-8") + "&grid_size=1&image_generator_version=hd"
                conn.outputStream.use { os ->
                    os.write(postData.toByteArray(Charsets.UTF_8))
                }

                val code = conn.responseCode
                if (code == 200) {
                    val resp = conn.inputStream.bufferedReader().readText()
                    val json = JSONObject(resp)
                    val outUrl = json.optString("output_url", "")
                    if (outUrl.isNotBlank() && outUrl.startsWith("http")) {
                        return@withContext outUrl
                    }
                }
            } catch (e: Exception) {
                Log.w("DeepAI", "Official API call failed, trying WebView & Fallback engine", e)
            }
        }

        // 2. DeepAI Browser & DOM Automation Bridge (deepai.org/chat)
        var webViewResult = ""
        try {
            val responseDeferred = kotlinx.coroutines.CompletableDeferred<String>()
            registerDeepAiResponseCallback { response ->
                if (!responseDeferred.isCompleted) {
                    responseDeferred.complete(response)
                }
            }
            _deepAiImagePromptEvent.value = verticalPrompt

            webViewResult = kotlinx.coroutines.withTimeoutOrNull(10000L) {
                responseDeferred.await()
            } ?: ""
            
            if (webViewResult.startsWith("http://") || webViewResult.startsWith("https://")) {
                return@withContext webViewResult
            }
        } catch (e: Exception) {
            Log.w("DeepAI", "WebView automation notice: ${e.localizedMessage}")
        }

        // 3. High-Quality 9:16 Cinematic Neural Image Engine (Pollinations Fast Turbo / Photorealistic Engine)
        // Generates instant, photorealistic, 9:16 vertical images reliably
        try {
            val cleanPrompt = verticalPrompt.take(280)
            val seed = (100000..999999).random()
            val encodedPrompt = java.net.URLEncoder.encode(cleanPrompt, "UTF-8")
            val pollinationsUrl = "https://image.pollinations.ai/prompt/$encodedPrompt?width=720&height=1280&nologo=true&seed=$seed"
            return@withContext pollinationsUrl
        } catch (e: Exception) {
            val fallbackSeed = (1000..9999).random()
            return@withContext "https://image.pollinations.ai/prompt/${java.net.URLEncoder.encode(prompt.take(150), "UTF-8")}?width=720&height=1280&nologo=true&seed=$fallbackSeed"
        }
    }

    private val _apiProvider = MutableStateFlow("ChatGPT (OpenAI API)") // "ChatGPT (OpenAI API)" or "Gemini API"
    val apiProvider: StateFlow<String> = _apiProvider.asStateFlow()

    private val _isLiveConnected = MutableStateFlow(false)
    val isLiveConnected: StateFlow<Boolean> = _isLiveConnected.asStateFlow()

    private val _isApiCalling = MutableStateFlow(false)
    val isApiCalling: StateFlow<Boolean> = _isApiCalling.asStateFlow()

    fun setWebUrl(newUrl: String) {
        _webUrl.value = newUrl.trim()
        logAction(
            actionName = "switch_web_url",
            locator = "web_url_field",
            payload = "Switched target Web Inspector URL to: $newUrl",
            status = "SUCCESS"
        )
    }

    fun setApiKey(key: String, provider: String = "ChatGPT (OpenAI API)") {
        _apiKey.value = key.trim()
        _apiProvider.value = provider
        _isLiveConnected.value = key.trim().isNotEmpty()
        logAction(
            actionName = "set_api_key",
            locator = "settings_api_key_field",
            payload = "API key updated for $provider (Length: ${key.length})",
            status = "SUCCESS"
        )
    }

    val appiumInspectorElements = listOf(
        AppiumElement(
            name = "Composer Plus Button",
            testTag = "composer_plus_btn",
            resourceId = "composer-plus-btn",
            xpath = "//button[@data-testid='composer-plus-btn']",
            accessibilityId = "Add files and more",
            description = "ChatGPT file upload / action menu button",
            actionType = "click"
        ),
        AppiumElement(
            name = "Prompt Textarea / ProseMirror",
            testTag = "prompt_textarea",
            resourceId = "prompt-textarea",
            xpath = "//div[@id='prompt-textarea' and @role='textbox']",
            accessibilityId = "Chat with ChatGPT",
            description = "Main contenteditable prompt input container for ChatGPT",
            actionType = "type_text"
        ),
        AppiumElement(
            name = "Composer Submit / Send Button",
            testTag = "send_button",
            resourceId = "composer-submit-button",
            xpath = "//button[@data-testid='send-button' or @id='composer-submit-button']",
            accessibilityId = "Send prompt",
            description = "Main prompt dispatch button for ChatGPT",
            actionType = "click"
        ),
        AppiumElement(
            name = "Mode Toggle Switch",
            testTag = "mode_toggle_switch",
            resourceId = "com.aistudio.chatgpthelper.appium:id/mode_toggle_switch",
            xpath = "//android.widget.Switch[@content-desc='mode_toggle_switch']",
            accessibilityId = "mode_toggle_switch",
            description = "Toggle between Native Interactive Screen and Live ChatGPT.com WebView",
            actionType = "click"
        ),
        AppiumElement(
            name = "Clear Chat Button",
            testTag = "clear_chat_button",
            resourceId = "com.aistudio.chatgpthelper.appium:id/clear_chat_button",
            xpath = "//android.widget.IconButton[@content-desc='clear_chat_button']",
            accessibilityId = "clear_chat_button",
            description = "Resets conversation and clears chat history",
            actionType = "click"
        ),
        AppiumElement(
            name = "Pixelbin Video Generator Prompt",
            testTag = "pixelbin_video_prompt",
            resourceId = "pixelbin-prompt-input",
            xpath = "//textarea[contains(@placeholder, 'video') or @name='prompt']",
            accessibilityId = "Pixelbin Prompt Input",
            description = "Pixelbin AI Video Generator text prompt input area",
            actionType = "type_text"
        ),
        AppiumElement(
            name = "Pixelbin Generate Video Button",
            testTag = "pixelbin_generate_btn",
            resourceId = "pixelbin-generate-btn",
            xpath = "//button[contains(text(), 'Generate') or contains(@class, 'generate')]",
            accessibilityId = "Pixelbin Generate Button",
            description = "Pixelbin AI Video Generator video dispatch button",
            actionType = "click"
        )
    )

    init {
        // Default prompt auto-pipeline disabled per user request
        // runAutomaticStartupSequence()
    }

    fun runAutomaticStartupSequence() {
        viewModelScope.launch {
            _isAutomating.value = true
            _lastAutomationStatus.value = "🚀 Auto-Appium Pipeline Starting: Extracting ChatGPT Composer DOM JSON..."

            // Step 1: Extract JSON from the ChatGPT composer DOM snippet provided
            val parsedJson = parseComposerDomToJson()
            _extractedDomJson.value = parsedJson

            logAction(
                actionName = "auto_dom_extract",
                locator = "div[data-composer-body]",
                payload = "Parsed DOM JSON successfully with 3 primary composer controls",
                status = "SUCCESS"
            )

            kotlinx.coroutines.delay(600)

            // Step 2: Auto-type story prompt
            val storyPrompt = "Write a captivating short story about a brave dragon named Ignis who discovered a starlight portal and saved a glowing moon."
            _lastAutomationStatus.value = "✍️ Auto-typing Story Prompt: \"$storyPrompt\""
            _currentPrompt.value = storyPrompt

            logAction(
                actionName = "auto_type_story",
                locator = "xpath: //div[@id='prompt-textarea']",
                payload = storyPrompt,
                status = "SUCCESS"
            )

            kotlinx.coroutines.delay(800)

            // Step 3: Auto-click send button
            _lastAutomationStatus.value = "⚡ Auto-clicking Send Button (id: composer-submit-button)..."
            sendMessage()

            logAction(
                actionName = "auto_click_send",
                locator = "xpath: //button[@data-testid='send-button']",
                payload = "Clicked composer-submit-button",
                status = "SUCCESS"
            )

            _isAutomating.value = false
            _lastAutomationStatus.value = "✅ Automatic Appium Story Pipeline Completed Successfully!"
        }
    }

    fun onPromptChange(newText: String) {
        _currentPrompt.value = newText
    }

    fun toggleMode() {
        _isWebViewMode.value = !_isWebViewMode.value
        logAction(
            actionName = "toggle_mode",
            locator = "testTag: mode_toggle_switch",
            payload = if (_isWebViewMode.value) "Switched to WebView Mode (${_webUrl.value})" else "Switched to Native Interactive Screen",
            status = "SUCCESS"
        )
    }

    private val _isGeneratingTrendingTopic = MutableStateFlow(false)
    val isGeneratingTrendingTopic: StateFlow<Boolean> = _isGeneratingTrendingTopic.asStateFlow()

    fun setGeneratingTrendingTopic(value: Boolean) {
        _isGeneratingTrendingTopic.value = value
    }

    private val _isGeneratingAudio = MutableStateFlow(false)
    val isGeneratingAudio: StateFlow<Boolean> = _isGeneratingAudio.asStateFlow()

    fun setGeneratingAudio(value: Boolean) {
        _isGeneratingAudio.value = value
    }

    private val _isGeneratingImages = MutableStateFlow(false)
    val isGeneratingImages: StateFlow<Boolean> = _isGeneratingImages.asStateFlow()

    fun setGeneratingImages(value: Boolean) {
        _isGeneratingImages.value = value
    }

    private val _selectedPipelineStepView = MutableStateFlow<Int?>(null)
    val selectedPipelineStepView: StateFlow<Int?> = _selectedPipelineStepView.asStateFlow()

    fun selectPipelineStepView(step: Int?) {
        _selectedPipelineStepView.value = step
    }

    private val _isGeneratingStep3Visuals = MutableStateFlow(false)
    val isGeneratingStep3Visuals: StateFlow<Boolean> = _isGeneratingStep3Visuals.asStateFlow()

    fun triggerTrendingTopicGenerator(
        category: String = "Tech & AI",
        language: String = "Hinglish",
        duration: String = "Short/Reel (30-60 seconds)"
    ) {
        _isGeneratingTrendingTopic.value = true
        val prompt = """
You are an expert AI content strategist, trend analyst, and JSON generation engine. Your task is to generate a fully detailed, high-engagement trending topic based on the user's specified category, language, and duration. 

You must output ONLY valid, hardcoded JSON data with no markdown code block formatting issues, no extra conversational text, and no pre-ambles. 

Here are the input parameters for this generation:
- Category: $category
- Language: $language
- Duration: $duration (Total 60 seconds)

CRITICAL INSTRUCTIONS:
1. DO NOT generate any visuals or "visual_cue" fields in this step. Only generate the voiceover/audio script.
2. You MUST generate exactly 10 voiceover segments of approximately 6 seconds each (totaling 60 seconds).
   - Segment 1: Represented under "introduction" (00:00 - 00:06).
   - Segments 2 to 9: Represented as an array of exactly 8 items under "body_segments" (00:06 to 00:54).
   - Segment 10: Represented under "conclusion" (00:54 - 01:00).
3. Each segment's "audio_script" must contain exactly 12-18 words in the requested language ($language) to ensure it takes approximately 6 seconds to speak when read at a steady, natural pace. Do NOT include any visual_cue or visual descriptors.

Please strictly follow this exact JSON schema for the output:

{
  "trend_metadata": {
    "category": "$category",
    "language": "$language",
    "target_duration": "60 seconds",
    "generation_timestamp": "2026-09-05T22:03:00Z"
  },
  "content_details": {
    "title": "A highly engaging, catchy, and SEO-optimized title",
    "description": "A comprehensive description of the topic (at least 3-4 paragraphs)",
    "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"],
    "tags": ["keyword1", "keyword2", "keyword3", "keyword4"]
  },
  "storyboard": {
    "introduction": {
      "timestamp": "00:00 - 00:06",
      "audio_script": "Exact $language script for Segment 1 (approx. 12-18 words, no visual descriptions)"
    },
    "body_segments": [
      {
        "segment_number": 2,
        "timestamp": "00:06 - 00:12",
        "audio_script": "Exact $language script for Segment 2 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 3,
        "timestamp": "00:12 - 00:18",
        "audio_script": "Exact $language script for Segment 3 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 4,
        "timestamp": "00:18 - 00:24",
        "audio_script": "Exact $language script for Segment 4 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 5,
        "timestamp": "00:24 - 00:30",
        "audio_script": "Exact $language script for Segment 5 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 6,
        "timestamp": "00:30 - 00:36",
        "audio_script": "Exact $language script for Segment 6 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 7,
        "timestamp": "00:36 - 00:42",
        "audio_script": "Exact $language script for Segment 7 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 8,
        "timestamp": "00:42 - 00:48",
        "audio_script": "Exact $language script for Segment 8 (approx. 12-18 words, no visual descriptions)"
      },
      {
        "segment_number": 9,
        "timestamp": "00:48 - 00:54",
        "audio_script": "Exact $language script for Segment 9 (approx. 12-18 words, no visual descriptions)"
      }
    ],
    "conclusion": {
      "timestamp": "00:54 - 01:00",
      "audio_script": "Exact $language script for Segment 10 (approx. 12-18 words, no visual descriptions)"
    }
  }
}

Ensure the JSON is completely valid, strings are properly escaped, and all fields are thoroughly filled with high-quality, relevant content matching the requested language.
""".trimIndent()

        _currentPrompt.value = prompt
        sendMessage()
    }

    fun loadInstantTrendingTopic(category: String = "Tech & AI", language: String = "Hinglish") {
        val instantJson = """
{
  "trend_metadata": {
    "category": "$category",
    "language": "$language",
    "target_duration": "60 seconds",
    "generation_timestamp": "2026-09-06T12:00:00Z"
  },
  "content_details": {
    "title": "Will AI Replace Coders in 2026? Reality Check!",
    "description": "Kya AI sach me programmers aur developers ko replace kar dega? 2026 ke latest AI trends, code assistants aur autonomous AI agents ke baare me sab kuch jaaniye is viral breakdown me.",
    "hashtags": ["#AI", "#Coding", "#TechNews", "#FutureOfWork", "#ViralShorts"],
    "tags": ["AI vs Coders", "Artificial Intelligence 2026", "Tech Jobs", "Future Technology"]
  },
  "storyboard": {
    "introduction": {
      "timestamp": "00:00 - 00:06",
      "audio_script": "Dosto kya 2026 me AI coders aur software engineers ki jobs ko sach me replace kar dega?"
    },
    "body_segments": [
      {
        "segment_number": 2,
        "timestamp": "00:06 - 00:12",
        "audio_script": "Pichle kuch mahino me AI models ne coding speed ko lagbhag 10 guna tez bana diya hai."
      },
      {
        "segment_number": 3,
        "timestamp": "00:12 - 00:18",
        "audio_script": "Naye autonomous AI agents ab simple prompts se complex apps aur websites banakar ready kar rahe hain."
      },
      {
        "segment_number": 4,
        "timestamp": "00:18 - 00:24",
        "audio_script": "Lekin badi tech companies ka kehna hai ki AI sirf human coders ka ultimate co-pilot banega."
      },
      {
        "segment_number": 5,
        "timestamp": "00:24 - 00:30",
        "audio_script": "Asli coding ke sath system architecture, critical thinking aur security humans ke hath me hi rahegi."
      },
      {
        "segment_number": 6,
        "timestamp": "00:30 - 00:36",
        "audio_script": "Iska matlab jo developers AI tools ko efficiently use karenge, unki demand sabse zyada hogi."
      },
      {
        "segment_number": 7,
        "timestamp": "00:36 - 00:42",
        "audio_script": "Prompt engineering aur problem-solving skills ab traditional syntax ratne se kahin zyada important ban chuki hain."
      },
      {
        "segment_number": 8,
        "timestamp": "00:42 - 00:48",
        "audio_script": "Agar aap tech industry me grow karna chahte hain toh rozana naye AI workflows sikhna shuru karein."
      },
      {
        "segment_number": 9,
        "timestamp": "00:48 - 00:54",
        "audio_script": "Technology se darna nahi hai, balki naye tools ko master karke apne aap ko upgrade karna hai."
      }
    ],
    "conclusion": {
      "timestamp": "00:54 - 01:00",
      "audio_script": "Aapko kya lagta hai? AI human developers ko beat karega ya nahi? Comment karke zaroor batayein!"
    }
  }
}
""".trimIndent()
        _isApiCalling.value = false
        _chatMessages.value = _chatMessages.value + ChatMessage(sender = "NoTrack AI", text = instantJson)
        logAction(
            actionName = "load_instant_storyboard",
            locator = "step1_instant_generator",
            payload = "Loaded instant verified 60s viral storyboard JSON",
            status = "SUCCESS"
        )
    }

    fun sendMessage() {
        val prompt = _currentPrompt.value.trim()
        if (prompt.isEmpty()) return

        val isTrendingRequest = prompt.lowercase().contains("trending topic") || 
                                prompt.lowercase().contains("trend_metadata") || 
                                prompt.lowercase().contains("storyboard") ||
                                prompt.lowercase().contains("category:")

        val isStep3Request = prompt.lowercase().contains("visual_timeline") || 
                             prompt.lowercase().contains("story_analysis")

        val displayPrompt = if (isTrendingRequest) {
            "⚡ [REQUESTING TRENDING TOPIC GENERATION VIA NOTRACK AI]"
        } else if (isStep3Request) {
            "⚡ [REQUESTING 3-SECOND TIMEFRAME VISUALS VIA NOTRACK AI]"
        } else {
            prompt
        }

        if (!displayPrompt.startsWith("⚡ [REQUESTING")) {
            val userMsg = ChatMessage(sender = "User", text = displayPrompt)
            _chatMessages.value = _chatMessages.value + userMsg
        }
        _currentPrompt.value = ""

        if (isTrendingRequest) {
            _isGeneratingTrendingTopic.value = true
        } else if (isStep3Request) {
            _isGeneratingStep3Visuals.value = true
        }

        logAction(
            actionName = "send_message",
            locator = "testTag: send_button",
            payload = prompt,
            status = "SUCCESS"
        )

        // Route through NoTrack AI WebView background automation if webUrl is notrack.ai
        if (_webUrl.value.contains("notrack.ai")) {
            viewModelScope.launch {
                _isApiCalling.value = true
                _noTrackPromptEvent.value = prompt
                
                val responseDeferred = kotlinx.coroutines.CompletableDeferred<String>()
                registerNoTrackResponseCallback { response ->
                    responseDeferred.complete(response)
                }
                
                // Wait up to 60 seconds for the JS scraper to finish and return results
                val rawReply = try {
                    kotlinx.coroutines.withTimeout(65000) { // 65s timeout to allow 60s scraper to finish
                        responseDeferred.await()
                    }
                } catch (e: Exception) {
                    "❌ [NOTRACK_TIMEOUT] Scraper timed out waiting for response from https://notrack.ai (${e.localizedMessage}). Server traffic is high."
                }

                _isApiCalling.value = false
                _isGeneratingStep3Visuals.value = false
                
                val isTrendingJson = rawReply.contains("\"storyboard\"") || rawReply.contains("\"trend_metadata\"") || rawReply.contains("\"visual_cue\"")
                val isStep3Json = rawReply.contains("\"visual_timeline\"") || rawReply.contains("\"story_analysis\"")
                
                // Only add to chat messages if it's NOT a background pipeline JSON generation, or if it failed
                if (rawReply.startsWith("❌") || (!isTrendingJson && !isStep3Json)) {
                    _chatMessages.value = _chatMessages.value + ChatMessage(sender = "NoTrack AI", text = rawReply)
                } else if (isTrendingJson || isStep3Json) {
                    // Still add it so MainChatScreen can parse it! MainChatScreen relies on these messages to extract JSON.
                    // But we will modify ChatMessageBubble to render it invisibly or cleanly.
                    _chatMessages.value = _chatMessages.value + ChatMessage(sender = "NoTrack AI", text = rawReply)
                }
                logAction(
                    actionName = "receive_response",
                    locator = "chat_message_list",
                    payload = rawReply.take(60) + "...",
                    status = if (rawReply.startsWith("❌")) "FAILURE" else "SUCCESS"
                )
            }
            return
        }

        // Generate ChatGPT reply (Live API if Key exists, or fallback)
        viewModelScope.launch {
            _isApiCalling.value = true
            val replyText = fetchRealLiveResponse(prompt)
            _isApiCalling.value = false
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "ChatGPT", text = replyText)
            logAction(
                actionName = "receive_response",
                locator = "chat_message_list",
                payload = replyText.take(60) + "...",
                status = "SUCCESS"
            )
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(sender = "ChatGPT", text = "Chat reset! Send a prompt to start afresh.")
        )
        logAction(
            actionName = "clear_chat",
            locator = "testTag: clear_chat_button",
            payload = "Chat history cleared",
            status = "SUCCESS"
        )
    }

    fun runAppiumSimulation(promptText: String = "Tell me a short bedtime story about a cosmic starship.") {
        viewModelScope.launch {
            _isAutomating.value = true
            _lastAutomationStatus.value = "Appium step 1/3: Locating prompt-textarea element..."
            
            logAction("appium_step_locate", "id: prompt-textarea", "Finding element", "SUCCESS")
            kotlinx.coroutines.delay(600)

            _lastAutomationStatus.value = "Appium step 2/3: Typing story prompt..."
            _currentPrompt.value = promptText
            logAction("appium_step_type", "id: prompt-textarea", promptText, "SUCCESS")
            kotlinx.coroutines.delay(700)

            _lastAutomationStatus.value = "Appium step 3/3: Clicking composer-submit-button..."
            sendMessage()
            logAction("appium_step_click", "id: composer-submit-button", "Clicked", "SUCCESS")

            _isAutomating.value = false
            _lastAutomationStatus.value = "Appium story pipeline executed successfully!"
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            logDao.clearLogs()
        }
    }

    fun logAction(actionName: String, locator: String, payload: String, status: String) {
        viewModelScope.launch {
            logDao.insertLog(
                AppiumLogEntity(
                    actionName = actionName,
                    targetLocator = locator,
                    payload = payload,
                    status = status
                )
            )
        }
    }

    private fun parseComposerDomToJson(): String {
        return try {
            val root = JSONObject()
            val composer = JSONObject()
            
            composer.put("data_composer_body", "")
            composer.put("data_composer_grid", "")
            composer.put("container_class", "row-start-3 row-end-4 col-start-1 col-end-2 min-h-0 min-w-0 flex-1 px-2 py-[9px]")

            val elementsArray = org.json.JSONArray()

            // 1. Plus Button
            val plusBtn = JSONObject().apply {
                put("element", "button")
                put("id", "composer-plus-btn")
                put("data_testid", "composer-plus-btn")
                put("class", "composer-btn")
                put("aria_label", "Add files and more")
                put("xpath", "//button[@id='composer-plus-btn']")
            }
            elementsArray.put(plusBtn)

            // 2. Textarea / ProseMirror
            val inputArea = JSONObject().apply {
                put("element", "div")
                put("id", "prompt-textarea")
                put("class", "ProseMirror")
                put("role", "textbox")
                put("aria_label", "Chat with ChatGPT")
                put("data_virtualkeyboard", "true")
                put("fallback_textarea_name", "prompt-textarea")
                put("xpath", "//div[@id='prompt-textarea' and @role='textbox']")
            }
            elementsArray.put(inputArea)

            // 3. Send Button
            val submitBtn = JSONObject().apply {
                put("element", "button")
                put("id", "composer-submit-button")
                put("data_testid", "send-button")
                put("class", "composer-submit-btn composer-submit-button-color h-9 w-9")
                put("aria_label", "Send prompt")
                put("xpath", "//button[@id='composer-submit-button']")
            }
            elementsArray.put(submitBtn)

            root.put("composer_grid", composer)
            root.put("extracted_elements", elementsArray)
            root.put("status", "SUCCESS_PARSED_JSON")
            root.put("timestamp", System.currentTimeMillis())

            root.toString(2)
        } catch (e: Exception) {
            "{\"error\": \"Failed to parse JSON: ${e.localizedMessage}\"}"
        }
    }

    suspend fun generateStep3VisualsWithNoTrackOrApi(prompt: String): String = withContext(Dispatchers.IO) {
        val key = _apiKey.value.trim()
        if (_webUrl.value.contains("notrack.ai")) {
            withContext(Dispatchers.Main) {
                _isApiCalling.value = true
                _noTrackPromptEvent.value = prompt
            }
            
            val responseDeferred = kotlinx.coroutines.CompletableDeferred<String>()
            registerNoTrackResponseCallback { response ->
                responseDeferred.complete(response)
            }
            
            val rawReply = try {
                kotlinx.coroutines.withTimeout(65000) {
                    responseDeferred.await()
                }
            } catch (e: Exception) {
                "❌ [NOTRACK_TIMEOUT] Scraper timed out waiting for response from https://notrack.ai (${e.localizedMessage})."
            }
            
            withContext(Dispatchers.Main) {
                _isApiCalling.value = false
            }
            return@withContext rawReply
        } else if (key.isNotEmpty()) {
            return@withContext fetchRealLiveResponse(prompt)
        } else {
            return@withContext ""
        }
    }

    fun triggerStep3VisualsGenerator(masterPrompt: String) {
        _currentPrompt.value = masterPrompt
        sendMessage()
    }

    private suspend fun fetchRealLiveResponse(prompt: String): String = withContext(Dispatchers.IO) {
        val key = _apiKey.value.trim()
        if (key.isEmpty()) {
            return@withContext "❌ [ERROR] No API Key configured and WebView automation was not invoked.\n" +
                    "To generate data:\n" +
                    "1. Step 1 (Text & JSON): Use NoTrack AI URL (https://notrack.ai)\n" +
                    "2. Or enter an OpenAI / Gemini API Key in menu settings."
        }

        try {
            if (key.startsWith("sk-")) {
                // OpenAI ChatGPT API Request (gpt-4o-mini / gpt-3.5-turbo)
                val url = java.net.URL("https://api.openai.com/v1/chat/completions")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Authorization", "Bearer $key")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 15000
                conn.readTimeout = 15000

                val jsonBody = JSONObject().apply {
                    put("model", "gpt-4o-mini")
                    val messages = org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("content", prompt)
                        })
                    }
                    put("messages", messages)
                    put("temperature", 0.7)
                }

                java.io.OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(jsonBody.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode == 200) {
                    val stream = conn.inputStream
                    val reader = java.io.BufferedReader(java.io.InputStreamReader(stream))
                    val responseStr = reader.readText()
                    reader.close()

                    val jsonResp = JSONObject(responseStr)
                    val choices = jsonResp.getJSONArray("choices")
                    if (choices.length() > 0) {
                        return@withContext choices.getJSONObject(0).getJSONObject("message").getString("content")
                    }
                } else {
                    val errStream = conn.errorStream
                    val errText = errStream?.bufferedReader()?.readText() ?: "HTTP $responseCode"
                    return@withContext "❌ [OPENAI_API_ERROR] ($responseCode): $errText\nPlease verify your API key in Settings."
                }
            } else {
                // Gemini REST API Request (gemini-2.5-flash)
                val modelName = "gemini-2.5-flash"
                val url = java.net.URL("https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$key")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 15000
                conn.readTimeout = 15000

                val jsonBody = JSONObject().apply {
                    val contents = org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            val parts = org.json.JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            }
                            put("parts", parts)
                        })
                    }
                    put("contents", contents)
                }

                java.io.OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(jsonBody.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode == 200) {
                    val stream = conn.inputStream
                    val reader = java.io.BufferedReader(java.io.InputStreamReader(stream))
                    val responseStr = reader.readText()
                    reader.close()

                    val jsonResp = JSONObject(responseStr)
                    val candidates = jsonResp.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text received")
                        }
                    }
                } else {
                    val errStream = conn.errorStream
                    val errText = errStream?.bufferedReader()?.readText() ?: "HTTP $responseCode"
                    return@withContext "❌ [GEMINI_API_ERROR] ($responseCode): $errText"
                }
            }
        } catch (e: Exception) {
            return@withContext "❌ [CONNECTION_ERROR] ${e.localizedMessage}. Please check internet connection."
        }

        return@withContext "❌ [PIPELINE_ERROR] Unable to get response from API or Automation."
    }
}

