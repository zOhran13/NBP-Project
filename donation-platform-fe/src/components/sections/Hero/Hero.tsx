import { Button } from '../../common/Button/Button';
import { CharityIllustration } from '../../common/CharityIllustration/CharityIllustration';
import styles from './Hero.module.css';

export const Hero: React.FC = () => {
  return (
    <section className={styles.hero}>
      <div className={styles.content}>
        <h1 className={styles.title}>
        Napravi Promjenu u Nečijem Životu
        </h1>
        <p className={styles.description}>
        Pridružite nam se u pomaganju onima kojima je pomoć potrebna kroz naše razne programe i inicijative.
        </p>
        <div className={styles.buttonContainer}>
          <Button variant="primary">Doniraj</Button>
          <Button variant="secondary">Saznaj više</Button>
        </div>
      </div>
      <div className={styles.image}>
        <CharityIllustration />
      </div>
    </section>
  );
};