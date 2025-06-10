import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './OrganizationDashboard.module.css';
import { jwtDecode } from 'jwt-decode';

interface Campaign {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  targetAmount: number;
  donatedAmount: number;
}

function isAdmin() {
  const token = localStorage.getItem('token');
  if (!token) return false;
  try {
    const decoded: any = jwtDecode(token);
    // U tokenu je role: 'Admin' (veliko A)
    return decoded.role === 'Admin';
  } catch {
    return false;
  }
}

export const OrganizationDashboard: React.FC = () => {
  const [campaigns, setCampaigns] = useState<Campaign[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCampaigns();
  }, []);

  const fetchCampaigns = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/campaign');
      if (response.ok) {
        const data = await response.json();
        setCampaigns(data);
      }
    } catch (error) {
      console.error('Error fetching campaigns:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateCampaign = () => {
    navigate('/create-campaign');
  };

  const handlePreviewReport = async (campaignId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/reports/campaign/${campaignId}/pdf`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
      }
    } catch (error) {
      console.error('Error previewing report:', error);
      alert('Greška prilikom otvaranja izvještaja');
    }
  };

  const handleDownloadReport = async (campaignId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/reports/campaign/${campaignId}/pdf`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `campaign_${campaignId}_report.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    } catch (error) {
      console.error('Error downloading report:', error);
      alert('Greška prilikom preuzimanja izvještaja');
    }
  };

  const handleDeleteCampaign = async (campaignId: number) => {
    if (!window.confirm('Da li ste sigurni da želite obrisati ovu kampanju?')) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/campaign/${campaignId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });

      if (response.ok) {
        setCampaigns(campaigns.filter(campaign => campaign.id !== campaignId));
        alert('Kampanja uspješno obrisana');
      }
    } catch (error) {
      console.error('Error deleting campaign:', error);
      alert('Greška prilikom brisanja kampanje');
    }
  };

  if (loading) {
    return <div className={styles.loading}>Učitavanje...</div>;
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>Upravljanje kampanjama</h1>
        {isAdmin() && (
          <button onClick={handleCreateCampaign} className={styles.createButton}>
            Kreiraj novu kampanju
          </button>
        )}
      </div>

      <div className={styles.campaignsGrid}>
        {campaigns.map((campaign) => (
          <div key={campaign.id} className={styles.campaignCard}>
            <h2>{campaign.name}</h2>
            <div className={styles.campaignDetails}>
              <p>Početak: {new Date(campaign.startDate).toLocaleDateString()}</p>
              <p>Kraj: {new Date(campaign.endDate).toLocaleDateString()}</p>
              <p>Cilj: {campaign.targetAmount} KM</p>
              <p>Prikupljeno: {campaign.donatedAmount} KM</p>
            </div>
            {isAdmin() && (
              <div className={styles.campaignActions}>
                <button 
                  onClick={() => handlePreviewReport(campaign.id)}
                  className={styles.reportButton}
                >
                  Pregledaj izvještaj
                </button>
                <button 
                  onClick={() => handleDownloadReport(campaign.id)}
                  className={styles.reportButton}
                >
                  Preuzmi izvještaj
                </button>
                <button 
                  onClick={() => handleDeleteCampaign(campaign.id)}
                  className={styles.deleteButton}
                >
                  Obriši
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}; 