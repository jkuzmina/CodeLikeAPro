package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likesCount = 0,
    sharesCount = 0
)

class PostViewModel(application: Application): AndroidViewModel(application) {
    private val repository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)


    fun edit(post: Post) {
        edited.value = post
    }

    fun save(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value?.let {
            repository.save(it.copy(content = text))
            repository.deleteDraft()
        }
        edited.value = empty
    }
    fun removeById(id: Long) = repository.removeById(id)

    fun saveDraft(content: String) {
        val text = content.trim()
        repository.saveDraft(empty.copy(content = text))
    }

    fun getDraft(): Post? {
        return repository.getDraft()
    }

    fun deleteDraft() {
        repository.deleteDraft()
    }
}