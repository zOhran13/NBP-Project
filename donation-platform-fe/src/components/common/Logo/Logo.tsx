import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHandsHelping } from '@fortawesome/free-solid-svg-icons';
import styles from './Logo.module.css';

export const Logo: React.FC = () => {
  return (
    <div className={styles.logoContainer}>
      <FontAwesomeIcon 
        icon={faHandsHelping} 
        className={styles.logo}
      />
      <span className={styles.title}>HumaNet</span>
    </div>
  );
}; 