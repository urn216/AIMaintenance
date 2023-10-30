package mki.ui.control;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class UIColours {

  public static final Map<String, ColourSet> THEMES = new HashMap<>(){{
    put("DEFAULT", new ColourSet(
      new Color(100, 100, 100, 127), 
      new Color(50 , 50 , 50 , 127), 
      new Color(160, 160, 160, 160), 
      new Color(180, 180, 180), 
      new Color(200, 200, 200), 
      new Color(0  , 255, 255), 
      new Color(0  , 180, 180),
      new Color(200, 200, 200)
    ));
  }};

  public static ColourSet ACTIVE = THEMES.get("DEFAULT");

  public static record ColourSet(
    Color background, 
    Color screenTint, 
    Color buttonBody, 
    Color buttonBodyLocked, 
    Color buttonAccentOut, 
    Color buttonAccentIn, 
    Color buttonAccentHighlighted, 
    Color text
    ){
      /**
       * Creates a new {@code ColourSet} with the same colours as this - however, 
       * the opacity of all the colours is scaled by some percentage
       * 
       * @param percent the percentage to multiply the {@code alpha} of all colours by
       * 
       * @return A new ColourSet copy of this, with a scaled opacity
       */
      public ColourSet withOpacity(double percent) {
        return new ColourSet(
          new Color(             background.getRed(),              background.getGreen(),              background.getBlue(), (int)(             background.getAlpha()*percent)), 
          new Color(             screenTint.getRed(),              screenTint.getGreen(),              screenTint.getBlue(), (int)(             screenTint.getAlpha()*percent)), 
          new Color(             buttonBody.getRed(),              buttonBody.getGreen(),              buttonBody.getBlue(), (int)(             buttonBody.getAlpha()*percent)), 
          new Color(       buttonBodyLocked.getRed(),        buttonBodyLocked.getGreen(),        buttonBodyLocked.getBlue(), (int)(       buttonBodyLocked.getAlpha()*percent)), 
          new Color(        buttonAccentOut.getRed(),         buttonAccentOut.getGreen(),         buttonAccentOut.getBlue(), (int)(        buttonAccentOut.getAlpha()*percent)), 
          new Color(         buttonAccentIn.getRed(),          buttonAccentIn.getGreen(),          buttonAccentIn.getBlue(), (int)(         buttonAccentIn.getAlpha()*percent)), 
          new Color(buttonAccentHighlighted.getRed(), buttonAccentHighlighted.getGreen(), buttonAccentHighlighted.getBlue(), (int)(buttonAccentHighlighted.getAlpha()*percent)), 
          new Color(                   text.getRed(),                    text.getGreen(),                    text.getBlue(), (int)(                   text.getAlpha()*percent))
        );
      }
    }
}
