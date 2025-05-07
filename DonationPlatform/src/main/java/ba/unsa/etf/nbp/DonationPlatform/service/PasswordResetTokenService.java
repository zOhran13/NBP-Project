package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.PasswordResetToken;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordTokenRepository tokenRepository;

    public void createToken(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(calculateExpiryDate(60));
        tokenRepository.save(resetToken);
    }

    public PasswordResetToken validateToken(String token) {
        PasswordResetToken prt = tokenRepository.findByToken(token);
        if (prt == null || prt.getExpiryDate().before(new Date())) {
            return null;
        }
        return prt;
    }

    private Date calculateExpiryDate(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
}

