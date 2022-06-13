package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment: Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        //binding.edit.setText(activity?.intent?.getStringExtra(Intent.EXTRA_TEXT))
        //arguments?.textArg
            //?.let(binding.edit::setText)
        if(arguments != null)
        {
            arguments?.textArg
                ?.let(binding.edit::setText)
        }else {
            if (viewModel.getDraft() != null) binding.edit.setText(viewModel.getDraft()?.content)
        }
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            /*val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }*/
            //activity?.finish()
            viewModel.save(binding.edit.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                viewModel.saveDraft(binding.edit.text.toString())
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
            )
        return binding.root
    }
}