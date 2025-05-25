import { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './ForgotPassword.module.css';

export const ForgotPassword: React.FC = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implement password reset functionality
    setMessage('Link za resetovanje lozinke je poslan na vaš email.');
  };

  return (
    <div className={styles.forgotContainer}>
      <form className={styles.form} onSubmit={handleSubmit}>
        <h1>Zaboravljena lozinka</h1>
        <p>Unesite vašu email adresu i poslat ćemo vam link za resetovanje lozinke.</p>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit">Pošalji</button>
        {message && <p className={styles.message}>{message}</p>}
        <div className={styles.links}>
          <Link to="/login" className={styles.linkGreen}>
            Nazad na prijavu
          </Link>
        </div>
      </form>
    </div>
  );
}; 