package com.old.database.to;

import android.provider.BaseColumns;


public class Home {

	// This class cannot be instantiated
	private Home() {
	}

	/**
	 * Notes table
	 */
	public static final class HomeColumns implements BaseColumns {

		private HomeColumns() {
		}


		/**
		 * The HOME_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String HOME_ID = "subSectionID";

		/**
		 * The HOME_SECTION_NAME
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String HOME_NAME = "secName";

		/**
		 * The HOME_TYPE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String HOME_TYPE = "type";

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
