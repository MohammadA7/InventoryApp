package com.example.moham.inventoryapp.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    public static final class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "Products";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PRODUCT_NAME = "ProductName";

        public static final String COLUMN_PRODUCT_PRICE = "Price";

        public static final String COLUMN_PRODUCT_QUANTITY = "Quantity";

        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "SupplierName";

        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "SupplierPhoneNumber";

    }
}
