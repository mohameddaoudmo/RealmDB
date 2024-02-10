package com.example.realmdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.realmdatabase.ui.theme.RealmDatabaseTheme
import com.example.realmdb.model.Courses

class MainActivity : ComponentActivity() {
    private val viewmodel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealmDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val courses by viewmodel.courses.collectAsState()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        items(courses) { course ->
                            studentItem(course = course,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable { viewmodel.showDetails(course) })
                        }
                    }
                    if (viewmodel.courseDetails != null) {
                        Dialog(  onDismissRequest = viewmodel::hideDetails) {
                            Column(
                                modifier = Modifier
                                    .widthIn(200.dp, 300.dp)
                                    .clip(
                                        RoundedCornerShape(15.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            ) {
                                viewmodel.courseDetails?.teacher?.addresse?.let {
                                    Text(text = it.city, fontSize = 16.sp)
                                    Text(text = it.fullName, fontSize = 16.sp)
                                    Text(text = it.street, fontSize = 16.sp)
                                    Text(text = it.houseName, fontSize = 16.sp)
                                    Button(
                                        onClick =  viewmodel::deletecourse ,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Text(text = "delete")

                                    }

                                }

                            }

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun studentItem(course: Courses, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = course.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = "held by ${course.teacher?.addresse?.fullName}", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = course.enrolledStudent.joinToString { it.name })


    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}