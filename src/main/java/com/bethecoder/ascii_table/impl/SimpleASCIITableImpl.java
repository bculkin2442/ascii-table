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
package com.bethecoder.ascii_table.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.AlignmentValues;
import com.bethecoder.ascii_table.spec.IASCIITable;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This implementation simply renders the tables as ASCII text with ASCII
 * bordering
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class SimpleASCIITableImpl implements IASCIITable {

	private static int getColumnCount(String[] header, String[][] data) {
		int columnCount = 0;

		for (int i = 0; i < data.length; i++) {
			columnCount = Math.max(data[i].length, columnCount);
		}

		columnCount = Math.max(header.length, columnCount);

		return columnCount;
	}

	private static String getFormattedData(int maxLength, String data,
			AlignmentValues align) {
		if (data.length() > maxLength) {
			return data;
		}

		boolean isSpacingLeft = true;

		while (data.length() < maxLength) {
			switch (align) {
				case ALIGN_CENTER:
					if (isSpacingLeft) {
						data = " " + data;
						isSpacingLeft = false;
					} else {
						data = data + " ";
						isSpacingLeft = true;
					}

					break;
				case ALIGN_LEFT:
					data = data + " ";
					break;
				case ALIGN_RIGHT:
					data = " " + data;
					break;
				default:
					break;
			}
		}

		return data;
	}

	private static String[] getHeaders(ASCIITableHeader[] headerObjects) {
		if (headerObjects != null && headerObjects.length > 0) {
			String[] headers = new String[headerObjects.length];

			for (int i = 0; i < headerObjects.length; i++) {
				headers[i] = headerObjects[i].getHeaderName();
			}

			return headers;
		}

		return new String[0];
	}

	private static List<Integer> getMaximumColumnLengths(int columnCount,
			String[] header, String[][] data) {
		List<Integer> maximumColumnLengths = new ArrayList<>(columnCount);
		List<String> columnData = null;

		int maximumLength;

		for (int i = 0; i < columnCount; i++) {
			columnData = new ArrayList<>();

			if (header != null && i < header.length) {
				columnData.add(header[i]);
			}

			for (int j = 0; j < data.length; j++) {
				if (i < data[j].length) {
					columnData.add(data[j][i]);
				} else {
					columnData.add("");
				}
			}

			maximumLength = getMaximumItemLength(columnData);
			maximumColumnLengths.add(maximumLength);
		}

		return maximumColumnLengths;
	}

	private static int getMaximumItemLength(List<String> columnData) {
		int maximumLength = 0;

		for (int i = 0; i < columnData.size(); i++) {
			maximumLength = Math.max(columnData.get(i).length(),
					maximumLength);
		}

		return maximumLength;
	}

	private static String getRowDataBuffer(int columnCount,
			List<Integer> maximumColumnLengths, String[] row,
			ASCIITableHeader[] headerObjects, boolean isHeader) {
		StringBuilder rowBuilder = new StringBuilder();
		String formattedData = null;

		for (int i = 0; i < columnCount; i++) {
			AlignmentValues align;

			if (isHeader) {
				align = DEFAULT_HEADER_ALIGN;
			} else {
				align = DEFAULT_DATA_ALIGN;
			}

			if (headerObjects != null && i < headerObjects.length) {
				if (isHeader) {
					align = headerObjects[i].getHeaderAlign();
				} else {
					align = headerObjects[i].getDataAlign();
				}
			}

			if (i < row.length) {
				formattedData = row[i];
			} else {
				formattedData = "";
			}

			formattedData = "| "
					+ getFormattedData(maximumColumnLengths.get(i),
							formattedData, align)
					+ " ";

			if (i + 1 == columnCount) {
				formattedData += "|";
			}

			rowBuilder.append(formattedData);
		}

		return rowBuilder.append("\n").toString();
	}

	/**
	 * Each string item rendering requires the border and a space on both
	 * sides.
	 * 
	 * 12 3 12 3 12 34 +----- +-------- +------+ abc venkat last
	 * 
	 * @param columnCount
	 *            The number of columns to render
	 * @param maximumColumnLengths
	 *            The maximum length of each column
	 * @param data
	 *            The data being stored
	 * @return The row seperating the lines
	 */
	private static String getRowSeparatorLine(int columnCount,
			List<Integer> maximumColumnLengths, String[][] data) {
		StringBuilder rowBuilder = new StringBuilder();
		int columnWidth = 0;

		for (int i = 0; i < columnCount; i++) {
			columnWidth = maximumColumnLengths.get(i) + 3;

			for (int j = 0; j < columnWidth; j++) {
				if (j == 0) {
					rowBuilder.append("+");
				} else if ((i + 1 == columnCount
						&& j + 1 == columnWidth)) {
					// for last column close the border
					rowBuilder.append("-+");
				} else {
					rowBuilder.append("-");
				}
			}
		}

		return rowBuilder.append("\n").toString();
	}

	@Override
	public String getTable(ASCIITableHeader[] headerObjects,
			String[][] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException(
					"Please provide valid data : "
							+ Arrays.toString(data));
		}

		/**
		 * Buffer holding the table string
		 */
		StringBuilder tableBuilder = new StringBuilder();

		/**
		 * Get maximum number of columns across all rows
		 */
		String[] header = getHeaders(headerObjects);

		int columnCount = getColumnCount(header, data);

		/**
		 * Get maximum length of data in each column
		 */
		List<Integer> maximumColumnLengths = getMaximumColumnLengths(
				columnCount, header, data);

		/**
		 * Check for the existence of headers
		 */
		if (header != null && header.length > 0) {
			/**
			 * 1. Row line
			 */
			tableBuilder.append(getRowSeparatorLine(columnCount,
					maximumColumnLengths, data));

			/**
			 * 2. Header line
			 */
			tableBuilder.append(getRowDataBuffer(columnCount,
					maximumColumnLengths, header, headerObjects, true));
		}

		/**
		 * 3. Row data lines
		 */
		tableBuilder.append(getRowSeparatorLine(columnCount,
				maximumColumnLengths, data));

		String[] rowData = null;

		// Build row data buffer by iterating through all rows
		for (int i = 0; i < data.length; i++) {
			// Build cell data in each row
			rowData = new String[columnCount];

			for (int j = 0; j < columnCount; j++) {
				if (j < data[i].length) {
					rowData[j] = data[i][j];
				} else {
					rowData[j] = "";
				}
			}

			tableBuilder.append(getRowDataBuffer(columnCount,
					maximumColumnLengths, rowData, headerObjects, false));
		}

		/**
		 * 4. Row line
		 */
		tableBuilder.append(getRowSeparatorLine(columnCount,
				maximumColumnLengths, data));
		return tableBuilder.toString();
	}

	@Override
	public <ContainedType> String getTable(
			IASCIITableAware<ContainedType> asciiTableAware) {
		ASCIITableHeader[] headerObjects = new ASCIITableHeader[0];
		ASCIITableHeader columnHeader = null;

		String[][] data = new String[0][0];

		List<ContainedType> rowData = null;
		ArrayList<String> transformedRowData = null;

		String[] rowContent = null;
		String cellData = null;

		if (asciiTableAware != null) {
			/**
			 * Get the row header.
			 */
			if (asciiTableAware.getHeaders() != null
					&& !asciiTableAware.getHeaders().isEmpty()) {
				int headerCount = asciiTableAware.getHeaders().size();

				headerObjects = new ASCIITableHeader[headerCount];

				for (int i = 0; i < headerCount; i++) {
					headerObjects[i] = asciiTableAware.getHeaders().get(i);
				}
			}

			/**
			 * Get the data.
			 */
			if (asciiTableAware.getData() != null
					&& !asciiTableAware.getData().isEmpty()) {
				int dataCount = asciiTableAware.getData().size();

				data = new String[dataCount][];

				for (int i = 0; i < dataCount; i++) {
					// Get the data from the row
					rowData = asciiTableAware.getData().get(i);

					transformedRowData = new ArrayList<>(rowData.size());

					// Transform each cell in the row
					for (int j = 0; j < rowData.size(); j++) {
						if (j < headerObjects.length) {
							columnHeader = headerObjects[j];
						} else {
							columnHeader = null;
						}

						// Transform the data using the transformation
						// function provided.
						cellData = asciiTableAware.formatData(columnHeader,
								i, j, rowData.get(j));

						if (cellData == null) {
							cellData = String.valueOf(rowData.get(j));
						}

						transformedRowData.add(cellData);
					} // iterate all columns

					// Set the transformed content
					rowContent = new String[transformedRowData.size()];

					int rowCount = 0;

					for (Iterator<String> iterator = transformedRowData
							.iterator(); iterator.hasNext();) {
						String string = iterator.next();

						rowContent[rowCount++] = string;
					}

					data[i] = rowContent;
				} // iterate all rows
			} // end data
		}

		return getTable(headerObjects, data);
	}

	@Override
	public String getTable(String[] headers, AlignmentValues headerAlign,
			String[][] data, AlignmentValues dataAlign) {
		ASCIITableHeader[] headerObjects = new ASCIITableHeader[0];

		if (headers != null && headers.length > 0) {
			headerObjects = new ASCIITableHeader[headers.length];

			for (int i = 0; i < headers.length; i++) {
				headerObjects[i] = new ASCIITableHeader(headers[i],
						dataAlign, headerAlign);
			}
		}

		return getTable(headerObjects, data);
	}

	@Override
	public String getTable(String[] header, String[][] data) {
		return getTable(header, DEFAULT_HEADER_ALIGN, data,
				DEFAULT_DATA_ALIGN);
	}

	@Override
	public String getTable(String[] header, String[][] data,
			AlignmentValues dataAlign) {
		return getTable(header, DEFAULT_HEADER_ALIGN, data, dataAlign);
	}

	@Override
	public void printTable(ASCIITableHeader[] headerObjects,
			String[][] data) {
		System.out.println(getTable(headerObjects, data));
	}

	@Override
	public <ContainedType> void printTable(
			IASCIITableAware<ContainedType> asciiTableAware) {
		System.out.println(getTable(asciiTableAware));
	}

	@Override
	public void printTable(String[] header, AlignmentValues headerAlign,
			String[][] data, AlignmentValues dataAlign) {
		System.out.println(getTable(header, headerAlign, data, dataAlign));
	}

	@Override
	public void printTable(String[] header, String[][] data) {
		printTable(header, DEFAULT_HEADER_ALIGN, data, DEFAULT_DATA_ALIGN);
	}

	@Override
	public void printTable(String[] header, String[][] data,
			AlignmentValues dataAlign) {
		printTable(header, DEFAULT_HEADER_ALIGN, data, dataAlign);
	}
}