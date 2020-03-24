package com.old.database.to;

import android.provider.BaseColumns;


public class SectionContent {

	// This class cannot be instantiated
	private SectionContent() {
	}

	/**
	 * Notes table
	 */
	public static final class SectionContentColumns implements BaseColumns {

		private SectionContentColumns() {
		}

		/**
		 * The TODAY_DATE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TODAY_DATE = "da";

		/**
		 * The YESTERDAY_DATE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String YESTERDAY_DATE = "yd";

		/**
		 * The HOLDS_ARTICLE
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String IS_HOLDS_ARTICLE = "s";

		/** this id is equal to followings
		 * {@link com.thehindubackend.to.Section.SectionColumns.SECTION_ID} or
		 * {@link com.thehindubackend.to.SubSection.SubSectionColumns.SUB_SECTION_ID} or
		 * {@link com.thehindubackend.to.Home.HomeColumns.HOME_ID} or
		 * The CONTENT_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CONTENT_ID = "sid";


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
		 * Type: TEXT
		 * </P>
		 */
		public static final String CONTENT_TYPE = "type";

		/**
		 * The HOME_PRIORITY
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String HOME_PRIORITY = "priority";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = HOME_PRIORITY
				+ " COLLATE NOCASE ASC";
	}

}
