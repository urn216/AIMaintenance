package mki.parser;

import java.io.BufferedReader;
import java.io.IOException;

abstract class ParseText {
  static final String parse(BufferedReader buf) throws IOException {
    StringBuilder builder = new StringBuilder();
    char c = (char)buf.read();
    while(c != '}') {
      builder.append(c);
      c = (char)buf.read();
    }

    return builder.toString();
  }
}
