import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { TopBar } from './components/layout/TopBar/TopBar';
import { Header } from './components/layout/Header/Header';
import { Hero } from './components/sections/Hero/Hero';
import { CampaignGallery } from './components/sections/CampaignGallery/CampaignGallery';
import { Programs } from './components/sections/Programs/Programs';
import { AboutUs } from './components/sections/AboutUs/AboutUs';
import { ProgramDetail } from './components/sections/ProgramDetail/ProgramDetail';
import CampaignDetail from './pages/CampaignDetail/CampaignDetail';
import { Footer } from './components/layout/Footer/Footer';
import { Volunteers } from './components/sections/Volunteers/Volunteers';
import { ForgotPassword } from './components/auth/ForgotPassword/ForgotPassword';
import { ProgramsPage } from './components/sections/Programs/ProgramsPage';
import { ResetPassword } from './pages/ResetPassword/ResetPassword';
import { Profile } from './pages/Profile/Profile';
import { Login } from './components/auth/Login/Login';
import { Signup } from './components/auth/Signup/Signup';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <TopBar />
        <Header />
        <main>
          <Routes>
            <Route path="/" element={
              <>
                <Hero />
                <CampaignGallery />
                <Programs />
              </>
            } />
            <Route path="/o-nama" element={<AboutUs />} />
            <Route path="/program/:id" element={<ProgramDetail />} />
            <Route path="/campaign/:id" element={<CampaignDetail />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/volontiranje" element={<Volunteers />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/reset-password" element={<ResetPassword />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/programi" element={<ProgramsPage />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
