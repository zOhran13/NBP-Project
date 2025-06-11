import Cookies from 'js-cookie';
import { API_CONFIG } from '../config/api.config';

export interface DonationDTO {
  id?: number;
  amount: number;
  donationDate?: Date;
  userId?: number;
  campaignId: number;
}

export const createDonation = async (donationDTO: DonationDTO): Promise<DonationDTO> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}/donations`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(donationDTO)
  });

  if (!response.ok) {
    throw new Error('Failed to create donation');
  }

  return response.json();
};

export const updateDonation = async (id: number, donationDTO: DonationDTO): Promise<DonationDTO> => {
  const token = Cookies.get('accessToken');
  
  const response = await fetch(`${API_CONFIG.BASE_URL}/donations/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(donationDTO)
  });

  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Donation not found');
    }
    throw new Error('Failed to update donation');
  }

  return response.json();
}; 