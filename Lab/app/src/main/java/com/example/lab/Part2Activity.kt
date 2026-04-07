package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lab.ui.theme.LabTheme

class Part2Activity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                var currentTask by remember { mutableIntStateOf(0) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                ScrollableTabRow(
                                    selectedTabIndex = currentTask,
                                    edgePadding = 0.dp,
                                    containerColor = Color.Transparent,
                                    divider = {}
                                ) {
                                    Tab(selected = currentTask == 0, onClick = { currentTask = 0 }) {
                                        Text("Swipe to Dismiss", modifier = Modifier.padding(8.dp))
                                    }
                                    Tab(selected = currentTask == 1, onClick = { currentTask = 1 }) {
                                        Text("Side Effects", modifier = Modifier.padding(8.dp))
                                    }
                                    Tab(selected = currentTask == 2, onClick = { currentTask = 2 }) {
                                        Text("WebView Interop", modifier = Modifier.padding(8.dp))
                                    }
                                    Tab(selected = currentTask == 3, onClick = { currentTask = 3 }) {
                                        Text("Adaptive Layout", modifier = Modifier.padding(8.dp))
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        when (currentTask) {
                            0 -> SwipeToDismissListScreen()
                            1 -> SnackbarEffectScreen()
                            2 -> WebViewScreen()
                            3 -> AdaptiveProfileScreen()
                        }
                    }
                }
            }
        }
    }
}
