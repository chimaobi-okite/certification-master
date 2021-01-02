package com.google.developers.lettervault.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.RawQuery


/**
 * Room data object for all database interactions.
 *
 * @see Dao
 */
@Dao
interface LetterDao {

    @RawQuery(observedEntities = [Letter::class])
    fun getLetters(query: SupportSQLiteQuery): DataSource.Factory<Int, Letter>

    @Query("SELECT * FROM letter WHERE id = :letterId")
    fun getLetter(letterId: Long): LiveData<Letter>

    @Query("SELECT * FROM letter WHERE id = :letterId")
    fun getLetterBlocking(letterId: Long): Letter

    @Query("SELECT * FROM letter WHERE id = (SELECT MAX(id) FROM letter)")
    fun getRecentLetter(): LiveData<Letter>

    @Insert
    fun insert(letter: Letter): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg letter: Letter)

    @Update
    fun update(letter: Letter)

    @Delete
    fun delete(letter: Letter)

}
