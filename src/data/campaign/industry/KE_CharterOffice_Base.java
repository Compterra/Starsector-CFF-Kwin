package data.campaign.industry;

import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;

/**
 * Concrete no-op plugin for the charter parent entry in industries.csv.
 * Some mods instantiate every industry plugin during load, so the parent
 * grouping row cannot point directly at vanilla BaseIndustry.
 */
public class KE_CharterOffice_Base extends BaseIndustry {
    @Override
    public void apply() {
        super.apply(false);
    }
}
