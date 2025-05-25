import Cookies from 'js-cookie';

const API_BASE_URL = 'http://localhost:8080/api';

export interface Event {
  id: number;
  title: string;
  description: string;
  // Add other event properties as needed
}

export const getEvents = async (): Promise<Event[]> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_BASE_URL}/events`, {
    method: 'GET',
    headers: {
      'accept': '*/*',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch events');
  }

  return response.json();
}; 