package com.google.developers.lettervault.data

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.work.*
import com.google.developers.lettervault.notification.NotificationWorker
import com.google.developers.lettervault.util.LETTER_ID
import com.google.developers.lettervault.util.LetterLock
import com.google.developers.lettervault.util.executeThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Handles data sources and execute on the correct threads.
 */
class DataRepository(private val letterDao: LetterDao) {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = LetterDatabase.getInstance(context)
                    instance = DataRepository(database.letterDao())
                }
                return instance
            }
        }
    }

    /**
     * Get letters with a filtered state for paging.
     */
    fun getLetters(filter: LetterState): LiveData<PagedList<Letter>> {
        val query = getFilteredQuery(filter)
        val letters = letterDao.getLetters(query)
        val data = LivePagedListBuilder(letters, 20)
        return data.build()
    }


    fun getLetter(id: Long): LiveData<Letter> {
        return letterDao.getLetter(id)
    }

    fun getLetterBlocking(id: Long): Letter {
        return letterDao.getLetterBlocking(id)
    }

    fun getRecentLetter(): LiveData<Letter> {
        return letterDao.getRecentLetter()
    }

    fun delete(letter: Letter) = executeThread {
        letterDao.delete(letter)
    }

    /**
     * Add a letter to database and schedule a notification on
     * when the letter vault can be opened.
     */
    fun save(letter: Letter) = executeThread {
        val letterId = letterDao.insert(letter)
        scheduleNotification(letterId)
    }

    private fun scheduleNotification(letterId: Long) {
        val manager = WorkManager.getInstance()

        val workerData = Data.Builder()
            .putLong(LETTER_ID, letterId)
            .build()


        val notifyRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(workerData)
            .build()

        manager.enqueue(notifyRequest)
    }


    /**
     * Update database with a decode letter content and update the opened timestamp.
     */
    fun openLetter(letter: Letter) = executeThread {
        val letterCopy = letter.copy(
            subject = LetterLock.retrieveMessage(letter.subject),
            content = LetterLock.retrieveMessage(letter.content),
            opened = System.currentTimeMillis()
        )

        letterDao.update(letterCopy)
    }

    /**
     * Create a raw query at runtime for filtering the letters.
     */
    private fun getFilteredQuery(filter: LetterState): SimpleSQLiteQuery {
        val now = System.currentTimeMillis()
        val simpleQuery = StringBuilder()
            .append("SELECT * FROM letter ")

        if (filter == LetterState.FUTURE) {
            simpleQuery.append("WHERE expires >= $now OR expires <= $now AND opened IS 0")
        }
        if (filter == LetterState.OPENED) {
            simpleQuery.append("WHERE opened IS NOT 0")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

}
