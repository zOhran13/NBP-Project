import React from 'react';
import { Link } from 'react-router-dom';
import styles from './AboutUs.module.css';
import aboutHero from '../../../assets/images/about-hero.png';
import missionImage from '../../../assets/images/charity-mission.png';
export const AboutUs: React.FC = () => {
  return (
    <div className={styles.aboutUsContainer}>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1>O Nama</h1>
          <p className={styles.subtitle}>
            Gradimo bolje sutra kroz zajedništvo i humanost
          </p>
        </div>
      </section>

      <section className={styles.mission}>
        <div className={styles.missionContent}>
          <h2>Naša Misija</h2>
          <p>
            Naša platforma je posvećena povezivanju ljudi dobre volje s onima kojima je potrebna pomoć.
            Kroz različite programe i inicijative, stvaramo mostove između donatora, volontera i
            korisnika, omogućavajući efikasan i transparentan način pružanja pomoći.
          </p>
        </div>
        <div className={styles.missionImage}>
          <img src={missionImage} alt="Naša misija" />
        </div>
      </section>

      <section className={styles.programs}>
  <h2>Naši Programi</h2>
  <div className={styles.programGrid}>
    <div className={styles.programCard}>
      
      <h3>🤝 Humanitarna Pomoć</h3>
      <p>
        Pružamo neposrednu pomoć osobama i zajednicama u potrebi kroz donacije
        i volonterske aktivnosti.
      </p>
     
    </div>

    <div className={styles.programCard}>
      <h3>🎓 Edukacija</h3>
      <p>
        Podržavamo obrazovne inicijative i programe koji omogućavaju pristup
        kvalitetnom obrazovanju svima.
      </p>
    </div>

    <div className={styles.programCard}>
      <div className={styles.programImage}>
      </div>
      <h3>🏥 Zdravlje</h3>
      <p>
        Radimo na poboljšanju zdravstvene zaštite i pristupa zdravstvenim uslugama
        za sve članove zajednice.
      </p>

    </div>
  </div>
</section>


      <section className={styles.values}>
        <h2>Naše Vrijednosti</h2>
        <div className={styles.valuesGrid}>
          <div className={styles.valueCard}>
            <h3>Transparentnost</h3>
            <p>
              Poslujemo s potpunom transparentnošću, osiguravajući da svaka donacija
              i aktivnost budu jasno praćene i dokumentovane.
            </p>
          </div>

          <div className={styles.valueCard}>
            <h3>Zajedništvo</h3>
            <p>
              Vjerujemo u snagu zajedništva i međusobne podrške u stvaranju
              pozitivnih promjena.
            </p>
          </div>

          <div className={styles.valueCard}>
            <h3>Održivost</h3>
            <p>
              Gradimo dugoročna rješenja koja će trajno poboljšati kvalitet života
              u našim zajednicama.
            </p>
          </div>
        </div>
      </section>

      <section className={styles.contact}>
        <div className={styles.contactContent}>
          <h2>Kontaktirajte Nas</h2>
          <p>
            Imate pitanja ili želite saznati više o našim programima? Slobodno nas kontaktirajte.
          </p>
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
      </section>
    </div>
  );
};