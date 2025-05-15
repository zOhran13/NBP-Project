import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import styles from './ResetPassword.module.css';

export const ResetPassword: React.FC = () => {
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get('token');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert('Lozinke se ne poklapaju!');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/auth/reset-password?token=${encodeURIComponent(token)}&newPassword=${encodeURIComponent(password)}`, {
        method: 'POST'
      });

      if (response.ok) {
        alert('Lozinka je uspješno promijenjena!');
        navigate('/login');
      } else {
        const data = await response.json();
        alert(data.message || 'Greška prilikom promjene lozinke.');
      }
    } catch (error) {
      console.error('Greška:', error);
      alert('Došlo je do greške. Pokušajte ponovo kasnije.');
    }
  };

  if (!token) {
    return <div className={styles.error}>Nevažeći ili istekao token za reset lozinke.</div>;
  }

  return (
    <div className={styles.resetContainer}>
      <form className={styles.form} onSubmit={handleSubmit}>
        <h1>Reset lozinke</h1>
        <p>Unesite novu lozinku</p>
        
        <input
          type="password"
          placeholder="Nova lozinka"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          minLength={6}
        />
        
        <input
          type="password"
          placeholder="Potvrdite novu lozinku"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
          minLength={6}
        />
        
        <button type="submit">Promijeni lozinku</button>
      </form>
    </div>
  );
}; 