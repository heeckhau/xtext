/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.util.formallang;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtext.util.GraphvizDotBuilder;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class NfaToProduction {

	protected static abstract class AbstractElementAlias<T> {
		protected boolean many = false;

		protected boolean optional = false;

		protected AbstractElementAlias() {
		}

		protected AbstractElementAlias(boolean optional, boolean many) {
			super();
			this.optional = optional;
			this.many = many;
		}

		protected abstract int getElementCount();

		public boolean isMany() {
			return many;
		}

		public boolean isOne() {
			return !optional && !many;
		}

		public boolean isOptional() {
			return optional;
		}

		public void setMany(boolean many) {
			this.many = many;
		}

		public void setOptional(boolean optional) {
			this.optional = optional;
		}

		@Override
		public String toString() {
			Function<Production<AbstractElementAlias<T>, T>, String> formatter = new ProductionFormatter<AbstractElementAlias<T>, T>();
			return formatter.apply(new AliasGrammarProvider<T>(this));
		}

	}

	protected static class AliasGrammarProvider<TOKEN> implements Production<AbstractElementAlias<TOKEN>, TOKEN> {

		protected AbstractElementAlias<TOKEN> root;

		public AliasGrammarProvider(AbstractElementAlias<TOKEN> root) {
			this.root = root;
		}

		public Iterable<AbstractElementAlias<TOKEN>> getAlternativeChildren(AbstractElementAlias<TOKEN> ele) {
			return ele instanceof AlternativeAlias ? ((AlternativeAlias<TOKEN>) ele).getChildren() : null;
		}

		public AbstractElementAlias<TOKEN> getParent(AbstractElementAlias<TOKEN> ele) {
			return null;
		}

		public AbstractElementAlias<TOKEN> getRoot() {
			return root;
		}

		public Iterable<AbstractElementAlias<TOKEN>> getSequentialChildren(AbstractElementAlias<TOKEN> ele) {
			return ele instanceof GroupAlias ? ((GroupAlias<TOKEN>) ele).getChildren() : null;
		}

		public TOKEN getToken(AbstractElementAlias<TOKEN> owner) {
			return owner instanceof ElementAlias ? ((ElementAlias<TOKEN>) owner).getElement() : null;
		}

		public Iterable<AbstractElementAlias<TOKEN>> getUnorderedChildren(AbstractElementAlias<TOKEN> ele) {
			return null;
		}

		public boolean isMany(AbstractElementAlias<TOKEN> ele) {
			return ele.isMany();
		}

		public boolean isOptional(AbstractElementAlias<TOKEN> ele) {
			return ele.isOptional();
		}
	}

	protected static class AlternativeAlias<T> extends AbstractElementAlias<T> {
		protected Set<AbstractElementAlias<T>> children = Sets.newHashSet();

		public AlternativeAlias() {
			super();
		}

		public AlternativeAlias(boolean optional, boolean many, AbstractElementAlias<T>... children) {
			super(optional, many);
			Collections.addAll(this.children, children);
		}

		public void addChild(AbstractElementAlias<T> child) {
			if (child == this)
				throw new RuntimeException();
			children.add(child);
		}

		public Set<AbstractElementAlias<T>> getChildren() {
			return children;
		}

		@Override
		protected int getElementCount() {
			int result = 1;
			for (AbstractElementAlias<T> child : children)
				result += child.getElementCount();
			return result;
		}

	}

	protected static class ElementAlias<T> extends AbstractElementAlias<T> {
		protected T element;

		public ElementAlias(boolean optional, boolean many, T element) {
			super(optional, many);
			this.element = element;
		}

		public ElementAlias(T element) {
			super();
			this.element = element;
		}

		public T getElement() {
			return element;
		}

		@Override
		protected int getElementCount() {
			return 1;
		}

	}

	protected static class GroupAlias<T> extends AbstractElementAlias<T> {
		protected List<AbstractElementAlias<T>> children = Lists.newArrayList();

		public GroupAlias() {
			super();
		}

		public GroupAlias(boolean optional, boolean many, AbstractElementAlias<T>... children) {
			super(optional, many);
			Collections.addAll(this.children, children);
		}

		public void addChild(AbstractElementAlias<T> child) {
			if (child == this)
				throw new RuntimeException();
			children.add(child);
		}

		public List<AbstractElementAlias<T>> getChildren() {
			return children;
		}

		@Override
		protected int getElementCount() {
			int result = 1;
			for (AbstractElementAlias<T> child : children)
				result += child.getElementCount();
			return result;
		}
	}

	protected static class StateAlias<TOKEN> {
		protected AbstractElementAlias<TOKEN> element;
		protected Set<StateAlias<TOKEN>> incoming = Sets.newHashSet();
		protected Set<StateAlias<TOKEN>> outgoing = Sets.newHashSet();

		protected StateAlias(AbstractElementAlias<TOKEN> element) {
			super();
			this.element = element;
		}

		public void absorbIncoming(StateAlias<TOKEN> state) {
			for (StateAlias<TOKEN> in : state.incoming) {
				in.outgoing.remove(state);
				in.outgoing.add(this);
				incoming.add(in);
			}
		}

		public void absorbOutgoing(StateAlias<TOKEN> state) {
			for (StateAlias<TOKEN> out : state.outgoing) {
				out.incoming.remove(state);
				out.incoming.add(this);
				outgoing.add(out);
			}
		}

		public void addOutgoing(StateAlias<TOKEN> state) {
			outgoing.add(state);
			state.incoming.add(this);
		}

		protected AbstractElementAlias<TOKEN> getElement() {
			return element;
		}

		protected Set<StateAlias<TOKEN>> getIncoming() {
			return incoming;
		}

		protected Set<StateAlias<TOKEN>> getOutgoing() {
			return outgoing;
		}

		@Override
		public String toString() {
			return "S(" + element + ")";
		}

	}

	protected static class StateAliasNfa<TOKEN> implements Nfa<StateAlias<TOKEN>> {

		protected StateAlias<TOKEN> start;

		protected StateAlias<TOKEN> stop;

		public StateAliasNfa(StateAlias<TOKEN> start, StateAlias<TOKEN> stop) {
			super();
			this.start = start;
			this.stop = stop;
		}

		public Iterable<StateAlias<TOKEN>> getFollowers(StateAlias<TOKEN> state) {
			return state.getOutgoing();
		}

		public StateAlias<TOKEN> getStart() {
			return start;
		}

		public StateAlias<TOKEN> getStop() {
			return stop;
		}

	}

	protected static class StatesToDot<T> extends GraphvizDotBuilder {

		@SuppressWarnings("unchecked")
		@Override
		protected Props drawObject(Object obj) {
			if (obj instanceof StateAlias<?>) {
				Digraph dg = new Digraph();
				drawState(dg, (StateAlias<T>) obj, Maps.<StateAlias<T>, Node> newHashMap());
				return dg;
			}
			return null;
		}

		protected Node drawState(Digraph dg, StateAlias<T> state, Map<StateAlias<T>, Node> nodes) {
			Node n = nodes.get(state);
			if (n != null)
				return n;
			n = new Node(state, state.getElement().toString());
			nodes.put(state, n);
			dg.add(n);
			for (StateAlias<T> follower : state.getOutgoing()) {
				drawState(dg, follower, nodes);
				Edge e = new Edge(state, follower);
				e.put("arrowhead", "onormal");
				dg.add(e);
			}
			return n;
		}

	}

	protected <T> boolean createAlternative(StateAliasNfa<T> states) {
		boolean created = false;
		Multimap<Pair<Set<StateAlias<T>>, Set<StateAlias<T>>>, StateAlias<T>> alternative = HashMultimap.create();

		for (StateAlias<T> candidate : new NfaUtil().collect(states))
			if (!candidate.getIncoming().isEmpty() && !candidate.getOutgoing().isEmpty())
				alternative.put(Tuples.create(candidate.getIncoming(), candidate.getOutgoing()), candidate);

		for (Pair<Set<StateAlias<T>>, Set<StateAlias<T>>> inout : alternative.keySet()) {
			Collection<StateAlias<T>> candidates = alternative.get(inout);

			if (candidates.size() < 2)
				continue;

			boolean many = inout.getFirst().containsAll(candidates) && inout.getSecond().containsAll(candidates);
			boolean single = !Iterables.any(inout.getFirst(), Predicates.in(candidates))
					&& !Iterables.any(inout.getSecond(), Predicates.in(candidates));
			if (!many && !single)
				continue;

			AlternativeAlias<T> alt = new AlternativeAlias<T>();
			alt.setMany(many);
			StateAlias<T> altState = new StateAlias<T>(alt);
			for (StateAlias<T> candidate : candidates) {
				alt.addChild(candidate.getElement());
				for (StateAlias<T> in : candidate.getIncoming())
					in.getOutgoing().remove(candidate);
				for (StateAlias<T> out : candidate.getOutgoing())
					out.getIncoming().remove(candidate);
			}
			for (StateAlias<T> in : inout.getFirst())
				if (!candidates.contains(in)) {
					altState.getIncoming().add(in);
					in.getOutgoing().add(altState);
				}
			for (StateAlias<T> out : inout.getSecond())
				if (!candidates.contains(out)) {
					altState.getOutgoing().add(out);
					out.getIncoming().add(altState);
				}
			created = true;
		}
		return created;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> void createGroup(StateAlias<T> first, StateAlias<T> second) {
		GroupAlias<T> group = new GroupAlias<T>();
		if (first.getElement() instanceof GroupAlias && first.getElement().isOne())
			group.getChildren().addAll(((GroupAlias) first.getElement()).getChildren());
		else
			group.addChild(first.getElement());
		if (second.getElement() instanceof GroupAlias && second.getElement().isOne())
			group.getChildren().addAll(((GroupAlias) second.getElement()).getChildren());
		else
			group.addChild(second.getElement());
		first.element = group;
		first.getOutgoing().clear();
		first.absorbOutgoing(second);
	}

	protected <T> boolean createGroups(StateAlias<T> state, Set<StateAlias<T>> visited) {
		if (!visited.add(state))
			return false;
		boolean created = false;
		if (state.getOutgoing().size() == 1 && state.getOutgoing().iterator().next().getIncoming().size() == 1) {
			StateAlias<T> follower = state.getOutgoing().iterator().next();
			if (state != follower) {
				createGroup(state, follower);
				created = true;
			}
		}
		for (StateAlias<T> out : state.getOutgoing()) {
			if (createGroups(out, visited))
				created = true;
		}
		return created;
	}

	protected <T> boolean createMany(StateAlias<T> state, Set<StateAlias<T>> visited) {
		if (!visited.add(state))
			return false;
		boolean created = false;
		if (state.getOutgoing().contains(state)) {
			state.getElement().setMany(true);
			state.getOutgoing().remove(state);
			state.getIncoming().remove(state);
			created = true;
		}
		for (StateAlias<T> out : state.getOutgoing()) {
			if (createMany(out, visited))
				created = true;
		}
		return created;
	}

	protected <STATE, TOKEN> StateAliasNfa<TOKEN> createNfa(Nfa<STATE> nfa, Function<STATE, TOKEN> state2token) {
		HashMap<STATE, StateAlias<TOKEN>> cache = Maps.<STATE, StateAlias<TOKEN>> newHashMap();
		StateAlias<TOKEN> stop = null;
		if (nfa.getStart() != nfa.getStop()) {
			stop = new StateAlias<TOKEN>(new ElementAlias<TOKEN>(state2token.apply(nfa.getStop())));
			cache.put(nfa.getStop(), stop);
		}
		StateAlias<TOKEN> start = toAlias(nfa, state2token, nfa.getStart(), cache);
		if (nfa.getStart() == nfa.getStop()) {
			stop = new StateAlias<TOKEN>(start.getElement());
			for (StateAlias<TOKEN> in : start.getIncoming()) {
				stop.getIncoming().add(in);
				in.getOutgoing().add(stop);
				in.getOutgoing().remove(start);
			}
			start.getIncoming().clear();
		}
		StateAliasNfa<TOKEN> states = new StateAliasNfa<TOKEN>(start, stop);
		return states;
	}

	protected <T> boolean createOptional(StateAliasNfa<T> states) {
		List<StateAlias<T>> opt = Lists.newArrayList();
		L: for (StateAlias<T> cand : new NfaUtil().collect(states)) {
			if (cand.getIncoming().isEmpty() || cand.getOutgoing().isEmpty())
				continue L;
			for (StateAlias<T> in : cand.getIncoming())
				if (!in.getOutgoing().containsAll(cand.getOutgoing()))
					continue L;
			opt.add(cand);
		}
		for (StateAlias<T> o : opt) {
			o.getElement().setOptional(true);
			for (StateAlias<T> in : Lists.newArrayList(o.getIncoming()))
				if (in != o)
					for (StateAlias<T> out : Lists.newArrayList(o.getOutgoing()))
						if (out != o) {
							out.getIncoming().remove(in);
							in.getOutgoing().remove(out);
						}
		}
		return !opt.isEmpty();
	}

	protected <T> Pair<Integer, StateAlias<T>> findSplitState(StateAlias<T> state, Integer depth,
			Set<StateAlias<T>> visited) {
		if (!visited.add(state))
			return null;
		Pair<Integer, StateAlias<T>> result;
		if (state.getOutgoing().size() > 0 && state.getIncoming().size() > 0
				&& state.getOutgoing().size() + state.getIncoming().size() > 2)
			result = Tuples.create(depth, state);
		else
			result = null;
		for (StateAlias<T> out : state.getOutgoing()) {
			Pair<Integer, StateAlias<T>> cand = findSplitState(out, depth + 1, visited);
			if (cand != null && (result == null || isPreferredSplitState(cand, result)))
				result = cand;
		}
		return result;
	}

	protected <T> boolean isPreferredSplitState(Pair<Integer, StateAlias<T>> state1, Pair<Integer, StateAlias<T>> state2) {
		int count1 = state1.getSecond().getElement().getElementCount();
		int count2 = state2.getSecond().getElement().getElementCount();
		if (count1 != count2)
			return count1 < count2;
		int size1 = state1.getSecond().getOutgoing().size() + state1.getSecond().getIncoming().size();
		int size2 = state2.getSecond().getOutgoing().size() + state2.getSecond().getIncoming().size();
		if (size1 != size2)
			return size1 < size2;
		return state1.getFirst() > state2.getFirst();
	}

	public <ELEMENT, STATE, TOKEN> ELEMENT nfaToGrammar(Nfa<STATE> nfa, Function<STATE, TOKEN> state2token,
			ProductionFactory<ELEMENT, TOKEN> grammarFactory) {
		StateAliasNfa<TOKEN> states = createNfa(nfa, state2token);
		boolean changed = true;
		//		System.out.println("init: " + Joiner.on(" ").join(getAllStates(start)));
		while (!states.getStart().getOutgoing().isEmpty() && changed) {
			while (!states.getStart().getOutgoing().isEmpty() && changed) {
				changed = createAlternative(states);
				//				System.out.println("after Alternative: " + Joiner.on(" ").join(getAllStates(start)));
				changed |= createOptional(states);
				//				System.out.println("after Optional: " + Joiner.on(" ").join(getAllStates(start)));
				changed |= createMany(states.getStart(), Sets.<StateAlias<TOKEN>> newHashSet());
				//				System.out.println("after Many: " + Joiner.on(" ").join(getAllStates(start)));
				changed |= createGroups(states.getStart(), Sets.<StateAlias<TOKEN>> newHashSet());
				//				System.out.println("after Groups: " + Joiner.on(" ").join(getAllStates(start)));
			}
			if (!states.getStart().getOutgoing().isEmpty()) {
				Pair<Integer, StateAlias<TOKEN>> splitState = findSplitState(states.getStart(), 0,
						Sets.<StateAlias<TOKEN>> newHashSet());
				if (splitState != null) {
					changed = true;
					//					System.out.println("Splitting " + splitState);
					splitState(splitState.getSecond());
				}
				//				System.out.println("after Split: " + Joiner.on(" ").join(getAllStates(states.getStart())));
			}
		}
		//		if (!start.getOutgoing().isEmpty()) {
		//			System.err.println("error creating grammar for nfa: " + Join.join(" ", getAllStates(start)));
		//			StatesToDot<T> dot = new StatesToDot<T>();
		//			try {
		//				dot.draw(start, "pdf/" + start.toString().replaceAll("[^a-zA-Z0-9]", "") + ".pdf", "-Tpdf");
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		}
		AliasGrammarProvider<TOKEN> production = new AliasGrammarProvider<TOKEN>(states.getStart().getElement());
		return new ProductionUtil().clone(production, grammarFactory);
	}

	public <ELEMENT, STATE> ELEMENT nfaToGrammar(Nfa<STATE> nfa, ProductionFactory<ELEMENT, STATE> grammarFactory) {
		return nfaToGrammar(nfa, Functions.<STATE> identity(), grammarFactory);
	}

	protected <T> void splitState(StateAlias<T> state) {
		if (state.getIncoming().size() >= state.getOutgoing().size()) {
			for (StateAlias<T> in : Lists.newArrayList(state.getIncoming())) {
				StateAlias<T> rep = new StateAlias<T>(state.getElement());
				rep.getIncoming().add(in);
				rep.getOutgoing().addAll(state.getOutgoing());
				in.getOutgoing().add(rep);
				in.getOutgoing().remove(state);
				for (StateAlias<T> out : state.getOutgoing())
					out.getIncoming().add(rep);
			}
			for (StateAlias<T> out : state.getOutgoing())
				out.getIncoming().remove(state);
		} else {
			for (StateAlias<T> out : Lists.newArrayList(state.getOutgoing())) {
				StateAlias<T> rep = new StateAlias<T>(state.getElement());
				rep.getOutgoing().add(out);
				rep.getIncoming().addAll(state.getIncoming());
				out.getIncoming().add(rep);
				out.getIncoming().remove(state);
				for (StateAlias<T> in : state.getIncoming())
					in.getOutgoing().add(rep);
			}
			for (StateAlias<T> in : state.getIncoming())
				in.getOutgoing().remove(state);
		}
		state.getOutgoing().clear();
		state.getIncoming().clear();
	}

	protected <STATE, TOKEN> StateAlias<TOKEN> toAlias(Nfa<STATE> nfa, Function<STATE, TOKEN> state2token, STATE state,
			Map<STATE, StateAlias<TOKEN>> cache) {
		StateAlias<TOKEN> result = cache.get(state);
		if (result != null)
			return result;
		result = new StateAlias<TOKEN>(new ElementAlias<TOKEN>(state2token.apply(state)));
		cache.put(state, result);
		for (STATE follower : nfa.getFollowers(state)) {
			StateAlias<TOKEN> followerState = toAlias(nfa, state2token, follower, cache);
			result.getOutgoing().add(followerState);
			followerState.getIncoming().add(result);
		}
		return result;
	}
}
