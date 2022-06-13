package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostDraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val author:String,
    val content:String,
    val published:String,
) {
    fun toDto() = Post(id, author, content, published, false, 0, 0, "")

    companion object {
        fun fromDto(dto: Post) =
            PostDraftEntity(dto.id, dto.author, dto.content, dto.published)

    }
}

