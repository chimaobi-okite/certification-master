package com.google.developers.lettervault.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.developers.lettervault.data.DataRepository

class HomeViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DataRepository::class.java)
            .newInstance(DataRepository.getInstance(context))
    }

}
