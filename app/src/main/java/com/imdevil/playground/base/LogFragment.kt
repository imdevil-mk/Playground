package com.imdevil.playground.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class LogFragment : Fragment() {

    open fun getLogTag(): String {
        return "Fragment-${this.javaClass.simpleName}"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(getLogTag(), "onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(getLogTag(), "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(getLogTag(), "onCreateView: ")
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(getLogTag(), "onViewCreated: ")
    }

    @Deprecated("Deprecated in Java")
    @SuppressWarnings
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(getLogTag(), "onActivityCreated: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(getLogTag(), "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(getLogTag(), "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(getLogTag(), "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(getLogTag(), "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(getLogTag(), "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(getLogTag(), "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(getLogTag(), "onDetach: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(getLogTag(), "onSaveInstanceState: ")
    }
}