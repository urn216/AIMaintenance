package mki.ui.components.interactables;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

import mki.ui.components.UIInteractable;
import mki.ui.control.UIActionGetter;
import mki.ui.control.UIActionSetter;

import mki.math.MathHelp;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
* Class for making functional Sliders
*/
public abstract class UISlider extends UIInteractable {
  protected float zeroLine = 0;
  protected float barLength = 0;

  protected final double min;
  protected final double max;

  /**
   * Hidden constructor for {@code UISlider} interactables. 
   * Handles shared data between the different datatypes.
   * 
   * @param text the user-visible label for the slider
   * @param min the minimum value of the slider
   * @param max the maximum value of the slider
   */
  protected UISlider(String text, double min, double max) {
    this.text = text;
    this.min = min;
    this.max = max;
  }
  
  public static class Integer extends UISlider {
    protected final int step;

    private final UIActionGetter<java.lang.Integer> get;
    private final UIActionSetter<java.lang.Integer> set;
    
    /**
     * Constructs interacatble sliders which alter an {@code Integer} value within a given range.
     * <p>
     * has a step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Integer} as {@code get}
     * @param min the minimum {@code Integer} value of the slider
     * @param max the maximum {@code Integer} value of the slider
     * @param step the step-size for each 'notch' in the slider
     */
    public Integer(String text, UIActionGetter<java.lang.Integer> get, UIActionSetter<java.lang.Integer> set, int min, int max, int step) {
      super(text, min, max);
      this.step = step;
      this.get = get;
      this.set = set;
      this.primeAction = () -> set(get());
    }

    /**
     * Constructs interacatble sliders which alter an {@code Integer} value within a given range.
     * <p>
     * has no step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Integer} as {@code get}
     * @param min the minimum {@code Integer} value of the slider
     * @param max the maximum {@code Integer} value of the slider
     */
    public Integer(String text, UIActionGetter<java.lang.Integer> get, UIActionSetter<java.lang.Integer> set, int min, int max) {
      this(text, get, set, min, max, 1);
    }

    @Override
    public double get() {return get.get();}

    @Override
    public void set(double v) {
      set.set((int)MathHelp.roundToNearestStep(MathHelp.clamp(v, min, max), step));
    }

    @Override
    public void moveNode(int x) {
      set((int)(((x-zeroLine)/barLength)*(max-min)+min));
    }
  }

  public static class Float extends UISlider {
    protected final float step;
    
    private final UIActionGetter<java.lang.Float> get;
    private final UIActionSetter<java.lang.Float> set;

    /**
     * Constructs interacatble sliders which alter a {@code Float} value within a given range.
     * <p>
     * has a step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Float} as {@code get}
     * @param min the minimum {@code Float} value of the slider
     * @param max the maximum {@code Float} value of the slider
     * @param step the step-size for each 'notch' in the slider
     */
    public Float(String text, UIActionGetter<java.lang.Float> get, UIActionSetter<java.lang.Float> set, float min, float max, float step) {
      super(text, min, max);
      this.step = step;
      this.get = get;
      this.set = set;
      this.primeAction = () -> set(get());
    }

    /**
     * Constructs interacatble sliders which alter a {@code Float} value within a given range.
     * <p>
     * has no step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Float} as {@code get}
     * @param min the minimum {@code Float} value of the slider
     * @param max the maximum {@code Float} value of the slider
     */
    public Float(String text, UIActionGetter<java.lang.Float> get, UIActionSetter<java.lang.Float> set, float min, float max) {
      this(text, get, set, min, max, 1);
    }

    @Override
    public double get() {return get.get();}

    @Override
    public void set(double v) {
      set.set((float)MathHelp.roundToNearestStep(MathHelp.clamp(v, min, max), step));
    }

    @Override
    public void moveNode(int x) {
      set((((x-zeroLine)/barLength)*(max-min)+min));
    }
  }

  public static class Double extends UISlider {
    protected final double step;
    
    private final UIActionGetter<java.lang.Double> get;
    private final UIActionSetter<java.lang.Double> set;
    
    /**
     * Constructs interacatble sliders which alter a {@code Double} value within a given range.
     * <p>
     * has a step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Double} as {@code get}
     * @param min the minimum {@code Double} value of the slider
     * @param max the maximum {@code Double} value of the slider
     * @param step the step-size for each 'notch' in the slider
     */
    public Double(String text, UIActionGetter<java.lang.Double> get, UIActionSetter<java.lang.Double> set, double min, double max, double step) {
      super(text, min, max);
      this.step = step;
      this.get = get;
      this.set = set;
      this.primeAction = () -> set(get());
    }

    /**
     * Constructs interacatble sliders which alter a {@code Double} value within a given range.
     * <p>
     * has no step-size to limit the degree of freedom within the slider
     * 
     * @param text the user-visible label for the slider
     * @param get the setting to read the current state from
     * @param set the setting to input a new value to - ideally linked to the same {@code Double} as {@code get}
     * @param min the minimum {@code Double} value of the slider
     * @param max the maximum {@code Double} value of the slider
     */
    public Double(String text, UIActionGetter<java.lang.Double> get, UIActionSetter<java.lang.Double> set, double min, double max) {
      this(text, get, set, min, max, 1);
    }

    @Override
    public double get() {return get.get();}

    @Override
    public void set(double v) {
      set.set(MathHelp.roundToNearestStep(MathHelp.clamp(v, min, max), step));
    }

    @Override
    public void moveNode(int x) {
      set(((x-zeroLine)/barLength)*(max-min)+min);
    }
  }
  
  /**
   * Gets the current value of this {@code UISlider}
   * 
   * @return the current value within this {@code UISlider}
   */
  public abstract double get();
  
  /**
   * Sets the value of this {@code UISlider} to a specific number within the given range, 
   * rounded to the nearest {@code step}
   * 
   * @param v the value to set
   */
  public abstract void set(double v);
  
  /**
   * Slides the node of this {@code UISlider} in screen-space,
   * setting the value of this {@code UISlider} to a specific number within the given range, 
   * rounded to the nearest {@code step}
   * 
   * @param x the x-coordinate to move the node to on the screen
   */
  public abstract void moveNode(int x);
  
  @Override
  protected void drawBody(Graphics2D g, int off, Color bodyCol, Color textCol) {
    height*=2;
    float inset = ((height/2 - metrics.getHeight())/2);
    float nodeWidth = (height/2-inset*2)/3;
    
    barLength = width-inset*2;
    zeroLine = x+inset-nodeWidth/2;
    
    float percent = (float)((get()-min)/(max-min));
    
    //bar base
    g.setColor(bodyCol);
    g.fill(new RoundRectangle2D.Float(x+inset, y+height*3/4-inset/4, barLength, inset/2, height/8, height/8));
    //bar filled
    g.setColor(textCol);
    g.fill(new RoundRectangle2D.Float(x+inset, y+height*3/4-inset/2, barLength*percent, inset, height/8, height/8));
    
    Shape node = new RoundRectangle2D.Float(zeroLine+percent*barLength-off/2, y-off/2+height/2+inset, nodeWidth+off, height/2-inset*2+off, height/8, height/8);
    //node base
    g.setColor(bodyCol);
    g.fill(node);
    //node filled
    g.setColor(textCol);
    g.draw(node);
    
    g.drawString(String.format(text, get()), x+inset, y+inset + metrics.getAscent());
  }
}
