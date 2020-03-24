package com.old.database.to;

import android.provider.BaseColumns;


public class RelatedArticle {

	// This class cannot be instantiated
	private RelatedArticle() {
	}

	/**
	 * Notes table
	 */
	public static final class RelatedArticleColumns implements BaseColumns {

		private RelatedArticleColumns() {
		}


		/**
		 * The ARTICLE_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ARTICLE_ID = "aid";

		/**
		 * The RELATED_ARTICLE_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String RELATED_ARTICLE_ID = "rnaid";

		/**
		 * The ARTICLE_ID
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String ARTICLE_TYPE = "type";

		/**
		 * The SECTION_CONTENT_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SECTION_CONTENT_ID = "sid";

		/**
		 * The PUBLISH_DATE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PUBLISH_DATE = "pd";

		/**
		 * The ORIGIN_DATE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ORIGIN_DATE = "od";

		/**
		 * The PRIORITY_OF_ARTICLE
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String PRIORITY_OF_ARTICLE = "pid";

		/**
		 * The TITLE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ARTICLE_TITLE = "ti";

		/**
		 * The AUTHOR
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String AUTHOR = "au";

		/**
		 * The ARTICLE_LINK
		 * <P>
		 * Type: TEXt
		 * </P>
		 */
		public static final String ARTICLE_LINK = "al";

		/**
		 * The IS_HOLD_BREAKING_NEWS
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String HAS_BREAKING_NEWS = "bk";

		/**
		 * The ARTICLE_PUBLISH_DATE
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String ARTICLE_PUBLISH_DATE = "gmt";

		/**
		 * The ARTICLE_DESCRIPTION
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ARTICLE_DESCRIPTION = "de";

		/**
		 * The ARTICLE_LEAD
		 * <P>
		 * Type: text
		 * </P>
		 */
		public static final String ARTICLE_LEAD = "le";

		/**
		 * The MEDIA
		 * <P>
		 * Type: text
		 * </P>
		 */
		public static final String MEDIA = "me";

		/**
		 * The IS_ARTICLE_IMG_AVAILABLE
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String IS_ARTICLE_IMG_AVAILABLE = "hi";

		/**
		 * The SECTION_CONTENT_NAME
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CONTENT_NAME = "sname";

		/**
		 * The HOME_TYPE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
//		public static final String CONTENT_TYPE = "type";

		/**
		 * The TODAY_DATE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String TODAY_DATE = "da";

		/**
		 * The YESTERDAY_DATE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String YESTERDAY_DATE = "yd";

		/**
		 * The IS_HOLDS_ARTICLE
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String IS_HOLDS_ARTICLE = "s";


		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = PRIORITY_OF_ARTICLE
				+ " COLLATE NOCASE ASC";
	}

}
