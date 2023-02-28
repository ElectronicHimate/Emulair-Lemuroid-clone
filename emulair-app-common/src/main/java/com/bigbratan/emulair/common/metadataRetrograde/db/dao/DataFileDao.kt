package com.bigbratan.emulair.common.metadataRetrograde.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bigbratan.emulair.common.metadataRetrograde.db.entity.DataFile

@Dao
interface DataFileDao {

    @Query("SELECT * FROM datafiles where gameId = :gameId")
    fun selectDataFilesForGame(gameId: Int): List<DataFile>

    @Query("SELECT * FROM datafiles WHERE lastIndexedAt < :lastIndexedAt")
    fun selectByLastIndexedAtLessThan(lastIndexedAt: Long): List<DataFile>

    @Insert
    fun insert(dataFile: DataFile)

    @Insert
    fun insert(dataFiles: List<DataFile>)

    @Delete
    fun delete(dataFiles: List<DataFile>)
}
