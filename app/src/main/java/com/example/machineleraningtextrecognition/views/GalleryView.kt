package com.example.machineleraningtextrecognition.views

import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.machineleraningtextrecognition.R
import com.example.machineleraningtextrecognition.viewModel.ScannerViewModel
import kotlin.contracts.contract

@Composable
fun GalleryView(viewModel: ScannerViewModel) {
    var image: Any? by remember { mutableStateOf(R.drawable.baseline_add_photo_alternate_24) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val photoPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {

            if (it != null) {
                image = it

                viewModel.onRecognizedtext(image, context)
            } else {
                viewModel.showToast(context, "No Image Selected")
            }

        }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(modifier = Modifier.fillMaxWidth()
            .clickable {

                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .padding(16.dp, 8.dp), painter = rememberAsyncImagePainter(model = image),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(25.dp))

        val scrollState = rememberScrollState()

        Text(
            viewModel.recognizedText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(
                    scrollState
                )
                .clickable {
                    clipboard.setText(AnnotatedString(viewModel.recognizedText))
                    viewModel.showToast(context, message = "Copied")
                }
        )

    }
}