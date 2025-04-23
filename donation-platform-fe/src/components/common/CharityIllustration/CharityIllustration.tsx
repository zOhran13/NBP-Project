import styles from './CharityIllustration.module.css';
import illustration from '../../../assets/images/HandCharityIllustration.png';
export const CharityIllustration: React.FC = () => {
    return (
        <div className={styles.container}>
            <div className={styles.illustrationWrapper}>
                <svg
                    className={styles.matteHeart}
                    viewBox="0 0 32 32"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path d="M23.6 2c-3.363 0-6.258 2.736-7.599 5.594-1.342-2.858-4.237-5.594-7.601-5.594-4.637 0-8.4 3.764-8.4 8.401 0 9.433 9.516 11.906 16 21.232 6.484-9.326 16-11.799 16-21.232 0-4.637-3.763-8.401-8.4-8.401z"/>
                </svg>
                <div className={styles.hand}>
              <img src={illustration} alt="Hand charity illustration" />
              </div>
            </div>
        </div>
    );
}; 