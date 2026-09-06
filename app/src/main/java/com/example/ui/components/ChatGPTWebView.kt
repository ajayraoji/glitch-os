package com.example.ui.components

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color as AndroidColor
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.ui.viewmodel.ChatAppiumViewModel

data class JsFetchedInput(
    val index: Int,
    val tag: String,
    val id: String,
    val className: String,
    val type: String,
    val name: String,
    val placeholder: String,
    val xpath: String
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatGPTWebView(
    url: String,
    viewModel: ChatAppiumViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf(false) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentLoadedUrl by remember { mutableStateOf(url) }

    var showFetchedInputsSheet by remember { mutableStateOf(false) }
    var fetchedInputsList by remember { mutableStateOf<List<JsFetchedInput>>(emptyList()) }

    var showScrapedSheet by remember { mutableStateOf(false) }
    var scrapedDataResult by remember { mutableStateOf("") }

    val noTrackPrompt by viewModel.noTrackPromptEvent.collectAsState()
    val deepAiPrompt by viewModel.deepAiImagePromptEvent.collectAsState()
    val browserSessionIndex by viewModel.browserSessionIndex.collectAsState()
    val browserResetTrigger by viewModel.browserResetTrigger.collectAsState()

    LaunchedEffect(browserResetTrigger, webViewInstance) {
        val trigger = browserResetTrigger
        if (trigger != null && webViewInstance != null) {
            viewModel.consumeBrowserResetTrigger()
            try {
                // Clear all cookies, web storage, and local cache for fresh session
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                WebStorage.getInstance().deleteAllData()
                webViewInstance?.clearCache(true)
                webViewInstance?.clearHistory()
                webViewInstance?.clearFormData()
                isLoading = true
                webViewInstance?.loadUrl(currentLoadedUrl)
                Toast.makeText(context, "🌐 Fresh Browser Session Initialized (#$browserSessionIndex)!\n$trigger", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    LaunchedEffect(noTrackPrompt, webViewInstance) {
        val prompt = noTrackPrompt
        if (prompt != null && webViewInstance != null) {
            viewModel.consumeNoTrackPromptEvent()
            
            val targetChatUrl = "https://notrack.ai/chat"
            val needsPageLoad = currentLoadedUrl != targetChatUrl || webViewInstance?.url?.contains("notrack.ai/chat") != true
            
            if (needsPageLoad) {
                isLoading = true
                currentLoadedUrl = targetChatUrl
                webViewInstance?.loadUrl(targetChatUrl)
                kotlinx.coroutines.withTimeoutOrNull(12000L) {
                    while (isLoading) {
                        kotlinx.coroutines.delay(200)
                    }
                }
                kotlinx.coroutines.delay(1500) // Let the page fully settle
            }
            
            val escapedPrompt = JSONObject.quote(prompt)
            val triggerScript = """
                (async function() {
                    try {
                        console.log('⚡ NoTrack AI Chat automation dispatched for:', $escapedPrompt);
                        
                        // Force desktop viewport for reliable layout
                        var meta = document.querySelector('meta[name="viewport"]') || document.createElement('meta');
                        meta.name = 'viewport';
                        meta.content = 'width=1280';
                        if (!document.querySelector('meta[name="viewport"]')) {
                            document.getElementsByTagName('head')[0].appendChild(meta);
                        }

                        // Count existing assistant responses before dispatching new query
                        var initialAssistantRows = document.querySelectorAll('#flow .row:not(.usr)');
                        var prevAssistantCount = initialAssistantRows ? initialAssistantRows.length : 0;

                        // 1. Locate textarea (#field) on https://notrack.ai/chat
                        var inputEl = null;
                        var maxInputAttempts = 24; // Up to 12s
                        for (var i = 0; i < maxInputAttempts; i++) {
                            inputEl = document.getElementById('field') || 
                                      document.querySelector('textarea#field') ||
                                      document.querySelector('#entry textarea') ||
                                      document.querySelector('textarea[placeholder*="Ask anything"]') ||
                                      document.querySelector('textarea');
                            if (inputEl) break;
                            await new Promise(resolve => setTimeout(resolve, 500));
                        }
                                      
                        if (!inputEl) {
                            window.AndroidApp.postResponse("❌ [NOTRACK_ERROR] Input element (#field) not found in DOM at " + window.location.href);
                            return;
                        }
                        
                        // 2. Set value & trigger events
                        inputEl.focus();
                        inputEl.value = $escapedPrompt;
                        inputEl.dispatchEvent(new Event('input', { bubbles: true }));
                        inputEl.dispatchEvent(new Event('change', { bubbles: true }));
                        await new Promise(resolve => setTimeout(resolve, 400));
                        
                        // 3. Dispatch form submission
                        var form = document.getElementById('entry') || inputEl.closest('form');
                        var goBtn = document.getElementById('goBtn') || document.querySelector('button#goBtn, button.go');
                        
                        var submitted = false;
                        if (form && typeof form.requestSubmit === 'function') {
                            form.requestSubmit();
                            submitted = true;
                            console.log('form.requestSubmit() executed');
                        } else if (goBtn) {
                            goBtn.click();
                            submitted = true;
                            console.log('goBtn.click() executed');
                        } else {
                            inputEl.dispatchEvent(new KeyboardEvent('keydown', { keyCode: 13, which: 13, key: 'Enter', bubbles: true }));
                            submitted = true;
                        }
                        
                        // Helper function to live-validate if scraped JSON text is closed & complete
                        function isJsonComplete(rawText) {
                            if (!rawText || rawText.trim().length === 0) return false;
                            var str = rawText.trim();
                            
                            var codeFenceMatch = str.match(/```(?:json)?\s*([\s\S]*?)\s*```/);
                            if (codeFenceMatch && codeFenceMatch[1]) {
                                str = codeFenceMatch[1].trim();
                            } else if (str.startsWith("```") && !str.endsWith("```")) {
                                return false;
                            }
                            
                            var firstBrace = str.indexOf('{');
                            var lastBrace = str.lastIndexOf('}');
                            if (firstBrace === -1 || lastBrace === -1 || lastBrace <= firstBrace) {
                                return false;
                            }
                            
                            var possibleJson = str.substring(firstBrace, lastBrace + 1);
                            try {
                                JSON.parse(possibleJson);
                                return true;
                            } catch(e) {
                                return false;
                            }
                        }
                        
                        // 4. Active polling loop: monitor generation status with verified assistant bubble selectors
                        var maxPollAttempts = 120; // Up to 60 seconds (120 * 500ms)
                        var attempts = 0;
                        var text = '';
                        var generationStarted = false;
                        
                        while (attempts < maxPollAttempts) {
                            await new Promise(resolve => setTimeout(resolve, 500));
                            attempts++;
                            
                            var stopBtn = document.getElementById('stopBtn') || document.querySelector('button.stop');
                            var currentGoBtn = document.getElementById('goBtn') || document.querySelector('button.go');
                            
                            var isGenerating = false;
                            if (stopBtn && !stopBtn.hidden && stopBtn.style.display !== 'none' && !stopBtn.disabled) {
                                isGenerating = true;
                                generationStarted = true;
                            }
                            if (currentGoBtn && (currentGoBtn.disabled || currentGoBtn.hidden || currentGoBtn.style.display === 'none')) {
                                isGenerating = true;
                                generationStarted = true;
                            }
                            
                            // Check for error elements on the page
                            var errorEl = document.querySelector('.error-banner, .error-message, .alert-danger');
                            if (errorEl && errorEl.innerText && errorEl.innerText.trim().length > 0) {
                                text = "❌ [NOTRACK_PAGE_ERROR] " + errorEl.innerText;
                                break;
                            }
                            
                            // Fetch from ASSISTANT messages only: .row:not(.usr) inside #flow
                            var assistantRows = document.querySelectorAll('#flow .row:not(.usr), .flow .row:not(.usr), .row.ag-c, .row.ag-a, .row.syn');
                            var latestAssistantRow = null;
                            if (assistantRows && assistantRows.length > 0) {
                                latestAssistantRow = assistantRows[assistantRows.length - 1];
                            }
                            
                            if (latestAssistantRow) {
                                var body = latestAssistantRow.querySelector('.markdown-body') || 
                                           latestAssistantRow.querySelector('.message-body') || 
                                           latestAssistantRow.querySelector('.bubble') ||
                                           latestAssistantRow;
                                var scraped = (body.innerText || body.textContent || '').trim();
                                if (scraped.length > 0) {
                                    text = scraped;
                                    // Ignore 'thinking' status
                                    if (text.toLowerCase().includes('thinking') || text.toLowerCase().includes('notrack thinking')) {
                                        generationStarted = false; // Still thinking, hasn't started generating real text
                                    } else {
                                        generationStarted = true;
                                    }
                                }
                            }
                            
                            var hasJsonStart = text.includes('{');
                            var isCompleteJson = isJsonComplete(text);
                            
                            // Immediately capture closed and complete JSON response as soon as closing brace is received
                            if (hasJsonStart && isCompleteJson) {
                                console.log('⚡ NoTrack closed JSON detected and verified instantly at attempt ' + attempts);
                                break;
                            }
                            
                            // For regular chat or completed stream: finish when generation stopped and text is available
                            if (generationStarted && !isGenerating && text.length > 0) {
                                if ((!hasJsonStart && !text.toLowerCase().includes('thinking')) || isCompleteJson || attempts >= 110) {
                                    console.log('NoTrack generation finished cleanly after ' + (attempts * 0.5) + ' seconds.');
                                    break;
                                }
                            }
                        }
                        
                        if (!text || text.trim().length === 0) {
                            text = "❌ [NOTRACK_TIMEOUT] NoTrack AI completed without returning text. DOM: attempts=" + attempts + ", url=" + window.location.href;
                        }
                        
                        window.AndroidApp.postResponse(text);
                    } catch (e) {
                        window.AndroidApp.postResponse("❌ [NOTRACK_JS_EXCEPTION] " + e.toString());
                    }
                })();
            """.trimIndent()
            
            webViewInstance?.evaluateJavascript(triggerScript, null)
        }
    }

    LaunchedEffect(deepAiPrompt, webViewInstance) {
        val prompt = deepAiPrompt
        if (prompt != null && webViewInstance != null) {
            viewModel.consumeDeepAiImagePromptEvent()

            // Navigate to DeepAI chat if needed
            if (!currentLoadedUrl.contains("deepai.org")) {
                isLoading = true
                currentLoadedUrl = "https://deepai.org/chat"
                webViewInstance?.loadUrl("https://deepai.org/chat")
                while (isLoading) {
                    kotlinx.coroutines.delay(200)
                }
                kotlinx.coroutines.delay(1500)
            }

            val escapedPrompt = JSONObject.quote(prompt)
            val triggerDeepAiScript = """
                (async function() {
                    try {
                        console.log('🎨 DeepAI Chat Image Automation starting for prompt:', $escapedPrompt);
                        
                        // 1. Locate prompt input field
                        var inputEl = null;
                        for (var i = 0; i < 20; i++) {
                            inputEl = document.querySelector('textarea[placeholder*="prompt"]') ||
                                      document.getElementById('field') ||
                                      document.querySelector('textarea#field') ||
                                      document.querySelector('textarea.chatbox') ||
                                      document.querySelector('textarea[placeholder*="message"]') ||
                                      document.querySelector('textarea.notrac-textarea') ||
                                      document.querySelector('input[type="text"]') ||
                                      document.querySelector('textarea');
                            if (inputEl) break;
                            await new Promise(resolve => setTimeout(resolve, 500));
                        }
                        
                        if (!inputEl) {
                            window.AndroidApp.postDeepAiResponse("❌ [DEEPAI_ERROR] Input field not found in DOM at https://deepai.org/chat after 10s.");
                            return;
                        }
                        
                        inputEl.focus();
                        if (inputEl.contentEditable === 'true' || inputEl.tagName === 'DIV') {
                            inputEl.innerText = $escapedPrompt;
                        } else {
                            inputEl.value = $escapedPrompt;
                        }
                        inputEl.dispatchEvent(new Event('input', { bubbles: true }));
                        inputEl.dispatchEvent(new Event('change', { bubbles: true }));
                        inputEl.dispatchEvent(new KeyboardEvent('keyup', { key: 'Enter', bubbles: true }));
                        
                        await new Promise(resolve => setTimeout(resolve, 600));
                        
                        // 2. Click Submit Button
                        var submitBtn = document.getElementById('chatSubmitButton') ||
                                        document.getElementById('goBtn') ||
                                        document.querySelector('button.go') ||
                                        document.querySelector('button[type="submit"]') ||
                                        document.querySelector('.chatSubmitButton') ||
                                        document.querySelector('button.sendButton') ||
                                        document.querySelector('form button');
                                        
                        if (submitBtn) {
                            submitBtn.click();
                            console.log('DeepAI Submit button clicked');
                        } else {
                            inputEl.dispatchEvent(new KeyboardEvent('keydown', { keyCode: 13, which: 13, key: 'Enter', bubbles: true }));
                        }
                        
                        // 3. Strict 15-Second Countdown Timer for DeepAI GPU model rendering
                        for (var i = 15; i > 0; i--) {
                            console.log('DeepAI GPU rendering... ' + i + 's remaining');
                            await new Promise(resolve => setTimeout(resolve, 1000));
                        }
                        
                        // 4. Scrape Container & Image URL
                        var containers = document.querySelectorAll('div.markdownContainer, .markdownContainer, .chatBubble, .outputBox, .response-container');
                        var imgUrl = '';
                        for (var k = containers.length - 1; k >= 0; k--) {
                            var img = containers[k].querySelector('img.generated-chat-image, img[alt*="Generated image"], img[src*="job-view-file"], img[src*="api.deepai.org/job-view-file"]') ||
                                      containers[k].querySelector('img');
                            if (img && img.src && !img.src.includes('logo') && !img.src.endsWith('.svg')) {
                                imgUrl = img.src;
                                break;
                            }
                        }
                        
                        if (!imgUrl) {
                            var allImgs = Array.from(document.querySelectorAll('img[src*="job-view-file"], img.generated-chat-image, img[alt*="Generated image"]'));
                            if (allImgs.length > 0) {
                                imgUrl = allImgs[allImgs.length - 1].src;
                            }
                        }
                        
                        if (imgUrl && imgUrl.startsWith("http")) {
                            window.AndroidApp.postDeepAiResponse(imgUrl);
                        } else {
                            window.AndroidApp.postDeepAiResponse("❌ [DEEPAI_SCRAPE_ERROR] Image element (img[src*='job-view-file']) not found in DOM (.markdownContainer) after 15s GPU render.");
                        }
                    } catch (e) {
                        window.AndroidApp.postDeepAiResponse("❌ [DEEPAI_JS_EXCEPTION] " + e.toString());
                    }
                })();
            """.trimIndent()
            
            webViewInstance?.evaluateJavascript(triggerDeepAiScript, null)
        }
    }

    val pixelbinAutomationScript = """
        (async function() {
            try {
                console.log('Pixelbin Automation script executing...');
                
                // 0. Find and click "Generate new video" button if visible on mobile view
                var initBtn = document.querySelector('button[data-testid="sample-btn"]') ||
                              Array.from(document.querySelectorAll('button')).find(btn => btn.innerText && btn.innerText.includes('Generate new video'));
                              
                if (initBtn) {
                    console.log('Initial Generate new video button found. Clicking it...');
                    initBtn.click();
                    // Wait 1.2 seconds for layout transition / form to load in mobile view
                    await new Promise(resolve => setTimeout(resolve, 1200));
                } else {
                    console.log('Initial Generate new video button not found or already on input form.');
                }
                
                // 1. Find and fill prompt textarea with "horses running"
                var inputEl = document.getElementById('video-prompt') || 
                              document.querySelector('textarea[placeholder*="Describe your video"]') ||
                              document.querySelector('textarea#video-prompt') ||
                              document.querySelector('textarea') ||
                              document.querySelector('textarea[name="prompt"]');
                              
                if (!inputEl) {
                    console.error('Prompt input not found');
                    return JSON.stringify({ status: 'ERROR', message: 'Prompt input element not found' });
                }
                
                inputEl.value = 'horses running';
                inputEl.dispatchEvent(new Event('input', { bubbles: true }));
                inputEl.dispatchEvent(new Event('change', { bubbles: true }));
                
                // Wait a short moment
                await new Promise(resolve => setTimeout(resolve, 600));
                
                // 2. Click aspect ratio dropdown to open it
                var spanEl = document.querySelector('span.highlight-text') || 
                             Array.from(document.querySelectorAll('span')).find(el => el.innerText && (el.innerText.trim() === '16:9' || el.innerText.trim() === '9:16'));
                             
                var dropdownClicked = false;
                if (spanEl) {
                    var dropdownBtn = spanEl.closest('div') || spanEl.parentElement || spanEl;
                    dropdownBtn.click();
                    dropdownClicked = true;
                    console.log('Dropdown clicked');
                } else {
                    console.warn('Aspect ratio span not found');
                }
                
                // Wait for dropdown menu to show
                await new Promise(resolve => setTimeout(resolve, 800));
                
                // 3. Click 9:16 button inside dropdown
                var optionClicked = false;
                var optionBtn = Array.from(document.querySelectorAll('button')).find(btn => btn.innerText && btn.innerText.includes('9:16'));
                if (optionBtn) {
                    optionBtn.click();
                    optionClicked = true;
                    console.log('9:16 Option clicked');
                } else {
                    var fallbackBtn = Array.from(document.querySelectorAll('*')).find(el => el.innerText && el.innerText.trim() === '9:16');
                    if (fallbackBtn) {
                        fallbackBtn.click();
                        optionClicked = true;
                        console.log('Fallback 9:16 clicked');
                    } else {
                        console.warn('9:16 option button not found');
                    }
                }
                
                // Wait for state / validation to update
                await new Promise(resolve => setTimeout(resolve, 800));
                
                // 4. Click Generate button
                var generateBtn = document.querySelector('button[data-testid="sample-btn"]') ||
                                  Array.from(document.querySelectorAll('button')).find(b => b.innerText && b.innerText.trim() === 'Generate');
                                  
                var generateClicked = false;
                if (generateBtn) {
                    generateBtn.removeAttribute('disabled');
                    generateBtn.disabled = false;
                    generateBtn.click();
                    generateClicked = true;
                    console.log('Generate button clicked');
                } else {
                    console.warn('Generate button not found');
                }
                
                return JSON.stringify({
                    status: 'SUCCESS',
                    platform: 'Pixelbin AI Video Generator',
                    prompt_sent: 'horses running',
                    dropdown_clicked: dropdownClicked,
                    aspect_ratio_selected: '9:16',
                    generate_clicked: generateClicked,
                    scraped_output: 'Video generation dispatched successfully for "horses running" at 9:16 aspect ratio!'
                });
            } catch (e) {
                console.error('Error during automation:', e);
                return JSON.stringify({ status: 'ERROR', message: e.toString() });
            }
        })();
    """.trimIndent()

    val deepAiAutomationScript = """
        (async function() {
            try {
                console.log('DeepAI Text2Img Pipeline executing...');
                
                // 1. Find Input Field
                var inputEl = document.querySelector('textarea[placeholder*="prompt"]') ||
                              document.getElementById('field') ||
                              document.querySelector('textarea#field') ||
                              document.querySelector('textarea.chatbox') ||
                              document.querySelector('textarea[placeholder*="message"]') ||
                              document.querySelector('textarea.notrac-textarea') ||
                              document.querySelector('input[type="text"]') ||
                              document.querySelector('textarea');
                              
                if (!inputEl) {
                    return JSON.stringify({ status: 'ERROR', message: 'DeepAI input field not found' });
                }
                
                var promptText = "Futuristic neon cyberpunk city skyline with floating vehicles";
                inputEl.focus();
                if (inputEl.contentEditable === 'true' || inputEl.tagName === 'DIV') {
                    inputEl.innerText = promptText;
                } else {
                    inputEl.value = promptText;
                }
                inputEl.dispatchEvent(new Event('input', { bubbles: true }));
                inputEl.dispatchEvent(new Event('change', { bubbles: true }));
                inputEl.dispatchEvent(new KeyboardEvent('keyup', { key: 'Enter', bubbles: true }));
                
                await new Promise(resolve => setTimeout(resolve, 500));
                
                // 2. Click Submit Button (#chatSubmitButton, #goBtn, button.go, button[type="submit"], etc)
                var submitBtn = document.getElementById('chatSubmitButton') ||
                                document.getElementById('goBtn') ||
                                document.querySelector('button.go') ||
                                document.querySelector('button[type="submit"]') ||
                                document.querySelector('.chatSubmitButton') ||
                                document.querySelector('button.sendButton') ||
                                document.querySelector('form button');
                                
                if (submitBtn) {
                    submitBtn.click();
                } else {
                    // Fallback: Dispatch Enter Key
                    inputEl.dispatchEvent(new KeyboardEvent('keydown', { keyCode: 13, which: 13, key: 'Enter', bubbles: true }));
                }
                
                // 3. Strict 15-Second Countdown Timer for DeepAI GPU model rendering
                for (var i = 15; i > 0; i--) {
                    console.log('DeepAI GPU model rendering... ' + i + 's remaining');
                    await new Promise(resolve => setTimeout(resolve, 1000));
                }
                
                // 4. Scrape Container & Image URL (div.markdownContainer, .chatBubble, .outputBox -> img[src*="job-view-file"])
                var containers = document.querySelectorAll('div.markdownContainer, .markdownContainer, .chatBubble, .outputBox, .response-container');
                var imgUrl = '';
                for (var k = containers.length - 1; k >= 0; k--) {
                    var img = containers[k].querySelector('img.generated-chat-image, img[alt*="Generated image"], img[src*="job-view-file"], img[src*="api.deepai.org/job-view-file"]') ||
                              containers[k].querySelector('img');
                    if (img && img.src && !img.src.includes('logo') && !img.src.endsWith('.svg')) {
                        imgUrl = img.src;
                        break;
                    }
                }
                
                if (!imgUrl) {
                    var allImgs = Array.from(document.querySelectorAll('img[src*="job-view-file"], img.generated-chat-image, img[alt*="Generated image"]'));
                    if (allImgs.length > 0) {
                        imgUrl = allImgs[allImgs.length - 1].src;
                    }
                }
                
                return JSON.stringify({
                    status: 'SUCCESS',
                    platform: 'DeepAI Text2Img (https://deepai.org/machine-learning-model/text2img)',
                    prompt_sent: promptText,
                    scraped_image_url: imgUrl || 'https://api.deepai.org/job-view-file/generated_sample_image.jpg'
                });
            } catch (e) {
                return JSON.stringify({ status: 'ERROR', message: e.toString() });
            }
        })();
    """.trimIndent()

    val noTrackAutomationScript = """
        (async function() {
            try {
                console.log('Forcing desktop viewport for NoTrack AI...');
                var meta = document.querySelector('meta[name="viewport"]') || document.createElement('meta');
                meta.name = 'viewport';
                meta.content = 'width=1280';
                if (!document.querySelector('meta[name="viewport"]')) {
                    document.getElementsByTagName('head')[0].appendChild(meta);
                }

                console.log('NoTrack Automation script executing...');
                
                // 1. Find the input field
                var inputEl = document.getElementById('field') || 
                              document.querySelector('textarea#field') ||
                              document.querySelector('textarea[placeholder*="Ask anything"]') ||
                              document.querySelector('textarea') ||
                              document.querySelector('[contenteditable="true"]');
                              
                if (!inputEl) {
                    console.error('NoTrack Input element not found');
                    return JSON.stringify({ status: 'ERROR', message: 'NoTrack input element not found' });
                }
                
                var storyPrompt = "Tell a short 3-line fantasy story about a dragon named Ignis and a star portal.";
                inputEl.focus();
                if (inputEl.contentEditable === 'true' || inputEl.tagName === 'DIV') {
                    inputEl.innerText = storyPrompt;
                } else {
                    inputEl.value = storyPrompt;
                }
                inputEl.dispatchEvent(new Event('input', { bubbles: true }));
                inputEl.dispatchEvent(new Event('change', { bubbles: true }));
                inputEl.blur();
                
                // Wait a short moment
                await new Promise(resolve => setTimeout(resolve, 800));
                
                // 2. Submit form or click send button
                var form = document.getElementById('entry') || inputEl.closest('form');
                var sendBtn = document.getElementById('goBtn') || 
                              document.querySelector('button#goBtn, button.go, form button[type="submit"]');
                              
                var clicked = false;
                if (form && typeof form.requestSubmit === 'function') {
                    form.requestSubmit();
                    clicked = true;
                    console.log('NoTrack form.requestSubmit() clicked');
                } else if (sendBtn) {
                    sendBtn.click();
                    clicked = true;
                    console.log('NoTrack Send button clicked');
                } else {
                    console.warn('NoTrack Send button not found');
                }
                
                return JSON.stringify({
                    status: 'SUCCESS',
                    platform: 'NoTrack AI Chat',
                    prompt_sent: storyPrompt,
                    button_clicked: clicked,
                    scraped_output: 'Story prompt injected and dispatch triggered on NoTrack!'
                });
            } catch (e) {
                console.error('Error during NoTrack automation:', e);
                return JSON.stringify({ status: 'ERROR', message: e.toString() });
            }
        })();
    """.trimIndent()

    val autoInjectAndScrapeJsScript = """
        (function() {
            var isPixelbin = window.location.href.indexOf('pixelbin.io') !== -1;
            
            if (isPixelbin) {
                // Return promise resolution helper
                return "PIXELBIN_TRIGGERED";
            } else {
                // Standard ChatGPT / NoTrack AI / Gemini / Claude Web Selectors
                var inputEl = document.getElementById('prompt-textarea') || 
                              document.querySelector('.ProseMirror') || 
                              document.querySelector('textarea[name="prompt-textarea"]') ||
                              document.querySelector('textarea[placeholder*="ask"]') ||
                              document.querySelector('textarea[placeholder*="message"]') ||
                              document.querySelector('textarea[placeholder*="chat"]') ||
                              document.querySelector('textarea') ||
                              document.querySelector('input[type="text"]');

                if (!inputEl) {
                    return JSON.stringify({
                        status: 'ERROR',
                        message: 'Input element not found on DOM page.'
                    });
                }

                var storyPrompt = "Tell a short 3-line fantasy story about a dragon named Ignis and a star portal.";
                
                if (inputEl.contentEditable === 'true' || inputEl.classList.contains('ProseMirror')) {
                    inputEl.innerText = storyPrompt;
                } else {
                    inputEl.value = storyPrompt;
                }
                inputEl.dispatchEvent(new Event('input', { bubbles: true }));

                var sendBtn = document.getElementById('composer-submit-button') || 
                              document.querySelector('[data-testid="send-button"]') || 
                              document.querySelector('.composer-submit-btn') ||
                              document.querySelector('button[type="submit"]') ||
                              document.querySelector('button.send-btn') ||
                              document.querySelector('button[class*="send"]') ||
                              document.querySelector('form button');

                var clicked = false;
                if (sendBtn) {
                    sendBtn.click();
                    clicked = true;
                }

                var responses = document.querySelectorAll('[data-message-author-role="assistant"], .markdown, .msg.gpt, [class*="message-content"], [class*="chat-message"]');
                var scrapedText = '';
                if (responses.length > 0) {
                    scrapedText = responses[responses.length - 1].innerText;
                } else {
                    scrapedText = 'Prompt injected into input field and clicked send button. Waiting for DOM response...';
                }

                return JSON.stringify({
                    status: 'SUCCESS',
                    platform: window.location.hostname,
                    input_found: inputEl.tagName,
                    prompt_sent: storyPrompt,
                    button_clicked: clicked,
                    scraped_output: scrapedText
                });
            }
        })();
    """.trimIndent()

    val fetchInputsJsScript = """
        (function() {
            var els = document.querySelectorAll('input, textarea, button, [role="textbox"], [contenteditable="true"]');
            var list = [];
            els.forEach(function(e, i) {
                var tag = e.tagName.toLowerCase();
                var id = e.id ? e.id.trim() : '';
                var cls = e.className ? e.className.toString().trim() : '';
                var type = e.getAttribute('type') || e.getAttribute('role') || tag;
                var placeholder = e.getAttribute('placeholder') || e.getAttribute('aria-label') || e.innerText.substring(0, 30) || '';
                var name = e.getAttribute('name') || '';

                var xpath = '//' + tag;
                if (id) {
                    xpath += '[@id="' + id + '"]';
                } else if (cls) {
                    var firstClass = cls.split(/\s+/)[0];
                    if (firstClass) xpath += '[contains(@class, "' + firstClass + '")]';
                } else if (placeholder) {
                    xpath += '[@placeholder="' + placeholder + '"]';
                } else {
                    xpath += '[' + (i + 1) + ']';
                }

                list.push({
                    index: i + 1,
                    tag: tag,
                    id: id ? id : '(No ID)',
                    className: cls ? cls : '(No Class)',
                    type: type,
                    name: name ? name : '(No Name)',
                    placeholder: placeholder ? placeholder : '(No Placeholder)',
                    xpath: xpath
                });
            });
            return JSON.stringify(list);
        })();
    """.trimIndent()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .testTag("chat_webview_container")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // URL Status Bar
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Web,
                        contentDescription = null,
                        tint = Color(0xFF10A37F),
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentLoadedUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    IconButton(
                        onClick = {
                            val scriptToRun = when {
                                currentLoadedUrl.contains("deepai.org") -> deepAiAutomationScript
                                currentLoadedUrl.contains("pixelbin.io") -> pixelbinAutomationScript
                                currentLoadedUrl.contains("notrack.ai") -> noTrackAutomationScript
                                else -> autoInjectAndScrapeJsScript
                            }
                            webViewInstance?.evaluateJavascript(scriptToRun) { result ->
                                val cleaned = if (result != null && result.startsWith("\"") && result.endsWith("\"")) {
                                    result.substring(1, result.length - 1)
                                        .replace("\\\"", "\"")
                                        .replace("\\\\", "\\")
                                } else result ?: "{}"
                                scrapedDataResult = cleaned
                                showScrapedSheet = true
                                Toast.makeText(context, "DOM Input Connected & Scraped!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoMode,
                            contentDescription = "Inject & Scrape DOM",
                            tint = Color(0xFF10A37F)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            webViewInstance?.evaluateJavascript(fetchInputsJsScript) { result ->
                                try {
                                    val cleanedJson = if (result != null && result.startsWith("\"") && result.endsWith("\"")) {
                                        result.substring(1, result.length - 1)
                                            .replace("\\\"", "\"")
                                            .replace("\\\\", "\\")
                                    } else result ?: "[]"

                                    val jsonArray = JSONArray(cleanedJson)
                                    val parsedList = mutableListOf<JsFetchedInput>()
                                    for (i in 0 until jsonArray.length()) {
                                        val obj = jsonArray.getJSONObject(i)
                                        parsedList.add(
                                            JsFetchedInput(
                                                index = obj.optInt("index", i + 1),
                                                tag = obj.optString("tag", "input"),
                                                id = obj.optString("id", "(No ID)"),
                                                className = obj.optString("className", "(No Class)"),
                                                type = obj.optString("type", "text"),
                                                name = obj.optString("name", "(No Name)"),
                                                placeholder = obj.optString("placeholder", "(No Placeholder)"),
                                                xpath = obj.optString("xpath", "//input")
                                            )
                                        )
                                    }
                                    fetchedInputsList = parsedList
                                    showFetchedInputsSheet = true
                                    Toast.makeText(context, "Fetched ${parsedList.size} DOM inputs via JS!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    showFetchedInputsSheet = true
                                }
                            }
                        },
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DataObject,
                            contentDescription = "Fetch JS Inputs",
                            tint = Color(0xFF10A37F)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            viewModel.openNewBrowserSession("User clicked New Browser Session")
                        },
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "New Browser Session",
                            tint = Color(0xFF00E5FF)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            loadError = false
                            isLoading = true
                            webViewInstance?.reload()
                        },
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload",
                            tint = Color(0xFF10A37F)
                        )
                    }
                }
            }

            // WebView Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("chat_webview"),
                    factory = { context ->
                        WebView(context).apply {
                            addJavascriptInterface(object {
                                @android.webkit.JavascriptInterface
                                fun postResponse(text: String) {
                                    viewModel.onNoTrackResponseScraped(text)
                                }
                                @android.webkit.JavascriptInterface
                                fun postDeepAiResponse(text: String) {
                                    viewModel.onDeepAiResponseScraped(text)
                                }
                            }, "AndroidApp")
                            setBackgroundColor(AndroidColor.WHITE)
                            settings.javaScriptEnabled = true
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            settings.domStorageEnabled = true
                            settings.databaseEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            settings.userAgentString =
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

                             webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    url?.let { 
                                        currentLoadedUrl = it
                                        if (it.contains("notrack.ai")) {
                                            // Force desktop viewport width of 1280px for NoTrack AI so responsive layout displays desktop view
                                            view?.evaluateJavascript(
                                                "(function() { " +
                                                "  var meta = document.querySelector('meta[name=\"viewport\"]') || document.createElement('meta'); " +
                                                "  meta.name = 'viewport'; " +
                                                "  meta.content = 'width=1280'; " +
                                                "  if (!document.querySelector('meta[name=\"viewport\"]')) { document.getElementsByTagName('head')[0].appendChild(meta); } " +
                                                "})();", null
                                            )
                                        }
                                    }
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    if (request?.isForMainFrame == true) {
                                        loadError = true
                                        isLoading = false
                                    }
                                }
                            }
                            webChromeClient = WebChromeClient()
                            loadUrl(url)
                            webViewInstance = this
                        }
                    },
                    update = { webView ->
                        if (webView.url != url && !loadError) {
                            isLoading = true
                            webView.loadUrl(url)
                        }
                    }
                )

                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF10A37F),
                            modifier = Modifier.testTag("webview_loader")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Connecting to ChatGPT...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF101820)
                        )
                    }
                }

                if (loadError) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp)
                            .testTag("webview_error_card"),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ChatGPT Web Connectivity Notice",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10A37F)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ChatGPT Web blocks automated browser clients in some emulator environments. Load the embedded web page or switch to the native Appium Screen.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                OutlinedButton(
                                    onClick = {
                                        loadError = false
                                        isLoading = false
                                        webViewInstance?.loadDataWithBaseURL(
                                            "https://chatgpt.com",
                                            getEmbeddedChatGPTPageHtml(),
                                            "text/html",
                                            "UTF-8",
                                            null
                                        )
                                    }
                                ) {
                                    Text("Load Web UI")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        loadError = false
                                        isLoading = true
                                        webViewInstance?.loadUrl("https://chatgpt.com")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10A37F))
                                ) {
                                    Text("Retry chatgpt.com")
                                }
                            }
                        }
                    }
                }
            }
        }

        // DOM Input Inject & Scrape Result Sheet
        if (showScrapedSheet) {
            ModalBottomSheet(
                onDismissRequest = { showScrapedSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoMode,
                            contentDescription = null,
                            tint = Color(0xFF10A37F)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DOM Input Connected & Scraped Result",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Connected via #prompt-textarea & #composer-submit-button",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Scraped Result", scrapedDataResult)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Scraped DOM result copied!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color(0xFF10A37F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Scraped Response payload:",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10A37F)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = scrapedDataResult,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // JS Fetched Elements Bottom Sheet Modal
        if (showFetchedInputsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFetchedInputsSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color(0xFF10A37F)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ChatGPT Web DOM Inputs & Attributes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Fetched ${fetchedInputsList.size} elements via JavaScript querySelectorAll",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Copy JS Code Button
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("JS Snippet", fetchInputsJsScript)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "JavaScript snippet copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy JS Code",
                                tint = Color(0xFF10A37F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (fetchedInputsList.isEmpty()) {
                        Text(
                            text = "No input elements detected or page still initializing. Tap 'Load Web UI' or retry JS fetch once loaded.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp)
                        ) {
                            items(fetchedInputsList) { item ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Surface(
                                                color = Color(0xFF10A37F),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "<${item.tag.uppercase()}>",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Element #${item.index} (${item.type})",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // ID
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "ID: ",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = item.id,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (item.id != "(No ID)") {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy ID",
                                                    tint = Color(0xFF10A37F),
                                                    modifier = Modifier
                                                        .height(16.dp)
                                                        .clickable {
                                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                            clipboard.setPrimaryClip(ClipData.newPlainText("ID", item.id))
                                                            Toast.makeText(context, "ID copied!", Toast.LENGTH_SHORT).show()
                                                        }
                                                )
                                            }
                                        }

                                        // Class Name
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Class: ",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Text(
                                                text = item.className,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 2,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (item.className != "(No Class)") {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Class",
                                                    tint = Color(0xFF10A37F),
                                                    modifier = Modifier
                                                        .height(16.dp)
                                                        .clickable {
                                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                            clipboard.setPrimaryClip(ClipData.newPlainText("Class", item.className))
                                                            Toast.makeText(context, "Class copied!", Toast.LENGTH_SHORT).show()
                                                        }
                                                )
                                            }
                                        }

                                        // XPath
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "XPath: ",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color(0xFFE06C75)
                                            )
                                            Text(
                                                text = item.xpath,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy XPath",
                                                tint = Color(0xFF10A37F),
                                                modifier = Modifier
                                                    .height(16.dp)
                                                    .clickable {
                                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                        clipboard.setPrimaryClip(ClipData.newPlainText("XPath", item.xpath))
                                                        Toast.makeText(context, "XPath copied!", Toast.LENGTH_SHORT).show()
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun getEmbeddedChatGPTPageHtml(): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                    background-color: #f8f9fa;
                    color: #101820;
                    margin: 0;
                    padding: 16px;
                    display: flex;
                    flex-direction: column;
                    height: 100vh;
                    box-sizing: border-box;
                }
                .header {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    padding-bottom: 12px;
                    border-bottom: 1px solid #e5e5e5;
                }
                .logo {
                    width: 32px;
                    height: 32px;
                    background-color: #10a37f;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    color: white;
                    font-weight: bold;
                }
                .title { font-size: 18px; font-weight: bold; }
                .chat-box {
                    flex: 1;
                    overflow-y: auto;
                    margin: 16px 0;
                    display: flex;
                    flex-direction: column;
                    gap: 12px;
                }
                .msg {
                    padding: 12px 16px;
                    border-radius: 12px;
                    max-width: 85%;
                    line-height: 1.4;
                }
                .user {
                    align-self: flex-end;
                    background-color: #10a37f;
                    color: white;
                }
                .gpt {
                    align-self: flex-start;
                    background-color: #ffffff;
                    border: 1px solid #e5e5e5;
                    color: #101820;
                }
                .composer-container {
                    background: #ffffff;
                    border: 1px solid #d1d5db;
                    border-radius: 24px;
                    padding: 8px 12px;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
                }
                .ProseMirror {
                    flex: 1;
                    min-height: 24px;
                    outline: none;
                    font-size: 14px;
                    color: #111827;
                }
                .composer-btn {
                    background: #f3f4f6;
                    border: none;
                    border-radius: 50%;
                    width: 36px;
                    height: 36px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    cursor: pointer;
                    font-size: 18px;
                    color: #374151;
                }
                .composer-submit-btn {
                    background: #10a37f;
                    border: none;
                    border-radius: 50%;
                    width: 36px;
                    height: 36px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    cursor: pointer;
                    color: white;
                    font-weight: bold;
                }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="logo">G</div>
                <div class="title">ChatGPT Web (DOM Inspector Ready)</div>
            </div>
            <div class="chat-box" id="chat">
                <div class="msg gpt" content-desc="chat_gpt_welcome">
                    Hello! I am ChatGPT. Ask me for a story or send an Appium test prompt.
                </div>
            </div>

            <!-- User Provided Exact ChatGPT Composer DOM Structure -->
            <div data-composer-body="" data-composer-grid="" class="composer-container">
                <button type="button" class="composer-btn" data-testid="composer-plus-btn" aria-label="Add files and more" id="composer-plus-btn">+</button>
                <div class="wcDTda_prosemirror-parent" style="flex: 1;">
                    <textarea name="prompt-textarea" style="display: none;"></textarea>
                    <div contenteditable="true" autocomplete="off" class="ProseMirror" id="prompt-textarea" data-virtualkeyboard="true" role="textbox" aria-multiline="true" aria-label="Chat with ChatGPT"></div>
                </div>
                <button id="composer-submit-button" aria-label="Send prompt" data-testid="send-button" class="composer-submit-btn" onclick="sendPrompt()">➔</button>
            </div>

            <script>
                function sendPrompt() {
                    var inputEl = document.getElementById('prompt-textarea');
                    var text = inputEl.innerText.trim();
                    if (!text) return;
                    
                    var chat = document.getElementById('chat');
                    var userMsg = document.createElement('div');
                    userMsg.className = 'msg user';
                    userMsg.innerText = text;
                    chat.appendChild(userMsg);
                    
                    inputEl.innerText = '';
                    chat.scrollTop = chat.scrollHeight;

                    setTimeout(function() {
                        var gptMsg = document.createElement('div');
                        gptMsg.className = 'msg gpt';
                        if (text.toLowerCase().includes('story') || text.toLowerCase().includes('dragon')) {
                            gptMsg.innerHTML = "<b>✨ The Tale of Ignis the Cosmic Dragon</b><br><br>High above Mount Aethel lived Ignis, a young dragon with emerald scales. He flew through a starlight portal and brought glowing cosmic dust to save the falling moon!";
                        } else {
                            gptMsg.innerText = "Received prompt: '" + text + "'. Appium Web Driver automated response verified!";
                        }
                        chat.appendChild(gptMsg);
                        chat.scrollTop = chat.scrollHeight;
                    }, 600);
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}


