package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DonationEntryDTO {
    private LocalDate date;
    private BigDecimal amount;

    public DonationEntryDTO(LocalDate date, BigDecimal amount) {
        this.date = date;
        this.amount = amount;
    }

}

