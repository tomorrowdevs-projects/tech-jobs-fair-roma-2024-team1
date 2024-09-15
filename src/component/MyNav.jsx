import { SignOutButton, useUser } from "@clerk/clerk-react";
import { Container, Dropdown, Navbar } from "react-bootstrap";
import styles from "../component/MyNav.module.css";
const MyNav = () => {
  const { isSignedIn, user } = useUser();
  return (
    <>
      <Navbar className={`${styles.navBar} py-0 `}>
        <Container>
          <Navbar.Toggle />
          <Navbar.Collapse className="justify-content-end">
            <Navbar.Text>
              <Dropdown autoClose="inside">
                <Dropdown.Toggle variant="btn" id="dropdown-autoclose-inside" className={styles.noCaret}>
                  {isSignedIn && <img src={user.imageUrl} alt="" className={`${styles.imgProfile} rounded rounded-circle `} />}
                  {isSignedIn && <p className={`${styles.navText} py-3 m-0 `}>Hello, {user.firstName}</p>}
                </Dropdown.Toggle>
                <Dropdown.Menu className={`${styles.navBar} `}>
                  <Dropdown.Item className={`${styles.navBar}`}>
                    {" "}
                    <SignOutButton>
                      <p className={`${styles.navText} py-3 m-0 `}>Sign out</p>
                    </SignOutButton>
                  </Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Navbar.Text>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};
export default MyNav;
