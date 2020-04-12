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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This class is useful for creating a basic table from a JDBC result set
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class JDBCASCIITableAware implements IASCIITableAware<Object> {
	// The headers in the table
	private List<ASCIITableHeader>	headers	= null;
	// The data in the table
	private List<List<Object>>		data	= null;

	/**
	 * Create an ASCII table from a JDBC result set
	 * 
	 * @param queryResults
	 *            The result set to build the table from
	 */
	public JDBCASCIITableAware(ResultSet queryResults) {
		try {
			init(queryResults);
		} catch (SQLException sqlex) {
			IllegalStateException isex = new IllegalStateException(
					"Unable to get table data ");

			isex.initCause(sqlex);

			throw isex;
		}
	}

	@Override
	public String formatData(ASCIITableHeader header, int row, int col,
			Object cell) {
		// Format only numbers
		try {
			BigDecimal bd = new BigDecimal(cell.toString());

			return NumberFormat.getInstance().format(bd);
		} catch (@SuppressWarnings("unused") NumberFormatException nfex) {
			return cell.toString();
		}
	}

	@Override
	public List<List<Object>> getData() {
		return data;
	}

	@Override
	public List<ASCIITableHeader> getHeaders() {
		return headers;
	}

	private void init(ResultSet queryResult) throws SQLException {
		// Populate headers from result set metadata
		int colCount = queryResult.getMetaData().getColumnCount();

		headers = new ArrayList<>(colCount);

		// Fill in headers names from metadata
		for (int i = 0; i < colCount; i++) {
			headers.add(new ASCIITableHeader(StringUtils.capitalize(
					queryResult.getMetaData().getColumnLabel(i + 1))));
		}

		// Populate data from set
		data = new ArrayList<>();
		
		List<Object> rowData = null;

		while (queryResult.next()) {
			rowData = new ArrayList<>();

			for (int i = 0; i < colCount; i++) {
				rowData.add(queryResult.getObject(i + 1));
			}

			data.add(rowData);
		}
	}
}
