package org.csc133.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class Drink extends Command {
    private GameWorld gw;

    public Drink(GameWorld gw){
        super("Drink");
        this.gw = gw;
    }

    public void actionPerformed(ActionEvent e){
        gw.drinkWater();
    }
}
