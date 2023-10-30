package mki.core;

import mki.ui.components.UIInteractable;

public record Prompt(String text, UIInteractable[] options, Prompt[] queuedFollowing, Prompt[] randomFollowing) {}
