package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lab.ui.theme.LabTheme

class Part1Activity : ComponentActivity() {
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
                                        Text("Contacts", modifier = Modifier.padding(8.dp))
                                    }
                                    Tab(selected = currentTask == 1, onClick = { currentTask = 1 }) {
                                        Text("Donut Chart", modifier = Modifier.padding(8.dp))
                                    }
                                    Tab(selected = currentTask == 2, onClick = { currentTask = 2 }) {
                                        Text("Like Button", modifier = Modifier.padding(8.dp))
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
                            0 -> ContactListScreen()
                            1 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                AnimatedDonutChart(
                                    proportions = listOf(30f, 40f, 30f),
                                    colors = listOf(
                                        Color(0xFFE91E63),
                                        Color(0xFF2196F3),
                                        Color(0xFFFFC107)
                                    ),
                                    modifier = Modifier.size(240.dp)
                                )
                            }
                            2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                AnimatedLikeButton()
                            }
                        }
                    }
                }
            }
        }
    }
}
