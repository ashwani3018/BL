package com.old.database.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.old.database.Constants;
import com.old.database.to.Article.ArticleColumns;
import com.old.database.to.CompanyName;
import com.old.database.to.Home;
import com.old.database.to.RelatedArticle.RelatedArticleColumns;
import com.old.database.to.Section.SectionColumns;
import com.old.database.to.SubSection.SubSectionColumns;


public class HinduProvider extends ContentProvider {

    public static final Uri SECTION_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.SECTION_TABLE_NAME);
    public static final Uri SUB_SECTION_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.SUB_SECTION_TABLE_NAME);
    public static final Uri HOME_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.HOME_TABLE_NAME);
    public static final Uri ARTICLE_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.ARTICLE_TABLE_NAME);
    public static final Uri RELATED_ARTICLE_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.RELATED_ARTICLE_TABLE_NAME);
    public static final Uri FAVORITE_ARTICLE_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.FAVORITE_ARTICLE_TABLE_NAME);
    public static final Uri FAVORITE_RELATED_ARTICLE_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME);
    public static final Uri COMPANY_NAME_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.COMPANY_NAME_TABLE_NAME);
    public static final Uri PREFERENCE_URI = Uri.parse("content://"
            + Constants.AUTHORITY + "/" + Constants.PREFERENCE_TABLE_NAME);

    private static final int SECTION = 1;
    private static final int SECTION_ID = 2;
    private static final int SUB_SECTION = 3;
    private static final int SUB_SECTION_ID = 4;
    private static final int HOME = 5;
    private static final int HOME_ID = 6;
    private static final int ARTICLE = 7;
    private static final int ARTICLE_ID = 8;
    private static final int RELATED_ARTICLE = 9;
    private static final int RELATED_ARTICLE_ID = 10;
    private static final int FAVORITE_ARTICLE = 11;
    private static final int FAVORITE_ARTICLE_ID = 12;
    private static final int FAVORITE_RELATED_ARTICLE = 13;
    private static final int FAVORITE_RELATED_ARTICLE_ID = 14;
    private static final int COMPANY_NAME = 15;
    private static final int COMPANY_NAME_ID = 16;
    private static final int PREFERENCE = 17;
    private static final int PREFERENCE_ID = 18;

    /**
     * The MIME type of a directory of events
     */
    private static final String SECTION_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.SECTION_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String SECTION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.SECTION_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String SUB_SECTION_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.SUB_SECTION_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String SUB_SECTION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.SUB_SECTION_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String HOME_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.HOME_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String HOME_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.HOME_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String ARTICLE_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String ARTICLE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String RELATED_ARTICLE_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.RELATED_ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String RELATED_ARTICLE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.RELATED_ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String FAVORITE_ARTICLE_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.FAVORITE_ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String FAVORITE_ARTICLE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.FAVORITE_ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a directory of events
     */
    private static final String FAVORITE_RELATED_ARTICLE_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String FAVORITE_RELATED_ARTICLE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME;

    private static final String COMPANY_NAME_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.COMPANY_NAME_TABLE_NAME;

    /**
     * The MIME type of a single event
     */
    private static final String COMPANY_NAME_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.COMPANY_NAME_TABLE_NAME;

    private static final String PREFERENCE_CONTENT_TYPE = "vnd.android.cursor.dir/" + Constants.PREFERENCE_TABLE_NAME;
    private static final String PREFERENCE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + Constants.PREFERENCE_TABLE_NAME;

    private UriMatcher uriMatcher;
    private HinduDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Constants.AUTHORITY, Constants.SECTION_TABLE_NAME, SECTION);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.SECTION_TABLE_NAME + "/#", SECTION_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.SUB_SECTION_TABLE_NAME, SUB_SECTION);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.SUB_SECTION_TABLE_NAME + "/#", SUB_SECTION_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.HOME_TABLE_NAME, HOME);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.HOME_TABLE_NAME + "/#", HOME_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.ARTICLE_TABLE_NAME, ARTICLE);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.ARTICLE_TABLE_NAME + "/#", ARTICLE_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.RELATED_ARTICLE_TABLE_NAME, RELATED_ARTICLE);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.RELATED_ARTICLE_TABLE_NAME + "/#", RELATED_ARTICLE_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.FAVORITE_ARTICLE_TABLE_NAME, FAVORITE_ARTICLE);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.FAVORITE_ARTICLE_TABLE_NAME + "/#", FAVORITE_ARTICLE_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME, FAVORITE_RELATED_ARTICLE);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME + "/#", FAVORITE_RELATED_ARTICLE_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.COMPANY_NAME_TABLE_NAME, COMPANY_NAME);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.COMPANY_NAME_TABLE_NAME + "/#", COMPANY_NAME_ID);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.PREFERENCE_TABLE_NAME, PREFERENCE);
        uriMatcher.addURI(Constants.AUTHORITY, Constants.PREFERENCE_TABLE_NAME + "/#", PREFERENCE_ID);

        dbHelper = new HinduDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String orderBy) {

        dbHelper.getWritableDatabase();
        String database;
        String limitString = null;

		/*
         * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
        switch (uriMatcher.match(uri)) {
            case SECTION:
                database = Constants.SECTION_TABLE_NAME;
                break;
            case SUB_SECTION:
                database = Constants.SUB_SECTION_TABLE_NAME;
                break;
            case HOME:
                database = Constants.HOME_TABLE_NAME;
                break;
            case ARTICLE:
                database = Constants.ARTICLE_TABLE_NAME;
                break;
            case RELATED_ARTICLE:
                database = Constants.RELATED_ARTICLE_TABLE_NAME;
                break;
            case FAVORITE_ARTICLE:
                database = Constants.FAVORITE_ARTICLE_TABLE_NAME;
                break;
            case FAVORITE_RELATED_ARTICLE:
                database = Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME;
                break;
            case COMPANY_NAME:
                database = Constants.COMPANY_NAME_TABLE_NAME;
                break;
            case PREFERENCE:
                database = Constants.PREFERENCE_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and run the query
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String suffix = "=?";
        if (selection != null) {
            if (selection.contains("AND")) {
                String[] arr = selection.split("AND");
                int len = arr.length;
                int count = 1;
                selection = "";
                for (String st : arr) {
                    if (count == len) {
                        selection += st + suffix;
                    } else {
                        selection += st + suffix + " AND ";
                    }
                    count++;
                }
            } else {
                selection = selection + suffix;
            }
        }


        if (orderBy != null) {
            if (orderBy.contains("LIMIT")) {
                String[] arr = orderBy.split("LIMIT");
                orderBy = arr[0];
                limitString = arr[1];
            }
        }

        Cursor cursor = db.query(database, projection, selection,
                selectionArgs, null, null, orderBy, limitString);

        // Tell the cursor what uri to watch, so it knows when its source data
        // changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {

		/*
         * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
        switch (uriMatcher.match(uri)) {
            case SECTION:
                return SECTION_CONTENT_TYPE;
            case SECTION_ID:
                return SECTION_CONTENT_ITEM_TYPE;
            case SUB_SECTION:
                return SUB_SECTION_CONTENT_TYPE;
            case SUB_SECTION_ID:
                return SUB_SECTION_CONTENT_ITEM_TYPE;
            case HOME:
                return HOME_CONTENT_TYPE;
            case HOME_ID:
                return HOME_CONTENT_ITEM_TYPE;
            case ARTICLE:
                return ARTICLE_CONTENT_TYPE;
            case ARTICLE_ID:
                return ARTICLE_CONTENT_ITEM_TYPE;
            case RELATED_ARTICLE:
                return RELATED_ARTICLE_CONTENT_TYPE;
            case RELATED_ARTICLE_ID:
                return RELATED_ARTICLE_CONTENT_ITEM_TYPE;
            case FAVORITE_ARTICLE:
                return FAVORITE_ARTICLE_CONTENT_TYPE;
            case FAVORITE_ARTICLE_ID:
                return FAVORITE_ARTICLE_CONTENT_ITEM_TYPE;
            case FAVORITE_RELATED_ARTICLE:
                return FAVORITE_RELATED_ARTICLE_CONTENT_TYPE;
            case FAVORITE_RELATED_ARTICLE_ID:
                return FAVORITE_RELATED_ARTICLE_CONTENT_ITEM_TYPE;
            case COMPANY_NAME:
                return COMPANY_NAME_CONTENT_TYPE;
            case COMPANY_NAME_ID:
                return COMPANY_NAME_CONTENT_ITEM_TYPE;
            case PREFERENCE:
                return PREFERENCE_CONTENT_TYPE;
            case PREFERENCE_ID:
                return PREFERENCE_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri newUri;
        long id;

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
        switch (uriMatcher.match(uri)) {
            case SECTION:
                // Insert into database
                id = db.insertOrThrow(Constants.SECTION_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(SECTION_URI, id);
                break;
            case SUB_SECTION:
                // Insert into database
                id = db.insertOrThrow(Constants.SUB_SECTION_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(SUB_SECTION_URI, id);
                break;
            case HOME:
                // Insert into database
                id = db.insertOrThrow(Constants.HOME_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(HOME_URI, id);
                break;
            case ARTICLE:
                // Insert into database
                id = db.insertOrThrow(Constants.ARTICLE_TABLE_NAME, null, values);
//                id = db.insertWithOnConflict(Constants.ARTICLE_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(ARTICLE_URI, id);
                break;
            case RELATED_ARTICLE:
                // Insert into database
                id = db.insertOrThrow(Constants.RELATED_ARTICLE_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(RELATED_ARTICLE_URI, id);
                break;
            case FAVORITE_ARTICLE:
                // Insert into database
                id = db.insertOrThrow(Constants.FAVORITE_ARTICLE_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(FAVORITE_ARTICLE_URI, id);
                break;
            case FAVORITE_RELATED_ARTICLE:
                // Insert into database
                id = db.insertOrThrow(Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(FAVORITE_RELATED_ARTICLE_URI, id);
                break;
            case COMPANY_NAME:
                // Insert into database
                id = db.insertOrThrow(Constants.COMPANY_NAME_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(COMPANY_NAME_URI, id);
                break;
            case PREFERENCE:
                // Insert into database
                id = db.insertOrThrow(Constants.PREFERENCE_TABLE_NAME, null, values);

                // Notify any watchers of the change
                newUri = ContentUris.withAppendedId(PREFERENCE_URI, id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affected;

        /*if (selection != null) {
            selection = selection + "=?";
        }*/

        String suffix = "=?";
        if (selection != null) {
            if (selection.contains("AND")) {
                String[] arr = selection.split("AND");
                int len = arr.length;
                int count = 1;
                selection = "";
                for (String st : arr) {
                    if (count == len) {
                        selection += st + suffix;
                    } else {
                        selection += st + suffix + " AND ";
                    }
                    count++;
                }
            } else {
                selection = selection + suffix;
            }
        }

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
        switch (uriMatcher.match(uri)) {
            case SECTION:
                affected = db.delete(Constants.SECTION_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case SUB_SECTION:
                affected = db.delete(Constants.SUB_SECTION_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case HOME:
                affected = db.delete(Constants.HOME_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case ARTICLE:
                affected = db.delete(Constants.ARTICLE_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case RELATED_ARTICLE:
                affected = db.delete(Constants.RELATED_ARTICLE_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case FAVORITE_ARTICLE:
                affected = db.delete(Constants.FAVORITE_ARTICLE_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case FAVORITE_RELATED_ARTICLE:
                affected = db.delete(Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case COMPANY_NAME:
                affected = db.delete(Constants.COMPANY_NAME_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PREFERENCE:
                affected = db.delete(Constants.PREFERENCE_TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int affected;

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
        switch (uriMatcher.match(uri)) {
            case SECTION:
                affected = db.update(Constants.SECTION_TABLE_NAME, values,
                        SectionColumns._ID + "=?", selectionArgs);
                break;
            case SUB_SECTION:
                affected = db.update(Constants.SUB_SECTION_TABLE_NAME, values,
                        SubSectionColumns._ID + "=?", selectionArgs);
                break;
            case HOME:
                affected = db.update(Constants.HOME_TABLE_NAME, values,
                        Home.HomeColumns._ID + "=?", selectionArgs);
                break;
            case ARTICLE:
                affected = db.update(Constants.ARTICLE_TABLE_NAME, values,
                        ArticleColumns._ID + "=?", selectionArgs);
                break;
            case RELATED_ARTICLE:
                affected = db.update(Constants.RELATED_ARTICLE_TABLE_NAME, values,
                        RelatedArticleColumns._ID + "=?", selectionArgs);
                break;
            case FAVORITE_ARTICLE:
                affected = db.update(Constants.FAVORITE_ARTICLE_TABLE_NAME, values,
                        ArticleColumns._ID + "=?", selectionArgs);
                break;
            case FAVORITE_RELATED_ARTICLE:
                affected = db.update(Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME, values,
                        ArticleColumns._ID + "=?", selectionArgs);
                break;
            case COMPANY_NAME:
                affected = db.update(Constants.COMPANY_NAME_TABLE_NAME, values,
                        CompanyName.CompanyNameColumns._ID + "=?", selectionArgs);
                break;
            case PREFERENCE:
                affected = db.update(Constants.PREFERENCE_TABLE_NAME, values,
                        SectionColumns._ID + "=?", selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }
}
