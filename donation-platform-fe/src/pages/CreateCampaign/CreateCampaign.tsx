import React, { useState } from 'react';
import styles from './CreateCampaign.module.css';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const CreateCampaign: React.FC = () => {
  const [name, setName] = useState('');
  const [image, setImage] = useState<File | null>(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [targetAmount, setTargetAmount] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!image) {
      toast.error('Molimo odaberite sliku.');
      return;
    }

    if (parseFloat(targetAmount) <= 0) {
      toast.error('Ciljani iznos mora biti veći od 0.');
      return;
    }

    try {
      const token = Cookies.get('accessToken');

      const formData = new FormData();
      formData.append('name', name);
      formData.append('image', image);
      formData.append('startDate', startDate);
      formData.append('endDate', endDate);
      formData.append('targetAmount', targetAmount);

      const response = await fetch('/api/campaign', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`
        },
        body: formData
      });

      if (response.ok) {
        toast.success('Kampanja uspješno kreirana!');
        setTimeout(() => navigate('/'), 2000); 
      } else {
        const err = await response.text();
        console.error('Greška sa servera:', err);
        toast.error('Greška pri kreiranju kampanje.');
      }
    } catch (error) {
      console.error('Greška:', error);
      toast.error('Došlo je do greške prilikom slanja podataka.');
    }
  };

  return (
    <div className={styles.formContainer}>
      <ToastContainer position="top-center" autoClose={3000} />

      <h2>Kreiraj kampanju</h2>
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <div className={styles.formGroup}>
          <label>Naziv kampanje</label>
          <input type="text" value={name} onChange={e => setName(e.target.value)} required />
        </div>

        <div className={styles.formGroup}>
          <label>Slika (JPG)</label>
          <input type="file" accept="image/jpeg" onChange={e => setImage(e.target.files?.[0] || null)} required />
        </div>

        <div className={styles.formGroup}>
          <label>Datum početka</label>
          <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} required />
        </div>

        <div className={styles.formGroup}>
          <label>Datum završetka</label>
          <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} required />
        </div>

        <div className={styles.formGroup}>
          <label>Ciljani iznos (KM)</label>
          <input type="number" value={targetAmount} onChange={e => setTargetAmount(e.target.value)} required />
        </div>

        <button type="submit" className={styles.submitButton}>
          Kreiraj kampanju
        </button>
      </form>
    </div>
  );
};

export default CreateCampaign;
