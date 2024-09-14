import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HabitPage from "./pages/HabitPage";
import SignUpPage from "./pages/Registration/SignUpPage";
import SignInPage from "./pages/SignIn/SignInPage";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<HabitPage />} />
          <Route path="/signUp" element={<SignUpPage />} />
          <Route path="/signIn" element={<SignInPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
