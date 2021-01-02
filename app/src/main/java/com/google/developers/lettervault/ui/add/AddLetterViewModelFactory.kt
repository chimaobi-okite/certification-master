package com.google.developers.lettervault.ui.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.developers.lettervault.data.DataRepository

class AddLetterViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DataRepository::class.java)
            .newInstance(DataRepository.getInstance(context))
    }

}
