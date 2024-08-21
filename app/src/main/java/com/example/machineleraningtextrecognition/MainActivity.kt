@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.machineleraningtextrecognition

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.machineleraningtextrecognition.ui.theme.MachineLeraningTextRecognitionTheme
import com.example.machineleraningtextrecognition.viewModel.ScannerViewModel
import com.example.machineleraningtextrecognition.views.TabsView

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: ScannerViewModel by viewModels()
        setContent {
            MachineLeraningTextRecognitionTheme {
                Scaffold(
                    topBar = {

                        TopAppBar(
                            title = { Text("Text Recognition") },

                            )
                    }
                ) { paddingValues ->
                    TabsView(viewModel, Modifier.padding(paddingValues))
                }
            }
        }
    }
}
