package com.blazeapps.fooddelivery.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.blazeapps.fooddelivery.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "deliveryFood.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ProductName", "Quantity", "Price", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                res.add(new Order(
                        getItem(cursor, sqlSelect[0]),
                        getItem(cursor, sqlSelect[1]),
                        getItem(cursor, sqlSelect[2]),
                        getItem(cursor, sqlSelect[3]),
                        getItem(cursor, sqlSelect[4])));
            } while (cursor.moveToNext());
        }
        return res;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount)" +
                        "VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    private String getItem(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }
}
