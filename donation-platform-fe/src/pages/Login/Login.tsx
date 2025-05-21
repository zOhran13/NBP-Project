import { useState } from 'react';
import Cookies from 'js-cookie';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Login.module.css';

export const Login: React.FC = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login attempt with email:', email);

    try {
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Login successful, received token:', data.accessToken);
        Cookies.set('accessToken', data.accessToken, { expires: 1 });
        console.log('Token stored in cookie:', Cookies.get('accessToken'));
        navigate('/');
      } else {
        console.error('Login failed with status:', response.status);
        setError('Neispravni email ili lozinka');
      }
    } catch (err) {
      console.error('Login error:', err);
      setError('Došlo je do greške. Pokušajte ponovo.');
    }
  };

  return (
    <div className={styles.loginContainer}>
      <form className={styles.form} onSubmit={handleLogin}>
        <h1>Prijavi se</h1>
        {error && <p className={styles.error}>{error}</p>}
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
        <button type="submit">Prijava</button>

       <div className={styles.links}>
         <span className={styles.textStatic}>
           Nemate račun?{' '}
           <Link to="/signup" className={styles.linkGreen}>Registruj se</Link>
         </span>
         <Link to="/forgot-password" className={styles.linkDefault}>Zaboravljena lozinka?</Link>
       </div>

      </form>
    </div>
  );
};
