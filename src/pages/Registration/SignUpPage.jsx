import { SignUp } from "@clerk/clerk-react";
import styles from "../Registration/SignUpPage.module.css";
import { Link } from "react-router-dom";
import { useState } from "react";
const SignUpPage = () => {
  const [animate, setAnimate] = useState(false);
  // const navigate = useNavigate();
  const handleClick = () => {
    setAnimate(true);

    // setTimeout(() => {
    //   navigate("/sign-in");
    // }, 300);
  };

  return (
    <>
      <div
        className={`${styles.signUpPage}  ${animate ? styles.fadeOut : ""} min-vh-100 d-flex flex-column  py-md-5  align-items-center justify-content-center`}
      >
        <SignUp
          appearance={{
            elements: {
              cardBox: styles.signUpCard,
              card: styles.signUpCard,
              headerTitle: styles.signUpFont,
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
        <div>
          <p className={`${styles.footerText} my-3`}>
            ALREADY HAVE AN ACCOUNT?{" "}
            <span>
              <Link onClick={handleClick}>SIGN IN</Link>
            </span>{" "}
          </p>
        </div>
      </div>
    </>
  );
};
export default SignUpPage;
