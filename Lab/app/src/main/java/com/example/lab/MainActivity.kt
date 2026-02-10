package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
                    .padding(32.dp)
            ) {
                // hp
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .background(color = Color.White)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "HP",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .fillMaxWidth(fraction = 0.56f)
                            .background(color = Color.Red)
                            .padding(8.dp)
                    )
                }

                // image
                Image(
                    painter = painterResource(id = R.drawable.peter),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(size = 400.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp)
                )

                var STR by remember { mutableStateOf(5) }
                var AGI by remember { mutableStateOf(3) }
                var DEF by remember { mutableStateOf(7) }
                val INT by remember { mutableStateOf(-2) }
                var showIntError by remember { mutableStateOf(false) }

                //status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ) {
                    // STR
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { STR++ }) {
                            Text(text = "+", fontSize = 24.sp)
                        }
                        Button(onClick = { STR-- }) {
                            Text(text = "-", fontSize = 24.sp)
                        }
                        Text(text = "STR", fontSize = 24.sp)
                        Text(text = STR.toString(), fontSize = 24.sp)
                    }

                    // AGI
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { AGI++ }) {
                            Text(text = "+", fontSize = 24.sp)
                        }
                        Button(onClick = { AGI-- }) {
                            Text(text = "-", fontSize = 24.sp)
                        }
                        Text(text = "AGI", fontSize = 24.sp)
                        Text(text = AGI.toString(), fontSize = 24.sp)
                    }

                    // DEF
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { DEF++ }) {
                            Text(text = "+", fontSize = 24.sp)
                        }
                        Button(onClick = { DEF-- }) {
                            Text(text = "-", fontSize = 24.sp)
                        }
                        Text(text = "DEF", fontSize = 24.sp)
                        Text(text = DEF.toString(), fontSize = 24.sp)
                    }

                    // INT
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { showIntError = true }) {
                            Text(text = "+", fontSize = 24.sp)
                        }
                        Button(onClick = { showIntError = true }) {
                            Text(text = "-", fontSize = 24.sp)
                        }
                        Text(text = "INT", fontSize = 24.sp)
                        if (showIntError) {
                            Text(text = "Cant up Int", fontSize = 16.sp, color = Color.Red)
                        } else {
                            Text(text = INT.toString(), fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}
