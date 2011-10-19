package org.eclipse.xtext.generator.serializer;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.generator.BindFactory;
import org.eclipse.xtext.generator.Binding;
import org.eclipse.xtext.generator.Generator;
import org.eclipse.xtext.generator.Xtend2ExecutionContext;
import org.eclipse.xtext.generator.Xtend2GeneratorFragment;
import org.eclipse.xtext.generator.serializer.AbstractSemanticSequencer;
import org.eclipse.xtext.generator.serializer.AbstractSyntacticSequencer;
import org.eclipse.xtext.generator.serializer.Context2DotRenderer;
import org.eclipse.xtext.generator.serializer.GrammarConstraints;
import org.eclipse.xtext.generator.serializer.SemanticSequencer;
import org.eclipse.xtext.generator.serializer.SerializerFragmentState;
import org.eclipse.xtext.generator.serializer.SyntacticSequencer;
import org.eclipse.xtext.generator.serializer.SyntacticSequencerPDA2ExtendedDot;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class SerializerFragment extends Xtend2GeneratorFragment {
  @Inject
  private AbstractSemanticSequencer abstractSemanticSequencer;
  
  @Inject
  private SemanticSequencer semanticSequencer;
  
  @Inject
  private AbstractSyntacticSequencer abstractSyntacticSequencer;
  
  @Inject
  private SyntacticSequencer syntacticSequencer;
  
  @Inject
  private GrammarConstraints grammarConstraints;
  
  @Inject
  private Context2DotRenderer dotRenderer;
  
  @Inject
  private SyntacticSequencerPDA2ExtendedDot seq2dot;
  
  public SerializerFragmentState state() {
    final ArrayList<?>_cacheKey = CollectionLiterals.newArrayList();
    final SerializerFragmentState _result;
    synchronized (_createCache_state) {
      if (_createCache_state.containsKey(_cacheKey)) {
        return _createCache_state.get(_cacheKey);
      }
      SerializerFragmentState _serializerFragmentState = new SerializerFragmentState();
      _result = _serializerFragmentState;
      _createCache_state.put(_cacheKey, _result);
    }
    _init_state(_result);
    return _result;
  }
  
  private final HashMap<ArrayList<?>,SerializerFragmentState> _createCache_state = CollectionLiterals.newHashMap();
  
  private void _init_state(final SerializerFragmentState result) {
  }
  
  public boolean setGenerateDebugData(final boolean doGenerate) {
    SerializerFragmentState _state = this.state();
    boolean _generateDebugData = _state.generateDebugData = doGenerate;
    return _generateDebugData;
  }
  
  public boolean setSrcGenOnly(final boolean srcGen) {
    SerializerFragmentState _state = this.state();
    boolean _srcGenOnly = _state.srcGenOnly = srcGen;
    return _srcGenOnly;
  }
  
  public boolean setGenerateStub(final boolean generateStub) {
    boolean _operator_not = BooleanExtensions.operator_not(generateStub);
    boolean _setSrcGenOnly = this.setSrcGenOnly(_operator_not);
    return _setSrcGenOnly;
  }
  
  public Set<Binding> getGuiceBindingsRt(final Grammar grammar) {
      BindFactory _bindFactory = new BindFactory();
      final BindFactory bf = _bindFactory;
      SerializerFragmentState _state = this.state();
      if (_state.srcGenOnly) {
        {
          String _name = org.eclipse.xtext.serializer.sequencer.ISemanticSequencer.class.getName();
          String _qualifiedName = this.abstractSemanticSequencer.getQualifiedName();
          bf.addTypeToType(_name, _qualifiedName);
          String _name_1 = org.eclipse.xtext.serializer.sequencer.ISyntacticSequencer.class.getName();
          String _qualifiedName_1 = this.abstractSyntacticSequencer.getQualifiedName();
          bf.addTypeToType(_name_1, _qualifiedName_1);
        }
      } else {
        {
          String _name_2 = org.eclipse.xtext.serializer.sequencer.ISemanticSequencer.class.getName();
          String _qualifiedName_2 = this.semanticSequencer.getQualifiedName();
          bf.addTypeToType(_name_2, _qualifiedName_2);
          String _name_3 = org.eclipse.xtext.serializer.sequencer.ISyntacticSequencer.class.getName();
          String _qualifiedName_3 = this.syntacticSequencer.getQualifiedName();
          bf.addTypeToType(_name_3, _qualifiedName_3);
        }
      }
      String _name_4 = org.eclipse.xtext.serializer.ISerializer.class.getName();
      String _name_5 = org.eclipse.xtext.serializer.impl.Serializer.class.getName();
      bf.addTypeToType(_name_4, _name_5);
      Set<Binding> _bindings = bf.getBindings();
      return _bindings;
  }
  
  public void generate(final Xtend2ExecutionContext ctx) {
      SerializerFragmentState _state = this.state();
      boolean _operator_not = BooleanExtensions.operator_not(_state.srcGenOnly);
      if (_operator_not) {
        {
          String _fileName = this.semanticSequencer.getFileName();
          CharSequence _fileContents = this.semanticSequencer.getFileContents();
          ctx.writeFile(Generator.SRC, _fileName, _fileContents);
          String _fileName_1 = this.syntacticSequencer.getFileName();
          CharSequence _fileContents_1 = this.syntacticSequencer.getFileContents();
          ctx.writeFile(Generator.SRC, _fileName_1, _fileContents_1);
        }
      }
      String _fileName_2 = this.abstractSemanticSequencer.getFileName();
      CharSequence _fileContents_2 = this.abstractSemanticSequencer.getFileContents();
      ctx.writeFile(Generator.SRC_GEN, _fileName_2, _fileContents_2);
      String _fileName_3 = this.abstractSyntacticSequencer.getFileName();
      CharSequence _fileContents_3 = this.abstractSyntacticSequencer.getFileContents();
      ctx.writeFile(Generator.SRC_GEN, _fileName_3, _fileContents_3);
      SerializerFragmentState _state_1 = this.state();
      if (_state_1.generateDebugData) {
        {
          String _fileName_4 = this.grammarConstraints.getFileName();
          CharSequence _fileContents_4 = this.grammarConstraints.getFileContents();
          ctx.writeFile(Generator.SRC_GEN, _fileName_4, _fileContents_4);
          Iterable<Pair<String,String>> _render2Dot = this.dotRenderer.render2Dot(this.seq2dot, "pda");
          for (final Pair<String,String> obj : _render2Dot) {
            String _key = obj.getKey();
            String _value = obj.getValue();
            ctx.writeFile(Generator.SRC_GEN, _key, _value);
          }
        }
      }
  }
  
  public List<String> getExportedPackagesRtList(final Grammar grammar) {
    String _packageName = this.semanticSequencer.getPackageName();
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_packageName);
    return _newArrayList;
  }
}
