package com.google.developers.lettervault.ui.add

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.developers.lettervault.R
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import java.text.SimpleDateFormat
import java.util.*


class AddLetterActivity : AppCompatActivity() {
    private lateinit var simpleDate: SimpleDateFormat
    private lateinit var viewModel: AddLetterViewModel

    private var hourOfDay: Int = 0
    private var minuteOfDay: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())
        val factory = AddLetterViewModelFactory(this)

        viewModel = ViewModelProviders.of(this, factory)[AddLetterViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initToolBar()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setMessageTime(simpleDate.format(System.currentTimeMillis()))
        }
    }


    private fun setMessageTime(timeString: String) {
        supportActionBar?.title =
            getString(
                R.string.created_title, timeString
            )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> saveMessage()
            R.id.action_time -> chooseTime()
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveMessage() {
        val subject = editTextSubject.text
        val message = editTextMessage.text

        if (message.isNotEmpty()) {
            viewModel.save(subject.toString(), message.toString(), hourOfDay, minuteOfDay)
            finish()
        } else
            Toast.makeText(this, R.string.cannot_save_message, Toast.LENGTH_LONG).show()
    }


    private fun chooseTime() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (view.isShown) {
                    this.hourOfDay = hourOfDay
                    this.minuteOfDay = minute
                }
            }

        val timePickerDialog = TimePickerDialog(
            this,
            timePickerListener,
            currentHour,
            currentMinute,
            false
        )

        timePickerDialog.setTitle(R.string.time)
        timePickerDialog.show()
    }

}