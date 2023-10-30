package mki.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mki.core.Prompt;

abstract class ParseList {

  static final Prompt[] parse(BufferedReader buf) throws IOException {
    StringBuilder builder = new StringBuilder();
    List<Prompt> list = new ArrayList<>();

    char c = (char)buf.read();
    while(c != '}') {
      if (c == ',') {
        list.add(ParsePrompt.parse(new File("../data/prompts/"+builder.toString())));
        builder = new StringBuilder();
      }
      else if (c != ' ') builder.append(c);

      c = (char)buf.read();
    }
    if (!builder.isEmpty()) {
      list.add(ParsePrompt.parse(new File("../data/prompts/"+builder.toString())));
    }

    return list.toArray(new Prompt[]{});
  }
}
