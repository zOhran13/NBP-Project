import React, { useEffect, useState, useCallback } from 'react';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import styles from './Volunteers.module.css';
import { registerVolunteer, getVolunteerCount, getUserShifts, VolunteerShift } from '../../../services/volunteer.service';

interface Event {
  id: number;
  title: string;
  imageLink: string;
  eventStart: string;
  eventEnd: string;
  location: string;
  description: string;
  volunteerGoal: number;
  donationGoal: number;
}

interface EventWithVolunteerCount extends Event {
  currentVolunteers: number;
}

export const Volunteers: React.FC = () => {
  const navigate = useNavigate();
  const [events, setEvents] = useState<EventWithVolunteerCount[]>([]);
  const [userShifts, setUserShifts] = useState<VolunteerShift[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [registrationError, setRegistrationError] = useState<string | null>(null);

  const fetchUserShifts = useCallback(async () => {
    try {
      const token = Cookies.get('accessToken');
      if (!token) {
        setUserShifts([]);
        return;
      }
      const shifts = await getUserShifts();
      setUserShifts(shifts);
      return shifts;
    } catch (err) {
      console.error('Error fetching user shifts:', err);
      setUserShifts([]);
      return [];
    }
  }, []);

  const fetchEvents = useCallback(async (shifts: VolunteerShift[] = []) => {
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
      const data: Event[] = await res.json();
      
      // Filter future events
      const now = new Date();
      const futureEvents = data.filter(event => new Date(event.eventStart) > now);
      
      // Get volunteer count for each event
      const eventsWithCount = await Promise.all(
        futureEvents.map(async (event) => {
          try {
            const count = await getVolunteerCount(event.id);
            return { ...event, currentVolunteers: count };
          } catch (err) {
            console.error(`Error fetching volunteer count for event ${event.id}:`, err);
            return { ...event, currentVolunteers: 0 };
          }
        })
      );
      
      // Filter events that still need volunteers and user hasn't registered for
      const availableEvents = eventsWithCount.filter(event => 
        event.currentVolunteers < event.volunteerGoal && 
        !shifts.some(shift => shift.event.id === event.id)
      );
      
      setEvents(availableEvents);
    } catch (err: any) {
      setError(err.message || 'Greška');
    } finally {
      setLoading(false);
    }
  }, []);

  const refreshData = useCallback(async () => {
    const shifts = await fetchUserShifts();
    await fetchEvents(shifts);
  }, [fetchUserShifts, fetchEvents]);

  useEffect(() => {
    refreshData();
  }, [refreshData]);

  const handleRegister = async (event: Event) => {
    const token = Cookies.get('accessToken');
    if (!token) {
      navigate('/login', { state: { from: '/volunteers' } });
      return;
    }

    try {
      setRegistrationError(null);
      
      // Parse start and end dates
      const startDate = new Date(event.eventStart);
      const endDate = new Date(event.eventEnd);
      
      // Extract time in HH:mm:ss format
      const shiftStart = startDate.toTimeString().split(' ')[0];
      const shiftEnd = endDate.toTimeString().split(' ')[0];
      
      // Calculate hours worked
      const diffInMs = endDate.getTime() - startDate.getTime();
      const diffInHours = diffInMs / (1000 * 60 * 60);
      const hoursWorked = Math.max(1, Math.round(diffInHours));

      const registration = {
        shiftStart,
        shiftEnd,
        hoursWorked,
        eventId: event.id
      };

      await registerVolunteer(registration);
      await refreshData(); // Refresh both user shifts and events list
      alert('Uspješno ste se prijavili za volontiranje!');
    } catch (err: any) {
      setRegistrationError(err.message || 'Greška pri prijavi za volontiranje');
    }
  };

  const formatDateTime = (dateString: string) => {
    const date = new Date(dateString);
    const datePart = date.toLocaleDateString('bs-BA', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).replace(/\./g, '.');
    const timePart = date.toLocaleTimeString('bs-BA', {
      hour: '2-digit',
      minute: '2-digit'
    });
    return `${datePart} u ${timePart}`;
  };

  const calculateProgress = (current: number, goal: number) => {
    return Math.min(100, (current / goal) * 100);
  };

  if (loading) return <div className={styles.loading}>Učitavanje događaja...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.volunteersContainer}>
      <h1 className={styles.title}>Volonterski Događaji</h1>
      {registrationError && <div className={styles.error}>{registrationError}</div>}
      <div className={styles.grid}>
        {events.map(event => (
          <div className={styles.card} key={event.id}>
            <img src={event.imageLink} alt={event.title} className={styles.image} />
            <div className={styles.cardContent}>
              <h2 className={styles.cardTitle}>{event.title}</h2>
              <div className={styles.dateLocation}>
                <div className={styles.dateTime}>
                  <span className={styles.dateLabel}>📅 Datum i vrijeme:</span>
                  <span className={styles.dateValue}>{formatDateTime(event.eventStart)} - {formatDateTime(event.eventEnd)}</span>
                </div>
                <div className={styles.locationContainer}>
                  <span className={styles.locationLabel}>📍 Lokacija:</span>
                  <span className={styles.locationValue}>{event.location}</span>
                </div>
              </div>
              <p className={styles.description}>{event.description}</p>
              <div className={styles.volunteerProgress}>
                <div className={styles.progressBar}>
                  <div 
                    className={styles.progressFill}
                    style={{ width: `${calculateProgress(event.currentVolunteers, event.volunteerGoal)}%` }}
                  />
                </div>
                <div className={styles.progressText}>
                  <span>{event.currentVolunteers} / {event.volunteerGoal} volontera</span>
                </div>
              </div>
              <div className={styles.goals}>
                <span>💰 {event.donationGoal} KM</span>
              </div>
              <button 
                className={styles.applyButton}
                onClick={() => handleRegister(event)}
              >
                Prijavi se za volontiranje
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}; 