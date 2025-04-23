import { ButtonProps } from '../../../types/common';
import styles from './Button.module.css';
import clsx from 'clsx';

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  className,
  onClick,
}) => {
  return (
    <button
      className={clsx(styles.button, styles[variant], className)}
      onClick={onClick}
    >
      {children}
    </button>
  );
}; 