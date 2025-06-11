import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import { Link } from 'react-router-dom';
import galleryStyles from '../CampaignGallery/CampaignGallery.module.css';
import styles from './ProgramsPage.module.css';
import { getCampaigns, getCampaignDonatedAmount, Campaign } from '../../../services/campaign.service';

interface CampaignWithDonations extends Campaign {
	donatedAmount: number;
}
export const ProgramsPage: React.FC = () => {
	const [campaigns, setCampaigns] = useState<CampaignWithDonations[]>([]);

	const [search, setSearch] = useState('');
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);

	useEffect(() => {
		const fetchCampaigns = async () => {
			try {
				const campaignsData = await getCampaigns();

				// Fetch donated amounts for each campaign
				const campaignsWithDonations = await Promise.all(
					campaignsData.map(async campaign => {
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

	return (
		<div className={styles.programsContainer}>
			<h1 className={styles.title}>Kamapanje</h1>
			<input
				className={styles.searchBar}
				type='text'
				placeholder='Pretraži programe...'
				value={search}
				onChange={e => setSearch(e.target.value)}
			/>
			{loading && <div className={styles.loading}>Učitavanje kampanja...</div>}
			{error && <div className={styles.error}>{error}</div>}
			<div className={styles.grid3col}>
				{campaigns.map(campaign => {
					const progress = (campaign.donatedAmount / campaign.targetAmount) * 100;

					return (
						<div key={campaign.id} className={styles.campaignCard}>
							<div className={styles.imageWrapper}>
								<img src={campaign.image} alt={campaign.name} className={styles.image} loading='lazy' />
							</div>

							<div className={styles.content}>
								<h3 className={styles.campaignTitle}>{campaign.name}</h3>

								<div className={styles.bottomContent}>
									<div className={styles.progressContainer}>
										<div className={styles.progressInfo}>
											<span className={styles.amount}>{campaign.donatedAmount} KM</span>
											<span className={styles.goal}>od {campaign.targetAmount} KM</span>
										</div>
										<div className={styles.progressBar}>
											<div className={styles.progress} style={{ width: `${Math.min(progress, 100)}%` }} />
										</div>
										<div className={styles.percentageInfo}>{Math.round(Math.min(progress, 100))}% prikupljeno</div>
									</div>

									<Link to={`/campaign/${campaign.id}`} className={styles.donateButton}>
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
