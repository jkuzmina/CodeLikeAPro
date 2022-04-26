package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                title.text = post.author
                published.text = post.published
                content.text = post.content
                likesCount.text = post.getCountStr(post.likesCount)
                sharesCount.text = post.getCountStr(post.sharesCount)
                likes.setImageResource(
                    if(post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
        }
        binding.likes.setOnClickListener {
            viewModel.like()
        }
        binding.shares.setOnClickListener {
            viewModel.share()
        }

    }
}