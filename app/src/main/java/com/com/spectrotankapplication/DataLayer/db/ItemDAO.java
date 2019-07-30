package com.com.spectrotankapplication.DataLayer.db;

import androidx.room.*;
import com.spectrotank.DataLayer.db.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    public void insert(Item... items);

    @Update
    public void update(Item... items);

    @Delete
    public void delete(Item... items);

    @Query("SELECT * FROM items WHERE IMEI = :imeNumber")
    public List<Item> getItems(String imeNumber);

    @Query("SELECT * FROM items")
    public List<Item> getAllItems();
}