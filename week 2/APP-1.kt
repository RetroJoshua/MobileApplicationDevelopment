package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Import Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Using MaterialTheme colors
                        Greeting("Android", textColor = MaterialTheme.colorScheme.primary)
                        // Using a predefined Color
                        MyFun(textColor = Color.Red)
                        MyInputField()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, textColor: Color = Color.Unspecified) {
    Text(
        text = "Hello $name!",
        modifier = modifier.padding(bottom = 8.dp),
        color = textColor // Apply the color here
    )
}

@Composable
fun MyFun(modifier: Modifier = Modifier, textColor: Color = Color.Unspecified) {
    Text(
        text = "Welcome to Mobile Application Development",
        modifier = modifier.padding(bottom = 16.dp),
        color = textColor // Apply the color here
    )
}

@Composable
fun MyInputField(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Enter your name") },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Using MaterialTheme colors in preview
            Greeting("Android", textColor = MaterialTheme.colorScheme.primary)
            // Using a predefined Color in preview
            MyFun(textColor = Color.Red)
            MyInputField()
        }
    }
}
