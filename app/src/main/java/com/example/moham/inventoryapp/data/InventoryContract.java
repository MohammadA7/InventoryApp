package com.example.moham.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {


    public static final String CONTENT_AUTHORITY = "com.example.moham.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String INVENTORY_PATH = "products";

    private InventoryContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "Products";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PRODUCT_NAME = "ProductName";

        public static final String COLUMN_PRODUCT_PRICE = "Price";

        public static final String COLUMN_PRODUCT_QUANTITY = "Quantity";

        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "SupplierName";

        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "SupplierPhoneNumber";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INVENTORY_PATH);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + INVENTORY_PATH;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + INVENTORY_PATH;
    }
}
