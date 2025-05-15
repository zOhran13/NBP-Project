import { Link } from 'react-router-dom';
import styles from './ForgotPassword.module.css';

export const ForgotPassword: React.FC = () => {
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  
    const form = e.target as HTMLFormElement;
    const email = form.querySelector('input[type="email"]') as HTMLInputElement | null;
    const emailValue = email?.value;
  
    try {
      const response = await fetch(`http://localhost:8080/auth/request-reset?email=${encodeURIComponent(emailValue ?? '')}`, {
        method: 'POST',
      });
  
      if (response.ok) {
        alert("Link za reset lozinke je poslat na email.");
      } else if (response.status === 404) {
        alert("Korisnik sa unesenom email adresom nije pronađen.");
      } else {
        alert("Greška prilikom slanja zahtjeva.");
      }
    } catch (error) {
      console.error("Greška:", error);
      alert("Došlo je do greške. Pokušajte ponovo kasnije.");
    }
  };
  

  return (
    <div className={styles.forgotContainer}>

      <form className={styles.form} onSubmit={handleSubmit}>
        <h1>Zaboravljena lozinka</h1>
        <p>Unesite email adresu kako bismo vam poslali link za reset lozinke.</p>
        <input type="email" placeholder="Email adresa" required />
        <button type="submit">Pošalji link</button>

        <div className={styles.links}>
          <Link to="/login" className={styles.linkGreen}>Nazad na prijavu</Link>
        </div>
      </form>
    </div>
  );
};
