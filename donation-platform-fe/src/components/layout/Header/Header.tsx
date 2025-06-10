import { Link } from 'react-router-dom';
import styles from './Header.module.css';
import { Button } from '../../common/Button/Button';
import { Logo } from '../../common/Logo/Logo';
export const Header: React.FC = () => {
	return (
		<header className={styles.header}>
			<div className={styles.headerContent}>
				<Link to='/' className={styles.logo}>
					<Logo />
				</Link>
				<nav className={styles.nav}>
					<Link to='/' className={styles.link}>
						Početna
					</Link>
					<Link to='/o-nama' className={styles.link}>
						O nama
					</Link>
					<Link to='/programi' className={styles.link}>
						Kampanje
					</Link>
					<Link to='/volontiranje' className={styles.link}>
						Volontiranje
					</Link>
				</nav>
			</div>
		</header>
	);
};
