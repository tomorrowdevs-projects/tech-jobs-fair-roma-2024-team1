import { SignIn } from "@clerk/clerk-react";
import styles from "../SignIn/SignInPage.module.css";

const SignInPage = () => {
  return (
    <>
      <div className={`${styles.signInPage}   min-vh-100 d-flex flex-column  py-md-5  align-items-center justify-content-center`}>
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
        />
      </div>
    </>
  );
};
export default SignInPage;
