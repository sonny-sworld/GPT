package com.sonny.gpt.repository

import com.sonny.gpt.App
import com.sonny.gpt.database.ChatDatabase
import com.sonny.gpt.database.entity.ContentEntity

class DatabaseRepository {

    private val context = App.context()
    private val database = ChatDatabase.getDatabase(context)

    fun getContentData() = database.contentDAO().getContentData()

    fun insertContent(content : String, gptOrUser : Int) = database.contentDAO().insertContent(
        ContentEntity(0, content, gptOrUser)
    )

    fun deleteSelectedContent(id : Int) = database.contentDAO().deleteSelectedContent(id)

}