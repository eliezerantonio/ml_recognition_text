package com.example.machineleraningtextrecognition.views

import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.machineleraningtextrecognition.R
import com.example.machineleraningtextrecognition.viewModel.ScannerViewModel
import java.io.File
import java.util.Date
import java.util.Objects
import kotlin.contracts.contract

@Composable
fun CameraView(viewModel: ScannerViewModel) {

    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current


    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        file
    )

    var image by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val imageDefault = R.drawable.photo

    val persmissionCheckResult =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            image = uri
        }


    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

            viewModel.showToast(context, "Permission granted")
            cameraLauncher.launch(uri)

        }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(modifier = Modifier
            .fillMaxWidth()
            .clickable {

                if (persmissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
            .padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(if (image.path?.isNotBlank() == true) image else imageDefault),
            contentDescription = null
        ).apply {
            viewModel.onRecognizedtext(image, context)
        }

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


//creating temporary image
fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timestamp + "_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}