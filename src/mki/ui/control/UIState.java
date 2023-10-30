package mki.ui.control;

/**
* Defines different ui states
*/
public enum UIState {
  //general

  /** The standard state used by default as the 'base layer' of a pane */
  DEFAULT,
  /** A state for options */
  OPTIONS,
  /** A state for video settings */
  VIDEO,
  /** A state for audio settings */
  AUDIO,
  /** A state for gameplay settings */
  GAMEPLAY,

  //menu specific
  
  /** A state for a new game menu */
  NEW_GAME,
  /** A state for a load game menu */
  LOAD_GAME,
  /** A state for a save game menu */
  SAVE_GAME,
  /** A state for a host game menu */
  SETUP_HOST,
  /** A state for a join game menu */
  SETUP_CLIENT,
  /** A state for a lobby menu */
  LOBBY,
  /** A state for a hosted lobby menu */
  LOBBY_HOST,

  //gameplay specific
  
  /** A state for a pause menu */
  PAUSED,
  /** A state for a win screen */
  WIN,
  /** A state for a lose screen */
  LOSE,
}
