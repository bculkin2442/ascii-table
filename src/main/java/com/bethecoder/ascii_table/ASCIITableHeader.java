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
package com.bethecoder.ascii_table;

import com.bethecoder.ascii_table.spec.AlignmentValues;
import com.bethecoder.ascii_table.spec.IASCIITable;

/**
 * Represents a header in an ASCII table
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class ASCIITableHeader {
	/*
	 * The name of the header
	 */
	private String			headerName;

	/*
	 * The alignment settings of this column
	 */
	private AlignmentValues	headerAlign	= IASCIITable.DEFAULT_HEADER_ALIGN;
	private AlignmentValues	dataAlign	= IASCIITable.DEFAULT_DATA_ALIGN;

	/**
	 * Create a new ASCII table header
	 * 
	 * @param headerName
	 *            The name of this header
	 */
	public ASCIITableHeader(String headerName) {
		this.headerName = headerName;
	}

	/**
	 * Create a new ASCII table header
	 * 
	 * @param headerName
	 *            The name of the header
	 * @param dataAlign
	 *            The alignment setting for the data in the column this
	 *            header is attached to
	 */
	public ASCIITableHeader(String headerName, AlignmentValues dataAlign) {
		this(headerName);

		this.dataAlign = dataAlign;
	}

	/**
	 * Create a new ASCII table header
	 * 
	 * @param headerName
	 *            The name of the header
	 * @param headerAlign
	 *            The alignment setting for the header
	 * @param dataAlign
	 *            The alignment setting for the data in the column this
	 *            header is attached to
	 */
	public ASCIITableHeader(String headerName, AlignmentValues dataAlign,
			AlignmentValues headerAlign) {
		this(headerName, dataAlign);

		this.headerAlign = headerAlign;
	}

	/**
	 * Get the alignment of the data in the column this header is attached
	 * to
	 * 
	 * @return The alignment of the data in the column this header is
	 *         attached to
	 */
	public AlignmentValues getDataAlign() {
		return dataAlign;
	}

	/**
	 * Get the alignment of this header
	 * 
	 * @return The alignment of this header
	 */
	public AlignmentValues getHeaderAlign() {
		return headerAlign;
	}

	/**
	 * Get the name of this header
	 * 
	 * @return The name of this header
	 */
	public String getHeaderName() {
		return headerName;
	}
}