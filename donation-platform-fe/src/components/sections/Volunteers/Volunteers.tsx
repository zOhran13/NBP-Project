import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import styles from './Volunteers.module.css';

interface Event {
  id: number;
  title: string;
  imageLink: string;
  eventDate: string;
  location: string;
  description: string;
  volunteerGoal: number;
  donationGoal: number;
}

export const Volunteers: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchEvents = async () => {
      setLoading(true);
      setError(null);
      try {
        const token = Cookies.get('accessToken');
        const res = await fetch('http://localhost:8080/api/events', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': '*/*',
          },
        });
        if (!res.ok) throw new Error('Greška pri dohvatanju događaja');
        const data = await res.json();
        setEvents(data);
      } catch (err: any) {
        setError(err.message || 'Greška');
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  if (loading) return <div className={styles.loading}>Učitavanje događaja...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.volunteersContainer}>
      <h1 className={styles.title}>Volonterski Događaji</h1>
      <div className={styles.grid}>
        {events.map(event => (
          <div className={styles.card} key={event.id}>
            <img src={event.imageLink} alt={event.title} className={styles.image} />
            <div className={styles.cardContent}>
              <h2 className={styles.cardTitle}>{event.title}</h2>
              <p className={styles.dateLocation}>
                <span>{new Date(event.eventDate).toLocaleDateString()}</span> | <span>{event.location}</span>
              </p>
              <p className={styles.description}>{event.description}</p>
              <div className={styles.goals}>
                <span>🎯 Volonteri: {event.volunteerGoal}</span>
                <span>💰 Donacije: {event.donationGoal} KM</span>
              </div>
              <button className={styles.applyButton}>Prijavi se za volontiranje</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}; 