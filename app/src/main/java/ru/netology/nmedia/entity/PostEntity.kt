package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val author:String,
    val content:String,
    val published:String,
    var likedByMe:Boolean,
    var likesCount:Int,
    var sharesCount:Int,
    val video:String = ""
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likesCount, sharesCount, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likesCount, dto.sharesCount, dto.video)

    }
}

