package com.example.user.todoapp.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.user.todoapp.R
import com.example.user.todoapp.getRealmConfig
import com.example.user.todoapp.model.Task
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailsActivity : AppCompatActivity() {


    private val dateFormat = SimpleDateFormat("HH:mm, d/M/yyyy", Locale.US)
    private lateinit var mRealm: Realm
    private lateinit var mTask: Task
    val TASK_ID = "taskId"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        mRealm = Realm.getInstance(getRealmConfig())

        if (intent != null) {
            try {
                mRealm.executeTransaction {
                    mTask = it.where(Task::class.java)
                            .equalTo("id", intent.getIntExtra(TASK_ID, 0))
                            .findFirst()!!
                    showInfo(mTask)
                }
            } finally {
                mRealm.close()
            }
        }
    }

    private fun showInfo(item: Task) {
        task_title.text = item.title
        task_description.text = item.description
        task_create_date.text = dateFormat.format(item.createDate)
        task_is_executed.isChecked = item.isExecuted!!
    }

}
