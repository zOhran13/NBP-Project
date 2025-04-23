import { Program } from '../../../types/common';
import styles from './Programs.module.css';

const programi: Program[] = [
  {
    icon: '💧',
    title: 'Pomoć u Nesreći',
    description: 'Pružanje hitne pomoći i podrške zajednicama pogođenim prirodnim katastrofama i drugim kriznim situacijama.',
  },
  {
    icon: '📚',
    title: 'Obrazovanje',
    description: 'Pružanje obrazovnih resursa i prilika za djecu kojoj je pomoć potrebna, uključujući školski pribor i mentorstvo.',
  },
  {
    icon: '🏥',
    title: 'Zdravstvene Usluge',
    description: 'Osiguravanje pristupa osnovnim zdravstvenim uslugama i medicinskoj pomoći za osobe u stanju potrebe.',
  },
];

export const Programs: React.FC = () => {
  return (
    <section className={styles.programi}>
      <h2 className={styles.naslov}>Naši Programi</h2>
      <p className={styles.podnaslov}>
        Kroz naše raznovrsne programe, radimo na stvaranju pozitivnog utjecaja u zajednici i pružanju podrške onima kojima je najpotrebnija.
      </p>
      <div className={styles.mreza}>
        {programi.map((program) => (
          <div key={program.title} className={styles.kartica}>
            <div className={styles.ikona}>{program.icon}</div>
            <h3 className={styles.naslovKartice}>{program.title}</h3>
            <p className={styles.opis}>{program.description}</p>
            <a href="#" className={styles.link}>Saznaj više</a>
          </div>
        ))}
      </div>
    </section>
  );
}; 