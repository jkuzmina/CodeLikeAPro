package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    companion object {
        var Bundle.longArg: Long? by LongArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )
        val postId = arguments?.longArg ?: -1

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding) {
                title.text = post.author
                published.text = post.published
                content.text = post.content
                likes.isChecked = post.likedByMe
                likes.text = post.getCountStr(post.likesCount)
                shares.text = post.getCountStr(post.sharesCount)
                if (post.video == "") videoGroup.visibility = View.GONE else videoGroup.visibility =
                    View.VISIBLE
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigate(R.id.feedFragment)
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(
                                        R.id.newPostFragment,
                                        Bundle().apply{
                                            textArg = post.content
                                    })
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
                likes.setOnClickListener{
                    viewModel.likeById(post.id)
                }
                shares.setOnClickListener {
                    viewModel.shareById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }
                play.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }
            }
        }
        return binding.root
    }

}