export interface Program {
  icon: string;
  title: string;
  description: string;
}

export interface ButtonProps {
  children: React.ReactNode;
  variant?: 'primary' | 'secondary';
  className?: string;
  onClick?: () => void;
}

export interface SectionProps {
  title: string;
  children: React.ReactNode;
  className?: string;
} 