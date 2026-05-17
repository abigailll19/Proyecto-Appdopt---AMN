package com.example.appdopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appdopt.ui.HomeScreen
import com.example.appdopt.ui.theme.AppdoptTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppdoptTheme {
                HomeScreen()
            }
        }
    }
}
