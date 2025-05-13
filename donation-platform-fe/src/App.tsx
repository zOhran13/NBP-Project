import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { TopBar } from './components/layout/TopBar/TopBar';
import { Header } from './components/layout/Header/Header';
import { Hero } from './components/sections/Hero/Hero';
import { Programs } from './components/sections/Programs/Programs';
import { CampaignGallery } from './components/sections/CampaignGallery/CampaignGallery';
import { Volunteers } from './components/sections/Volunteers/Volunteers';
import { Login } from './pages/Login/Login';
import { Signup } from './pages/Signup/Signup';
import { ForgotPassword } from './pages/ForgotPassword/ForgotPassword';
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
              <div className="contentWrapper">
                <Hero />
                <CampaignGallery />
                <Programs />
              </div>
            } />
            <Route path="/get-involved" element={
              <div className="contentWrapper">
                <Volunteers />
              </div>
            } />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
