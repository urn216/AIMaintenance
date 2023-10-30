package mki.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mki.core.Prompt;

public abstract class PromptParser {
  public static final List<Prompt> loadPrompts() {
    List<Prompt> roots = new ArrayList<>();
    for (File file : new File("../data/prompts").listFiles()) {
      if (!file.getName().contains("Root")) continue;

      roots.add(ParsePrompt.parse(file));
    }
    return roots;
  }

  static final void require(BufferedReader buf, char c) throws IOException {
    if (buf.read()!=c) throw new IOException("Expected '"+c+"'");
  }
}
