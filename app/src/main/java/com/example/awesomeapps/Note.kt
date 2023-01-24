package com.example.awesomeapps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
class Note (
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "created") var created: Date = Date(),
    @ColumnInfo(name = "tag", index = true) var tag: String? = null
) {

}
