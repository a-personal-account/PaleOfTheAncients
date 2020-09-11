package paleoftheancients;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.bandit.intent.DeadlyDashIntent;
import paleoftheancients.bandit.intent.HappyHitIntent;
import paleoftheancients.bandit.intent.MassivePartyIntent;
import paleoftheancients.bandit.intent.MoveAttackIntent;
import paleoftheancients.bandit.intent.arrows.GreenLeftArrowIntent;
import paleoftheancients.bandit.intent.arrows.GreenRightArrowIntent;
import paleoftheancients.bandit.intent.arrows.RedLeftArrowIntent;
import paleoftheancients.bandit.intent.arrows.RedRightArrowIntent;
import paleoftheancients.bandit.monsters.TheBandit;
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
import paleoftheancients.slimeboss.monsters.SlimeBossest;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.thesilent.monsters.TheSilentBoss;
import paleoftheancients.thevixen.intent.*;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.thorton.monsters.Thorton;
import paleoftheancients.watcher.intent.BlasphemyIntent;
import paleoftheancients.watcher.intent.PressurePointsIntent;
import paleoftheancients.watcher.monsters.TheWatcher;
import paleoftheancients.wokeone.monsters.WokeCultist;
import paleoftheancients.wokeone.monsters.WokeOne;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

@SpireInitializer
public class PaleMod implements
        PostInitializeSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber {
    public static final Logger logger = LogManager.getLogger(PaleMod.class.getSimpleName());

    private static Properties configProperties = new Properties();
    public static boolean skillsOnThorns = true;

    public static void initialize() {
        BaseMod.subscribe(new PaleMod());
        Color SHOWMAN_PURPLE = CardHelper.getColor(143, 109, 237);
        BaseMod.addColor(TheShowmanBoss.Enums.PALE_COLOR_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, SHOWMAN_PURPLE, PaleMod.assetPath("images/TheShowman/512/bg_attack_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/bg_skill_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/bg_power_default_gray.png"), PaleMod.assetPath("images/TheShowman/512/card_default_gray_orb.png"), PaleMod.assetPath("images/TheShowman/1024/bg_attack_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/bg_skill_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/bg_power_default_gray.png"), PaleMod.assetPath("images/TheShowman/1024/card_default_gray_orb.png"), PaleMod.assetPath("images/TheShowman/512/card_small_orb.png"));
    }

    public PaleMod() {
        configProperties.setProperty("skillsOnThorns", Boolean.toString(skillsOnThorns));
        loadConfigData();
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

        modPanel.addUIElement(new ModLabeledToggleButton(
                CardCrawlGame.languagePack.getRelicStrings(SoulOfTheGuardian.ID).FLAVOR,
                400.0f, 650.0f, Settings.CREAM_COLOR,
                FontHelper.charDescFont, skillsOnThorns, modPanel,
                label -> {},
                button -> {
                    skillsOnThorns = button.enabled;
                    saveConfigData();
                }));

        int before = AbstractDungeon.floorNum;
        AbstractDungeon.floorNum = 5;
        CustomDungeon.addAct(CustomDungeon.THEENDING, new PaleOfTheAncients());
        AbstractDungeon.floorNum = before;


        BaseMod.addMonster(TheShowmanBoss.ID, TheShowmanBoss::new);
        BaseMod.addMonster(TheVixenBoss.ID, TheVixenBoss::new);
        BaseMod.addMonster(IronCluck.ID, (BaseMod.GetMonster) IronCluck::new);
        BaseMod.addMonster(BardBoss.ID, BardBoss::new);
        BaseMod.addMonster(TheDefectBoss.ID, TheDefectBoss::new);
        BaseMod.addMonster(TheSilentBoss.ID, TheSilentBoss::new);
        BaseMod.addMonster(TheWatcher.ID, TheWatcher::new);
        BaseMod.addMonster(Reimu.ID, (BaseMod.GetMonster) Reimu::new);
        BaseMod.addMonster(TheBandit.ID, TheBandit::new);

        BaseMod.addMonster(N.ID, N::new);

        BaseMod.addMonster(Guardianest.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Guardianest(true),
                new Guardianest()
        }));
        BaseMod.addMonster(HexaghostPrime.ID, HexaghostPrime::new);
        BaseMod.addMonster(SlimeBossest.ID, SlimeBossest::new);


        BaseMod.addMonster(WokeOne.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new WokeOne(100.0F, 15.0F),
                new WokeCultist(-298.0F, -10.0F),
                new WokeCultist(-590.0F, 10.0F)
        }));
        BaseMod.addMonster(DonuDeca.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Decaer(),
                new Donuer()
        }));

        BaseMod.addMonster(SpireWaifu.ID, SpireWaifu::new);

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

        CustomIntent.add(new PressurePointsIntent());
        CustomIntent.add(new BlasphemyIntent());

        CustomIntent.add(new HappyHitIntent());
        CustomIntent.add(new DeadlyDashIntent());
        CustomIntent.add(new MoveAttackIntent());
        CustomIntent.add(new MassivePartyIntent());
        CustomIntent.add(new RedLeftArrowIntent());
        CustomIntent.add(new RedRightArrowIntent());
        CustomIntent.add(new GreenLeftArrowIntent());
        CustomIntent.add(new GreenRightArrowIntent());
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
        BaseMod.addAudio(makeID("touhou_timeout"), assetPath("/sfx/touhou_timeout.wav"));
    }

    @Override
    public void receiveEditRelics() {
        //event relics
        BaseMod.addRelic(new Timepiece(), RelicType.SHARED);
        BaseMod.addRelic(new KeyRelic(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheVixen(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheDefect(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheShowman(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheThorton(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheSilent(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheIroncluck(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheHexaghost(), RelicType.SHARED);
        BaseMod.addRelic(new BlurryLens(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheShrineMaiden(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheShapes(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheGuardian(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheWokeBloke(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheBard(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheCollector(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheWatcher(), RelicType.SHARED);
        BaseMod.addRelic(new SoulOfTheBandit(), RelicType.SHARED);
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

    public static String getCardName(Class clz) {
        return ((CardStrings) ReflectionHacks.getPrivateStatic(clz, "cardStrings")).NAME;
    }

    private void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_ID, "config", configProperties);
            config.load();

            skillsOnThorns = config.getBool("skillsOnThorns");
        } catch (Exception e) {
            e.printStackTrace();
            saveConfigData();
        }
    }
    private void saveConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_ID, "config", configProperties);
            config.setBool("skillsOnThorns", skillsOnThorns);

            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
