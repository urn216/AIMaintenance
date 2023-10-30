package mki.ui.components.interactables;

import java.awt.Graphics2D;

import mki.ui.components.UIComponent;
import mki.ui.components.UIInteractable;
import mki.ui.control.UIColours;

public class UINumfield extends UIInteractable {

  private final UITextfield field = new UITextfield(text, 20, 1);

  private final UIButton incr = new UIButton("\u02C4", ()-> field.setText(""+(Double.parseDouble(field.getText())+1)));
  private final UIButton decr = new UIButton("\u02C5", ()-> field.setText(""+(Double.parseDouble(field.getText())-1)));

  protected float halfHeight = 0;

  //TODO functionality and testing

  @Override
  public UIComponent touching(double oX, double oY) {
    return field.touching(oX, oY) != null ? field :
            incr.touching(oX, oY) != null ?  incr :
            decr.touching(oX, oY);
  }

  @Override
  public void draw(Graphics2D g, UIColours.ColourSet c) {
    this.halfHeight = height/2;
    this.field.draw(g, x, y, width-halfHeight, height, c);
    this.incr.draw(g, x+width-halfHeight, y, halfHeight, halfHeight, c);
    this.decr.draw(g, x+width-halfHeight, y+halfHeight, halfHeight, halfHeight, c);
  }
}
