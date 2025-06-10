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

export interface VolunteerRegistration {
  shiftStart: string;
  shiftEnd: string;
  hoursWorked: number;
  eventId: number;
}

export interface VolunteerShift {
  id: number;
  shiftStart: string;
  shiftEnd: string;
  hoursWorked: number;
  event: {
    id: number;
  };
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

export const registerVolunteer = async (registration: VolunteerRegistration): Promise<void> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.VOLUNTEER.REGISTER}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(registration)
  });

  if (!response.ok) {
    throw new Error('Failed to register volunteer');
  }
};

export const getVolunteerCount = async (eventId: number): Promise<number> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.VOLUNTEER.COUNT(eventId)}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch volunteer count');
  }

  return response.json();
};

export const getUserShifts = async (): Promise<VolunteerShift[]> => {
  const token = Cookies.get('accessToken');
  console.log(token);
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.VOLUNTEER.USER_SHIFTS}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch user shifts');
  }

  return response.json();
}; 