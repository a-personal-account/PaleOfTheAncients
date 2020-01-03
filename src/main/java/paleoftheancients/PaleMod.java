package paleoftheancients;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.intents.DervishDanceIntent;
import paleoftheancients.bard.intents.FlourishIntent;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.collector.monsters.SpireWaifu;
import paleoftheancients.donudeca.monsters.Decaer;
import paleoftheancients.donudeca.monsters.DonuDeca;
import paleoftheancients.donudeca.monsters.Donuer;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.guardian.monsters.Guardianest;
import paleoftheancients.helpers.BASlime;
import paleoftheancients.hexaghost.monsters.HexaghostPrime;
import paleoftheancients.ironcluck.monsters.IronCluck;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.relics.*;
import paleoftheancients.savefields.BreadCrumbs;
import paleoftheancients.savefields.ElitesSlain;
import paleoftheancients.slimeboss.monsters.SlimeBossest;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.thesilent.monsters.TheSilentBoss;
import paleoftheancients.thevixen.intent.*;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.thorton.monsters.Thorton;
import paleoftheancients.wokeone.monsters.WokeCultist;
import paleoftheancients.wokeone.monsters.WokeOne;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpireInitializer
public class PaleMod implements
        PostInitializeSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber {
    public static final Logger logger = LogManager.getLogger(PaleMod.class.getSimpleName());

    public static void initialize() {
        BaseMod.subscribe(new PaleMod());
        BreadCrumbs.initialize();
        ElitesSlain.initialize();
        Color SHOWMAN_PURPLE = CardHelper.getColor(143.0F, 109.0F, 237.0F);
        BaseMod.addColor(TheShowmanBoss.Enums.PALE_COLOR_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, PaleMod.assetPath("images/TheShowman/512/bg_attack_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/bg_skill_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/bg_power_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/card_default_gray_orb.png"), PaleMod.assetPath("images/TheShowman/1024/bg_attack_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/bg_skill_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/bg_power_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/card_default_gray_orb.png"), PaleMod.assetPath("images/TheShowman/512/card_small_orb.png"));
    }

    public static boolean bardLoaded() {
        return Loader.isModLoaded("bard");
    }

    public static String MOD_ID = "paleoftheancients";
    public static String makeID(String id) {
        return MOD_ID + ":" + id;
    }
    public static String assetPath(String path) {
        return MOD_ID + "/" + path;
    }

    @Override
    public void receivePostInitialize() {

        Texture badgeTexture = new Texture(MOD_ID + "/images/badge.png");
        ModPanel modPanel = new ModPanel();
        BaseMod.registerModBadge(
                badgeTexture, "Pale of the Ancients", "Razash",
                "It's an act 4. It probably sucks.", modPanel);

        int before = AbstractDungeon.floorNum;
        AbstractDungeon.floorNum = 5;
        CustomDungeon.addAct(CustomDungeon.THEENDING, new PaleOfTheAncients());
        AbstractDungeon.floorNum = before;


        BaseMod.addMonster(TheShowmanBoss.ID, () -> new TheShowmanBoss());
        BaseMod.addMonster(TheVixenBoss.ID, () -> new TheVixenBoss());
        BaseMod.addMonster(IronCluck.ID, () -> new IronCluck());
        BaseMod.addMonster(BardBoss.ID, () -> new BardBoss());
        BaseMod.addMonster(TheDefectBoss.ID, () -> new TheDefectBoss());
        BaseMod.addMonster(TheSilentBoss.ID, () -> new TheSilentBoss());
        BaseMod.addMonster(Reimu.ID, () -> new Reimu());

        BaseMod.addMonster(N.ID, () -> new N());

        BaseMod.addMonster(Guardianest.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Guardianest(true),
                new Guardianest()
        }));
        BaseMod.addMonster(HexaghostPrime.ID, () -> new HexaghostPrime());
        BaseMod.addMonster(SlimeBossest.ID, () -> new SlimeBossest());


        BaseMod.addMonster(WokeOne.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new WokeOne(100.0F, 15.0F),
                new WokeCultist(-298.0F, -10.0F),
                new WokeCultist(-590.0F, 10.0F)
        }));
        BaseMod.addMonster(DonuDeca.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Decaer(),
                new Donuer()
        }));

        BaseMod.addMonster(SpireWaifu.ID, () -> new SpireWaifu());

        PaleOfTheAncients.addEventMonster(Thorton.ID, new Thorton());
        PaleOfTheAncients.addEventMonster(BASlime.ID, new BASlime());

        BaseMod.addBoss(PaleOfTheAncients.ID, N.ID, assetPath("images/misc/emptypixel.png"), assetPath("images/misc/emptypixel.png"));


        CustomIntent.add(new SwaggerIntent());
        CustomIntent.add(new PsybeamIntent());
        CustomIntent.add(new EmberIntent());
        CustomIntent.add(new SolarBeamIntent());
        CustomIntent.add(new SelfDebuffIntent());
        CustomIntent.add(new FacadeIntent());
        CustomIntent.add(new FlameWheelIntent());
        CustomIntent.add(new FireSpinIntent());

        CustomIntent.add(new DervishDanceIntent());
        CustomIntent.add(new FlourishIntent());
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(makeID("vixencry"), assetPath("/sfx/braix.wav"));

        BaseMod.addAudio(makeID("cluck"), assetPath("/sfx/cluck.ogg"));
        BaseMod.addAudio(makeID("cuccostart"), assetPath("/sfx/OOT_6amRooster.wav"));
        BaseMod.addAudio(makeID("cucco1"), assetPath("/sfx/OOT_Cucco1.wav"));
        BaseMod.addAudio(makeID("cucco2"), assetPath("/sfx/OOT_Cucco2.wav"));

        BaseMod.addAudio(makeID("touhou_powerup"), assetPath("/sfx/touhou_powerup.ogg"));
        BaseMod.addAudio(makeID("touhou_spellcard"), assetPath("/sfx/touhou_spellcard.ogg"));
        BaseMod.addAudio(makeID("touhou_attack"), assetPath("/sfx/touhou_attack.ogg"));
        BaseMod.addAudio(makeID("touhou_death"), assetPath("/sfx/touhou_death.ogg"));
        BaseMod.addAudio(makeID("touhou_defeat"), assetPath("/sfx/touhou_defeat.ogg"));
        BaseMod.addAudio(makeID("touhou_shot"), assetPath("/sfx/touhou_shot.ogg"));
        BaseMod.addAudio(makeID("touhou_seal"), assetPath("/sfx/touhou_seal.ogg"));
        BaseMod.addAudio(makeID("touhou_border"), assetPath("/sfx/touhou_border.ogg"));
    }

    @Override
    public void receiveEditRelics() {
        //event relics
        BaseMod.addRelic(new Timepiece(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheVixen(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheDefect(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheShowman(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheThorton(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheSilent(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheIroncluck(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheHexaghost(), RelicType.SHARED);
        BaseMod.addRelic(new BlurryLens(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheShrineMaiden(), RelicType.SHARED);
    }

    private Settings.GameLanguage languageSupport()
    {
        switch (Settings.language) {
            case ZHS:
                return Settings.language;
            default:
                return Settings.GameLanguage.ENG;
        }
    }
    private void loadLocStrings(Settings.GameLanguage language)
    {
        String path = "localization/" + language.toString().toLowerCase() + "/";

        MelodyManager.loadMelodyStrings(assetPath(path + "MelodyStrings.json"));

        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath(path + "events.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath(path + "ui.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(path + "cards.json"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, assetPath(path + "monsters.json"));
        BaseMod.loadCustomStringsFile(ScoreBonusStrings.class, assetPath(path + "score_bonuses.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath(path + "powers.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath(path + "relics.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath(path + "characters.json"));
    }

    @Override
    public void receiveEditStrings()
    {
        Settings.GameLanguage language = languageSupport();

        // Load english first to avoid crashing if translation doesn't exist for something
        loadLocStrings(Settings.GameLanguage.ENG);
        if(!language.equals(Settings.GameLanguage.ENG)) {
            loadLocStrings(language);
        }
    }


    void loadLocKeywords(Settings.GameLanguage language) {
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/" + language.toString().toLowerCase() + "/keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        Map<String, Keyword> keywords = gson.fromJson(json, typeToken);
        keywords.forEach((k, v) -> {
            BaseMod.addKeyword(MOD_ID.toLowerCase(), v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditKeywords() {
        Settings.GameLanguage language = languageSupport();

        // Load english first to avoid crashing if translation doesn't exist for something
        loadLocKeywords(Settings.GameLanguage.ENG);
        if(!language.equals(Settings.GameLanguage.ENG)) {
            loadLocKeywords(language);
        }
    }

}
