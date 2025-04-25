import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Signup.module.css';

export const Signup: React.FC = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    username: '',
    phoneNumber: '',
    birthDate: '',
    addressId: 21,
    roleId: 144
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      alert("Lozinke se ne poklapaju.");
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          firstName: formData.firstName,
          lastName: formData.lastName,
          email: formData.email,
          password: formData.password,
          username: formData.username,
          phoneNumber: formData.phoneNumber,
          birthDate: formData.birthDate,
          addressId: formData.addressId,
          roleId: formData.roleId,
        }),
      });

      if (response.ok) {
        alert('Registracija uspješna!');
        navigate('/login');
      } else {
        const err = await response.text();
        alert(`Greška: ${err}`);
      }
    } catch (error) {
      alert('Greška prilikom registracije.');
      console.error(error);
    }
  };

  return (
    <div className={styles.signupContainer}>
      <form className={styles.form} onSubmit={handleSignup}>
        <h1>Registruj se</h1>
        <input type="text" name="firstName" placeholder="Ime" required onChange={handleChange} />
        <input type="text" name="lastName" placeholder="Prezime" required onChange={handleChange} />
        <input type="text" name="username" placeholder="Korisničko ime" required onChange={handleChange} />
        <input type="email" name="email" placeholder="Email" required onChange={handleChange} />
        <input type="password" name="password" placeholder="Lozinka" required onChange={handleChange} />
        <input type="password" name="confirmPassword" placeholder="Potvrdi lozinku" required onChange={handleChange} />
        <input type="text" name="phoneNumber" placeholder="Telefon" onChange={handleChange} />
        <input type="date" name="birthDate" placeholder="Datum rođenja" onChange={handleChange} />
        <button type="submit">Kreiraj račun</button>

        <span className={styles.textStatic}>
          Već imate račun? <Link to="/login" className={styles.linkGreen}>Prijavite se</Link>
        </span>

      </form>
    </div>
  );
};
