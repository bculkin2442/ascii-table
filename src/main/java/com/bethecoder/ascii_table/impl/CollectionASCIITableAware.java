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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This class is an implementation of ASCIITable that works well with lists
 * of javabeans
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class CollectionASCIITableAware
		implements IASCIITableAware<Object> {
	// The headers for each of the columns
	private List<ASCIITableHeader>	headers	= null;
	// The data in the table
	private List<List<Object>>		data	= null;

	/**
	 * Create a new table of objects from a collection
	 * 
	 * @param beanList
	 *            The list of objects to convert into a table
	 * @param beanProperties
	 *            The properties in the object
	 * @param propertyHeaders
	 *            The headers for each of the properties
	 */
	public CollectionASCIITableAware(List<Object> beanList,
			List<String> beanProperties, List<String> propertyHeaders) {
		if (beanList != null && !beanList.isEmpty()
				&& beanProperties != null && !beanProperties.isEmpty()) {
			// Fill out table headers if they've been provided
			String header = null;

			headers = new ArrayList<>(beanProperties.size());

			for (int i = 0; i < beanProperties.size(); i++) {
				if (i < propertyHeaders.size()) {
					header = propertyHeaders.get(i);
				} else {
					header = beanProperties.get(i);
				}

				headers.add(new ASCIITableHeader(header.toUpperCase()));
			}

			// Populate data
			data = new ArrayList<>();

			List<Object> rowData = null;

			Class<?> dataClazz = beanList.get(0).getClass();

			Map<String, Method> propertyMethodMap = new HashMap<>();

			for (int i = 0; i < beanList.size(); i++) {
				rowData = new ArrayList<>();

				for (int j = 0; j < beanProperties.size(); j++) {
					rowData.add(getProperty(propertyMethodMap, dataClazz,
							beanList.get(i), beanProperties.get(j)));
				}

				data.add(rowData);
			}
		}
	}

	/**
	 * Create a new ASCII table from a list of objects
	 * 
	 * @param beanList
	 *            The list of object to turn into a table
	 * @param beanProperties
	 *            The defined properties on those objects
	 */
	public CollectionASCIITableAware(List<Object> beanList,
			String... beanProperties) {
		this(beanList, Arrays.asList(beanProperties),
				Arrays.asList(beanProperties));
	}

	private static String capitalize(String property) {
		if (property.length() == 0) {
			return property;
		}

		return property.substring(0, 1).toUpperCase()
				+ property.substring(1).toLowerCase();
	}

	@Override
	public String formatData(ASCIITableHeader rowHeader, int row, int col,
			Object cellData) {
		// Format only numbers
		try {
			BigDecimal decimal = new BigDecimal(cellData.toString());

			return NumberFormat.getInstance().format(decimal);
		} catch (@SuppressWarnings("unused") NumberFormatException nfex) {
			return cellData.toString();
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

	private static Method getMethod(Class<?> beanClass,
			String methodName) {
		try {
			return beanClass.getMethod(methodName, new Class<?>[0]);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsmex) {
			// Punt that we don't have a method by that name
			return null;
		}
	}

	private static Object getProperty(Map<String, Method> accessorMap,
			Class<?> beanClass, Object bean, String beanProperty) {
		try {
			Method accessorMethod = null;

			if (accessorMap.containsKey(beanProperty)) {
				// Retrieve properties we already know about
				accessorMethod = accessorMap.get(beanProperty);
			} else {
				// Attempt to handle properties that follow java bean
				// naming conventions
				String methodName = "get" + capitalize(beanProperty);
				accessorMethod = getMethod(beanClass, methodName);

				if (accessorMethod == null) {
					// Attempt to handle boolean properties
					methodName = "is" + capitalize(beanProperty);
					accessorMethod = getMethod(beanClass, methodName);
				}

				if (accessorMethod != null) {
					// Add any methods we found so we don't have to resolve
					// them again
					accessorMap.put(beanProperty, accessorMethod);
				}
			}

			if (accessorMethod == null) {
				throw new IllegalStateException(
						"Could not find accessor for property "
								+ beanProperty + " of "
								+ beanClass.getSimpleName());
			}

			return accessorMethod.invoke(bean, new Object[0]);
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
}
