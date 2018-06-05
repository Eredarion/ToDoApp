package com.example.user.todoapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.user.todoapp.R
import com.example.user.todoapp.getRealmConfig
import com.example.user.todoapp.model.Task
import com.example.user.todoapp.ui.create.CreateTask
import com.example.user.todoapp.ui.detail.TaskDetailsActivity
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*


class TaskListActivity : AppCompatActivity(),
                         BaseQuickAdapter.OnItemClickListener,
                         BaseQuickAdapter.OnItemChildClickListener,
                         BaseQuickAdapter.OnItemLongClickListener {


    private lateinit var mRealm: Realm
    private val mAdapter = TaskListAdapter()
    private lateinit var mDataTasks: MutableList<Task>
    private lateinit var mEmptyView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        mRealm = Realm.getInstance(getRealmConfig())

        initView()
        initAdapter()
    }

    private fun initView() {
        mEmptyView = layoutInflater.inflate(R.layout.empty_view, recycler_view.parent as ViewGroup, false)
        mEmptyView.setOnClickListener { startCreateTaskActivity() }

        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initAdapter() {
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        mAdapter.onItemLongClickListener = this
        mAdapter.emptyView = mEmptyView
    }

    override fun onResume() {
        super.onResume()
        getAllTask()
    }

    private fun getAllTask() {
        mDataTasks = mRealm.where(Task::class.java)
                .findAllAsync()
                .sort("createDate", Sort.DESCENDING)
                .toMutableList()
        mAdapter.replaceData(mDataTasks)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val task = mAdapter.getItem(position)!!
        val intent = Intent(this, TaskDetailsActivity::class.java)
        intent.putExtra(TaskDetailsActivity().TASK_ID, task.id)
        startActivity(intent)
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        AlertDialog.Builder(this)
                .setTitle("Deleting!")
                .setMessage("Delete this task?")
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    val task = mAdapter.getItem(position)!!
                    mRealm.executeTransaction {
                        it.where(Task::class.java)
                                .equalTo("id", task.id)
                                .findAll()
                                .deleteAllFromRealm()
                        getAllTask()
                    }
                })
                .setNegativeButton(android.R.string.cancel, { _, _ -> })
                .show()
        return true
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
        when (view.id) {
            R.id.task_is_executed -> {
                mRealm.executeTransaction {
                    val task = mAdapter.getItem(position)!!
                    task.isExecuted = !task.isExecuted!!
                    it.insertOrUpdate(task)
                }
            }
        }
    }

    private fun deleteAllDoneTask() {
        AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setMessage("Delete all done tasks?")
                .setPositiveButton(android.R.string.ok, {_, _ ->
                    mRealm.executeTransaction {
                        it.where(Task::class.java)
                                .equalTo("isExecuted", true)
                                .findAll()
                                .deleteAllFromRealm()
                        getAllTask()
                    }
                })
                .setNegativeButton(android.R.string.cancel, {_, _ -> })
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_task -> {
                startCreateTaskActivity()
                true
            }
            R.id.delete_all -> {
                deleteAllDoneTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startCreateTaskActivity() {
        startActivity(Intent(this, CreateTask::class.java))
    }

    override fun onDestroy() {
        mRealm.close()
        super.onDestroy()
    }

}
