package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;
import org.csc133.a2.gameobjects.Helicopter;

public class Fight extends Command {
    public Fight() {
        super("Fight");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        GameWorld.getInstance()
                 .attemptFightFire(Helicopter.getInstance());
    }
}
