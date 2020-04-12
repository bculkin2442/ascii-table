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

import com.bethecoder.ascii_table.impl.SimpleASCIITableImpl;
import com.bethecoder.ascii_table.spec.IASCIITable;

/**
 * Create ASCII table formatters of varying sorts
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class ASCIITableFactory {
	/**
	 * Get a default implementation of IASCIITable
	 * 
	 * @return A default implementation of IASCIITable
	 */
	public static IASCIITable getDefault() {
		return new SimpleASCIITableImpl();
	}
}
