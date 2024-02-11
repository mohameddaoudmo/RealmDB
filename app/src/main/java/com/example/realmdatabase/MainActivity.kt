package com.example.realmdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.realmdatabase.ui.theme.RealmDatabaseTheme
import com.example.realmdb.model.Courses
import kotlinx.coroutines.delay
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.Duration

class MainActivity : ComponentActivity() {
    private val viewmodel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealmDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    val courses by viewmodel.courses.collectAsState()
                    val lazyListState = rememberLazyListState()
                    var isRemoved by remember {
                        mutableStateOf(false)
                    }
                    var isFavourite by remember {
                        mutableStateOf(false)
                    }
                    val state = rememberDismissState(confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            isRemoved = true
                            true
                        } else {
                            false
                        }
                    })


                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        state = lazyListState
                    ) {
                        items(courses, key = null) { course ->
                            val delete = SwipeAction(
                                onSwipe = {
//                                    onSwipeToDelete(it)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete chat",
                                        modifier = Modifier.padding(16.dp),
                                        tint = Color.White
                                    )
                                }, background = Color.Red,
                                isUndo = true
                            )
                            val archive = SwipeAction(
                                onSwipe = {  viewmodel.showDetails(course) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "archive chat",
                                        modifier = Modifier.padding(16.dp),

                                        tint = Color.White

                                    )
                                }, background = Color(0xFF50B384).copy(alpha = 0.7f)
                            )
                            SwipeableActionsBox(
                                modifier = Modifier,
                                swipeThreshold = 40.dp,
                                startActions = listOf(archive),
                                endActions = listOf(delete),
                                backgroundUntilSwipeThreshold = Color.Transparent,
                            ) {

                                studentItem(course = course,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clickable { viewmodel.showDetails(course) })

                            }

//                            SwipeToDismiss(state = state, background = { background(
//                                swipedismissState = state
//                            )} , dismissContent ={
//                                studentItem(course = course,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(16.dp)
//                                        .clickable { viewmodel.showDetails(course) })
//                            } )


                        }
                    }
                    if (viewmodel.courseDetails != null) {
                        Dialog(onDismissRequest = viewmodel::hideDetails) {
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
                                        onClick = viewmodel::deletecourse,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> swipe(
    item: T, ondelete: (T) -> Unit, animationDuration: Int = 500, content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    var isFavourite by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(confirmValueChange = {
        if (it == DismissValue.DismissedToStart) {
            isRemoved = true
            true
        } else {
            false
        }
    })
    LaunchedEffect(key1 = isRemoved) {
        delay(animationDuration.toLong())
        ondelete
    }
    AnimatedVisibility(
        visible = isRemoved, exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration), shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(state = state,
            background = { background(swipedismissState = state) },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
        )

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun studentItem(course: Courses, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = course.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = "held by ${course.teacher?.addresse?.fullName}", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = course.enrolledStudent.joinToString { it.name })


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun background(swipedismissState: DismissState) {
    var color = when (swipedismissState.dismissDirection) {
        DismissDirection.EndToStart -> Color.Red
        DismissDirection.StartToEnd -> Color.Green
        null -> Color.Transparent

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color)
    ) {

        if (swipedismissState.dismissDirection == DismissDirection.EndToStart) {
            Icon(
                imageVector = Icons.Default.Delete, contentDescription = ""
            )
        } else if (swipedismissState.dismissDirection == DismissDirection.StartToEnd) {
            Icon(
                imageVector = Icons.Default.Favorite, contentDescription = ""
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}