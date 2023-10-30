package mki.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mki.parser.PromptParser;
import mki.ui.components.UIInteractable;
import mki.ui.components.interactables.UIButton;
import mki.ui.control.UIController;

public abstract class Core {
  
  public static final Window WINDOW = new Window();
  
  private static int textSpeed = 16;

  private static long tickCount;
  
  private static boolean quit = false;

  private static final List<Prompt> roots = PromptParser.loadPrompts();

  private static final Stack<Prompt> stack = new Stack<>();

  private static final List<Prompt> random = new ArrayList<>();

  private static Prompt currentPrompt = null;

  private static final UIInteractable[] mainMenu = {
    new UIButton("New Game"       , Core::newGame),
    new UIButton("Quit to Desktop", Core::quitToDesk)
  };

  private static final Prompt winPrompt = new Prompt(
    "Well done, you've run out of problems to solve without failing catastrophically!\n\nYou win!",
    mainMenu,
    new Prompt[0],
    new Prompt[0]
  );

  public static final String SPACER = "\n\n\n\n                                          \n";
  
  static {
    WINDOW.setFullscreen(false);
    
    UIController.putPane("Main Menu", UICreator.createMain());
    UIController.setCurrentPane("Main Menu");

    UIController.displayTempElement(UICreator.createButtons(mainMenu));

    Controls.initialiseControls(WINDOW.FRAME);
  }
  
  public static void main(String[] args) {
    run();
  }

  public static void newGame() {
    TextWindow.clear();
    stack.clear();
    random.clear();

    currentPrompt = roots.get((int)(Math.random()*roots.size()));

    TextWindow.addText(currentPrompt.text());
    stack.addAll(List.of(currentPrompt.queuedFollowing()));
    random.addAll(List.of(currentPrompt.randomFollowing()));
    UIController.clearTempElement();
  }

  public static void addNewPrompts(Prompt[] queued, Prompt[] random) {
    Core.stack.addAll(List.of(queued));
    Core.random.addAll(List.of(random));
  }

  public static void nextPrompt() {
    currentPrompt = stack.empty() ? random.isEmpty() ? winPrompt : 
      random.remove((int)(Math.random()*random.size())) : 
      stack.pop();

    TextWindow.addText(SPACER+currentPrompt.text());
    stack.addAll(List.of(currentPrompt.queuedFollowing()));
    random.addAll(List.of(currentPrompt.randomFollowing()));
    UIController.clearTempElement();
  }

  public static void onFinishedTextWindow() {
    UIController.displayTempElement(UICreator.createButtons(currentPrompt.options()));
  }

  public static void setTextSpeed(int textSpeed) {
    Core.textSpeed = textSpeed;
  }

  public static UIInteractable[] getMainMenu() {
    return new UIInteractable[]{
      new UIButton("New Game"       , Core::newGame),
      new UIButton("Quit to Desktop", Core::quitToDesk)
    };
  }
  
  /**
  * Sets a flag to close the program at the nearest convenience
  */
  public static void quitToDesk() {
    quit = true;
  }
  
  /**
  * Main loop. Should always be running. Runs the rest of the game engine
  */
  private static void run() {
    while (true) {
      long tickTime = System.currentTimeMillis();

      if (tickCount%2 == 0) TextWindow.update();
      
      if (quit) {
        System.exit(0);
      }
      
      WINDOW.PANEL.repaint();
      tickTime = System.currentTimeMillis() - tickTime;
      tickCount++;
      try {
        Thread.sleep(Math.max((long)(textSpeed - tickTime), 0));
      } catch(InterruptedException e){System.out.println(e); System.exit(0);}
    }
  }
  
  /**
  * Paints the contents of the program to the given {@code Graphics} object.
  * 
  * @param gra the supplied {@code Graphics} object
  */
  public static void paintComponent(Graphics gra) {
    Graphics2D g = (Graphics2D)gra;
    
    g.setColor(Color.black);
    g.fillRect(0, 0, WINDOW.screenWidth(), WINDOW.screenHeight());

    TextWindow.draw(g);
    
    g.setColor(Color.darkGray);
    g.fillRect(0, (int)(0.868*WINDOW.screenHeight()), WINDOW.screenWidth(), (int)(0.132*WINDOW.screenHeight()));
    
    UIController.draw(g, WINDOW.screenWidth(), WINDOW.screenHeight());

    g.setColor(Color.lightGray);
    g.fillRect(0, (int)(0.860*WINDOW.screenHeight()), WINDOW.screenWidth(), (int)(0.016*WINDOW.screenHeight()));
  }
}