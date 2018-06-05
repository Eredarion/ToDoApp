package com.example.user.todoapp

import android.content.Context
import android.widget.Toast
import io.realm.RealmConfiguration


fun getRealmConfig(): RealmConfiguration {
    return RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
