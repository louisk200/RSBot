package hr_fisher.user;/*
    Name:
    Version:
    Author(s):
    
    Description:
    
*/

import hr_fisher.locations.*;

public class Variables {
    public static final int ITEM_FEATHER = 314;
    public static final int ITEM_BAIT = 313;

    public static final int TYPE_POWERFISH = 0;
    public static final int TYPE_BANK = 1;
    public static final int TYPE_STILES = 2;
    public static final int TYPE_F1D1 = 3;

    public static final int TUNA_ID = 359;

    public static boolean hasStarted = false;
    public static boolean isDropping = false;
    public static boolean dropTuna = false;

    public static long startTime = 0;
    public static int startXP = 0;
    public static int[] fishCaught;
    public static int[] fishPrice;

    public static int startLevel = 0;
    public static int bankingType = TYPE_BANK;

    public static Location[] locations = new Location[] {
            new BarbarianVillage(),
            new AlKharid(),
            new Karamja(),
            new Draynor(),
            new Lumbridge(),
            new Catherby(),
            new FishingGuild(),
            new ShiloVillage()
    };

    public static Location chosenLocation = locations[0];

    public static Util.FishingTypes chosenFishingType = Util.FishingTypes.TYPE_BAIT;
}