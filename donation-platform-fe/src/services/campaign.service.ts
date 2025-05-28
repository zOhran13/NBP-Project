import { API_CONFIG } from '../config/api.config';

export interface Campaign {
  id: number;
  name: string;
  image: string;
  startDate: string;
  endDate: string;
  targetAmount: number;
}

export const getCampaigns = async (): Promise<Campaign[]> => {
  try {
    const response = await fetch(`${API_CONFIG.BASE_URL}/campaign`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch events');
    }

    return response.json();
  } catch (error) {
    if (error instanceof Error) {
      throw error;
    }
    throw new Error('An unexpected error occurred while fetching events');
  }
};

export const getCampaignDonatedAmount = async (campaignId: number): Promise<number> => {
  try {
    const response = await fetch(`${API_CONFIG.BASE_URL}/campaign/${campaignId}/donated-amount`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch donated amount');
    }

    const amount = await response.json();
    return Math.round(Number(amount)) || 0;
  } catch (error) {
    console.error('Error fetching donated amount:', error);
    return 0;
  }
};
export const createCampaign = async (
  name: string,
  image: File,
  startDate: string,
  endDate: string,
  targetAmount: string
): Promise<Response> => {
  const formData = new FormData();
  formData.append('name', name);
  formData.append('image', image);
  formData.append('startDate', startDate);
  formData.append('endDate', endDate);
  formData.append('targetAmount', targetAmount);

  return await fetch('/api/campaign', {
    method: 'POST',
    body: formData
  });
};

