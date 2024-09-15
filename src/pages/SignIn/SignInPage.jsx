import { SignIn, useAuth } from "@clerk/clerk-react";
import { Navigate } from "react-router-dom";
import styles from "./SignInPage.module.css";

const SignInPage = () => {
  const { isSignedIn } = useAuth();

  if (isSignedIn) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.signInPage}>
      <div className={styles.contentWrapper}>
        <SignIn
          appearance={{
            elements: {
              cardBox: styles.signInCard,
              card: styles.signInCard,
              headerTitle: styles.signInFont,
              headerSubtitle: styles.dNone,
              socialButtonsBlockButton: styles.googleButton,
              socialButtonsProviderIcon: styles.googleIcon,
              socialButtonsBlockButtonText: styles.socialText,
              dividerLine: styles.dNone,
              dividerText: styles.dividerText,
              formFieldInput: styles.formFieldInput,
              formButtonPrimary: styles.formButtonPrimary,
              buttonArrowIcon: styles.dNone,
              formFieldLabelRow: styles.dNone,
              footer: styles.dNone,
            },
            variables: {
              colorText: "white",
              fontFamily: "Rubik, sans-serif",
              colorBackground: "#5251b5",
            },
          }}
          redirectUrl="/"
        />
      </div>
    </div>
  );
};

export default SignInPage;