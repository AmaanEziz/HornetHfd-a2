package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class TurnLeft extends Command {
    public TurnLeft() {
        super("Left");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        GameWorld.getInstance().turnLeft();
    }
}
