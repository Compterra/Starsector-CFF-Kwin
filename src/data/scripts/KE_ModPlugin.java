package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.campaign.KE_CampaignPluginImpl;
import data.campaign.KE_UE_Service;
import data.campaign.item.KE_EB_CloneCoreItemEffect;
import data.combat.ai.KE_YZ_EnergyParticleAI;
import data.combat.ai.KE_YZ_MEM_EP_RocketAI;
import data.hullmods.KE_Delicacy_Fleet_Manager.KE_UE_Delicacy_FleetListener;
import data.combat.ai.KE_YZ_ScimitarTorpedoAI;
import data.combat.ai.KE_YZ_SunChaserAI;
import org.apache.log4j.Logger;
import org.dark.shaders.util.TextureData;

public class KE_ModPlugin extends BaseModPlugin {
    private static final Logger log = Global.getLogger(KE_ModPlugin.class);

    private static final String[] CORPORATE_FACTIONS = {
            "KE_EWCorporation",
            "KE_TACorporation",
            "KE_UECorporation",
            "KE_YZCorporation",
    };

    public static final String ScimitarTorpedo_ID = "KE_YZ_ScimitarTorpedo_missile";
    public static final String SunChaser_ID = "KE_YZ_SunChaserMissile_missile";
    public static final String EnergySwarm_ID = "KE_YZ_EnergyParticle_shell";
    public static final String MS_EnergySwarm_ID = "KE_YZ_MS_EnergyParticle_shell";
    public static final String MEM_EnergySwarm_ID = "KE_YZ_MEM_EnergyParticle_shell";
    public static final String MEM_EnergyParticle_Rocket_ID = "KE_YZ_MEM_EnergyParticle_rocket_shell";

    @Override
    public void onGameLoad(boolean newGame) {
//        log.info("onGameLoad Activate");
//        if(!Global.getSector().getListenerManager().hasListenerOfClass(KE_UE_Delicacy_FleetListener.class)){
//            Global.getSector().addListener(new KE_UE_Delicacy_FleetListener());
//        }
//        if(Global.getSector().getListenerManager().hasListenerOfClass(KE_UE_Delicacy_FleetListener.class)){
//            log.info("listener Activate");
//        }
        Global.getSector().registerPlugin(new KE_CampaignPluginImpl());
        KE_UE_Delicacy_FleetListener.register();
        KE_UE_Service.register();
        registerCorporateBountyFactions();
    }

    @Override
    public PluginPick<MissileAIPlugin> pickMissileAI(MissileAPI missile, ShipAPI launchingShip) {
        switch (missile.getProjectileSpecId()) {
            case ScimitarTorpedo_ID:
                return new PluginPick<MissileAIPlugin>(new KE_YZ_ScimitarTorpedoAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case SunChaser_ID:
                return new PluginPick<MissileAIPlugin>(new KE_YZ_SunChaserAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case EnergySwarm_ID:
            case MS_EnergySwarm_ID:
            case MEM_EnergySwarm_ID:
                return new PluginPick<MissileAIPlugin>(new KE_YZ_EnergyParticleAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case MEM_EnergyParticle_Rocket_ID:
                return new PluginPick<MissileAIPlugin>(new KE_YZ_MEM_EP_RocketAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
        }
        return null;
    }

    @Override
    public void beforeGameSave() {

    }

    @Override
    public void onApplicationLoad() {
        loadGraphicsLibTextureData();
        ItemEffectsRepo.ITEM_EFFECTS.put("KE_EB_CloneCore", new KE_EB_CloneCoreItemEffect("KE_EB_CloneCore"));
    }

    @Override
    public void onNewGame() {
        new KE_NormalGenerate().generate(Global.getSector());

        KE_UE_Delicacy_FleetListener.register();
        KE_UE_Service.register();
        registerCorporateBountyFactions();
    }

    private static void registerCorporateBountyFactions() {
        for (String factionId : CORPORATE_FACTIONS) {
            if (!SharedData.getData().getPersonBountyEventData().getParticipatingFactions().contains(factionId)) {
                SharedData.getData().getPersonBountyEventData().addParticipatingFaction(factionId);
            }
        }
    }

    private static void loadGraphicsLibTextureData() {
        try {
            TextureData.readTextureDataCSVNoOverwrite("data/lights/kwin_texture_data.csv");
        } catch (Exception ex) {
            log.warn("Failed to load Kwin GraphicsLib texture data", ex);
        }
    }

    public static boolean NEX() {
        return Global.getSettings().getModManager().isModEnabled("nexerelin");
    }
}
