package com.example.moham.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.moham.inventoryapp.data.InventoryContract.ProductEntry;
import com.example.moham.inventoryapp.data.InventoryDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryActivity extends AppCompatActivity {

    InventoryDbHelper dbHelper;

    @BindView(R.id.text_view)
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = new InventoryDbHelper(this);

        insertData();
        insertData();
        insertData();
        insertData();

        queryData();

    }
    public void insertData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Pixel 2 XL");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1398.00);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 2);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Target");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "1.800.440.0680");

        db.insert(ProductEntry.TABLE_NAME, null, values);
    }

    public void queryData() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(ProductEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        try {
            int indexOfColumnName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int indexOfColumnPrice = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int indexOfColumnQuantity = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int indexOfColumnSupplierName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int indexOfColumnSupplierPhoneNumber = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            output.setText("Output:");
            while (cursor.moveToNext()) {

                String currentName = cursor.getString(indexOfColumnName);
                Double currentPrice = cursor.getDouble(indexOfColumnPrice);
                int currentQuantity = cursor.getInt(indexOfColumnQuantity);
                String currentSupplierName = cursor.getString(indexOfColumnSupplierName);
                String currentSupplierPhoneNumber = cursor.getString(indexOfColumnSupplierPhoneNumber);

                output.append("\n " + currentName + " | " + currentPrice + " | " + currentQuantity + " | " + currentSupplierName + " | " + currentSupplierPhoneNumber);

            }
        }
        finally {
            cursor.close();
        }
    }

}
