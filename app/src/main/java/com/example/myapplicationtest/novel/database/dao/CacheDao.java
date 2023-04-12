package com.example.myapplicationtest.novel.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.renrui.libraries.util.UtilitySecurity;
import com.example.myapplicationtest.novel.database.tb.TbCache;
import com.example.myapplicationtest.novel.utils.UtilityException;

@Dao
public abstract class CacheDao {

    @Update
    public abstract void update(TbCache... entities);

    @Insert
    public abstract void insert(TbCache... entities);

    @Delete
    public abstract void delete(TbCache... entities);

    @Query("select * from TbCache where cType = :cType")
    public abstract TbCache getEntity(String cType);

    public boolean exists(String cType) {
        TbCache entity = getEntity(cType);
        return entity != null && !UtilitySecurity.isEmpty(entity.cContent);
    }

    @Transaction
    public void addOrUpdate(TbCache tbCache) {
        if (tbCache == null)
            return;

        try {
            TbCache entity = getEntity(tbCache.cType);
            if (entity == null) {
                insert(tbCache);
            } else {
                tbCache.id = entity.id;
                update(tbCache);
            }
        } catch (Exception ex) {
            UtilityException.catchException(ex);
        }
    }
}