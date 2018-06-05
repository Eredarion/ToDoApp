package com.example.user.todoapp.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.user.todoapp.R
import com.example.user.todoapp.getRealmConfig
import com.example.user.todoapp.model.Task
import com.example.user.todoapp.showToast
import io.realm.Realm
import io.realm.exceptions.RealmException
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailsActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("HH:mm, d/M/yyyy", Locale.US)
    private lateinit var mRealm: Realm
    private lateinit var mTask: Task
    val TASK_ID = "taskId"
    private val TAG = "TaskDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        mRealm = Realm.getInstance(getRealmConfig())

        getTask()
    }

    private fun getTask() {
        if (intent != null) {
            try {
                mRealm.executeTransaction {
                    mTask = it.where(Task::class.java)
                              .equalTo("id", intent.getLongExtra(TASK_ID, 0))
                              .findFirst()!!
                    showTaskDetails(mTask) }
            } catch (exception: RealmException) {
                Log.d(TAG, exception.printStackTrace().toString())
            } finally {
                mRealm.close()
            }
        } else {
            showToast("Empty")
        }
    }

    private fun showTaskDetails(item: Task) {
        task_title.text = item.title
        task_description.text = item.description
        task_create_date.text = dateFormat.format(item.createDate)
        task_is_executed.isChecked = item.isExecuted!!
    }

}
