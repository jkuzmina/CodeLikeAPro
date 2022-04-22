package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var post = Post(
            1,
            getResources().getString(R.string.title),
            getResources().getString(R.string.content),
            getResources().getString(R.string.published),
            false,
            10349,
            400
        )
        with(binding){
            title.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = post.getCountStr(post.likesCount)
            sharesCount.text = post.getCountStr(post.sharesCount)
            if(post.likedByMe){
                likes?.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            likes?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if(post.likedByMe) post.likesCount++ else post.likesCount--
                likesCount.text = post.getCountStr(post.likesCount)
                likes.setImageResource(
                    if(post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
            shares?.setOnClickListener {
                post.sharesCount++
                sharesCount.text = post.getCountStr(post.sharesCount)
                likes.setImageResource(
                    if(post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }

        }

    }
}