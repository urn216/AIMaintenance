package mki.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mki.math.MathHelp;

public abstract class TextWindow {
  
  private static final List<Character> charBuffer = new ArrayList<>();
  private static final List<Character> charsInUse = new ArrayList<>();
  private static final List<String> lines = new ArrayList<>(List.of(""));

  private static Font font;
  
  private static int lineWidth;

  private static int lineIncr;

  private static int bottomLine = 0;

  private static boolean newlyFinished = false;

  public static synchronized void scroll(int lines) {
    bottomLine = MathHelp.clamp(bottomLine+lines, -15, TextWindow.lines.size());
  }

  public static synchronized void updateScreenRegion(int screenSizeX, int screenSizeY) {
    int fontSize = screenSizeY/20;
    font = new Font(Font.MONOSPACED, Font.BOLD, fontSize);
    lineWidth = (int)(screenSizeX/(fontSize*3.0/5.0))-1;
    lineIncr = fontSize;

    List<Character> temp = List.copyOf(charBuffer);
    charBuffer.clear();
    charBuffer.addAll(charsInUse);
    charsInUse.clear();
    lines.clear();
    lines.add("");

    while(!charBuffer.isEmpty()) update();

    charBuffer.addAll(temp);
  }

  public static synchronized void addText(String text) {
    newlyFinished = true;
    char[] chars = text.toCharArray();
    for(int i = 0; i < text.length(); i++) charBuffer.add(chars[i]);
  }

  public static synchronized void clear() {
    charBuffer.clear();
    charsInUse.clear();
    lines.clear();
    lines.add("");

    bottomLine = 0;
  }

  public static synchronized void update() {
    if (charBuffer.isEmpty()) {
      if (newlyFinished) {
        newlyFinished = false;
        Core.onFinishedTextWindow();
      }
      return;
    }

    char c = charBuffer.remove(0);

    charsInUse.add(c);

    int lastLineIndex = lines.size()-1;

    if (c == '\n') lines.add("");
    else if (      lines.get(lastLineIndex).length() >= lineWidth) overflow(c);
    else           lines.set(lastLineIndex, lines.get(lastLineIndex) + c);

    if (bottomLine >= 2) bottomLine = 0;
  }

  private static synchronized void overflow(char c) {
    if (c==' ') {
      lines.add(""+ (c==' '?"":c)); 
      return;
    }

    Stack<Character> stack = new Stack<>();
    char[] lastLine = lines.get(lines.size()-1).toCharArray();
    for (int i = lastLine.length-1; i > 0 && lastLine[i] != ' ' && lastLine[i] != '-'; i--) {
      stack.push(lastLine[i]);
      lastLine[i] = '\0';
    }
    String res = "";
    while (!stack.isEmpty()) {
      res+=stack.pop();
    }
    
    lines.set(lines.size()-1, new String(lastLine));
    lines.add(res + c);
  }

  public static void draw(Graphics2D g) {
    g.setColor(Color.white);
    g.setFont(font);
    for (int i = 0; i < lines.size(); i++) {
      g.drawString(lines.get(i), 10, (int)(0.85*Core.WINDOW.screenHeight()-(lines.size()-bottomLine-i)*lineIncr));
    }
  }
}
