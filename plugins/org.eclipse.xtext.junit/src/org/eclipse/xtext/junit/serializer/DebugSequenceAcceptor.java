/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.junit.serializer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.grammaranalysis.impl.GrammarElementTitleSwitch;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.CompositeNode;
import org.eclipse.xtext.serializer.acceptor.ISequenceAcceptor;
import org.eclipse.xtext.util.EmfFormatter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class DebugSequenceAcceptor implements ISequenceAcceptor {

	protected final static int COLS = 5;

	protected static final ICompositeNode NO_NODE = new CompositeNode();

	protected int indentation = 0;

	protected boolean printInstantly;

	protected List<List<String>> table = Lists.newArrayList();

	protected GrammarElementTitleSwitch titles = new GrammarElementTitleSwitch().showAssignments();

	public DebugSequenceAcceptor() {
		this(false);
	}

	public DebugSequenceAcceptor(boolean printInstantly) {
		super();
		this.printInstantly = printInstantly;
	}

	public void acceptAssignedCrossRefDatatype(RuleCall rc, String token, EObject value, int index, ICompositeNode node) {
		add(titles.doSwitch(rc), token, EmfFormatter.objPath(value), index, node);
	}

	public void acceptAssignedCrossRefEnum(RuleCall enumRC, String token, EObject value, int index, ICompositeNode node) {
		add(titles.doSwitch(enumRC), token, EmfFormatter.objPath(value), index, node);
	}

	public void acceptAssignedCrossRefTerminal(RuleCall rc, String token, EObject value, int index, ILeafNode node) {
		add(titles.doSwitch(rc), token, EmfFormatter.objPath(value), index, node);
	}

	public void acceptAssignedDatatype(RuleCall datatypeRC, String token, Object value, int index, ICompositeNode node) {
		add(titles.doSwitch(datatypeRC), token, "'" + value + "'", index, node);
	}

	public void acceptAssignedEnum(RuleCall enumRC, String token, Object value, int index, ICompositeNode node) {
		add(titles.doSwitch(enumRC), token, "'" + value + "'", index, node);
	}

	public void acceptAssignedKeyword(Keyword keyword, String token, Boolean value, int index, ILeafNode node) {
		add(titles.doSwitch(keyword), token, String.valueOf(value), index, node);
	}

	public void acceptAssignedKeyword(Keyword keyword, String token, String value, int index, ILeafNode node) {
		add(titles.doSwitch(keyword), token, "'" + value + "'", index, node);
	}

	public void acceptAssignedTerminal(RuleCall terminalRC, String token, Object value, int index, ILeafNode node) {
		add(titles.doSwitch(terminalRC), token, "'" + value + "'", index, node);
	}

	public void acceptComment(AbstractRule rule, String token, ILeafNode node) {
		add(titles.doSwitch(rule), token, "", -1, node);
	}

	public void acceptUnassignedAction(Action action) {
		add(titles.doSwitch(action), "", "", -1, NO_NODE);
	}

	public void acceptUnassignedDatatype(RuleCall datatypeRC, String value, ICompositeNode node) {
		add(titles.doSwitch(datatypeRC), "'" + value + "'", "", -1, node);
	}

	public void acceptUnassignedEnum(RuleCall enumRC, String value, ICompositeNode node) {
		add(titles.doSwitch(enumRC), "'" + value + "'", "", -1, node);
	}

	public void acceptUnassignedKeyword(Keyword keyword, String token, ILeafNode node) {
		add(titles.doSwitch(keyword), "'" + token + "'", "", -1, node);
	}

	public void acceptUnassignedTerminal(RuleCall terminalRC, String value, ILeafNode node) {
		add(titles.doSwitch(terminalRC), "'" + value + "'", "", -1, node);
	}

	public void acceptWhitespace(AbstractRule rule, String token, ILeafNode node) {
		add(titles.doSwitch(rule), token, "", -1, node);
	}

	protected void add(String grammar, String token, String value, int index, INode node) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < indentation; i++)
			buf.append("  ");
		buf.append(grammar);
		List<String> row = Lists.newArrayList();
		row.add(buf.toString());
		row.add(token);
		row.add(value);
		row.add(index < 0 ? "" : String.valueOf(index));
		row.add(node == NO_NODE ? "" : node2text(node));
		table.add(row);
		if (printInstantly)
			System.out.println(Joiner.on('\t').join(row));
	}

	public boolean enterAssignedAction(Action action, EObject semanticChild, ICompositeNode node) {
		add(titles.doSwitch(action) + " {", semanticChild.eClass().getName(), "", -1, node);
		indentation++;
		return true;
	}

	public boolean enterAssignedParserRuleCall(RuleCall rc, EObject newCurrent, ICompositeNode node) {
		add(titles.doSwitch(rc) + " {", newCurrent.eClass().getName(), "", -1, node);
		indentation++;
		return true;
	}

	public void enterUnassignedParserRuleCall(RuleCall rc) {
		add(titles.doSwitch(rc) + " {", "", "", -1, NO_NODE);
		indentation++;
	}

	public void finish() {
	}

	public List<String> getColumn(int i) {
		List<String> result = Lists.newArrayList();
		for (List<String> line : table)
			if (line.size() > i && line.get(i) != null && line.get(i).length() > 0)
				result.add(line.get(i));
		return result;
	}

	public List<String> getList() {
		int columns = 5;

		Integer width[] = new Integer[columns - 1];
		Arrays.fill(width, Integer.valueOf(1));
		for (List<String> line : table)
			for (int i = 0; i < line.size() && i < width.length; i++)
				width[i] = Math.max(width[i], line.get(i).length());

		String format = "%-" + Joiner.on("s %-").join(width) + "s %s";
		List<String> lines = Lists.newArrayList();
		for (List<String> line : table)
			lines.add(String.format(format, line.toArray()));
		return lines;
	}

	public List<List<String>> getTable() {
		return table;
	}

	public void leaveAssignedAction(Action action, EObject semanticChild) {
		indentation--;
		add("}", "", "", -1, NO_NODE);
	}

	public void leaveAssignedParserRuleCall(RuleCall rc, EObject semanticChild) {
		indentation--;
		add("}", "", "", -1, NO_NODE);
	}

	public void leaveUnssignedParserRuleCall(RuleCall rc) {
		indentation--;
		add("}", "", "", -1, NO_NODE);
	}

	protected String node2text(INode node) {
		if (node == null)
			return "(node is null)";
		if (node instanceof ILeafNode)
			return titles.doSwitch(node.getGrammarElement()) + " -> " + node.getText();
		if (node instanceof ICompositeNode)
			return titles.doSwitch(node.getGrammarElement());
		return "(unknown node)";
	}

	@Override
	public String toString() {
		return Joiner.on("\n").join(getList());
	}
}
