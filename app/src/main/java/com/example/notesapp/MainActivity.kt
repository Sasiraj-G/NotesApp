package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.notesapp.data.NotesDatabase
import com.example.notesapp.presentation.AddNoteScreen
import com.example.notesapp.presentation.NotesMainScreen
import com.example.notesapp.presentation.NotesViewModel
import com.example.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    private val database by lazy{
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()
    }
    private val viewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.dao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    val state by viewModel.state.collectAsState()
                    val navController= rememberNavController()
                    NavHost(navController=navController, startDestination = "NotesMainScreen"){
                        composable("NotesMainScreen"){
                            NotesMainScreen(
                                state=state,
                                navController=navController,
                                onEvent=viewModel::onEvent
                            )
                        }
                        composable("AddNoteScreen"){
                            AddNoteScreen(
                                state=state,
                                navController=navController,
                                onEvent=viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String,modifier: Modifier = Modifier) {

}