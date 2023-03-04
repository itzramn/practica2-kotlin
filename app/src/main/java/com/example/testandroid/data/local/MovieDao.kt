package com.example.testandroid.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.MovieType

@Dao
interface MovieDao {

    @Query("SELECT * FROM movieEntity where movie_type = :movieType")
    fun getAllMovies(movieType: Int): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: MovieEntity)

}