package paleoftheancients.bandit.intent;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class EnumBuster {
    @SpireEnum
    public static AbstractMonster.Intent HappyHitIntent;
    @SpireEnum
    public static AbstractMonster.Intent DeadlyDashIntent;
    @SpireEnum
    public static AbstractMonster.Intent MoveAttackIntent;
    @SpireEnum
    public static AbstractMonster.Intent MassivePartyIntent;
}
