package com.example.moham.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.moham.inventoryapp.data.InventoryContract.ProductEntry;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class InventoryProvider extends ContentProvider {

    private static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.INVENTORY_PATH, PRODUCTS);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.INVENTORY_PATH + "/#", PRODUCT_ID);

    }

    private InventoryDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = uriMatcher.match(uri);

        switch (match) {

            case PRODUCTS:
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Connot query unknown URL" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                id = db.insert(ProductEntry.TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
        if (id == -1) {
            Log.e(LOG_TAG, "failed to insert " + id);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                rows = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int match = uriMatcher.match(uri);
        int rows;

        switch (match) {
            case PRODUCTS:
                rows = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URL" + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}
