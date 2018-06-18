package com.example.moham.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.inventoryapp.data.InventoryContract.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    Uri currentUri;
    int id;
    int currentQuantity;
    String currentPhoneNumber;

    @BindView(R.id.nameTextView)
    TextView name;

    @BindView(R.id.priceTextView)
    TextView price;

    @BindView(R.id.quantityTextView)
    TextView quantity;

    @BindView(R.id.supplierNameTextView)
    TextView supName;

    @BindView(R.id.supplierPhoneNumberTextView)
    TextView phoneNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        currentUri = intent.getData();
        id = (int) ContentUris.parseId(currentUri);

        getLoaderManager().initLoader(id, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.product_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(ProductDetails.this, EditProduct.class);
                intent.setData(currentUri);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void decreaseQuantity(View view) {
        if (currentQuantity > 0) {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, --currentQuantity);
            getContentResolver().update(currentUri, values, null, null);
        }
    }

    public void increaseQuantity(View view) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, ++currentQuantity);
        getContentResolver().update(currentUri, values, null, null);
    }

    public void delete(View view) {

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        getContentResolver().delete(currentUri, null, null);
                        Intent intent = new Intent(ProductDetails.this, InventoryActivity.class);
                        startActivity(intent);
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_massage);
        builder.setPositiveButton(R.string.discard_massage, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_product, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void order(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + currentPhoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
            currentQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
            quantity.setText(String.valueOf(currentQuantity));
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