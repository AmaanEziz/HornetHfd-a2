package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class TurnRight extends Command {
    private GameWorld gw;

    public TurnRight(GameWorld gw){
        super("Right");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        gw.headHelicopterRight();
    }
}
