/**
 * Copyright (C) 2011 K Venkata Sudhakar <kvenkatasudhakar@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bethecoder.ascii_table.spec;

import com.bethecoder.ascii_table.ASCIITableHeader;

/**
 * Interface specifying the API for an ASCII table
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public interface IASCIITable {
	// Default settings of alignments
	/**
	 * The default alignment setting for headers
	 * 
	 * Default to aligning to the center
	 */
	public static final AlignmentValues	DEFAULT_HEADER_ALIGN	= AlignmentValues.ALIGN_CENTER;
	/**
	 * The default alignment setting for data
	 * 
	 * Default to aligning to the right
	 */
	public static final AlignmentValues	DEFAULT_DATA_ALIGN		= AlignmentValues.ALIGN_RIGHT;

	/**
	 * Create the ASCII table as a string which can be rendered in a
	 * console or in a JSP.
	 * 
	 * @param headerObjs
	 *            The objects for the headers
	 * @param data
	 *            The data in the table
	 * @return The table in string form
	 */
	public String getTable(ASCIITableHeader[] headerObjs, String[][] data);

	/**
	 * Create the ASCII table as a string which can be rendered in a
	 * console or in a JSP.
	 * 
	 * @param <ContainedType>
	 *            The type of data in the table
	 * 
	 * @param asciiTableAware
	 *            The source for table data
	 * @return The table in string form
	 */
	public <ContainedType> String getTable(
			IASCIITableAware<ContainedType> asciiTableAware);

	/**
	 * Create the ASCII table as a string which can be rendered in a
	 * console or in a JSP.
	 * 
	 * @param header
	 *            The headers of the table
	 * @param headerAlign
	 *            The alignment settings for the headers
	 * @param data
	 *            The data in the table
	 * @param dataAlign
	 *            The alignment settings for the data
	 * @return The table in string form
	 */
	public String getTable(String[] header, AlignmentValues headerAlign,
			String[][] data, AlignmentValues dataAlign);

	/**
	 * Create the ASCII table as a string which can be rendered in a
	 * console or in a JSP.
	 * 
	 * @param header
	 *            The headers of the table
	 * @param data
	 *            The data in the table
	 * @return The table in string form
	 */
	public String getTable(String[] header, String[][] data);

	/**
	 * Create the ASCII table as a string which can be rendered in a
	 * console or in a JSP.
	 * 
	 * @param header
	 *            The headers of the table
	 * @param data
	 *            The data in the table
	 * @param dataAlign
	 *            The alignment settings for the data
	 * @return The table in string form
	 */
	public String getTable(String[] header, String[][] data,
			AlignmentValues dataAlign);

	/**
	 * Print the table to console
	 * 
	 * @param headerObjs
	 *            The header objects for the console
	 * @param data
	 *            The data in the table
	 */
	public void printTable(ASCIITableHeader[] headerObjs, String[][] data);

	/**
	 * Print the table to console
	 * 
	 * @param <ContainedType>
	 *            The type of data in the table
	 * 
	 * @param asciiTableAware
	 *            The source for data about the table
	 */
	public <ContainedType> void printTable(
			IASCIITableAware<ContainedType> asciiTableAware);

	/**
	 * Print the ASCII table to console, with the specified data alignment
	 * 
	 * @param header
	 *            The headers of the tables
	 * @param headerAlign
	 *            The way to align the headers
	 * @param data
	 *            The data to print in the table
	 * @param dataAlign
	 *            The way to align the data
	 */
	public void printTable(String[] header, AlignmentValues headerAlign,
			String[][] data, AlignmentValues dataAlign);

	/**
	 * Prints the ASCII table to console.
	 * 
	 * @param header
	 *            The headers of the tables
	 * @param data
	 *            The data to print in the table
	 */
	public void printTable(String[] header, String[][] data);

	/**
	 * Print the ASCII table to console, with the specified data alignment
	 * 
	 * @param header
	 *            The headers of the tables
	 * @param data
	 *            The data to print in the table
	 * @param dataAlign
	 *            The way to align the data
	 */
	public void printTable(String[] header, String[][] data,
			AlignmentValues dataAlign);
}
