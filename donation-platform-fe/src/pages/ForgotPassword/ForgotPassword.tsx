import { Link } from 'react-router-dom';
import styles from './ForgotPassword.module.css';

export const ForgotPassword: React.FC = () => {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    alert("Link za reset lozinke je poslat na email.");
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
