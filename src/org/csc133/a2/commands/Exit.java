package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class Exit extends Command {
    private GameWorld gw;

    public Exit(GameWorld gw){
        super("Exit");
        this.gw = gw;
    }

    public void actionPerformed(ActionEvent e){
        gw.quit();
    }
}
