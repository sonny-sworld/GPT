package com.sonny.gpt.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonny.gpt.database.entity.ContentEntity

@Dao
interface ContentDAO {

    @Query("SELECT * FROM ContentTable")
    fun getContentData() : List<ContentEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContent(content : ContentEntity)

    @Query("DELETE FROM ContentTable WHERE id = :id")
    fun deleteSelectedContent(id : Int)

}