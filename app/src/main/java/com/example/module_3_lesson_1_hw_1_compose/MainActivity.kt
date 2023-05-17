package com.example.module_3_lesson_1_hw_1_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.module_3_lesson_1_hw_1_compose.ui.theme.Mint10
import com.example.module_3_lesson_1_hw_1_compose.ui.theme.Mint40
import com.example.module_3_lesson_1_hw_1_compose.ui.theme.Mint90
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// Написать органайзер. На главном экране список дел (у каждого дела: тема, описание, время, и
// возможность сделать важным и оно выделится в списке более другим цветом). Можно
// создавать дела, редактировать, удалять.
// Дела сохраняются в БД, т.е. не пропадают при новом запуске приложения


class MainActivity : ComponentActivity(), DbThreadAddNewTaskCallback, DbThreadCallback {


    private val tasksState =  mutableStateOf(emptyList<Task>())
    private val callbackNewTask: DbThreadAddNewTaskCallback = this
    private val callbackUpdatedTask: DbThreadCallback = this

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            LaunchedEffect(Unit) {
                    val tasks = withContext(Dispatchers.IO) {

                        // code

                        val db = App.instance.database
                        val taskDao = db.taskDao
                        val tasks = taskDao.allTask

//                        updateTasksState()

                        if (tasks.isNotEmpty()){
                            Log.d("MYLOG", "${tasks[0].title} ${tasks[1].title}")

                            tasksState.value = tasks
                        } else {
                            Log.d("MYLOG", " DB is empty.")
                        }




                    }
            }




            Box(modifier = Modifier.fillMaxSize()) {


                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(tasksState.value){ index, task ->


                        val isEditingState = remember { mutableStateOf(false) }
                        val textFieldValueTitleState = remember { mutableStateOf(task.title) }
                        val textFieldValueDescriptionState = remember { mutableStateOf(task.description) }


                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isEditingState.value) 192.dp else 96.dp)
                                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            colors = CardDefaults.cardColors(
                                if (task.flagged) Mint40 else Mint10
                            ),
                            onClick = {

                                if (!isEditingState.value){
                                    DbThread(task.id, callbackUpdatedTask)
                                }



                            }
                        ) {
                            Box(){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxHeight()
                                ) {


                                    Column (
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        if (isEditingState.value) {
                                            TextField(
                                                value = textFieldValueTitleState.value,
                                                onValueChange = {newValue ->
                                                    textFieldValueTitleState.value = newValue

                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {

                                                    }
                                                ),
                                                modifier = Modifier
                                                    .padding(start = 24.dp),
                                                textStyle = TextStyle(fontSize = 16.sp)
                                            )

                                            TextField(
                                                value = textFieldValueDescriptionState.value,
                                                onValueChange = {newValue ->
                                                    textFieldValueDescriptionState.value = newValue

                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {

                                                    }
                                                ),
                                                modifier = Modifier
                                                    .padding(start = 24.dp),
                                                textStyle = TextStyle(fontSize = 16.sp)
                                            )

                                        } else {
                                            Text(
                                                text = tasksState.value[index].title,
                                                modifier = Modifier
                                                    .padding(start = 32.dp),
                                                style = TextStyle(fontSize = 16.sp),
                                            )
                                            Text(
                                                text = tasksState.value[index].description,
                                                modifier = Modifier
                                                    .padding(start = 32.dp),
                                                style = TextStyle(fontSize = 12.sp)
                                            )
                                        }



                                    }


                                    if (!isEditingState.value){
                                        Image(
                                            painter = painterResource(id = R.drawable.flag_6464),
                                            contentDescription = "flag",
                                            modifier = Modifier
                                                .alpha(if (task.flagged) 1f else 0f)
                                                .size(48.dp)
                                                .weight(0.2f)
                                        )
                                    }



                                    if (isEditingState.value) {
                                        Column() {
                                            Image(
                                                painter = painterResource(id = R.drawable.done),
                                                contentDescription = "edit",
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .offset(x = (-24).dp, y = 8.dp)
                                                    .weight(0.2f)
                                                    .clickable {
                                                        isEditingState.value = false

                                                        tasksState.value[index].title =
                                                            textFieldValueTitleState.value
                                                        tasksState.value[index].description =
                                                            textFieldValueDescriptionState.value

                                                        runBlocking {
                                                            launch(Dispatchers.IO) {
                                                                updateTitle(
                                                                    task.id,
                                                                    textFieldValueTitleState.value
                                                                )
                                                                updateDescription(
                                                                    task.id,
                                                                    textFieldValueDescriptionState.value
                                                                )
                                                                updateTasksState()
                                                            }
                                                        }

                                                    },



                                                )

                                            Image(
                                                painter = painterResource(id = R.drawable.delete),
                                                contentDescription = "edit",
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .offset(x = (-24).dp, y = (-8).dp)
                                                    .weight(0.2f)
                                                    .clickable {
                                                        isEditingState.value = false

                                                        runBlocking {
                                                            launch(Dispatchers.IO) {

                                                                deleteTask(task.id)
                                                                updateTasksState()
                                                            }
                                                        }
                                                    },



                                                )
                                        }
                                    } else {
                                        Image(
                                            painter = painterResource(id = R.drawable.edit_icon_64x64),
                                            contentDescription = "edit",
                                            modifier = Modifier
                                                .size(32.dp)
                                                .offset(x = (-8).dp)
                                                .weight(0.2f)
                                                .clickable {
                                                    isEditingState.value = true
                                                    textFieldValueTitleState.value = task.title
                                                    textFieldValueDescriptionState.value =
                                                        task.description
                                                },



                                            )
                                    }



                                }
                            }

                        }
                    }
                }

                AddButton(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 24.dp),
                    callbackNewTask
                )


            }


        }

    }

    private fun updateTitle(idItemClicked: Int, newTitle: String) {
        var db = App.instance.database
        var taskDao = db.taskDao

        val itemById = taskDao.getById(idItemClicked)

        itemById.title = newTitle

        taskDao.update(itemById)

    }

    private fun updateDescription(idItemClicked: Int, newDescription: String) {
        var db = App.instance.database
        var taskDao = db.taskDao

        val itemById = taskDao.getById(idItemClicked)

        itemById.description = newDescription

        taskDao.update(itemById)

    }

    private fun deleteTask(idItemClicked: Int) {
        var db = App.instance.database
        var taskDao = db.taskDao

        val itemById = taskDao.getById(idItemClicked)

        taskDao.delete(itemById)

    }

    private fun updateTasksState() {
        val db = App.instance.database
        val taskDao = db.taskDao
        val tasks = taskDao.allTask

        tasksState.value = tasks

    }




    override fun onTaskAdded(task: Task) {
        updateTasksState()
    }



    override fun onTaskUpdated(task: Task) {
        updateTasksState()
    }
}





@Composable
fun AddButton(modifier: Modifier, dbThreadAddNewTaskCallback: DbThreadAddNewTaskCallback) {
    Button(
        onClick = {


                  DbThreadAddNewTask(dbThreadAddNewTaskCallback)


        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(Mint90),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ){
        Text(text = "Add")

    }
}