import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { getCampaigns, getCampaignDonatedAmount, Campaign } from '../../../services/campaign.service';
import styles from './CampaignGallery.module.css';

interface CampaignWithDonations extends Campaign {
  donatedAmount: number;
}

export const CampaignGallery: React.FC = () => {
  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const [activeIndex, setActiveIndex] = useState(0);
  const [isPaused, setIsPaused] = useState(false);
  const [campaigns, setCampaigns] = useState<CampaignWithDonations[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [scrollLeft, setScrollLeft] = useState(0);

  useEffect(() => {
    const fetchCampaigns = async () => {
      try {
        const campaignsData = await getCampaigns();
        
        // Fetch donated amounts for each campaign
        const campaignsWithDonations = await Promise.all(
          campaignsData.map(async (campaign) => {
            const donatedAmount = await getCampaignDonatedAmount(campaign.id);
            return { ...campaign, donatedAmount };
          })
        );

        setCampaigns(campaignsWithDonations);
        setError(null);
      } catch (err) {
        const errorMessage = err instanceof Error ? err.message : 'Failed to load campaigns';
        setError(errorMessage);
        console.error('Error fetching campaigns:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchCampaigns();
  }, []);

  useEffect(() => {
    if (campaigns.length === 0) return;

    const autoScrollInterval = setInterval(() => {
      if (!isPaused && !isDragging && scrollContainerRef.current) {
        const nextIndex = (activeIndex + 1) % campaigns.length;
        scrollContainerRef.current.scrollTo({
          left: nextIndex * 400,
          behavior: 'smooth'
        });
        setActiveIndex(nextIndex);
      }
    }, 3000);

    return () => clearInterval(autoScrollInterval);
  }, [activeIndex, isPaused, campaigns.length, isDragging]);

  const handleMouseDown = (e: React.MouseEvent) => {
    if (!scrollContainerRef.current) return;
    setIsDragging(true);
    setStartX(e.pageX - scrollContainerRef.current.offsetLeft);
    setScrollLeft(scrollContainerRef.current.scrollLeft);
  };

  const handleMouseMove = (e: React.MouseEvent) => {
    if (!isDragging || !scrollContainerRef.current) return;
    e.preventDefault();
    const x = e.pageX - scrollContainerRef.current.offsetLeft;
    const walk = (x - startX) * 3;
    scrollContainerRef.current.scrollLeft = scrollLeft - walk;
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  const handleScroll = () => {
    if (scrollContainerRef.current) {
      const { scrollLeft } = scrollContainerRef.current;
      const cardWidth = 400;
      const currentIndex = Math.round(scrollLeft / cardWidth);
      setActiveIndex(currentIndex);
    }
  };

  // Calculate number of indicators to show (max 5)
  const numIndicators = Math.min(5, Math.ceil(campaigns.length / 2));

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <div className={styles.loading}>Kampanje se učitavaju...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div className={styles.error}>{error}</div>
      </div>
    );
  }

  if (campaigns.length === 0) {
    return (
      <div className={styles.noCampaignsContainer}>
        <div className={styles.noCampaigns}>
          Nema trenutno dostupnih kampanja.
        </div>
      </div>
    );
  }

  return (
    <section className={styles.gallery}>
      <h2 className={styles.naslov}>Aktivne Kampanje</h2>
      <p className={styles.podnaslov}>
        Pogledajte naše trenutne kampanje i pridružite se u stvaranju pozitivnih promjena.
      </p>
      
      <div 
        className={styles.campaignWrapper}
        onMouseEnter={() => setIsPaused(true)}
        onMouseLeave={() => setIsPaused(false)}
      >
        <div 
          className={styles.campaignContainer} 
          ref={scrollContainerRef}
          onScroll={handleScroll}
          onMouseDown={handleMouseDown}
          onMouseMove={handleMouseMove}
          onMouseUp={handleMouseUp}
          onMouseLeave={handleMouseUp}
        >
          {campaigns.map(campaign => {
            const progress = (campaign.donatedAmount / campaign.targetAmount) * 100;
            
            return (
              <div key={campaign.id} className={styles.campaignCard}>
                <div className={styles.imageWrapper}>
                  <img 
                    src={campaign.imageLink} 
                    alt={campaign.name} 
                    className={styles.image}
                    loading="lazy"
                  />
                </div>
                
                <div className={styles.content}>
                  <h3 className={styles.campaignTitle}>{campaign.name}</h3>
                  
                  <div className={styles.bottomContent}>
                    <div className={styles.progressContainer}>
                      <div className={styles.progressInfo}>
                        <span className={styles.amount}>
                          {campaign.donatedAmount} KM
                        </span>
                        <span className={styles.goal}>
                          od {campaign.targetAmount} KM
                        </span>
                      </div>
                      <div className={styles.progressBar}>
                        <div 
                          className={styles.progress} 
                          style={{ width: `${Math.min(progress, 100)}%` }}
                        />
                      </div>
                      <div className={styles.percentageInfo}>
                        {Math.round(Math.min(progress, 100))}% prikupljeno
                      </div>
                    </div>

                    <Link 
                      to={`/campaign/${campaign.id}`}
                      className={styles.donateButton}
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
    </section>
  );
}; 