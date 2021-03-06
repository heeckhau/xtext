/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.parser.packrat.tokens;

import org.eclipse.xtext.parser.packrat.IParsedTokenVisitor;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public abstract class AbstractParsedToken {

	private int offset;

	private int length;

	private boolean skipped;

	/**
	 * @param input
	 * @param offset
	 * @param length
	 */
	public AbstractParsedToken(int offset, int length) {
		super();
		this.setLength(length);
		this.setOffset(offset);
	}

	public void accept(IParsedTokenVisitor visitor) {
		visitor.visitAbstractParsedToken(this);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public CharSequence getText(CharSequence input) {
		return input.subSequence(offset, offset + length);
	}

	public boolean isSkipped() {
		return skipped;
	}

	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": @" + getOffset() + ":" + getLength() + (skipped?"(skipped)":"");
	}

	public static class End extends AbstractParsedToken {
		public End(int offset) {
			super(offset, 0);
		}

		@Override
		public void accept(IParsedTokenVisitor visitor) {
			visitor.visitAbstractParsedTokenEnd(this);
		}
	}
}
