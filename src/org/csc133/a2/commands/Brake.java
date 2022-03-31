package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class Brake extends Command {
    public Brake() {
        super("Brake");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        GameWorld.getInstance().brake();
    }
}
