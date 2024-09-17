import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "@clerk/clerk-react";
import HabitPage from "./pages/HabitPage";
import HabitChartPage from "./pages/HabitChartPage"; 
import SignUpPage from "./pages/Registration/SignUpPage";
import SignInPage from "./pages/SignIn/SignInPage";
import MyNav from "./component/MyNav";

function App() {
  const { isSignedIn, isLoaded } = useAuth();

  if (!isLoaded) {
    return <div>Loading...</div>;
  }

  return (
    <div className="App">
      {isSignedIn && <MyNav />}
      <Routes>
        <Route
          path="/"
          element={
            isSignedIn ? <HabitPage /> : <Navigate to="/signIn" replace />
          }
        />
        <Route
          path="/habit-chart"
          element={
            isSignedIn ? <HabitChartPage /> : <Navigate to="/signIn" replace />
          }
        />
        <Route 
          path="/signIn" 
          element={isSignedIn ? <Navigate to="/" replace /> : <SignInPage />} 
        />
        <Route 
          path="/signUp" 
          element={isSignedIn ? <Navigate to="/" replace /> : <SignUpPage />} 
        />
        <Route path="*" element={<Navigate to={isSignedIn ? "/" : "/signIn"} replace />} />
      </Routes>
    </div>
  );
}

export default App;