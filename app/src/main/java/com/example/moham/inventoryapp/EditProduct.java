package com.example.moham.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moham.inventoryapp.data.InventoryContract.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProduct extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    Uri currentUri;
    boolean editMode;

    @BindView(R.id.editName)
    EditText name;

    @BindView(R.id.editPrice)
    EditText price;

    @BindView(R.id.editQuantity)
    EditText quantity;

    @BindView(R.id.editSupName)
    EditText supName;

    @BindView(R.id.editPhone)
    EditText phoneNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        currentUri = intent.getData();

        if (currentUri == null) {
            editMode = false;
            setTitle(getString(R.string.add_product));
        } else {
            editMode = true;
            setTitle(getString(R.string.edit_product));
            int id = (int) ContentUris.parseId(currentUri);
            getLoaderManager().initLoader(id, null, this);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                savePet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePet() {

        String nameValue = String.valueOf(name.getText()).trim();
        String priceString = String.valueOf(price.getText()).trim();
        String quantityString = String.valueOf(quantity.getText()).trim();
        String supNameValue = String.valueOf(supName.getText()).trim();
        String phoneNumberValue = String.valueOf(phoneNumber.getText()).trim();
        int quantityValue = 0;
        double priceValue = 0;
        if (!TextUtils.isEmpty(nameValue) && !TextUtils.isEmpty(priceString)) {

            if (!TextUtils.isEmpty(quantityString)) {
                quantityValue = Integer.parseInt(quantityString);
                if (quantityValue < 0) {
                    Toast.makeText(this, getString(R.string.please_input_correct_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                priceValue = Double.parseDouble(priceString);
                if (priceValue < 0) {
                    Toast.makeText(this, getString(R.string.please_input_correct_info), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameValue);
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceValue);
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityValue);
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supNameValue);
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, phoneNumberValue);

            if (editMode) {
                int rows = getContentResolver().update(currentUri, values, null, null);

                if (rows > 0) {
                    Toast.makeText(this, getString(R.string.product_updated), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProduct.this, InventoryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.product_couldnt_update), Toast.LENGTH_SHORT).show();
                }
            } else {
                Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

                if (newUri != null) {
                    Toast.makeText(this, getString(R.string.product_added), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProduct.this, InventoryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.product_couldnt_added), Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(this, getString(R.string.please_input_correct_info), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        String selection = ProductEntry._ID + "=?";
        String selectionArgs[] = {String.valueOf(ContentUris.parseId(currentUri))};

        return new CursorLoader(this, currentUri, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor = (Cursor) data;

        if (cursor.moveToNext()) {
            name.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME)));
            price.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE))));
            quantity.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY))));
            supName.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)));
            phoneNumber.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER))));
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        name.setText("");
        price.setText("");
        quantity.setText("");
        supName.setText("");
        phoneNumber.setText("");

    }
}
