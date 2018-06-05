package com.example.user.todoapp.ui.create

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.user.todoapp.R
import com.example.user.todoapp.getRealmConfig
import com.example.user.todoapp.model.Task
import com.example.user.todoapp.showToast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_task.*
import java.util.*

class CreateTask : AppCompatActivity() {

    private lateinit var mRealm: Realm
    private lateinit var mTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        mRealm = Realm.getInstance(getRealmConfig())
    }

    private fun saveTask() {
        if (title_input.text.toString().isNotEmpty()) {
                mRealm.executeTransaction {
                    mTask = it.createObject(Task::class.java, UUID.randomUUID().mostSignificantBits)
                    mTask.title = title_input.text.toString()
                    mTask.description = desc_input.text.toString()
                    mTask.createDate = Date(Calendar.getInstance().timeInMillis)
                    mTask.isExecuted = false
                    finish() }
        } else {
            showToast("Please enter title :)")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_task_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_task -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        mRealm.close()
        super.onDestroy()
    }

}
