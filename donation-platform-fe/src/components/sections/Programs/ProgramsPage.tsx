import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import { Link } from 'react-router-dom';
import galleryStyles from '../CampaignGallery/CampaignGallery.module.css';
import styles from './ProgramsPage.module.css';

interface Campaign {
  id: number;
  name: string;
  description: string;
  imageLink: string;
  targetAmount: number;
  donatedAmount: number;
}

export const ProgramsPage: React.FC = () => {
  const [campaigns, setCampaigns] = useState<Campaign[]>([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCampaigns = async () => {
      setLoading(true);
      setError(null);
      try {
        const token = Cookies.get('accessToken');
        const res = await fetch('http://localhost:8080/api/campaign', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': '*/*',
          },
        });
        if (!res.ok) throw new Error('Greška pri dohvatanju programa');
        const data = await res.json();
        setCampaigns(data);
      } catch (err: any) {
        setError(err.message || 'Greška');
      } finally {
        setLoading(false);
      }
    };
    fetchCampaigns();
  }, []);

  const filtered = campaigns.filter(c => 
    (c.name?.toLowerCase() || "").includes(search.toLowerCase()) ||
    (c.description?.toLowerCase() || "").includes(search.toLowerCase())
  );

  return (
    <div className={styles.programsContainer}>
      <h1 className={styles.title}>Programi</h1>
      <input
        className={styles.searchBar}
        type="text"
        placeholder="Pretraži programe..."
        value={search}
        onChange={e => setSearch(e.target.value)}
      />
      {loading && <div className={styles.loading}>Učitavanje programa...</div>}
      {error && <div className={styles.error}>{error}</div>}
      <div className={styles.grid3col}>
        {filtered.map(campaign => {
          const progress = (campaign.donatedAmount / campaign.targetAmount) * 100;
          return (
            <div key={campaign.id} className={galleryStyles.campaignCard} style={{ pointerEvents: 'auto', marginBottom: '2rem' }}>
              <div className={galleryStyles.imageWrapper}>
                <img 
                  src={campaign.imageLink} 
                  alt={campaign.name} 
                  className={galleryStyles.image}
                  loading="lazy"
                />
              </div>
              <div className={galleryStyles.content}>
                <h3 className={galleryStyles.campaignTitle}>{campaign.name}</h3>
                <div className={galleryStyles.bottomContent}>
                  <div className={galleryStyles.progressContainer}>
                    <div className={galleryStyles.progressInfo}>
                      <span className={galleryStyles.amount}>
                        {campaign.donatedAmount} KM
                      </span>
                      <span className={galleryStyles.goal}>
                        od {campaign.targetAmount} KM
                      </span>
                    </div>
                    <div className={galleryStyles.progressBar}>
                      <div 
                        className={galleryStyles.progress} 
                        style={{ width: `${Math.min(progress, 100)}%` }}
                      />
                    </div>
                    <div className={galleryStyles.percentageInfo}>
                      {Math.round(Math.min(progress, 100))}% prikupljeno
                    </div>
                  </div>
                  <Link 
                    to={`/campaign/${campaign.id}`}
                    className={galleryStyles.donateButton}
                  >
                    Doniraj
                  </Link>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}; 