package com.example.machineleraningtextrecognition.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import okio.IOException
import java.net.URI

class  ScannerViewModel : ViewModel(){

    var recognizedText by mutableStateOf("")
        private set

    fun cleanText(){
        recognizedText = ""

    }

    fun  onRecognizedtext(text:Any?, context: Context){
        var image: InputImage? = null

        try {
            image = InputImage.fromFilePath(context, text as Uri)
        } catch (e: IOException){
            e.printStackTrace()

        }

        image?.let {
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(it)
                .addOnSuccessListener {  text ->
                    recognizedText = text.text
                }.addOnFailureListener {
                    showToast(context, "Error when read image")
                }
        }

    }
    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()



    }
}