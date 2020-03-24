package com.old.database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.old.database.Constants;
import com.old.database.to.Article.ArticleColumns;
import com.old.database.to.CompanyName.CompanyNameColumns;
import com.old.database.to.Home.HomeColumns;
import com.old.database.to.RelatedArticle.RelatedArticleColumns;
import com.old.database.to.Section.SectionColumns;
import com.old.database.to.SubSection.SubSectionColumns;


public class HinduDBHelper extends SQLiteOpenHelper {

    public final String TAG = HinduDBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "TheHinduBusinessLine.db";
    public static final int DATABASE_VERSION = 1;

    public HinduDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer sectionQuery = new StringBuffer("CREATE TABLE ");
        sectionQuery.append(Constants.SECTION_TABLE_NAME + " (");
        sectionQuery.append(SectionColumns._ID + " TEXT PRIMARY KEY,");
        sectionQuery.append(SectionColumns.SECTION_ID + " TEXT,");
        sectionQuery.append(SectionColumns.SECTION_NAME + " VARCHAR,");
        sectionQuery.append(SectionColumns.SECTION_TYPE + " VARCHAR,");
        sectionQuery.append(SectionColumns.ENABLED_FOR_HOME_SCREEN + " BOOLEAN NOT NULL default 0,");
        sectionQuery.append(SectionColumns.SECTION_PRIORITY + " INTEGER");
        sectionQuery.append(");");

        StringBuffer homeQuery = new StringBuffer("CREATE TABLE ");
        homeQuery.append(Constants.HOME_TABLE_NAME + " (");
        homeQuery.append(HomeColumns._ID + " TEXT PRIMARY KEY,");
        homeQuery.append(HomeColumns.HOME_ID + " TEXT,");
        homeQuery.append(HomeColumns.HOME_NAME + " VARCHAR,");
        homeQuery.append(HomeColumns.HOME_TYPE + " VARCHAR,");
        homeQuery.append(HomeColumns.HOME_PRIORITY + " INTEGER");
        homeQuery.append(");");

        StringBuffer subSectionQuery = new StringBuffer("CREATE TABLE ");
        subSectionQuery.append(Constants.SUB_SECTION_TABLE_NAME + "(");
        subSectionQuery.append(SubSectionColumns._ID + " TEXT PRIMARY KEY, ");
        subSectionQuery.append(SubSectionColumns.SUB_SECTION_ID + " VARCHAR UNIQUE ,");
        subSectionQuery.append(SubSectionColumns.SUB_SECTION_NAME + " VARCHAR, ");
        subSectionQuery.append(SubSectionColumns.SUB_SECTION_TYPE + " VARCHAR, ");
        subSectionQuery.append(SubSectionColumns.SUB_SECTION_PRIORITY + " INTEGER, ");
        subSectionQuery.append(SubSectionColumns.LATITUDE + " VARCHAR, ");
        subSectionQuery.append(SubSectionColumns.LONGITUDE + " VARCHAR, ");
        subSectionQuery.append(SubSectionColumns.SECTION_ID + " VARCHAR,");
        subSectionQuery.append("FOREIGN KEY(" + SubSectionColumns.SECTION_ID
                + ") REFERENCES " + Constants.SECTION_TABLE_NAME + "("
                + SubSectionColumns._ID + "))");


        StringBuffer articleQuery = new StringBuffer("CREATE TABLE ");
        articleQuery.append(Constants.ARTICLE_TABLE_NAME + "(");
        articleQuery.append(ArticleColumns._ID + " TEXT, ");
//		articleQuery.append(ArticleColumns.ARTICLE_ID + " VARCHAR PRIMARY KEY,");
        articleQuery.append(ArticleColumns.ARTICLE_ID + " VARCHAR ,");
        articleQuery.append(ArticleColumns.ARTICLE_TYPE + " VARCHAR ,");
        articleQuery.append(ArticleColumns.SECTION_CONTENT_ID + " VARCHAR ,");
        articleQuery.append(ArticleColumns.PUBLISH_DATE + " VARCHAR, ");
        articleQuery.append(ArticleColumns.ORIGIN_DATE + " VARCHAR, ");
        articleQuery.append(ArticleColumns.PRIORITY_OF_ARTICLE + " INTEGER, ");
        articleQuery.append(ArticleColumns.ARTICLE_TITLE + " TEXT, ");
        articleQuery.append(ArticleColumns.AUTHOR + " VARCHAR, ");
        articleQuery.append(ArticleColumns.ARTICLE_LINK + " VARCHAR, ");
        articleQuery.append(ArticleColumns.IS_HOLD_BREAKING_NEWS + " BOOLEAN NOT NULL default 0, ");
        articleQuery.append(ArticleColumns.ARTICLE_PUBLISH_DATE + " VARCHAR, ");
        articleQuery.append(ArticleColumns.ARTICLE_DESCRIPTION + " TEXT, ");
        articleQuery.append(ArticleColumns.ARTICLE_LEAD + " TEXT, ");
        articleQuery.append(ArticleColumns.MEDIA + " TEXT, ");
        articleQuery.append(ArticleColumns.HOME_IMG_URL + " TEXT, ");
        articleQuery.append(ArticleColumns.IS_ARTICLE_IMG_AVAILABLE + " BOOLEAN NOT NULL default 0, ");
        articleQuery.append(ArticleColumns.SECTION_CONTENT_NAME + " VARCHAR, ");
        articleQuery.append(ArticleColumns.TODAY_DATE + " VARCHAR, ");
        articleQuery.append(ArticleColumns.YESTERDAY_DATE + " VARCHAR, ");
        articleQuery.append(ArticleColumns.IS_HOLDS_ARTICLE + " BOOLEAN NOT NULL default 0, ");
        articleQuery.append("FOREIGN KEY(" + ArticleColumns.SECTION_CONTENT_ID
                + ") REFERENCES " + Constants.ARTICLE_TABLE_NAME + "("
                + ArticleColumns._ID + "))");


        StringBuffer relatedArticleQuery = new StringBuffer("CREATE TABLE ");
        relatedArticleQuery.append(Constants.RELATED_ARTICLE_TABLE_NAME + "(");
        relatedArticleQuery.append(RelatedArticleColumns._ID + " TEXT PRIMARY KEY, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_ID + " VARCHAR ,");
        relatedArticleQuery.append(RelatedArticleColumns.RELATED_ARTICLE_ID + " VARCHAR ,");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_TYPE + " VARCHAR ,");
        relatedArticleQuery.append(RelatedArticleColumns.SECTION_CONTENT_ID + " VARCHAR ,");
        relatedArticleQuery.append(RelatedArticleColumns.PUBLISH_DATE + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.ORIGIN_DATE + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.PRIORITY_OF_ARTICLE + " INTEGER, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_TITLE + " TEXT, ");
        relatedArticleQuery.append(RelatedArticleColumns.AUTHOR + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_LINK + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.HAS_BREAKING_NEWS + " BOOLEAN NOT NULL default 0, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_PUBLISH_DATE + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_DESCRIPTION + " TEXT, ");
        relatedArticleQuery.append(RelatedArticleColumns.ARTICLE_LEAD + " TEXT, ");
        relatedArticleQuery.append(RelatedArticleColumns.MEDIA + " TEXT, ");
        relatedArticleQuery.append(RelatedArticleColumns.IS_ARTICLE_IMG_AVAILABLE + " BOOLEAN NOT NULL default 0, ");
        relatedArticleQuery.append(RelatedArticleColumns.CONTENT_NAME + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.TODAY_DATE + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.YESTERDAY_DATE + " VARCHAR, ");
        relatedArticleQuery.append(RelatedArticleColumns.IS_HOLDS_ARTICLE + " BOOLEAN NOT NULL default 0, ");
        relatedArticleQuery.append("FOREIGN KEY(" + RelatedArticleColumns.SECTION_CONTENT_ID
                + ") REFERENCES " + Constants.RELATED_ARTICLE_TABLE_NAME + "("
                + RelatedArticleColumns._ID + "))");

        StringBuffer favoriteQuery = new StringBuffer("CREATE TABLE ");
        favoriteQuery.append(Constants.FAVORITE_ARTICLE_TABLE_NAME + "(");
        favoriteQuery.append(ArticleColumns._ID + " TEXT PRIMARY KEY, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_ID + " VARCHAR ,");
        favoriteQuery.append(ArticleColumns.ARTICLE_TYPE + " VARCHAR ,");
        favoriteQuery.append(ArticleColumns.SECTION_CONTENT_ID + " VARCHAR ,");
        favoriteQuery.append(ArticleColumns.PUBLISH_DATE + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.ORIGIN_DATE + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.PRIORITY_OF_ARTICLE + " INTEGER, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_TITLE + " TEXT, ");
        favoriteQuery.append(ArticleColumns.AUTHOR + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_LINK + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.IS_HOLD_BREAKING_NEWS + " BOOLEAN NOT NULL default 0, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_PUBLISH_DATE + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_DESCRIPTION + " TEXT, ");
        favoriteQuery.append(ArticleColumns.ARTICLE_LEAD + " TEXT, ");
        favoriteQuery.append(ArticleColumns.MEDIA + " TEXT, ");
        favoriteQuery.append(ArticleColumns.HOME_IMG_URL + " TEXT, ");
        favoriteQuery.append(ArticleColumns.IS_ARTICLE_IMG_AVAILABLE + " BOOLEAN NOT NULL default 0, ");
        favoriteQuery.append(ArticleColumns.SECTION_CONTENT_NAME + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.TODAY_DATE + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.YESTERDAY_DATE + " VARCHAR, ");
        favoriteQuery.append(ArticleColumns.IS_HOLDS_ARTICLE + " BOOLEAN NOT NULL default 0, ");
        favoriteQuery.append("FOREIGN KEY(" + ArticleColumns.SECTION_CONTENT_ID
                + ") REFERENCES " + Constants.ARTICLE_TABLE_NAME + "("
                + ArticleColumns._ID + "))");

        StringBuffer favoriteRelatedArticleQuery = new StringBuffer("CREATE TABLE ");
        favoriteRelatedArticleQuery.append(Constants.FAVORITE_RELATED_ARTICLE_TABLE_NAME + "(");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns._ID + " TEXT PRIMARY KEY, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_ID + " VARCHAR ,");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.RELATED_ARTICLE_ID + " VARCHAR ,");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_TYPE + " VARCHAR ,");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.SECTION_CONTENT_ID + " VARCHAR ,");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.PUBLISH_DATE + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ORIGIN_DATE + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.PRIORITY_OF_ARTICLE + " INTEGER, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_TITLE + " TEXT, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.AUTHOR + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_LINK + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.HAS_BREAKING_NEWS + " BOOLEAN NOT NULL default 0, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_PUBLISH_DATE + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_DESCRIPTION + " TEXT, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.ARTICLE_LEAD + " TEXT, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.MEDIA + " TEXT, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.IS_ARTICLE_IMG_AVAILABLE + " BOOLEAN NOT NULL default 0, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.CONTENT_NAME + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.TODAY_DATE + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.YESTERDAY_DATE + " VARCHAR, ");
        favoriteRelatedArticleQuery.append(RelatedArticleColumns.IS_HOLDS_ARTICLE + " BOOLEAN NOT NULL default 0, ");
        favoriteRelatedArticleQuery.append("FOREIGN KEY(" + RelatedArticleColumns.SECTION_CONTENT_ID
                + ") REFERENCES " + Constants.RELATED_ARTICLE_TABLE_NAME + "("
                + RelatedArticleColumns._ID + "))");

        StringBuffer companyNameQuery = new StringBuffer("CREATE TABLE ");
        companyNameQuery.append(Constants.COMPANY_NAME_TABLE_NAME + "(");
        companyNameQuery.append(CompanyNameColumns._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, ");
        companyNameQuery.append(CompanyNameColumns.COMPANY_ID + " VARCHAR ,");
        companyNameQuery.append(CompanyNameColumns.COMPANY_NAME + " VARCHAR ,");
        companyNameQuery.append(CompanyNameColumns.COMPANY_BSE + " VARCHAR ,");
        companyNameQuery.append(CompanyNameColumns.COMPANY_NSE + " VARCHAR ,");
        companyNameQuery.append(CompanyNameColumns.COMPANY_GP + " VARCHAR )");

        StringBuffer preferenceQuery = new StringBuffer("CREATE TABLE ");
        preferenceQuery.append(Constants.PREFERENCE_TABLE_NAME + " (");
        preferenceQuery.append(SectionColumns._ID + " TEXT PRIMARY KEY,");
        preferenceQuery.append(SectionColumns.SECTION_ID + " TEXT,");
        preferenceQuery.append(SectionColumns.SECTION_NAME + " VARCHAR,");
        preferenceQuery.append(SectionColumns.SECTION_TYPE + " VARCHAR,");
        preferenceQuery.append(SectionColumns.ENABLED_FOR_HOME_SCREEN + " BOOLEAN NOT NULL default 0,");
        preferenceQuery.append(SectionColumns.SECTION_PRIORITY + " INTEGER");
        preferenceQuery.append(");");

        db.execSQL(homeQuery.toString());

        db.execSQL(sectionQuery.toString());

        db.execSQL(subSectionQuery.toString());

        db.execSQL(articleQuery.toString());

        db.execSQL(relatedArticleQuery.toString());

        db.execSQL(favoriteQuery.toString());

        db.execSQL(favoriteRelatedArticleQuery.toString());

        db.execSQL(companyNameQuery.toString());

        db.execSQL(preferenceQuery.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + Constants.SECTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SUB_SECTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.HOME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.COMPANY_NAME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PREFERENCE_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Downgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SECTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SUB_SECTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.COMPANY_NAME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PREFERENCE_TABLE_NAME);

        onCreate(db);
    }
}
