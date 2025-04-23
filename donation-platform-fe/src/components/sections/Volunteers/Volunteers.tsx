import VolunteerBadge from '../../common/VolunteerAnimation/VolunteerAnimation';
import styles from './Volunteers.module.css';

interface VolunteerPosition {
  id: number;
  title: string;
  description: string;
  requirements: string[];
  location: string;
  commitment: string;
  image: string;
}

const volunteerPositions: VolunteerPosition[] = [
  {
    id: 1,
    title: "Koordinator Volontera",
    description: "Tražimo osobu koja će pomoći u organizaciji i koordinaciji volonterskih aktivnosti.",
    requirements: [
      "Izvrsne komunikacijske vještine",
      "Sposobnost rada u timu",
      "Osnovno poznavanje rada na računaru",
      "Minimalno 4 sata sedmično"
    ],
    location: "Sarajevo",
    commitment: "Part-time",
    image: "https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=800"
  },
  {
    id: 2,
    title: "Mentor za Djecu",
    description: "Potrebni su nam mentori koji će raditi sa djecom na obrazovnim aktivnostima.",
    requirements: [
      "Iskustvo u radu sa djecom",
      "Strpljenje i empatija",
      "Poznavanje osnovnoškolskog gradiva",
      "Minimalno 6 sati sedmično"
    ],
    location: "Tuzla",
    commitment: "Fleksibilno",
    image: "https://images.unsplash.com/photo-1516627145497-ae6968895b74?w=800"
  },
  {
    id: 3,
    title: "Socijalni Radnik",
    description: "Tražimo volontere sa iskustvom u socijalnom radu za pomoć ugroženim porodicama.",
    requirements: [
      "Obrazovanje iz oblasti socijalnog rada",
      "Iskustvo u radu sa ranjivim grupama",
      "Vozačka dozvola B kategorije",
      "Minimalno 8 sati sedmično"
    ],
    location: "Mostar",
    commitment: "Part-time",
    image: "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=800"
  },
  {
    id: 4,
    title: "IT Podrška",
    description: "Potrebna nam je pomoć u održavanju web stranice i društvenih mreža.",
    requirements: [
      "Poznavanje web tehnologija",
      "Iskustvo sa društvenim mrežama",
      "Kreativnost i inovativnost",
      "Fleksibilno radno vrijeme"
    ],
    location: "Remote",
    commitment: "Fleksibilno",
    image: "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=800"
  }
];

export const Volunteers: React.FC = () => {
  return (
    <section className={styles.volunteers}>
      <div className={styles.header}>
        <VolunteerBadge />
        <h1 className={styles.title}>Volontiraj sa Nama</h1>
        <p className={styles.subtitle}>
          Pridružite se našem timu volontera i pomozite nam da zajedno stvaramo pozitivne promjene u zajednici.
          Svaki volonter je važan dio naše misije.
        </p>
      </div>

      <div className={styles.grid}>
        {volunteerPositions.map((position) => (
          <div key={position.id} className={styles.card}>
            <div className={styles.imageWrapper}>
              <img 
                src={position.image} 
                alt={position.title} 
                className={styles.image}
              />
              <div className={styles.location}>
                <span>{position.location}</span>
                <span className={styles.dot}>•</span>
                <span>{position.commitment}</span>
              </div>
            </div>

            <div className={styles.content}>
              <h2 className={styles.positionTitle}>{position.title}</h2>
              <p className={styles.description}>{position.description}</p>
              
              <div className={styles.requirements}>
                <h3 className={styles.requirementsTitle}>Potrebne kvalifikacije:</h3>
                <ul className={styles.requirementsList}>
                  {position.requirements.map((requirement, index) => (
                    <li key={index} className={styles.requirementItem}>
                      {requirement}
                    </li>
                  ))}
                </ul>
              </div>

              <button className={styles.applyButton}>
                Prijavi se za volontiranje
              </button>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}; 