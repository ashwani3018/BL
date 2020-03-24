package com.old.database.to;

import android.provider.BaseColumns;


public class Section {

	// This class cannot be instantiated
	private Section() {
	}

	/**
	 * Notes table
	 */
	public static final class SectionColumns implements BaseColumns {

		private SectionColumns() {
		}


		/**
		 * The SECTION_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SECTION_ID = "secId";

		/**
		 * The SECTION_NAME
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SECTION_NAME = "secName";

		/**
		 * The SECTION_TYPE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SECTION_TYPE = "type";

		/**
		 * The SUB_SECTION_PRIORITY
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String SECTION_PRIORITY = "priority";


		/**
		 * The ENABLED_FOR_HOME_SCREEN
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String ENABLED_FOR_HOME_SCREEN = "enabledForHomeScreen";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = SECTION_PRIORITY
				+ " COLLATE NOCASE ASC";
	}

}
