package ba.unsa.etf.nbp.DonationPlatform.model;

import java.io.Serializable;
import java.util.Objects;

public class ReportUserCampaignTotalId implements Serializable {
    private Long userId;
    private Long campaignId;

    // default constructor
    public ReportUserCampaignTotalId() {}

    public ReportUserCampaignTotalId(Long userId, Long campaignId) {
        this.userId = userId;
        this.campaignId = campaignId;
    }

    // equals and hashCode required for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportUserCampaignTotalId)) return false;
        ReportUserCampaignTotalId that = (ReportUserCampaignTotalId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(campaignId, that.campaignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, campaignId);
    }
}

