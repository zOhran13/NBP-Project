import React from 'react';
import { useParams, Link } from 'react-router-dom';
import styles from './ProgramDetail.module.css';

interface ProgramData {
  id: string;
  title: string;
  description: string;
  imageUrl: string;
  impact: string[];
  howToHelp: string[];
  currentProjects: {
    title: string;
    description: string;
    status: string;
  }[];
}

const programData: Record<string, ProgramData> = {
  humanitarnapomoc: {
    id: 'humanitarna-pomoc',
    title: 'Humanitarna Pomoć',
    description: 'Naš program humanitarne pomoći je posvećen pružanju neposredne podrške osobama i zajednicama u potrebi. Kroz koordinirane akcije i strateške inicijative, nastojimo osigurati da pomoć stigne do onih kojima je najpotrebnija.',
    imageUrl: '/images/humanitarian.jpg',
    impact: [
      'Pružanje neposredne pomoći u slučajevima prirodnih katastrofa',
      'Podrška ugroženim grupama stanovništva',
      'Distribucija humanitarne pomoći i osnovnih životnih potrepština',
      'Organizacija volonterskih akcija za pomoć zajednici'
    ],
    howToHelp: [
      'Donirajte novčana sredstva za humanitarne akcije',
      'Pridružite se kao volonter',
      'Donirajte materijalnu pomoć',
      'Podijelite informacije o našim aktivnostima'
    ],
    currentProjects: [
      {
        title: 'Pomoć žrtvama poplava',
        description: 'Koordinirana akcija za pružanje pomoći porodicama pogođenim poplavama.',
        status: 'Aktivno'
      },
      {
        title: 'Zimska humanitarna akcija',
        description: 'Prikupljanje i distribucija tople odjeće i obuće za ugrožene grupe.',
        status: 'U pripremi'
      }
    ]
  },
  education: {
    id: 'edukacija',
    title: 'Edukacija',
    description: 'Program edukacije je osmišljen da omogući pristup kvalitetnom obrazovanju svima. Kroz različite inicijative, podržavamo razvoj obrazovnih institucija i pružamo podršku učenicima i studentima.',
    imageUrl: '/images/education.jpg',
    impact: [
      'Poboljšanje pristupa obrazovanju',
      'Podrška obrazovnim institucijama',
      'Mentorstvo i savjetovanje učenika',
      'Organizacija radionica i edukativnih aktivnosti'
    ],
    howToHelp: [
      'Donirajte za obrazovne programe',
      'Pridružite se kao mentor',
      'Pomozite u organizaciji radionica',
      'Podijelite svoje znanje i iskustvo'
    ],
    currentProjects: [
      {
        title: 'Digitalna učionica',
        description: 'Opremanje škola računarskom opremom i internetom.',
        status: 'Aktivno'
      },
      {
        title: 'Program mentorstva',
        description: 'Povezivanje profesionalaca sa studentima za mentorstvo.',
        status: 'U pripremi'
      }
    ]
  },
  health: {
    id: 'zdravlje',
    title: 'Zdravlje',
    description: 'Program zdravlja fokusiran je na poboljšanje zdravstvene zaštite i pristupa zdravstvenim uslugama. Radimo na podizanju svijesti o važnosti prevencije i zdravog načina života.',
    imageUrl: '/images/health.jpg',
    impact: [
      'Poboljšanje pristupa zdravstvenim uslugama',
      'Edukacija o zdravom načinu života',
      'Podrška preventivnim programima',
      'Organizacija zdravstvenih kampanja'
    ],
    howToHelp: [
      'Donirajte za zdravstvene programe',
      'Pridružite se kao zdravstveni volonter',
      'Pomozite u organizaciji zdravstvenih kampanja',
      'Podijelite informacije o zdravlju'
    ],
    currentProjects: [
      {
        title: 'Zdravstvena edukacija',
        description: 'Serija radionica o prevenciji i zdravom načinu života.',
        status: 'Aktivno'
      },
      {
        title: 'Mobilna ambulanta',
        description: 'Opremanje mobilne ambulante za pružanje zdravstvenih usluga.',
        status: 'U pripremi'
      }
    ]
  },
  sustainable: {
    id: 'sustainable',
    title: 'Održivi Razvoj',
    description: 'Program održivog razvoja promoviše ekološki održive prakse i projekte koji doprinose zaštiti okoliša. Radimo na podizanju svijesti o važnosti održivog razvoja i implementaciji zelenih inicijativa.',
    imageUrl: '/images/sustainable.jpg',
    impact: [
      'Implementacija zelenih inicijativa',
      'Edukacija o održivom razvoju',
      'Podrška ekološkim projektima',
      'Promocija reciklaže i smanjenja otpada'
    ],
    howToHelp: [
      'Donirajte za ekološke projekte',
      'Pridružite se kao ekološki volonter',
      'Implementirajte zelene prakse',
      'Podijelite informacije o održivom razvoju'
    ],
    currentProjects: [
      {
        title: 'Zelena zajednica',
        description: 'Implementacija sistema reciklaže u lokalnim zajednicama.',
        status: 'Aktivno'
      },
      {
        title: 'Edukacija o održivom razvoju',
        description: 'Serija radionica o održivom razvoju i zaštiti okoliša.',
        status: 'U pripremi'
      }
    ]
  }
};

export const ProgramDetail: React.FC = () => {
  const { programId } = useParams<{ programId: string }>();
  const program = programData[programId || ''];

  if (!program) {
    return (
      <div className={styles.errorContainer}>
        <h2>Program nije pronađen</h2>
        <Link to="/about" className={styles.backButton}>
          Povratak na O nama
        </Link>
      </div>
    );
  }

  return (
    <div className={styles.programDetailContainer}>
      <div className={styles.hero} style={{ backgroundImage: `url(${program.imageUrl})` }}>
        <div className={styles.heroContent}>
          <h1>{program.title}</h1>
        </div>
      </div>

      <div className={styles.content}>
        <section className={styles.description}>
          <h2>O Programu</h2>
          <p>{program.description}</p>
        </section>

        <section className={styles.impact}>
          <h2>Naš Uticaj</h2>
          <ul>
            {program.impact.map((item, index) => (
              <li key={index}>{item}</li>
            ))}
          </ul>
        </section>

        <section className={styles.howToHelp}>
          <h2>Kako Možete Pomoci</h2>
          <ul>
            {program.howToHelp.map((item, index) => (
              <li key={index}>{item}</li>
            ))}
          </ul>
        </section>

        <section className={styles.projects}>
          <h2>Trenutni Projekti</h2>
          <div className={styles.projectGrid}>
            {program.currentProjects.map((project, index) => (
              <div key={index} className={styles.projectCard}>
                <h3>{project.title}</h3>
                <p>{project.description}</p>
                <span className={styles.status}>{project.status}</span>
              </div>
            ))}
          </div>
        </section>

        <div className={styles.actions}>
          <Link to="/about" className={styles.backButton}>
            Povratak na O nama
          </Link>
          <Link to="/contact" className={styles.contactButton}>
            Kontaktirajte nas
          </Link>
        </div>
      </div>
    </div>
  );
}; 