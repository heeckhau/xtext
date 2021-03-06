/*******************************************************************************
 * Copyright (c) 2011 Sigasi N.V. (http://www.sigasi.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.nodemodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/** @author Mark Christiaens */

public class SyntaxErrorMessageTest extends TestCase {
	public void testSerialization() throws IOException {
		final String message = "hi";
		String [] issueCodes = { null, "issue" };
		String [][] issueDatas = { null, {null}, {"issue data"}};
		
		for (String[] issueData : issueDatas) {
			for (String issueCode : issueCodes) {
				SyntaxErrorMessage sem = new SyntaxErrorMessage(message, issueCode, issueData);
				ByteArrayOutputStream out = new ByteArrayOutputStream ();
				DataOutputStream dout = new DataOutputStream(out); 
				sem.write(dout, null);
				dout.close();
				byte[] array = out.toByteArray();
				ByteArrayInputStream in = new ByteArrayInputStream(array); 
				DataInputStream din = new DataInputStream(in);
				SyntaxErrorMessage sem2 = SyntaxErrorMessage.read(din, null);
				assertEquals(sem, sem2); 
			}
		}
	}
}
