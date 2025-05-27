import Cookies from 'js-cookie';
import { API_CONFIG } from '../config/api.config';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface SignupData {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
  phoneNumber: string;
  birthDate: string;
}


export const login = async (credentials: LoginCredentials): Promise<{ accessToken: string }> => {
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.AUTH.LOGIN}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  });

  if (!response.ok) {
    throw new Error('Neispravni email ili lozinka');
  }

  const data = await response.json();
  Cookies.set('accessToken', data.accessToken);

  // Fetch user info with the token
  const userRes = await fetch(`${API_CONFIG.BASE_URL}/users/user`, {
    headers: {
      'Authorization': `Bearer ${data.accessToken}`,
      'Accept': '*/*'
    }
  });
  if (!userRes.ok) {
    throw new Error('Ne mogu dohvatiti podatke o korisniku');
  }
  const user = await userRes.json();
  Cookies.set('username', user.username); // or whatever field is the username
  Cookies.set('userRole', user.role); 


  return data;
};

export const signup = async (userData: SignupData): Promise<void> => {
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.AUTH.SIGNUP}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });

  if (!response.ok) {
    throw new Error('Greška prilikom registracije');
  }
};

export const forgotPassword = async (email: string): Promise<void> => {
  const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.AUTH.FORGOT_PASSWORD}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email })
  });

  if (!response.ok) {
    throw new Error('Greška prilikom slanja zahtjeva za resetovanje lozinke');
  }
};

export const logout = (): void => {
  Cookies.remove('accessToken');
  Cookies.remove('username');
}; 