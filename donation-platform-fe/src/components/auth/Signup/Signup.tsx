import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Signup.module.css';
import { AddressForm, AddressDTO } from '../../common/AddressForm/AddressForm';

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
    roleId: 144
  });
  const [address, setAddress] = useState<AddressDTO>({
    street: '',
    city: '',
    postalCode: '',
    country: ''
  });
  const [addressInput, setAddressInput] = useState('');

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
          address: {
            street: address.street,
            city: address.city,
            postalCode: address.postalCode ? Number(address.postalCode) : undefined,
            country: address.country
          },
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
    <div className={styles.signupFlexContainer}>
      <form className={styles.form} onSubmit={handleSignup}>
      <h1>Registracija</h1>
        <div className={styles.formContainer}>
          {/* Left column */}
          <div>
            <input
              type="text"
              name="firstName"
              placeholder="Ime"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="lastName"
              placeholder="Prezime"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="username"
              placeholder="Korisničko ime"
              value={formData.username}
              onChange={handleChange}
              required
            />
            <input
              type="password"
              name="password"
              placeholder="Lozinka"
              value={formData.password}
              onChange={handleChange}
              required
            />
            <input
              type="password"
              name="confirmPassword"
              placeholder="Potvrdi lozinku"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
          </div>
          {/* Right column */}
          <div>
            <input
              type="tel"
              name="phoneNumber"
              placeholder="Broj telefona"
              value={formData.phoneNumber}
              onChange={handleChange}
              required
            />
            <input
              type="date"
              name="birthDate"
              value={formData.birthDate}
              onChange={handleChange}
              required
            />
            <div className={styles.addressFormContainer}>
              <h2>Adresa</h2>
              <AddressForm
                address={address}
                onAddressChange={setAddress}
                addressInput={addressInput}
                onAddressInputChange={setAddressInput}
                disabled={false}
                isEditing={true}
                hideLabels={true}
                columnLayout={true}
              />
            </div>
          </div>
        </div>
        <button type="submit">Registruj se</button>
        <div className={styles.links}>
          <span className={styles.textStatic}>
            Već imate račun?{' '}
            <Link to="/login" className={styles.linkDefault}>
              Prijavite se
            </Link>
          </span>
        </div>
      </form>
     
    </div>
  );
}; 