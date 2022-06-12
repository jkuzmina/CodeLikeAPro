package ru.netology.nmedia.dto

data class Post(
    val id:Long,
    val author:String,
    val content:String,
    val published:String,
    var likedByMe:Boolean,
    var likesCount:Int,
    var sharesCount:Int,
    val video:String = ""
)
{
    fun getCountStr(count:Int):String{
        when
        {
            count in 1000..999_999 -> return String.format("%.1f K", (count * 1.0 / 1_000))
            count >= 1_000_000 -> return String.format("%.1f M", (count * 1.0 / 1_000_000))
            else -> return count.toString()
        }

    }

}

