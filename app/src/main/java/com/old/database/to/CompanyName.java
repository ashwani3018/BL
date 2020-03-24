package com.old.database.to;

import android.provider.BaseColumns;


public class CompanyName {

	// This class cannot be instantiated
	private CompanyName() {
	}

	/**
	 * Notes table
	 */
	public static final class CompanyNameColumns implements BaseColumns {

		private CompanyNameColumns() {
		}


		/**
		 * The SECTION_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String COMPANY_ID = "companyId";

		/**
		 * The SECTION_NAME
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String COMPANY_NAME = "companyName";

		/**
		 * The SECTION_TYPE
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String COMPANY_BSE = "bse";

		/**
		 * The SUB_SECTION_PRIORITY
		 * <P>
		 * Type: int
		 * </P>
		 */
		public static final String COMPANY_NSE = "nse";


		/**
		 * The ENABLED_FOR_HOME_SCREEN
		 * <P>
		 * Type: boolean
		 * </P>
		 */
		public static final String COMPANY_GP = "gp";

		/**
		 * The default sort order for this table
		 */
//		public static final String DEFAULT_SORT_ORDER = SECTION_PRIORITY
//				+ " COLLATE NOCASE ASC";
	}

}
