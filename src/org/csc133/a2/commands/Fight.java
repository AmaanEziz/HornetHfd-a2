package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class Fight extends Command {
    private GameWorld gw;

    public Fight(GameWorld gw){
        super("Fight");
        this.gw = gw;
    }

    public void actionPerformed(ActionEvent evt) {
        gw.fightFire();
    }
}
