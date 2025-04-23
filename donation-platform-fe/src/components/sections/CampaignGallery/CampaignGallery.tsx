import { useState, useRef, useEffect } from 'react';
import styles from './CampaignGallery.module.css';

interface Campaign {
  id: number;
  title: string;
  description: string;
  image: string;
  currentAmount: number;
  goalAmount: number;
}

const campaigns: Campaign[] = [
  {
    id: 1,
    title: "Pomoć Djeci bez Roditeljskog Staranja",
    description: "Prikupljanje sredstava za obrazovanje i osnovne potrebe djece bez roditeljskog staranja.",
    image: "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800",
    currentAmount: 15000,
    goalAmount: 20000,
  },
  {
    id: 2,
    title: "Obnova Lokalnog Dječijeg Igrališta",
    description: "Projekat obnove igrališta za sigurnije i bolje mjesto za igru naše djece.",
    image: "https://www.sutherlandshire.nsw.gov.au/__data/assets/image/0029/78617/2024-03-11_10-49-32.jpg",
    currentAmount: 5000,
    goalAmount: 10000,
  },
  {
    id: 3,
    title: "Pomoć Starijim Osobama",
    description: "Prikupljanje sredstava za pomoć starijim osobama u našoj zajednici.",
    image: "https://www.griswoldcare.com/wp-content/uploads/2024/04/shutterstock_735361786.jpg",
    currentAmount: 8000,
    goalAmount: 15000,
  },
  {
    id: 4,
    title: "Podrška Lokalnim Umjetnicima",
    description: "Pomoć umjetnicima u našoj zajednici da nastave stvarati i inspirisati.",
    image: "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?w=800",
    currentAmount: 3000,
    goalAmount: 12000,
  },
  {
    id: 5,
    title: "Inicijativa Zelenog Grada",
    description: "Projekat sadnje drveća i uređenja zelenih površina u našem gradu.",
    image: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfvhukV2RyoRTl3ToMnpR1ZuVS9-G3rWqxeg&s",
    currentAmount: 7000,
    goalAmount: 25000,
  }
];

export const CampaignGallery: React.FC = () => {
  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const [showLeftArrow, setShowLeftArrow] = useState(false);
  const [showRightArrow, setShowRightArrow] = useState(true);
  const [activeIndex, setActiveIndex] = useState(0);
  const [isPaused, setIsPaused] = useState(false);

  useEffect(() => {
    const autoScrollInterval = setInterval(() => {
      if (!isPaused && scrollContainerRef.current) {
        const nextIndex = (activeIndex + 1) % campaigns.length;
        scrollContainerRef.current.scrollTo({
          left: nextIndex * 400,
          behavior: 'smooth'
        });
        setActiveIndex(nextIndex);
      }
    }, 3000); // Change slide every 5 seconds

    return () => clearInterval(autoScrollInterval);
  }, [activeIndex, isPaused]);

  const handleScroll = () => {
    if (scrollContainerRef.current) {
      const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;
      setShowLeftArrow(scrollLeft > 0);
      setShowRightArrow(scrollLeft < scrollWidth - clientWidth - 10);
      
      // Calculate active index based on scroll position
      const cardWidth = 400;
      const currentIndex = Math.round(scrollLeft / cardWidth);
      setActiveIndex(currentIndex % campaigns.length);
    }
  };

  const scroll = (direction: 'left' | 'right') => {
    if (scrollContainerRef.current) {
      const container = scrollContainerRef.current;
      const cardWidth = 400;
      const currentScroll = container.scrollLeft;
      const totalWidth = container.scrollWidth;
      const containerWidth = container.clientWidth;

      let newScroll;
      if (direction === 'left') {
        newScroll = currentScroll - cardWidth;
        if (newScroll < 0) {
          newScroll = totalWidth - containerWidth;
        }
      } else {
        newScroll = currentScroll + cardWidth;
        if (newScroll > totalWidth - containerWidth) {
          newScroll = 0;
        }
      }

      container.scrollTo({
        left: newScroll,
        behavior: 'smooth'
      });
    }
  };

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
        <button 
          className={`${styles.scrollButton} ${styles.scrollLeft}`}
          onClick={() => scroll('left')}
          aria-label="Scroll left"
        >
          ←
        </button>
        
        <div 
          className={styles.campaignContainer} 
          ref={scrollContainerRef}
          onScroll={handleScroll}
        >
          {campaigns.map(campaign => {
            const progress = (campaign.currentAmount / campaign.goalAmount) * 100;
            
            return (
              <div key={campaign.id} className={styles.campaignCard}>
                <div className={styles.imageWrapper}>
                  <img 
                    src={campaign.image} 
                    alt={campaign.title} 
                    className={styles.image}
                  />
                </div>
                
                <div className={styles.content}>
                  <div>
                    <h3 className={styles.campaignTitle}>{campaign.title}</h3>
                    <p className={styles.description}>{campaign.description}</p>
                  </div>
                  
                  <div className={styles.bottomContent}>
                    <div className={styles.progressContainer}>
                      <div className={styles.progressInfo}>
                        <span className={styles.amount}>
                          {campaign.currentAmount.toLocaleString()} KM
                        </span>
                        <span className={styles.goal}>
                          od {campaign.goalAmount.toLocaleString()} KM
                        </span>
                      </div>
                      <div className={styles.progressBar}>
                        <div 
                          className={styles.progress} 
                          style={{ width: `${progress}%` }}
                        />
                      </div>
                      <div className={styles.percentageInfo}>
                        {Math.round(progress)}% prikupljeno
                      </div>
                    </div>

                    <button className={styles.donateButton}>
                      Doniraj za ovu kampanju
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        <button 
          className={`${styles.scrollButton} ${styles.scrollRight}`}
          onClick={() => scroll('right')}
          aria-label="Scroll right"
        >
          →
        </button>

        <div className={styles.indicators}>
          {campaigns.map((_, index) => (
            <button
              key={index}
              className={`${styles.indicator} ${index === activeIndex ? styles.active : ''}`}
              onClick={() => {
                if (scrollContainerRef.current) {
                  scrollContainerRef.current.scrollTo({
                    left: index * 400,
                    behavior: 'smooth'
                  });
                }
              }}
              aria-label={`Go to slide ${index + 1}`}
            />
          ))}
        </div>
      </div>
    </section>
  );
}; 