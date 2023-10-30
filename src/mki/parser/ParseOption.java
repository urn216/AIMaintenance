package mki.parser;

import java.io.BufferedReader;
import java.io.IOException;

import mki.core.Core;
import mki.core.Prompt;
import mki.core.TextWindow;
import mki.ui.components.UIInteractable;
import mki.ui.components.interactables.UIButton;

abstract class ParseOption {
  
  static final UIInteractable parse(BufferedReader buf) throws IOException {
    char c = (char)buf.read();
    String title = null;
    String output = null;
    Prompt[] queuedPrompts = null;
    Prompt[] randomPrompts = null;
    
    while(c != '}') {
      switch (c) {
        case 'N':
          PromptParser.require(buf, '{');
          title = ParseText.parse(buf);
          break;
        case 'O':
          PromptParser.require(buf, '{');
          output = ParseText.parse(buf);
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
      c = (char)buf.read();
    }

    String o = output;
    Prompt[] queued = queuedPrompts;
    Prompt[] random = randomPrompts;
    return new UIButton(title, ()->{
      TextWindow.addText("\n\n\n"+o);
      Core.addNewPrompts(queued, random);
      Core.nextPrompt();
    });
  }
}
