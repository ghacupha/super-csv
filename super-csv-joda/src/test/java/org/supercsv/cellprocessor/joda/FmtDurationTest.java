/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.cellprocessor.joda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.supercsv.cellprocessor.joda.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the FmtDuration cell processor.
 */
public class FmtDurationTest {

	private static final String DURATION_STRING = "PT34304461S";
	private static final DateTime START = new DateTime(2013, 10, 25, 1, 2, 3,
			0, DateTimeZone.UTC);
	private static final DateTime END = new DateTime(2014, 11, 26, 2, 3, 4, 0,
			DateTimeZone.UTC);
	private static final Duration DURATION = new Duration(START, END);

	private FmtDuration processor1;
	private FmtDuration processorChain1;
	private List<FmtDuration> processors;

	@Before
	public void setUp() {
		processor1 = new FmtDuration();
		processorChain1 = new FmtDuration(new IdentityTransform());
		processors = Arrays.asList(processor1, processorChain1);
	}

	@Test
	public void testValidDurationString() {
		for (CellProcessor p : processors) {
			assertEquals(DURATION_STRING,
					p.execute(DURATION, ANONYMOUS_CSVCONTEXT));
		}
	}

	@Test
	public void testNullInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(null, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"this processor does not accept null input - "
								+ "if the column is optional then chain an Optional() processor before this one",
						e.getMessage());
			}
		}
	}

	@Test
	public void testNonDurationInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(123, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"the input value should be of type org.joda.time.Duration but is java.lang.Integer",
						e.getMessage());
			}
		}
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullNext() {
		new FmtDuration((CellProcessor) null);
	}

}
