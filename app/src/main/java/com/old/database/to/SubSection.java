package com.old.database.to;

import android.provider.BaseColumns;


public class SubSection {

	// This class cannot be instantiated
	private SubSection() {
	}

	/**
	 * Book table
	 */
	public static final class SubSectionColumns implements BaseColumns {

		private SubSectionColumns() {
		}

		/**
		 * The SUB_SECTION_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SUB_SECTION_ID = "subSectionID";

		/**
		 * The SECTION_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SECTION_ID = "sectionID";

		/**
		 * The SUB_SECTION_NAME
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SUB_SECTION_NAME = "secName";

		/**
		 * The SECTION_TYPE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String SUB_SECTION_TYPE = "type";

		/**
		 * The SUB_SECTION_PRIORITY
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String SUB_SECTION_PRIORITY = "priority";

		/**
		 * The LATITUDE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String LATITUDE = "lat";

		/**
		 * The LONGITUDE
		 * <P>
		 * Type: VARCHAR
		 * </P>
		 */
		public static final String LONGITUDE = "long";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = SUB_SECTION_PRIORITY
				+ " COLLATE NOCASE ASC";
	}

}
