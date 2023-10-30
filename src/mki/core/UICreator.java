package mki.core;

import mki.math.vector.Vector2;
import mki.ui.control.UIPane;
import mki.ui.components.UIInteractable;

import mki.ui.elements.*;

public abstract class UICreator {

  private static final double COMPON_HEIGHT = 0.1;
  private static final double BUFFER_HEIGHT = 0.016;
  private static final double ONE_LAYER_HEIGHT = BUFFER_HEIGHT*2+COMPON_HEIGHT;
  
  /**
  * Creates the UI pane for the main menu.
  */
  public static UIPane createMain() {
    UIPane mainMenu = new UIPane();
    
    // mainMenu.addState(UIState.DEFAULT, createButtons(
    //   new UIButton("New Game"       , Core::newGame),
    //   new UIButton("Quit to Desktop", Core::quitToDesk)
    // ));

    mainMenu.clear();
    
    return mainMenu;
  }

  public static UIElement createButtons(UIInteractable... interactables) {
    return new ElemListHoriz(
      new Vector2(0, 1-ONE_LAYER_HEIGHT),
      new Vector2(1, 1),
      COMPON_HEIGHT,
      BUFFER_HEIGHT,
      interactables,
      UIElement.TRANSITION_SLIDE_DOWN
    );
  }
}
