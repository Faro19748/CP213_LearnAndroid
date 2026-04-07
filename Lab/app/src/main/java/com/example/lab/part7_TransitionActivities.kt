package com.example.lab

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import com.example.lab.ui.theme.LabTheme

class TransitionMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = {
                            val intent = Intent(this@TransitionMainActivity, TransitionDetailActivity::class.java).apply {
                                putExtra("EXTRA_MESSAGE", "Hello from MainActivity! (Slide Up)")
                            }
                            
                            // ActivityOptionsCompat: สร้าง Bundle ช่วยคุม Transition ตอนเปิดหน้าต่างใหม่
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                this@TransitionMainActivity,
                                R.anim.slide_in_up, // หน้าใหม่สไลด์ขึ้นมาจากล่างสุดของจอ (100%p to 0%)
                                R.anim.no_anim      // หน้าเดิมอยู่นิ่งๆ เพื่อกันหน้าจอมืดชั่วขณะ
                            )
                            startActivity(intent, options.toBundle())
                        }) {
                            Text("Open Detail (Slide Up Transition)")
                        }
                    }
                }
            }
        }
    }
}

class TransitionDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "No Message"

        setContent {
            LabTheme {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant) { // ใช้พื้นหลังคนละสีให้เห็นภาพชัดเจน
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Detail Activity", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(message)
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(onClick = { 
                                finish() // สั่งปิด DetailActivity กลับไป MainActivity
                                applySlideDownTransition() // สั่งเล่น Animation
                            }) {
                                Text("Close Activity (Slide Down)")
                            }
                        }
                    }
                }
            }
        }
    }
    
    // ดักจับเวลาผู้ใช้ปัดจากขอบจอหรือกด Back ให้มี Animation ดึงลงด้วย
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        applySlideDownTransition()
    }

    private fun applySlideDownTransition() {
        // ใช้คำสั่งแยกเวอร์ชันมือถือ เนื่องจาก API เก่าโดน Deprecate ในแอนดรอยด์ 14 (API 34)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_CLOSE,
                R.anim.no_anim,
                R.anim.slide_out_down
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.no_anim, R.anim.slide_out_down)
        }
    }
}
