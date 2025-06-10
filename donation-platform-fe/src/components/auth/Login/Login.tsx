import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../../../services/auth.service';
import styles from './Login.module.css';
import { jwtDecode } from 'jwt-decode';

export const Login: React.FC = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const data = await login({ email, password });
      console.log('Login response:', data);
      let token = data?.accessToken || localStorage.getItem('token');
      if (token && typeof token === 'string') {
        localStorage.setItem('token', token);
        const decoded: any = jwtDecode(token);
        if (decoded.role === 'Admin') {
          navigate('/organization-dashboard');
          return;
        }
      } else {
        setError('Neispravan token sa servera.');
        return;
      }
      navigate('/');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Došlo je do greške. Pokušajte ponovo.');
    }
  };

  return (
    <div className={styles.loginContainer}>
      <form className={styles.form} onSubmit={handleLogin}>
        <h1>Prijava</h1>
        {error && <div className={styles.error}>{error}</div>}
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Lozinka"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Prijavi se</button>
        <div className={styles.links}>
          <Link to="/forgot-password" className={styles.linkGreen}>
            Zaboravili ste lozinku?
          </Link>
          <span className={styles.textStatic}>
            Nemate račun?{' '}
            <Link to="/signup" className={styles.linkDefault}>
              Registrujte se
            </Link>
          </span>
        </div>
      </form>
    </div>
  );
}; 