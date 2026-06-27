package data.campaign;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.AICoreOfficerPlugin;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;

public class KE_CampaignPluginImpl extends BaseCampaignPlugin {
    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public PluginPick<AICoreOfficerPlugin> pickAICoreOfficerPlugin(String commodityId) {

        if (commodityId.contentEquals("KE_entropy_core")) {
            return new PluginPick<AICoreOfficerPlugin>(new KE_EntropyCoreOfficerImpl(), PickPriority.MOD_SPECIFIC);
        }

        /*
        if (commodityId.contentEquals("mimikko_zero_core")) {
            return new PluginPick<AICoreOfficerPlugin>(new Mimikko_aicorepluginImpl(), PickPriority.MOD_SPECIFIC);
        }
        if (commodityId.contentEquals("mimikko_ariana_core")) {
            return new PluginPick<AICoreOfficerPlugin>(new Mimikko_aicorepluginImpl(), PickPriority.MOD_SPECIFIC);
        }
        if (commodityId.contentEquals("mimikko_miruko_core")) {
            return new PluginPick<AICoreOfficerPlugin>(new Mimikko_aicorepluginImpl(), PickPriority.MOD_SPECIFIC);
        }
        if (commodityId.contentEquals("mimikko_miruku_core")) {
            return new PluginPick<AICoreOfficerPlugin>(new Mimikko_aicorepluginImpl(), PickPriority.MOD_SPECIFIC);
        }

         */
        //for (OfficerDataAPI officer : Global.getSector().getPlayerFleet().getFleetData().getOfficersCopy()) {
        //    if (officer.getPerson().getId().equals("Mimikko_ariana")) {
        //        return new PluginPick<AICoreOfficerPlugin>(new Mimikko_aicorepluginImpl(), PickPriority.MOD_SPECIFIC);
        //    }

        //    }
        return null;
    }
    public PluginPick<AICoreAdminPlugin> pickAICoreAdminPlugin(String commodityId) {
        if (commodityId.contentEquals("KE_entropy_core")) {
            return new PluginPick<AICoreAdminPlugin>(new KE_EntropyCoreAdminImpl(), PickPriority.MOD_SPECIFIC);
        }
        return null;
    }
}
