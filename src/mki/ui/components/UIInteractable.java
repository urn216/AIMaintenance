package mki.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Graphics2D;

import mki.ui.control.UIAction;
import mki.ui.control.UIActionGetter;
import mki.ui.control.UIColours;

/**
* Class for making clickable items in a User Interface
*/
public abstract class UIInteractable extends UIComponent {

  protected UIAction primeAction;

  protected UIActionGetter<Boolean> lockCheck = this::isLocked;

  protected boolean in = false;
  protected boolean locked = false;
  protected boolean highlightedForAFrame = false;

  protected FontMetrics metrics;
  protected int fontStyle = Font.BOLD;

  /**
   * Depresses this {@code UIInteractable}, changing its look and signifying an active interaction
   */
  public void setIn() {in = true;}

  /**
   * Resets this {@code UIInteractable} to its default look
   */
  public void setOut() {in = false;}

  /**
   * Locks this {@code UIInteractable}, making it non-clickable.
   */
  public void lock() {locked = true;}

  /**
   * Unlocks this {@code UIInteractable}, making it clickable.
   */
  public void unlock() {locked = false;}

  /**
   * Sets the lock status of this {@code UIInteractable}.
   * 
   * @param locked a {@code boolean} representing whether this component should be locked or not
   */
  public void setLocked(boolean locked) {this.locked = locked;}

  /**
   * Makes this {@code UIInteractable} lock when a condition is met.
   * 
   * @param lockCheck the condition to check to determine the lock status of this {@code UIInteractable}
   * 
   * @return itself, for use in builder methods
   */
  public UIInteractable setLockCheck(UIActionGetter<Boolean> lockCheck) {
    this.lockCheck = lockCheck;
    return this;
  }

  /**
   * Highlights this {@code UIInteractable} until it has been drawn once
   */
  public void highlightForAFrame() {
    this.highlightedForAFrame = true;
  }

  /**
   * Checks whether or not this {@code UIInteractable} is presently set 'in'
   * 
   * @return {@code true} if this {@code UIInteractable} is being pressed
   */
  public boolean isIn() {return in;}

  /**
   * Checks whether or not this {@code UIInteractable} is presently locked
   * 
   * @return {@code true} if this {@code UIInteractable} is locked
   */
  public boolean isLocked() {return locked;}

  /**
   * sets the primary {@code UIAction} of this {@code UIInteractable},
   * 
   * @param action the {@code UIAction} to perform on interaction with this {@code UIInteractable}
   */
  public void setPrimeAct(UIAction action) {primeAction = action;}

  /**
   * Interacts with this {@code UIInteractable}, activating its primary {@code UIAction}, provided one exists.
   */
  public void primeAct() {if (primeAction != null) primeAction.perform();}

  @Override
  public void draw(Graphics2D g, UIColours.ColourSet c) {
    Font font = new Font("Copperplate", fontStyle, (int) Math.round((height/2)));
    metrics = g.getFontMetrics(font);
    g.setFont(font);

    setLocked(lockCheck.get());

    if (locked) {
      drawBody(g, 0, c.buttonBodyLocked(), c.text());
    }
    else if (in) {
      drawBody(g, 2, c.buttonAccentOut(), c.buttonAccentIn());
    }
    else if (highlightedForAFrame) {
      drawBody(g, 0, c.buttonBody(), c.buttonAccentHighlighted());
    }
    else {
      drawBody(g, 0, c.buttonBody(), c.text());
    }

    this.highlightedForAFrame = false;
  }

  /**
   * Draws the contents of this {@code UIInteractable} to a {@code Graphics2D} object.
   * 
   * @param g the {@code Graphics2D} space to draw to
   * @param off an offset from the expected coordinates to translate part of or all of the body by
   * @param bodyCol the {@code Color} to fill in the main body of this {@code UIInteractable} with
   * @param textCol the {@code Color} to draw in the details of this {@code UIInteractable} with
   */
  protected void drawBody(Graphics2D g, int off, Color bodyCol, Color textCol) {}
}


// g.setColor(bodyCol);
// g.fill(new Rectangle2D.Double(x+off, y+off, width-off, height-off));
// g.setColor(textCol);
// g.draw(new Rectangle2D.Double(x, y, width, height));
// g.drawString(name, x+off+(width-metrics.stringWidth(name))/2, y+off+((height - metrics.getHeight())/2) + metrics.getAscent());
