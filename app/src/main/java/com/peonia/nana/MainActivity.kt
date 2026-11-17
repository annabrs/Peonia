package com.peonia.nana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.peonia.nana.ui.NanaApp
import com.peonia.nana.ui.theme.NanaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NanaTheme {
                NanaApp()
            }
        }
    }
}
