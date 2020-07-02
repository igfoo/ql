package com.semmle.js.extractor;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

import com.semmle.js.parser.TypeScriptParser;

/**
 * Contains the state to be shared between extractions of different files.
 *
 * <p>In general, this contains both semantic state that affects the extractor output as well as
 * shared resources that are simply expensive to reacquire.
 *
 * <p>The {@link #reset()} method resets the semantic state while retaining shared resources when
 * possible.
 *
 * <p>Concretely, the shared resource is the Node.js process running the TypeScript compiler, which
 * is expensive to restart. The semantic state is the set of projects currently open in that
 * process, which affects the type information obtained during extraction of a file.
 */
public class ExtractorState {
  private TypeScriptParser typeScriptParser = new TypeScriptParser();
  
  private final ConcurrentHashMap<Path, FileSnippet> snippets = new ConcurrentHashMap<>();

  public TypeScriptParser getTypeScriptParser() {
    return typeScriptParser;
  }

  /**
   * Returns the mapping that denotes where a snippet file originated from.
   *
   * <p>The map is thread-safe and may be mutated by the caller.
   */
  public ConcurrentHashMap<Path, FileSnippet> getSnippets() {
    return snippets;
  }

  /**
   * Makes this semantically equivalent to a fresh state, but may internally retain shared resources
   * that are expensive to reacquire.
   */
  public void reset() {
    typeScriptParser.reset();
    snippets.clear();
  }
}
