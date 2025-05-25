import React, { useState } from 'react';
import styles from './AddressForm.module.css';

export interface AddressDTO {
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

interface AddressFormProps {
  address: AddressDTO;
  onAddressChange: (address: AddressDTO) => void;
  addressInput: string;
  onAddressInputChange: (input: string) => void;
  disabled?: boolean;
  isEditing?: boolean;
  hideLabels?: boolean;
  columnLayout?: boolean;
}

export const AddressForm: React.FC<AddressFormProps> = ({
  address,
  onAddressChange,
  addressInput,
  onAddressInputChange,
  disabled = false,
  isEditing = false,
  hideLabels = false,
  columnLayout = false
}) => {
  const [addressSuggestions, setAddressSuggestions] = useState<any[]>([]);

  const handleStreetInput = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    onAddressInputChange(value);
    onAddressChange({ ...address, street: value });
    if (value.length < 3) {
      setAddressSuggestions([]);
      return;
    }
    const res = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(value)}&addressdetails=1`);
    const data = await res.json();
    setAddressSuggestions(data);
  };

  const handleAddressSelect = (suggestion: any) => {
    const street = (suggestion.address.road || '') + (suggestion.address.house_number ? ' ' + suggestion.address.house_number : '');
    onAddressInputChange(street);
    setAddressSuggestions([]);
    onAddressChange({
      street,
      city: suggestion.address.city || suggestion.address.town || suggestion.address.village || '',
      postalCode: suggestion.address.postcode || '',
      country: suggestion.address.country || ''
    });
  };

  return (
    <>
      <div className={styles.formGroup} style={{ position: 'relative' }}>
        {!hideLabels && <label>Ulica</label>}
        <input
          type="text"
          name="street"
          value={addressInput}
          onChange={handleStreetInput}
          disabled={disabled || !isEditing}
          required
          placeholder="Unesite adresu"
          autoComplete="off"
          className={styles.addressInput}
        />
        {addressSuggestions.length > 0 && (
          <ul className={styles.suggestionsList}>
            {addressSuggestions.map((s, idx) => (
              <li key={idx} onClick={() => handleAddressSelect(s)}>
                {s.display_name}
              </li>
            ))}
          </ul>
        )}
      </div>
        <div className={styles.twoColumnRow}>
          <div className={styles.formGroup}>
            {!hideLabels && <label>Grad</label>}
            <input
              type="text"
              name="city"
              value={address.city}
              disabled
              placeholder="Grad"
            />
          </div>
          <div className={styles.formGroup}>
            {!hideLabels && <label>Poštanski broj</label>}
            <input
              type="text"
              name="postalCode"
              value={address.postalCode}
              disabled
              placeholder="Poštanski broj"
            />
          </div>
          <div className={styles.formGroup}>
            {!hideLabels && <label>Država</label>}
            <input
              type="text"
              name="country"
              value={address.country}
              disabled
              placeholder="Država"
            />
          </div>
        </div>
          
        </>
      )
}; 