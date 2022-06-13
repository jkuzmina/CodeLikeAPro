package ru.netology.nmedia

import android.content.Context
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.viewmodel.PostViewModel

class OnInteractionListenerImpl(val viewModel: PostViewModel, context: Context): OnInteractionListener {

    /*override fun onEdit(post: Post) {
        viewModel.edit(post)
    }

    override fun onLike(post: Post) {
        viewModel.likeById(post.id)
    }

    override fun onShare(post: Post) {
        viewModel.shareById(post.id)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, post.content)
            type = "text/plain"
        }

        val shareIntent =
            Intent.createChooser(intent, getString(getResources()))
        startActivity(shareIntent)
    }

    override fun onRemove(post: Post) {
        viewModel.removeById(post.id)
    }

    override fun onPlay(post: Post) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
        startActivity(intent)

    }

    override fun onOpen(post: Post) {
        findNavController().navigate(
            R.id.action_feedFragment_to_postFragment,
            Bundle().apply{
                longArg = post.id
            })
    }*/
}