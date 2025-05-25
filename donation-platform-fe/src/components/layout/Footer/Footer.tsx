import React from 'react';
import { Link } from 'react-router-dom';
import styles from './Footer.module.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

export const Footer: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.footerContent}>
        <div className={styles.footerSection}>
          <h3>O Nama</h3>
          <p>
            Gradimo bolje sutra kroz zajedništvo i humanost. Naša platforma povezuje
            ljude dobre volje s onima kojima je potrebna pomoć.
          </p>
        </div>

        <div className={styles.footerSection}>
          <h3>Brzi Linkovi</h3>
          <ul>
            <li><Link to="/">Početna</Link></li>
            <li><Link to="/o-nama">O Nama</Link></li>
            <li><Link to="/get-involved">Pridruži se</Link></li>
            <li><Link to="/programs">Programi</Link></li>
          </ul>
        </div>

        <div className={styles.footerSection}>
          <h3>Kontakt</h3>
          <ul className={styles.contactInfo}>
            <li>
              <i className="fa-solid fa-location-dot"></i>
              <span>Adresa: Ferhadija 12, Sarajevo 71000</span>
            </li>
            <li>
              <i className="fa-solid fa-phone"></i>
              <span>Telefon: +387 33 123 456</span>
            </li>
            <li>
              <i className="fa-solid fa-envelope"></i>
              <span>Email: info@humanet.ba</span>
            </li>
          </ul>
        </div>

        <div className={styles.footerSection}>
          <h3>Pratite Nas</h3>
          <div className={styles.socialLinks}>
            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" aria-label="Facebook">
              <i className="fa-brands fa-facebook-f"></i>
            </a>
            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" aria-label="Twitter">
              <i className="fa-brands fa-twitter"></i>
            </a>
            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" aria-label="Instagram">
              <i className="fa-brands fa-instagram"></i>
            </a>
            <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn">
              <i className="fa-brands fa-linkedin-in"></i>
            </a>
          </div>
        </div>
      </div>

      <div className={styles.footerBottom}>
        <p>&copy; 2025 Humanet</p>
      </div>
    </footer>
  );
}; 