import Cookies from 'js-cookie';
import { API_CONFIG } from '../config/api.config';

export interface Program {
  id: number;
  icon: string;
  title: string;
  description: string;
  details?: string;
  impact?: string;
  requirements?: string[];
}

export const getPrograms = async (): Promise<Program[]> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PROGRAMS.BASE}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch programs');
  }

  return response.json();
};

export const getProgramById = async (id: number): Promise<Program> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PROGRAMS.BY_ID(id)}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch program details');
  }

  return response.json();
}; 