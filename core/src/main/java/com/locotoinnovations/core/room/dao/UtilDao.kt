package com.locotoinnovations.core.room.dao

import android.database.sqlite.SQLiteException
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Util interface with set of common methods like insert, delete, update, etc.
 */
interface UtilDao<Entity> {

    /**
     * Insert an object in the database.
     * Throws [SQLiteException] if insertion is not possible
     *
     * @param entity the object to be inserted.
     * @return new rowId
     * @throws [SQLiteException]
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: Entity): Long

    /**
     * Insert or replaces an object in the database.
     *
     * WARNING:
     * In case of replace will delete existing which will call triggers,
     * items with foreign key and cascade delete will be deleted.
     * Another possible issue insertion of item that violates constraints and has different primary key,
     * in this case new primary key will be inserted and records in other tables would not be updated
     *
     * This will still throw exception if foreign key is violated
     *
     * @param entity the object to be inserted.
     * @return new rowId
     * @throws [SQLiteException]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entity: Entity): Long

    /**
     * Insert an object in the database or ignores if object exists/constraints violated.
     *
     * @param entity the object to be inserted.
     * @return new rowId or -1 if insertion was not performed
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(entity: Entity): Long

    /**
     * Insert an object in the database.
     * Throws [SQLiteException] if insertion is not possible
     *
     * @param entities objects to be inserted.
     * @return new rowId
     * @throws [SQLiteException]
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entities: List<Entity>): List<Long>

    /**
     * Insert or replaces an object in the database.
     *
     * WARNING:
     * In case of replace will delete existing which will call triggers,
     * items with foreign key and cascade delete will be deleted.
     * Another possible issue insertion of item that violates constraints and has different primary key,
     * in this case new primary key will be inserted and records in other tables would not be updated
     *
     * This will still throw exception if foreign key is violated
     *
     * @param entities objects to be inserted.
     * @return list of new rowIds
     * @throws [SQLiteException]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entities: List<Entity>): List<Long>

    /**
     * Insert an object in the database or ignores if object exists/constraints violated.
     *
     * @param entities objects to be inserted.
     * @return list of new rowIds or -1 if insertion was not performed
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(entities: List<Entity>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param entity the object to be updated
     * @return number of updated items or throws
     * @throws [SQLiteException]
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateOrAbort(entity: Entity): Int

    /**
     * Update or replace an object from the database.
     *
     * @param entity the object to be updated
     * @return number of updated items or throws
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrReplace(entity: Entity): Int

    /**
     * Update or ignore an object from the database.
     *
     * @param entity the object to be updated
     * @return number of updated items or throws
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateOrIgnore(entity: Entity): Int

    /**
     * Update an object from the database.
     *
     * @param entities be updated
     * @return number of updated items or throws
     * @throws [SQLiteException]
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateOrAbort(entities: List<Entity>): Int

    /**
     * Update or replace an object from the database.
     *
     * @param entities be updated
     * @return number of updated items or throws
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrReplace(entities: List<Entity>): Int

    /**
     * Update or ignore an object from the database.
     *
     * @param entities be updated
     * @return number of updated items or throws
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateOrIgnore(entities: List<Entity>): Int

    /**
     * Delete an object from the database
     *
     * @param entity the object to be deleted
     */
    @Delete
    abstract suspend fun delete(entity: Entity): Int

    /**
     * Delete an object from the database
     *
     * @param entities objects to be deleted
     */
    @Delete
    suspend fun delete(entities: List<Entity>): Int
}