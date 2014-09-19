/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputFunction;
import net.imglib2.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.module.MethodCallException;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 * TODO: Clean-up TODO: JavaDoc
 * 
 * @author Christian Dietz
 */
@Plugin(type = Service.class)
public class DefaultFeatureService<I> extends AbstractService implements
		FeatureService<I> {

	@Parameter
	private OpService ops;

	@Parameter
	private OpMatchingService matcher;

	@Override
	public OutputFunction<I, Collection<Pair<String, Double>>> compileFeatureSet(
			FeatureSetInfo info, Class<I> inputClass) {

		try {
			final Set<Class<? extends Op>> asOps = new HashSet<Class<? extends Op>>();
			asOps.addAll(info.getFeatures());

			final Source<I> inputSource = new Source<I>(inputClass);

			final Map<Class<?>, CachedModule> modulePool = new HashMap<Class<?>, CachedModule>();

			for (final Class<? extends Op> op : asOps) {
				CachedModule module;
				module = resolveModule(op, inputSource, modulePool, asOps);

				if (module == null) {
					throw new IllegalArgumentException(
							"Can't compile DescriptorSet!" + " Reason:"
									+ op.getSimpleName()
									+ " can't be instantiated!");
				}
			}

			postProcess(modulePool, inputSource);

			final Map<Class<? extends Op>, Module> compiledOps = new HashMap<Class<? extends Op>, Module>();

			for (final Class<? extends Op> op : info.getFeatures()) {
				compiledOps.put(op, modulePool.get(op));
			}
			return new CompiledFeatureSet(compiledOps, inputSource);
		} catch (ModuleException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public OutputFunction<I, Collection<Pair<String, Double>>> compileFeature(
			Class<? extends Feature> featureClass, Class<I> inputClass) {

		final HashSet<Class<? extends Feature>> set = new HashSet<Class<? extends Feature>>();
		set.add(featureClass);

		OutputFunction<I, Collection<Pair<String, Double>>> compileFeatureSet;
		compileFeatureSet = compileFeatureSet(new FeatureSetInfo() {

			@Override
			public Set<Class<? extends Feature>> getEnabledFeatures() {
				return set;
			}

			@Override
			public Set<Class<? extends Op>> getAdditionalOps() {
				return new HashSet<Class<? extends Op>>();
			}
		}, inputClass);
		return compileFeatureSet;

	}

	/*
	 * Recursively checking if we can automatically instantiate a descriptor set
	 * and setting all instances
	 */
	private CachedModule resolveModule(final Class<? extends Op> op,
			final Source<?> inputSource,
			final Map<Class<?>, CachedModule> compiledModules,
			final Collection<Class<? extends Op>> opsToCompile)
			throws ModuleException {

		if (compiledModules.containsKey(op)) {
			return compiledModules.get(op);
		}

		// get all candidate ops for this module type
		final List<ModuleInfo> candidates = matcher.findCandidates(null, op);

		// if there are no canidates, we can't resolve this module (and we fail)
		if (candidates.size() == 0) {
			return null;
		}

		// now: we check for the candidates. A candidate can be used, if all
		// fields can be resolved, given the available set of operations.
		// the only exceptions are special fields which are neither of
		// inputType nor a DescriptorParameterSet.
		loop: for (final ModuleInfo required : candidates) {

			final List<Object> parameters = new ArrayList<Object>();

			final List<CachedModule> dependencies = new ArrayList<CachedModule>();

			final HashMap<Class<?>, CachedModule> tmpCompiledModules = new HashMap<Class<?>, CachedModule>(
					compiledModules);

			// we have to parse our items for other ops/features
			for (final ModuleItem<?> item : required.inputs()) {
				final Class<?> itemType = item.getType();

				if (!item.isRequired()) {
					continue;
				}

				// Ignore if it is a service
				if (Service.class.isAssignableFrom(itemType)) {
					continue;
				}

				// It's an input parameter, we can ignore it
				if (itemType.isAssignableFrom(inputSource.getType())) {
					parameters.add(itemType);
					continue;
				}

				// it has a default value and can be set from outside (e.g. some
				// additional parameter)
				if (itemType.isPrimitive()
						|| String.class.isAssignableFrom(itemType)) {
					parameters.add(item.getInitializer());
					continue;
				}

				// Handle operation
				if (Op.class.isAssignableFrom(itemType)) {
					@SuppressWarnings("unchecked")
					final Class<? extends Op> opToResolve = (Class<? extends Op>) itemType;

					if (tmpCompiledModules.containsKey(opToResolve)) {
						parameters.add(tmpCompiledModules.get(opToResolve)
								.getDelegateObject());
						dependencies.add(tmpCompiledModules.get(opToResolve));
					} else {
						final CachedModule res = resolveModule(opToResolve,
								inputSource, tmpCompiledModules, opsToCompile);

						if (res == null) {
							continue loop;
						}

						dependencies.add(res);
						parameters.add(res.getDelegateObject());
					}
					continue;
				}

				continue loop;
			}

			final CachedModule module = new CachedModule(ops.module(op,
					parameters.toArray()));

			// set-up graph...
			for (final CachedModule dependency : dependencies) {
				dependency.addSuccessor(module);
				module.addPredecessor(dependency);
			}

			// we build our "tree"
			tmpCompiledModules.put(op, module);

			// we know that only additional modules are in local map
			for (final Entry<Class<?>, CachedModule> entry : tmpCompiledModules
					.entrySet()) {
				compiledModules.put(entry.getKey(), entry.getValue());
			}

			return module;
		}

		return null;
	}

	/*
	 * Set InputUpdaters. These classes listen, if some input is updated from
	 * outside, i.e. the input or some parameters.
	 */
	private void postProcess(final Map<Class<?>, CachedModule> modulePool,
			final Source<?> inputSource) {

		for (final Entry<Class<?>, CachedModule> entry : modulePool.entrySet()) {
			if (!Op.class.isAssignableFrom(entry.getKey())) {
				continue;
			}

			final CachedModule module = entry.getValue();

			for (final ModuleItem<?> item : module.getInfo().inputs()) {
				final Class<?> type = item.getType();

				// fields we can ignore during post-processing
				if (Op.class.isAssignableFrom(type)
						|| Service.class.isAssignableFrom(type)
						|| !item.isRequired()) {
					continue;
				}

				// TODO: we need to take care about generics here.
				final InputUpdateListener listener = createUpdateListener(
						module, item);

				if (type.isAssignableFrom(inputSource.getType())) {
					inputSource.registerListener(listener);
					continue;
				}

				// now we check if the update is performed by an internal
				// operation or from outside
				final CachedModule internalModule = modulePool.get(type);

				// from inside, then its an update listener
				if (internalModule != null) {

					final Iterable<ModuleItem<?>> outputs = internalModule
							.getInfo().outputs();
					for (final ModuleItem<?> info : outputs) {
						if (type.isAssignableFrom(info.getType())) {
							internalModule.registerOutputReceiver(info,
									listener);
							break;
						}
					}

				}

				continue;
			}

		}
	}

	/* Create one update listener */
	private InputUpdateListener createUpdateListener(final CachedModule module,
			final ModuleItem<?> item) {
		return new InputUpdateListener() {

			@Override
			public void update(final Object o) {
				module.setInput(item.getName(), o);
			}

			// TODO: be more restrictive concerning generics here
			@Override
			public boolean listensTo(final Class<?> clazz) {
				return item.getType().isAssignableFrom(clazz);
			}

			@Override
			public String toString() {
				return module.getInfo().getName();
			}
		};
	}

	/* <!-- Internal Classes --> */

	/*
	 * Simple Interface to mark Descriptors which listen for updates of external
	 * inputs (i.e. inputs which are not generated by an {@link Op}).
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	interface InputUpdateListener {

		void update(Object o);

		boolean listensTo(Class<?> clazz);
	}

	/*
	 * A {@link Source} can consume input values and will notify all connected
	 * {@link CachedModule}s that their output should be recalculated. A source
	 * is wrapped by a {@link CompiledFeatureSet}.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	class Source<II> {

		private ArrayList<InputUpdateListener> listeners = new ArrayList<InputUpdateListener>();

		private Class<II> type;

		public Source(final Class<II> type) {
			this.type = type;
		}

		public void update(final II input) {
			for (final InputUpdateListener listener : listeners) {
				listener.update(input);
			}
		}

		public void registerListener(final InputUpdateListener listener) {
			listeners.add(listener);
		}

		public Class<II> getType() {
			return type;
		}
	}

	/*
	 * A CompiledFeatureSet is a ready to go OutputFunction<I,
	 * Pair<String,Double>>. Whenever I is updated, the underyling modules get
	 * notified and will recalculate there outputs. (@see CachedModule}.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	class CompiledFeatureSet extends
			AbstractOutputFunction<I, Collection<Pair<String, Double>>> {

		private Source<I> source;
		private Map<Class<? extends Op>, Module> compiledModules;

		public CompiledFeatureSet(
				final Map<Class<? extends Op>, Module> compiledModules,
				final Source<I> source) {
			this.source = source;
			this.compiledModules = compiledModules;
		}

		@Override
		public Collection<Pair<String, Double>> createOutput(I input) {
			return new ArrayList<Pair<String, Double>>();
		}

		@Override
		protected Collection<Pair<String, Double>> safeCompute(I input,
				Collection<Pair<String, Double>> output) {
			source.update(input);

			for (final Module module : compiledModules.values()) {
				module.run();
				output.add(new ValuePair<String, Double>(module.getInfo()
						.getLabel(), ((Feature) module.getDelegateObject())
						.getFeatureValue()));
			}

			return output;
		}
	}

	/*
	 * A CachedModule can be informed and can informed other {@link
	 * CachedModule}s that on the next call of 'run()' they should recalculate
	 * the outputs.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	class CachedModule implements Module {

		private ArrayList<CachedModule> successors = new ArrayList<CachedModule>();

		private ArrayList<CachedModule> predeccessors = new ArrayList<CachedModule>();

		private final Map<ModuleItem<?>, Set<InputUpdateListener>> outputReceivers = new HashMap<ModuleItem<?>, Set<InputUpdateListener>>();

		private final Module module;

		public CachedModule(final Module module) {
			this.module = module;

			System.out.println(module);
		}

		boolean dirty = true;

		@Override
		public void run() {
			if (dirty) {
				runPredeccessors();
				module.run();

				for (final Entry<ModuleItem<?>, Set<InputUpdateListener>> entry : outputReceivers
						.entrySet()) {

					// update the listeners if there are any
					for (final InputUpdateListener listener : entry.getValue()) {
						listener.update(module.getOutput(entry.getKey()
								.getName()));
					}
				}
				dirty = false;
			}
		}

		void markDirty() {
			dirty = true;
			notifySuccessors();
		}

		private void notifySuccessors() {
			for (final CachedModule op : successors) {
				op.markDirty();
			}
		}

		private void runPredeccessors() {
			for (final CachedModule module : predeccessors) {
				module.run();
			}
		}

		public void addSuccessor(final CachedModule op) {
			successors.add(op);
		}

		public void addPredecessor(final CachedModule op) {
			predeccessors.add(op);
		}

		boolean isDirty() {
			return dirty;
		}

		@Override
		public void preview() {
			module.preview();
		}

		@Override
		public void cancel() {
			module.cancel();
		}

		@Override
		public void initialize() throws MethodCallException {
			module.initialize();
		}

		@Override
		public ModuleInfo getInfo() {
			return module.getInfo();
		}

		@Override
		public Object getDelegateObject() {
			return module.getDelegateObject();
		}

		@Override
		public Object getInput(final String name) {
			return module.getInput(name);
		}

		@Override
		public Object getOutput(final String name) {
			return module.getOutput(name);
		}

		@Override
		public Map<String, Object> getInputs() {
			return module.getInputs();
		}

		@Override
		public Map<String, Object> getOutputs() {
			return module.getOutputs();
		}

		@Override
		public void setInput(final String name, final Object value) {
			markDirty();
			module.setInput(name, value);
		}

		@Override
		public void setOutput(final String name, final Object value) {
			module.setOutput(name, value);
		}

		@Override
		public void setInputs(final Map<String, Object> inputs) {
			for (final Entry<String, Object> entry : inputs.entrySet()) {
				setInput(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public void setOutputs(final Map<String, Object> outputs) {
			module.setOutputs(outputs);
		}

		@Override
		public boolean isResolved(final String name) {
			return module.isResolved(name);
		}

		@Override
		public void setResolved(final String name, final boolean resolved) {
			module.setResolved(name, resolved);
		}

		public void registerOutputReceiver(final ModuleItem<?> item,
				final InputUpdateListener listener) {
			Set<InputUpdateListener> listeners = outputReceivers.get(item);
			if (listeners == null) {
				listeners = new HashSet<InputUpdateListener>();
				outputReceivers.put(item, listeners);
			}
			listeners.add(listener);
		}

		@Override
		public String toString() {
			return module.getInfo().getName();
		}
	}
}
