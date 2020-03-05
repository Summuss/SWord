package top.summus.sword.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface BaseDao<T> {
    @Insert
    Single<Long> insert(T entity);

    @Update
    Completable update(T entity);

    @Delete
    Completable delete(T entity);


}
