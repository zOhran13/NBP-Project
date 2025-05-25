import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './Profile.module.css';
import { faDownload } from '@fortawesome/free-solid-svg-icons/faDownload';
import { AddressForm } from '../../components/common/AddressForm/AddressForm';

interface UserProfile {
  id: number;
  email: string;
  username: string;
  firstname: string;
  lastname: string;
  birthdate: string;
  phoneNumber: string;
  addressId: number;
  role?: string;
}

interface AddressDTO {
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

interface VolunteerShift {
  id: number;
  shiftStart: string;
  shiftEnd: string;
  hoursWorked: number;
}

interface ReportData {
  userId: number;
  campaignId: number;
  subtotal: number;
}

// PDF Preview component using blob
const PdfPreview: React.FC<{ userId: number }> = ({ userId }) => {
  const [pdfUrl, setPdfUrl] = useState<string | null>(null);

  useEffect(() => {
    let objectUrl: string | null = null;
    fetch(`http://localhost:8080/reports/user/pdf`, {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${Cookies.get('accessToken')}`
      }
    })
      .then(response => response.blob())
      .then(blob => {
        objectUrl = URL.createObjectURL(blob);
        setPdfUrl(objectUrl);
      });
    return () => {
      if (objectUrl) URL.revokeObjectURL(objectUrl);
    };
  }, [userId]);

  if (!pdfUrl) return <div>Loading PDF...</div>;

  return (
    <div className={styles.pdfPreviewCard}>
      <div className={styles.pdfPreviewHeader}>
        <span className={styles.pdfPreviewTitle}>Ukupan izvještaj donacija</span>
        <button 
          className={styles.pdfDownloadButton}
          onClick={handleDownload}
        >
          <FontAwesomeIcon icon={faDownload} /> Preuzmi PDF
        </button>
      </div>
      <iframe
        src={pdfUrl}
        title="Ukupan izvještaj donacija"
        width="100%"
        height="700"
        className={styles.pdfIframe}
      />
    </div>
  );
};
const handleDownload = async () => {
  const response = await fetch('http://localhost:8080/reports/user/pdf/download', {
    headers: {
      "Authorization": `Bearer ${Cookies.get('accessToken')}`
    }
  });
  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'ukupan-izvjestaj.pdf';
  a.click();
  window.URL.revokeObjectURL(url);
};
export const Profile: React.FC = () => {
  console.log('Profile component rendering');
  
  const [isEditing, setIsEditing] = useState(false);
  const [activeTab, setActiveTab] = useState<'profile' | 'volunteer' | 'reports'>('profile');
  const [profile, setProfile] = useState<UserProfile>({
    id: 0,
    email: '',
    username: '',
    firstname: '',
    lastname: '',
    birthdate: '',
    phoneNumber: '',
    addressId: 0
  });
  const [addressInput, setAddressInput] = useState('');
  const [addressSuggestions, setAddressSuggestions] = useState<any[]>([]);
  const [address, setAddress] = useState({
    street: '',
    city: '',
    postalCode: '',
    country: ''
  });
  const [volunteerShifts, setVolunteerShifts] = useState<VolunteerShift[]>([]);
  const [reports, setReports] = useState<ReportData[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [showSuccess, setShowSuccess] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    console.log('Profile component mounted');
    console.log('Current cookies:', document.cookie);
    console.log('Access token from cookie:', Cookies.get('accessToken'));
    fetchProfile();
  }, []);

  useEffect(() => {
   
    if (profile.id) {
      fetchVolunteerShifts();
      fetchReports();
    }
  }, [profile.id, profile.addressId]);

  const fetchProfile = async () => {
    console.log('Starting to fetch profile...');
    try {
      const token = Cookies.get('accessToken');
      console.log('Token from cookie:', token);
      
      if (!token) {
        console.error('No token found in cookies');
        setError('Niste prijavljeni. Molimo prijavite se.');
        return;
      }

      console.log('Making API request to /api/users/me');
      const response = await fetch('http://localhost:8080/api/users/me', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      console.log('Response status:', response.status);
      
      if (response.ok) {
        const data = await response.json();
        console.log('User Profile Data:', data);
        setProfile({
          id: data.id,
          email: data.email,
          username: data.username,
          firstname: data.firstName || '',
          lastname: data.lastName || '',
          birthdate: data.birthDate || '',
          phoneNumber: data.phoneNumber || '',
          addressId: data.addressId || 0,
          role: data.role || 'USER'
        });
        setAddress({
          street: data.address.street || '',
          city: data.address.city || '',
          postalCode: data.address.postalCode || '',
          country: data.address.country || ''
        });
        setAddressInput(data.address.street || '');
        setError(null);
      } else {
        console.error('Failed to fetch profile:', response.status);
        const errorText = await response.text();
        console.error('Error details:', errorText);
        setError(`Greška prilikom učitavanja profila (${response.status}): ${errorText}`);
      }
    } catch (error) {
      console.error('Error fetching profile:', error);
      setError('Došlo je do greške. Molimo pokušajte ponovo kasnije.');
    }
  };

  /*const fetchAddress = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/address/${profile.addressId}`, {
        headers: {
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setAddress(data);
      }
    } catch (error) {
      console.error('Greška:', error);
    }
  };*/



  const handleStreetInput = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setAddressInput(value);
    setAddress(prev => ({ ...prev, street: value }));
    if (value.length < 3) {
      setAddressSuggestions([]);
      return;
    }
    const res = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(value)}&addressdetails=1`);
    const data = await res.json();
    setAddressSuggestions(data);
  };

  const handleAddressSelect = (suggestion: any) => {
    setAddressInput(
      (suggestion.address.road || '') +
      (suggestion.address.house_number ? ' ' + suggestion.address.house_number : '')
    );
    setAddressSuggestions([]);
    setAddress({
      street: (suggestion.address.road || '') + (suggestion.address.house_number ? ' ' + suggestion.address.house_number : ''),
      city: suggestion.address.city || suggestion.address.town || suggestion.address.village || '',
      postalCode: suggestion.address.postcode || '',
      country: suggestion.address.country || ''
    });
  };

  const fetchVolunteerShifts = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/volunteers/user/${profile.id}/shifts`, {
        headers: {
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setVolunteerShifts(data);
      }
    } catch (error) {
      console.error('Greška:', error);
    }
  };

  const fetchReports = async () => {
    try {
      const response = await fetch(`http://localhost:8080/reports/user/pdf`, {
        headers: {
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setReports(data);
      }
    } catch (error) {
      console.error('Greška:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      console.log(JSON.stringify({
        firstName: profile.firstname,
        lastName: profile.lastname,
        email: profile.email,
        username: profile.username,
        phoneNumber: profile.phoneNumber,
        birthDate: profile.birthdate,
        address: {
          street: address.street,
          city: address.city,
          postalCode: address.postalCode ? Number(address.postalCode) : undefined,
          country: address.country
        },
        role: profile.role || "USER"
      }));
      const response = await fetch('http://localhost:8080/api/users/profile', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        },
        body: JSON.stringify({
          firstName: profile.firstname,
          lastName: profile.lastname,
          email: profile.email,
          username: profile.username,
          phoneNumber: profile.phoneNumber,
          birthDate: profile.birthdate,
          address: {
            street: address.street,
            city: address.city,
            postalCode: address.postalCode ? Number(address.postalCode) : undefined,
            country: address.country
          },
          role: profile.role || "USER"
        })
      });
  
      if (response.ok) {
        const data = await response.json();
        setIsEditing(false);
        setSuccessMessage('Profil uspješno ažuriran!');
        Cookies.set('accessToken', data.accessToken); // Set the new token!
        setShowSuccess(true);
        setTimeout(() => {
          setShowSuccess(false);
        }, 3000);
      } else {
        alert('Greška prilikom ažuriranja profila.');
      }
    } catch (error) {
      console.error('Greška:', error);
      alert('Došlo je do greške. Pokušajte ponovo kasnije.');
    }
  };
  

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfile(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleDownload = async () => {
    const response = await fetch('http://localhost:8080/reports/user/pdf/download', {
      headers: {
        "Authorization": `Bearer ${Cookies.get('accessToken')}`
      }
    });
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'ukupan-izvjestaj.pdf';
    a.click();
    window.URL.revokeObjectURL(url);
  };

  const renderProfileTab = () => (
    <div className={styles.profileTabs}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.profileSection}>
          <h2>Osnovni podaci</h2>
          <div className={styles.formGroup}>
            <label>Ime</label>
            <input
              type="text"
              name="firstname"
              value={profile.firstname}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Prezime</label>
            <input
              type="text"
              name="lastname"
              value={profile.lastname}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Korisničko ime</label>
            <input
              type="text"
              name="username"
              value={profile.username}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={profile.email}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Broj telefona</label>
            <input
              type="text"
              name="phoneNumber"
              value={profile.phoneNumber}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Datum rođenja</label>
            <input
              type="date"
              name="birthdate"
              value={profile.birthdate}
              onChange={handleChange}
              disabled={!isEditing}
              required
            />
          </div>
        </div>
        <div className={styles.addressSection}>
          <h2>Adresa</h2>
          <AddressForm
            address={address}
            onAddressChange={setAddress}
            addressInput={addressInput}
            onAddressInputChange={setAddressInput}
            disabled={!isEditing}
            isEditing={isEditing}
          />
          {isEditing && (
            <div className={styles.addressActions}>
              <button type="submit" className={styles.saveButton}>
                Sačuvaj promjene
              </button>
            </div>
          )}
        </div>
      </form>
    </div>
  );

  const renderVolunteerTab = () => (
    <div className={styles.volunteerSection}>
      <h2>Moje smjene</h2>
      <div className={styles.shiftsList}>
        {volunteerShifts.map((shift) => (
          <div key={shift.id} className={styles.shiftCard}>
            <div className={styles.shiftInfo}>
              <p>Početak: {shift.shiftStart}</p>
              <p>Kraj: {shift.shiftEnd}</p>
              <p>Održano sati: {shift.hoursWorked}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );

  const renderReportsTab = () => (
    <div className={styles.reportsSection}>
      <h2>Moji izvještaji</h2>
      <div className={styles.reportsList}>
        {reports.map((report) => (
          <div key={`${report.userId}-${report.campaignId}`} className={styles.reportCard}>
            <div className={styles.reportInfo}>
              <p>Kampanja ID: {report.campaignId}</p>
              <p>Ukupno: {report.subtotal} KM</p>
            </div>
            <div className={styles.reportActions}>
              <button 
                className={styles.viewReportButton}
                onClick={() => {
                  const url = `http://localhost:8080/reports/campaign/${report.campaignId}/pdf`;
                  window.open(url, '_blank');
                }}
              >
                Pogledaj izvještaj kampanje
              </button>
              <a 
                href={`http://localhost:8080/reports/campaign/${report.campaignId}/pdf`}
                download={`kampanja-${report.campaignId}.pdf`}
                className={styles.downloadButton}
              >
                Preuzmi izvještaj kampanje
              </a>
            </div>
          </div>
        ))}
        {/* User's full donation report */}
        <PdfPreview userId={profile.id} />
      </div>
    </div>
  );

  return (
    <div className={styles.profileContainer}>
      {showSuccess && (
        <div className={styles.successNotification}>
          {successMessage}
        </div>
      )}
      {error ? (
        <div className={styles.errorMessage}>
          <h2>Greška</h2>
          <p>{error}</p>
          <button onClick={() => window.location.href = '/login'}>Idi na prijavu</button>
        </div>
      ) : (
        <div className={styles.profileCard}>
          <div className={styles.header}>
            <h1>Moj Profil</h1>
            {activeTab === 'profile' && (
              <button 
                className={styles.editButton}
                onClick={() => setIsEditing(!isEditing)}
              >
                {isEditing ? 'Odustani' : 'Uredi profil'}
              </button>
            )}
          </div>

          <div className={styles.tabs}>
            <button 
              className={`${styles.tabButton} ${activeTab === 'profile' ? styles.active : ''}`}
              onClick={() => setActiveTab('profile')}
            >
              Profil
            </button>
            <button 
              className={`${styles.tabButton} ${activeTab === 'volunteer' ? styles.active : ''}`}
              onClick={() => setActiveTab('volunteer')}
            >
              Volontiranje
            </button>
            <button 
              className={`${styles.tabButton} ${activeTab === 'reports' ? styles.active : ''}`}
              onClick={() => setActiveTab('reports')}
            >
              Izvještaji
            </button>
          </div>

          <div className={styles.tabContent}>
            {activeTab === 'profile' && renderProfileTab()}
            {activeTab === 'volunteer' && renderVolunteerTab()}
            
          </div>
          {activeTab === 'reports' && renderReportsTab()}
        </div>
      )}
    </div>
  );
}; 