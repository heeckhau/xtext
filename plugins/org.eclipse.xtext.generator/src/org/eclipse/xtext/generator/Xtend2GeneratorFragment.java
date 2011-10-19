/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.generator;

import java.util.List;

import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtext.Grammar;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 * @since 2.0
 */
public class Xtend2GeneratorFragment extends DefaultGeneratorFragment implements NamingAware {

	private Naming naming;

	protected Module createModule(final Grammar grammar) {
		return new Module() {
			public void configure(Binder binder) {
				binder.bind(Grammar.class).toInstance(grammar);
				binder.bind(Naming.class).toInstance(naming);
			}
		};
	}

	@Override
	final public void generate(Grammar grammar, XpandExecutionContext ctx) {
		Guice.createInjector(createModule(grammar)).injectMembers(this);
		generate(grammar, new Xtend2ExecutionContext(ctx));
	}
	
	

	/**
	 * @since 2.1
	 */
	public void generate(Grammar grammar, Xtend2ExecutionContext ctx) {
		generate(ctx);
	}
	
	public void generate(Xtend2ExecutionContext ctx) {
	}

	public void registerNaming(Naming n) {
		naming = n;
	}
	
	/**
	 * @since 2.1
	 */
	public Naming getNaming() {
		return naming;
	}

	/**
	 * @since 2.0
	 */
	protected List<String> getExportedPackagesRtList(Grammar grammar) {
		return null; // workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=345955
	}

	@Override
	public String[] getExportedPackagesRt(Grammar grammar) {
		List<String> list = getExportedPackagesRtList(grammar);
		return list == null ? null : list.toArray(new String[list.size()]);
	}

}
