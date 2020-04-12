package com.bethecoder.ascii_table.impl;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.AlignmentValues;
import com.bethecoder.ascii_table.spec.IASCIITable;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This class renders a ASCII table as an HTML table
 * 
 * @author ben
 *
 */
public class HTMLASCIITableImpl implements IASCIITable {

	@Override
	public String getTable(ASCIITableHeader[] headerObjs,
			String[][] data) {
		StringBuilder tableHTML = new StringBuilder("<table>");

		AlignmentValues[] dataAlignments;

		if (headerObjs.length > 0) {
			dataAlignments = buildTableHeader(headerObjs, tableHTML);
		} else {
			dataAlignments = new AlignmentValues[data[0].length];

			for (int i = 0; i < dataAlignments.length; i++) {
				dataAlignments[i] = DEFAULT_DATA_ALIGN;
			}
		}

		buildTableBody(data, tableHTML, dataAlignments);

		tableHTML.append("</table>");

		return tableHTML.toString();
	}

	private static void buildTableBody(String[][] data,
			StringBuilder tableHTML, AlignmentValues[] dataAlignments) {
		tableHTML.append("<tbody>");

		for (int i = 0; i < data.length; i++) {
			String[] row = data[i];

			tableHTML.append("<tr>");

			for (int j = 0; j < row.length; j++) {
				String cell = row[j];

				tableHTML.append("<td text=\"text-align: ");

				tableHTML.append(
						convertAlignmentValueToCSS(dataAlignments[j]));

				tableHTML.append(";\">");

				tableHTML.append(StringEscapeUtils.escapeHtml4(cell));

				tableHTML.append("</td>");
			}

			tableHTML.append("</tr>");
		}

		tableHTML.append("</tbody>");
	}

	private static AlignmentValues[] buildTableHeader(
			ASCIITableHeader[] headers, StringBuilder tableHTML) {
		AlignmentValues[] dataAlignments = new AlignmentValues[headers.length];
		dataAlignments = new AlignmentValues[headers.length];

		tableHTML.append("<thead><tr>");

		for (int i = 0; i < headers.length; i++) {
			ASCIITableHeader header = headers[i];

			dataAlignments[i] = header.getDataAlign();

			tableHTML.append("<th style=\"text-align: ");

			tableHTML.append(
					convertAlignmentValueToCSS(header.getHeaderAlign()));

			tableHTML.append(";\" >");

			tableHTML.append(
					StringEscapeUtils.escapeHtml4(header.getHeaderName()));

			tableHTML.append("</th>");
		}

		tableHTML.append("</tr></thead>");

		return dataAlignments;
	}

	private static String convertAlignmentValueToCSS(
			AlignmentValues align) {
		switch (align) {
			case ALIGN_CENTER:
				return "center";
			case ALIGN_LEFT:
				return "left";
			case ALIGN_RIGHT:
				return "right";
			default:
				return "justify";
		}
	}

	@Override
	public <ContainedType> String getTable(
			IASCIITableAware<ContainedType> asciiTableAware) {
		StringBuilder tableHTML = new StringBuilder();

		tableHTML.append("<table>");

		List<ASCIITableHeader> headers = asciiTableAware.getHeaders();

		AlignmentValues[] dataAlignment;

		if (headers.isEmpty()) {
			dataAlignment = new AlignmentValues[asciiTableAware.getData()
					.get(0).size()];
		} else {
			dataAlignment = buildTableHeader(
					headers.toArray(new ASCIITableHeader[0]), tableHTML);
		}

		tableHTML.append("<tbody>");

		int rowCounter = 0;

		for (List<ContainedType> row : asciiTableAware.getData()) {
			tableHTML.append("<tr>");

			int colCounter = 0;
			for (ContainedType containedType : row) {
				tableHTML.append("<td style=\"text-align: ");

				tableHTML.append(convertAlignmentValueToCSS(
						dataAlignment[colCounter]));

				tableHTML.append(";\">");

				String dat = asciiTableAware.formatData(
						headers.get(colCounter), rowCounter, colCounter,
						containedType);

				if (dat == null) {
					dat = String.valueOf(containedType);
				}

				tableHTML.append(StringEscapeUtils.escapeHtml4(dat));

				colCounter++;

				tableHTML.append("</td>");
			}

			tableHTML.append("</tr>");

			rowCounter++;
		}

		tableHTML.append("</tbody>");

		tableHTML.append("</table");

		return tableHTML.toString();
	}

	@Override
	public String getTable(String[] stringHeaders,
			AlignmentValues headerAlign, String[][] data,
			AlignmentValues dataAlign) {
		ASCIITableHeader[] headers = new ASCIITableHeader[stringHeaders.length];

		for (int i = 0; i < stringHeaders.length; i++) {
			String header = stringHeaders[i];

			headers[i] = new ASCIITableHeader(header, dataAlign,
					headerAlign);
		}

		return getTable(headers, data);
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
	public void printTable(ASCIITableHeader[] headerObjs,
			String[][] data) {
		System.out.println(getTable(headerObjs, data));
	}

	@Override
	public <ContainedType> void printTable(
			IASCIITableAware<ContainedType> asciiTableAware) {
		System.out.println(asciiTableAware);
	}

	@Override
	public void printTable(String[] header, AlignmentValues headerAlign,
			String[][] data, AlignmentValues dataAlign) {
		System.out.println(getTable(header, headerAlign, data, dataAlign));
	}

	@Override
	public void printTable(String[] header, String[][] data) {
		System.out.println(getTable(header, data));
	}

	@Override
	public void printTable(String[] header, String[][] data,
			AlignmentValues dataAlign) {
		System.out.println(getTable(header, data, dataAlign));
	}

}
