import { motion } from "framer-motion";
import { FaGlobe } from "react-icons/fa";
import styles from './VolunteerAnimation.module.css';
const VolunteerBadge = () => {
    return (
        <div className={styles.globeContainer}>
            <span className={styles.bigGlobe}>🌍</span>
        </div>
    );
};

export default VolunteerBadge;