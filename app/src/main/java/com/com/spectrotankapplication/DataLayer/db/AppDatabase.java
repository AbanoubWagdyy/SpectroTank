package com.com.spectrotankapplication.DataLayer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.spectrotank.DataLayer.db.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDAO getItemDAO();
}