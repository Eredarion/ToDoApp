package com.example.user.todoapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task (@PrimaryKey var id: Int? = null,
                 var title: String? = null,
                 var description: String? = null,
                 var isExecuted: Boolean? = null,
                 var createDate: Date? = null) : RealmObject()