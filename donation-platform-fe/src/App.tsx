import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/layout/Header/Header';
import { Hero } from './components/sections/Hero/Hero';
import { Programs } from './components/sections/Programs/Programs';
import { CampaignGallery } from './components/sections/CampaignGallery/CampaignGallery';
import { Volunteers } from './components/sections/Volunteers/Volunteers';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
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
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App; 