package mki.ui.components.interactables;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

import mki.ui.components.UIInteractable;
import mki.ui.control.UIAction;
import mki.ui.control.UIActionGetter;
import mki.ui.control.UIActionSetter;
import mki.ui.control.UIController;

/**
 * Class for functional text fields, supporting keyboard inputs and multiple lines
 * 
 * @author William Kilty
 */
public class UITextfield extends UIInteractable {
  /** {@code char} array containing all the {@code char}s input into this text field */
  protected char[] textChars;
  /** the index into which the next {@code char} should be inserted for each line */
  protected int ind[];
  /** the current line the cursor is pointed at */
  protected int line = 0;
  /** the index through {@code textChars} the next {@code char} should be inserted */
  protected int totind = 0;
  /** the total number of lines contained within this text field */
  protected final int numLines;

  private final UIAction enterAction;
  private final UIAction clearAction;
  private final UIActionGetter<String> textGetter;

  private final String blacklistedChars;

  /**
   * Constructor for Text Fields. Pressing enter will enter a newline.
   * 
   * @param defaultText the text to display in the text box when it has no user-input
   * @param maxLength the maximum number of characters allowed in the field.
   * @param numLines the maximum number of lines this field may utilise. Must be at least 1.
   */
  public UITextfield(String defaultText, int maxLength, int numLines) {
    this(defaultText, maxLength, numLines, null, null);
  }

  /**
   * Constructor for Text Fields with an 'enter' action that isn't entering a newline.
   * 
   * @param defaultText the text to display in the text box when it has no user-input
   * @param maxLength the maximum number of characters allowed in the field.
   * @param numLines the maximum number of lines this field may utilise. Must be at least 1.
   * @param enter the destination for the text in this field when enter is pressed.
   */
  public UITextfield(String defaultText, int maxLength, int numLines, UIActionSetter<String> enter) {
    this(defaultText, maxLength, numLines, enter, null);
  }

  /**
   * Constructor for Text Fields with an 'enter' action that isn't entering a newline,
   * as well as a textGetter for when the text within the field can be changed outside of user input.
   * 
   * @param defaultText the text to display in the text box when it has no user-input
   * @param maxLength the maximum number of characters allowed in the field.
   * @param numLines the maximum number of lines this field may utilise. Must be at least 1.
   * @param enter the destination for the text in this field when enter is pressed.
   * @param textGetter where to retrieve the body text from.
   */
  public UITextfield(String defaultText, int maxLength, int numLines, UIActionSetter<String> enter, UIActionGetter<String> textGetter) {
    this(defaultText, maxLength, numLines, enter, textGetter, "");
  }

  /**
   * Constructor for Text Fields with an 'enter' action that isn't entering a newline,
   * as well as a textGetter for when the text within the field can be changed outside of user input.
   * 
   * @param defaultText the text to display in the text box when it has no user-input
   * @param maxLength the maximum number of characters allowed in the field.
   * @param numLines the maximum number of lines this field may utilise. Must be at least 1.
   * @param enter the destination for the text in this field when enter is pressed.
   * @param textGetter where to retrieve the body text from.
   * @param regex a regex to check each entered character against.
   */
  public UITextfield(String defaultText, int maxLength, int numLines, UIActionSetter<String> enter, UIActionGetter<String> textGetter, String regex) {
    if (numLines < 1) throw new IllegalArgumentException("Must have at least one line.");
    this.text = defaultText;
    this.textChars = new char[maxLength+1];
    this.numLines = numLines;
    this.ind = new int[numLines];
    this.primeAction = () -> UIController.setActiveTextfield(this);

    this.clearAction = enter == null ? 
    () -> UIController.setActiveTextfield(null) :
    () -> {UIController.setActiveTextfield(null); enterAct();};

    this.enterAction = enter != null ? 
    () -> enter.set(getText()) : 
    this::newLine;

    this.textGetter = textGetter;

    this.blacklistedChars = regex;
  }

  /**
   * Performs the functionality associated with pressing the {@code Enter} key over this {@code UITextfield}.
   */
  public void enterAct() {enterAction.perform();}

  /**
   * Performs the functionality associated with deselecting this {@code UITextfield}.
   */
  public void clearAct() {clearAction.perform();}

  /**
   * When this component is transitioned into view, it will update the text within the field, if there is a {@code textGetter} present.
   */
  public void onTransIn() {
    if (textGetter != null) {
      reset();
      for (char c : textGetter.get().toCharArray()) print(c);
    }
  }

  @Override
  public void setText(String text) {
    this.textChars = Arrays.copyOf(text.toCharArray(), this.textChars.length);
  }

  /**
   * Gets the text within this {@code UITextfield}'s char buffer as a single {@code String}.
   * 
   * @return the contents of this {@code UITextfield} as a {@code String}
   */
  public String getText() {
    return new String(textChars, 0, totind);
  }

  /**
   * Gets the lines of this {@code UITextfield} as an array of {@code String}s.
   * Each {@code String} representing a single line of the field.
   * 
   * @return a {@code String} array containing the lines of this {@code UITextfield}.
   */
  public String[] getTextLines() {
    if (totind == 0) {
      fontStyle = Font.ITALIC;
      return new String[] {this.text};
    }

    fontStyle = Font.BOLD;
    String[] res = new String[numLines];
    int j = 0;
    for (int i = 0; i < numLines; i++) {
      res[i] = "";
      char c = '\u0000';
      while (c!='\n' && j < textChars.length) {
        res[i]+=c;
        c = textChars[j++];
      }
    }
    return res;
  }

  /**
   * Checks to see if the contents of this {@code UITextfield} obey the given blacklisted char sequence.
   * 
   * @return {@code true} if no character in this {@code UITextfield} matches 
   * any character found in the blacklisted characters of this field.
   */
  public boolean checkValid() {
    if (textChars[0]=='\u0000') return false;
    char[] checker = blacklistedChars.toCharArray();
    for (char cc : checker) {
      for (char tc : textChars) {
        if (tc == cc) {return false;}
      }
    }
    return true;
  }

  /**
   * Prints a character to this {@code UITextfield}, 
   * provided there is space remaining in the field, 
   * and the input character is not forbidden by the
   * blacklisted chars of this {@code UITextfield}.
   * 
   * @param c the character to print to this field
   */
  public void print(char c) {
    if (totind>=textChars.length-1
    ||  blacklistedChars.indexOf(c) >= 0) return;
    // System.out.print(c);
    textChars[totind] = c;
    totind++;
    ind[line]++;
  }

  /**
   * Removes a single character from the end of this {@code UITextfield}.
   */
  public void backspace() {
    if (totind<=0) return;
    totind--;
    if (textChars[totind]=='\n') line--;
    ind[line]--;
    textChars[totind] = '\u0000';
  }

  /**
   * Adds a single new-line character {@code \n} to the end of this {@code UITextfield},
   * provided there is enough space to add one.
   */
  public void newLine() {
    if (line >= numLines-1) {
      clearAction.perform();
      return;
    }
    print('\n');
    // System.out.println(ind[line]);
    line++;
  }

  /**
   * Resets this {@code UITextfield} to its default state; its pointer at index {@code 0}, 
   * and the char buffer cleared.
   */
  public void reset() {
    textChars = new char[textChars.length];
    ind = new int[numLines];
    line = 0;
    totind = 0;
  }

  @Override
  protected void drawBody(Graphics2D g, int off, Color bodyCol, Color textCol) {
    Shape body = new RoundRectangle2D.Float(x, y, width, height, height/8, height/8);
    g.setColor(bodyCol);
    g.fill(body);
    g.setColor(textCol);
    g.draw(body);

    String[] s = getTextLines();
    for (int i = 0; i < s.length; i++) {
      g.drawString(s[i], x+width/32, y+((height - metrics.getHeight())/2) + metrics.getAscent() + i*metrics.getAscent());
    }
  }
}
