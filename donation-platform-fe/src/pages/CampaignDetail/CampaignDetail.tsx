import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCampaigns, getCampaignDonatedAmount, Campaign } from '../../services/campaign.service';
import styles from './CampaignDetail.module.css';
import { createDonation, DonationDTO } from '../../services/donation.service';
import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';
const CampaignDetail: React.FC = () => {
	const { id } = useParams<{ id: string }>();
	const navigate = useNavigate();
	const [campaign, setCampaign] = useState<Campaign | null>(null);
	const [donatedAmount, setDonatedAmount] = useState<number>(0);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);
	const [donationAmount, setDonationAmount] = useState<string>('');
	const [showDonationForm, setShowDonationForm] = useState(false);
	const [isSubmitting, setIsSubmitting] = useState(false);
	useEffect(() => {
		const fetchCampaign = async () => {
			try {
				if (!id) return;
				const campaigns = await getCampaigns();
				const campaign = campaigns.find(c => c.id === parseInt(id));
				if (campaign) {
					setCampaign(campaign);
					const amount = await getCampaignDonatedAmount(campaign.id);
					setDonatedAmount(amount);
				} else {
					setError('Campaign not found');
				}
			} catch (err) {
				setError('Failed to load campaign details');
				console.error('Error fetching campaign:', err);
			} finally {
				setLoading(false);
			}
		};

		fetchCampaign();
	}, [id]);

	const handleDonationSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		if (!campaign || !donationAmount) return;

		setIsSubmitting(true);
		try {
			const token = Cookies.get('accessToken');
			if (!token) {
				alert('Please login to make a donation');
				navigate('/login');
				return;
			}

			const decoded: any = jwtDecode(token);
			const userId = decoded.sub;

			const donationDTO: DonationDTO = {
				amount: parseFloat(donationAmount),
				campaignId: campaign.id,
				userId: userId,
				donationDate: new Date(),
			};

			await createDonation(donationDTO);

			// Refresh the donated amount
			const newAmount = await getCampaignDonatedAmount(campaign.id);
			setDonatedAmount(newAmount);

			// Show success message and close form
			alert(`Thank you for your donation of ${donationAmount} KM!`);
			setShowDonationForm(false);
			setDonationAmount('');
		} catch (error) {
			console.error('Error creating donation:', error);
			alert('Failed to process donation. Please try again.');
		} finally {
			setIsSubmitting(false);
		}
	};

	if (loading) {
		return (
			<div className={styles.loadingContainer}>
				<div className={styles.loading}>Loading...</div>
			</div>
		);
	}

	if (error || !campaign) {
		return (
			<div className={styles.errorContainer}>
				<div className={styles.error}>{error || 'Campaign not found'}</div>
				<button onClick={() => navigate('/')} className={styles.backButton}>
					Back to Home
				</button>
			</div>
		);
	}

	const progress = (donatedAmount / campaign.targetAmount) * 100;

	return (
		<div className={styles.mainContent}>
			<div className={`${styles.leftColumn} ${styles.hero}`} style={{ backgroundImage: `url(${campaign.image})` }}>
				<div className={styles.heroContent}>
					<h1>{campaign.name}</h1>
				</div>
			</div>

			<div className={`${styles.rightColumn} ${styles.content}`}>
				<div className={styles.donationContent}>
					<div className={styles.description}>
						<h2>About this Campaign</h2>
						<p>
							Campaign period: {new Date(campaign.startDate).toLocaleDateString()} -{' '}
							{new Date(campaign.endDate).toLocaleDateString()}
						</p>
					</div>

					<div className={styles.progressSection}>
						<div className={styles.progressInfo}>
							<div className={styles.amount}>
								<span className={styles.currentAmount}>{donatedAmount} KM</span>
								<span className={styles.goalAmount}>of {campaign.targetAmount} KM</span>
							</div>
							<div className={styles.progressBar}>
								<div className={styles.progressFill} style={{ width: `${progress}%` }} />
							</div>
							<div className={styles.endDate}>Ends on: {new Date(campaign.endDate).toLocaleDateString()}</div>
						</div>

						<button className={styles.donateButton} onClick={() => setShowDonationForm(true)}>
							Doniraj
						</button>
					</div>
				</div>
			</div>

			{showDonationForm && (
				<div className={styles.donationModal}>
					<div className={styles.donationForm}>
						<h2>Make a Donation</h2>
						<form onSubmit={handleDonationSubmit}>
							<div className={styles.formGroup}>
								<label htmlFor='amount'>Amount (KM)</label>
								<input
									type='number'
									id='amount'
									value={donationAmount}
									onChange={e => setDonationAmount(e.target.value)}
									min='1'
									required
									placeholder='Enter amount'
								/>
							</div>
							<div className={styles.formActions}>
								<button type='submit' className={styles.submitButton}>
									Confirm Donation
								</button>
								<button type='button' className={styles.cancelButton} onClick={() => setShowDonationForm(false)}>
									Cancel
								</button>
							</div>
						</form>
					</div>
				</div>
			)}
		</div>
	);
};

export default CampaignDetail;
