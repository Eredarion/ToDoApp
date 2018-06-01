package com.example.user.todoapp.ui.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.user.todoapp.R
import com.example.user.todoapp.model.Task
import java.text.SimpleDateFormat
import java.util.*


class TaskListAdapter : BaseQuickAdapter<Task, BaseViewHolder>(R.layout.task_item) {

    private val dateFormat = SimpleDateFormat("HH:mm, d/M/yyyy", Locale.US)

    override fun convert(helper: BaseViewHolder, item: Task) {
        helper.setText(R.id.task_title, item.title)
        helper.setText(R.id.task_description, item.description)
        helper.setText(R.id.task_create_date, dateFormat.format(item.createDate))
        helper.setChecked(R.id.task_is_executed, item.isExecuted!!)
        helper.addOnClickListener(R.id.task_is_executed)
    }

}