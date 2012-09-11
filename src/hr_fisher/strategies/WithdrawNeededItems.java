package hr_fisher.strategies;/*
    Name:
    Version:
    Author(s):
    
    Description:
    
*/

import hr_fisher.user.Util;
import hr_fisher.user.Variables;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.Item;

public class WithdrawNeededItems extends Strategy implements Runnable {
    @Override
    public void run() {

        if(Util.hasNeededItems()) {
            return;
        }

        Entity bank = Bank.getNearest();

        if(bank != null) {
            if(bank.isOnScreen()) {
                Camera.turnTo((Locatable)bank);
                if(!Bank.isOpen()) {
                    Bank.open();
                    Time.sleep(1000, 2000);
                } else {
                    int[] neededItems = Variables.chosenFishingType.getNeededItems();
                    for(int i : neededItems) {
                        final int temp = i;

                        if(Inventory.getCount(true, i) != 0)
                            continue;

                        if(Bank.getItemCount(true, i) == 0) {
                            Util.logout();
                            Util.waitFor(120 * 1000, new Condition() {
                                @Override
                                public boolean validate() {
                                    return !Game.isLoggedIn();
                                }
                            });
                            return;
                        }

                        if(i == Variables.ITEM_FEATHER || i == Variables.ITEM_BAIT) {
                            Bank.withdraw(i, Bank.getItemCount(true, i));

                            Util.waitFor(7000, new Condition() {
                                @Override
                                public boolean validate() {
                                    return Inventory.getCount(true, temp) > 0;
                                }
                            });

                        } else {

                            Bank.withdraw(i, 1);

                            Util.waitFor(7000, new Condition() {
                                @Override
                                public boolean validate() {
                                    return Inventory.getCount(true, temp) > 0;
                                }
                            });
                        }

                    }

                    Item[] toDeposit = Inventory.getItems(new Filter<Item>() {
                        @Override
                        public boolean accept(Item item) {
                            for(int j : Variables.chosenFishingType.getNeededItems()) {
                                if(j == item.getId())
                                    return false;
                            }
                            return true;
                        }
                    });

                    for(Item i : toDeposit) {

                        final int itemID = i.getId();
                        Bank.deposit(itemID, Inventory.getCount(true, itemID));
                        Util.waitFor(1000, new Condition() {
                            @Override
                            public boolean validate() {
                                return Inventory.getCount(true, itemID) == 0;
                            }
                        });
                    }

                }
            }
        }
    }

    @Override
    public boolean validate() {
        return Variables.hasStarted && !Util.hasNeededItems();
    }
}