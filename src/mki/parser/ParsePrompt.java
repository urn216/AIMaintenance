package mki.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import mki.core.Core;
import mki.core.Prompt;
import mki.ui.components.UIInteractable;

abstract class ParsePrompt {

  private static int indent = 0;

  static final Prompt parse(File file) {
    try {
      for (int i = 0; i < indent; i++) {
        System.out.print("| ");
      }
      indent++;
      System.out.println("parsing " + file.getName());
      BufferedReader buf = Files.newBufferedReader(file.toPath());
      String text = "";
      List<UIInteractable> options = new ArrayList<>();
      Prompt[] queuedPrompts = new Prompt[]{};
      Prompt[] randomPrompts = new Prompt[]{};

      while (buf.ready()) {
        char type = (char)buf.read();

        switch (type) {
          case 'P':
            PromptParser.require(buf, '{');
            text = ParseText.parse(buf);
            break;
          case 'C':
            PromptParser.require(buf, '{');
            options.add(ParseOption.parse(buf));
            break;
          case 'Q':
            PromptParser.require(buf, '{');
            queuedPrompts = ParseList.parse(buf);
            break;
          case 'R':
            PromptParser.require(buf, '{');
            randomPrompts = ParseList.parse(buf);
            break;
          default:
            break;
        }
      }
      indent--;
      for (int i = 0; i < indent; i++) {
        System.out.print("| ");
      }
      System.out.println("parsed " + file.getName() + " successfully");
      return new Prompt(text, options.isEmpty() ? Core.getMainMenu() : options.toArray(new UIInteractable[]{}), queuedPrompts, randomPrompts);
      
    } catch (IOException e) {
      System.err.println(e);
      System.exit(0);
    }
    return null;
  }
}
