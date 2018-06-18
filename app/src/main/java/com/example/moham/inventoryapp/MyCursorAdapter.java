package com.example.moham.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.moham.inventoryapp.data.InventoryContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCursorAdapter extends CursorAdapter {

    static ContentResolver contentResolver;
    Context context;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.quantity)
    TextView quantity;

    @BindView(R.id.sale_button)
    Button saleButton;


    public MyCursorAdapter(Context context, Cursor c, ContentResolver contentResolver) {
        super(context, c, 0);
        this.context = context;
        this.contentResolver = contentResolver;
    }

    public static void sale(int id, int oldValue) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, oldValue - 1);
        contentResolver.update(ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id), values, null, null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ButterKnife.bind(this, view);

        name.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME)));
        price.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE))));
        final int currentQuantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        quantity.setText(String.valueOf(currentQuantity));
        final int id = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry._ID));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity > 0) {
                    sale(id, currentQuantity);
                }
            }
        });
    }
}
