package com.github.databinding.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.databinding.R
import com.github.databinding.databinding.FragmentBasicBinding


class BasicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBasicBinding.inflate(inflater, container, false)
        binding.isEmpty = true
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = BasicFragment()
    }
}
