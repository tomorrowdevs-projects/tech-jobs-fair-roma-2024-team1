import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HabitPage from './pages/HabitPage'; 
import HabitChartPage from './pages/HabitChartPage';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<HabitPage />} />
          <Route path="/habit-chart" element={<HabitChartPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
