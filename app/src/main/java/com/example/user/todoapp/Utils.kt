package com.example.user.todoapp

import io.realm.RealmConfiguration


fun getRealmConfig(): RealmConfiguration {
    return RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
}
