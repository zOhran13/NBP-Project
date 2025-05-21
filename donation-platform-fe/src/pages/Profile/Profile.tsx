import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import styles from './Profile.module.css';

interface UserProfile {
  id: number;
  email: string;
  username: string;
  addressId: number;
}

interface Address {
  id: number;
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
    fetch(`http://localhost:8080/reports/user/${userId}/pdf`, {
      method: 'GET',
      // Add headers if needed
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
    <iframe
      src={pdfUrl}
      title="Ukupan izvještaj donacija"
      width="100%"
      height="500"
    />
  );
};

export const Profile: React.FC = () => {
  console.log('Profile component rendering');
  
  const [isEditing, setIsEditing] = useState(false);
  const [activeTab, setActiveTab] = useState<'profile' | 'volunteer' | 'reports'>('profile');
  const [profile, setProfile] = useState<UserProfile>({
    id: 0,
    email: '',
    username: '',
    addressId: 0
  });
  const [address, setAddress] = useState<Address | null>(null);
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
    if (profile.addressId) {
      fetchAddress();
    }
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
        setProfile(data);
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

  const fetchAddress = async () => {
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
  };

  const handleAddressSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!address) return;

    try {
      const response = await fetch(`http://localhost:8080/api/address/${address.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        },
        body: JSON.stringify(address)
      });

      if (response.ok) {
        setSuccessMessage('Adresa uspješno ažurirana!');
        setShowSuccess(true);
        setTimeout(() => {
          setShowSuccess(false);
        }, 3000);
      } else {
        alert('Greška prilikom ažuriranja adrese.');
      }
    } catch (error) {
      console.error('Greška:', error);
      alert('Došlo je do greške. Pokušajte ponovo kasnije.');
    }
  };

  const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!address) return;
    const { name, value } = e.target;
    setAddress(prev => prev ? {
      ...prev,
      [name]: value
    } : null);
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
      const response = await fetch(`http://localhost:8080/reports/user/${profile.id}`, {
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
      const response = await fetch('http://localhost:8080/api/users/profile', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Cookies.get('accessToken')}`
        },
        body: JSON.stringify({
          email: profile.email,
          username: profile.username
        })
      });
  
      if (response.ok) {
        setIsEditing(false);
        setSuccessMessage('Profil uspješno ažuriran!');
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

  const renderProfileTab = () => (
    <div className={styles.profileTabs}>
      <div className={styles.profileSection}>
        <h2>Osnovni podaci</h2>
        <form onSubmit={handleSubmit} className={styles.form}>


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



          {isEditing && (
            <button type="submit" className={styles.saveButton}>
              Spremi promjene
            </button>
          )}
        </form>
      </div>

      <div className={styles.addressSection}>
        <h2>Adresa</h2>
        {address ? (
          <form onSubmit={handleAddressSubmit} className={styles.form}>
            <div className={styles.formGroup}>
              <label>Ulica</label>
              <input
                type="text"
                name="street"
                value={address.street}
                onChange={handleAddressChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className={styles.formGroup}>
              <label>Grad</label>
              <input
                type="text"
                name="city"
                value={address.city}
                onChange={handleAddressChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className={styles.formGroup}>
              <label>Poštanski broj</label>
              <input
                type="text"
                name="postalCode"
                value={address.postalCode}
                onChange={handleAddressChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className={styles.formGroup}>
              <label>Država</label>
              <input
                type="text"
                name="country"
                value={address.country}
                onChange={handleAddressChange}
                disabled={!isEditing}
                required
              />
            </div>

            {isEditing && (
              <button type="submit" className={styles.saveButton}>
                Spremi adresu
              </button>
            )}
          </form>
        ) : (
          <>
            <p>Nema dostupne adrese.</p>
            {profile.addressId ? (
              <p style={{ color: 'red' }}>
                (Debug) Pokušavam dohvatiti adresu s ID-jem: {profile.addressId}
              </p>
            ) : null}
          </>
        )}
      </div>
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
        <div className={styles.reportCard}>
          <div className={styles.reportInfo}>
            <h3>Ukupan izvještaj donacija</h3>
            <p>Pregled svih vaših donacija</p>
          </div>
          <div className={styles.reportActions}>
            <a 
              href={`http://localhost:8080/reports/user/${profile.id}/pdf/download`}
              className={styles.downloadButton}
            >
              Preuzmi ukupan izvještaj
            </a>
          </div>
          <div className={styles.pdfPreview}>
            <PdfPreview userId={profile.id} />
          </div>
        </div>
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
            {activeTab === 'reports' && renderReportsTab()}
          </div>
        </div>
      )}
    </div>
  );
}; 