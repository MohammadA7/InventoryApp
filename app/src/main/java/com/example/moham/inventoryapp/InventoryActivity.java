package com.example.moham.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.moham.inventoryapp.data.InventoryContract.ProductEntry;
import com.example.moham.inventoryapp.data.InventoryDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private final int LOADER_ID = 0;
    MyCursorAdapter cursorAdapter;
    Uri currentUri;

    @BindView(R.id.list_view)
    ListView listView;

    @BindView(R.id.empty_view)
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listView.setEmptyView(emptyView);

        cursorAdapter = new MyCursorAdapter(this, null, getContentResolver());
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this, ProductDetails.class);

                currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.inventory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(InventoryActivity.this, EditProduct.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        cursorAdapter.swapCursor((Cursor) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        cursorAdapter.swapCursor(null);
    }
}
