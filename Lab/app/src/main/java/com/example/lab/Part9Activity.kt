package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lab.ui.theme.LabTheme

class Part9Activity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        LargeTopAppBar(
                            title = {
                                Text(
                                    "Collapsing Toolbar Concept",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    LazyColumn(
                        contentPadding = innerPadding,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Concept",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "การทำ Collapsing Toolbar ใน Jetpack Compose ประกอบด้วยส่วนหลักๆ ดังนี้:\n\n" +
                                            "1. ScrollBehavior: เป็นตัวจัดการพฤติกรรม เช่น TopAppBarDefaults.exitUntilCollapsedScrollBehavior() เมื่อเราเลื่อนจอขึ้น (Scroll Down) TopAppBar จะย่อขนาดลงเป็นขนาดปกติ และไม่หายไปจากหน้าจอจนหมด\n\n" +
                                            "2. nestedScroll Modifier: นำ scrollBehavior.nestedScrollConnection ไปใส่ที่ Modifier ของ Scaffold เพื่อให้ UI สามารถรับ Event การรูดหน้าจอจากชิ้นส่วนอื่นๆ เช่น LazyColumn ได้\n\n" +
                                            "3. LargeTopAppBar / MediumTopAppBar: ประกอบด้วยส่วนต่างๆ เช่น เลย์เอาต์และ title เมื่อถูกผูกกับ scrollBehavior จะสามารถพับเก็บหรือขยายออกตามการ Scroll ของผู้ใช้งาน",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "ลองเลื่อนหน้าจอดูเอฟเฟกต์:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        items(30) { index ->
                            Text(
                                text = "Item Number $index",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
