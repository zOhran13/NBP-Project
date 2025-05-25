import { Link, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import styles from './TopBar.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';

export const TopBar: React.FC = () => {
  const navigate = useNavigate();
  const token = Cookies.get('accessToken');
  const handleLogout = () => {
    Cookies.remove('accessToken');
    navigate('/login');
  };

  return (
    <div className={styles.topBar}>
      <div className={styles.left}>
        <span>📞 +387 61 234 567</span>
        <span>✉️ info@humanet.ba</span>
      </div>
      <div className={styles.right}>
        {token ? (
          <>
            <Link to="/profile" className={styles.profileIconBtn} title="Profil">
              <FontAwesomeIcon icon={faUserCircle} />
            </Link>
            <button onClick={handleLogout} className={styles.logoutButton}>Odjavi se</button>
          </>
        ) : (
          <Link to="/login" className={styles.loginLink}>Prijavi se</Link>
        )}
        <select className={styles.languageSelect}>
          <option value="bs">BS</option>
          <option value="en">EN</option>
        </select>
      </div>
    </div>
  );
};