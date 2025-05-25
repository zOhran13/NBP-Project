import Cookies from 'js-cookie';
import { API_CONFIG } from '../config/api.config';

export interface VolunteerPosition {
  id: number;
  title: string;
  description: string;
  requirements: string[];
  location: string;
  commitment: string;
  image: string;
}

export interface VolunteerApplication {
  positionId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  motivation: string;
  experience: string;
}

export const getVolunteerPositions = async (): Promise<VolunteerPosition[]> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.VOLUNTEER.POSITIONS}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch volunteer positions');
  }

  return response.json();
};

export const applyForPosition = async (application: VolunteerApplication): Promise<void> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.VOLUNTEER.APPLY}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(application)
  });

  if (!response.ok) {
    throw new Error('Failed to submit volunteer application');
  }
}; 