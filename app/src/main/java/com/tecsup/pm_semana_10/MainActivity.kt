package com.tecsup.pm_semana_10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.pm_semana_10.screen.UserListScreen
import com.tecsup.pm_semana_10.ui.theme.PMSemana10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PMSemana10Theme {
                UserListScreen()
            }
        }
    }
}