import React from "react";
import { createRoot } from "react-dom/client";
import { ClerkProvider } from "@clerk/clerk-react";
import { BrowserRouter } from "react-router-dom";
import App from "./App.jsx";
import "./index.css";
import "bootstrap/dist/css/bootstrap.min.css";

const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

const localization = {
  signIn: {
    start: {
      title: "Sign in to your account",
      subtitle: "Welcome back! Please enter your details.",
    },
    formButtonPrimary: "Sign in",
  },
  signUp: {
    start: {
      title: "Create an account",
      subtitle: "Get started with our app",
    },
    formButtonPrimary: "Sign up",
  },
};

if (!PUBLISHABLE_KEY) {
  throw new Error("Missing Publishable Key");
}

createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ClerkProvider localization={localization} publishableKey={PUBLISHABLE_KEY} >
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </ClerkProvider>
  </React.StrictMode>
);