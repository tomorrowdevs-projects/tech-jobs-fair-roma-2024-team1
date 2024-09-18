import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import styles from "./HabitPage.module.css";
import { useDispatch, useSelector } from "react-redux";
import { AddNewHabits, DeleteHabit, fetchProtectedResource, updateHabitCompletion } from "../redux/action/habit";

const HabitPage = () => {
  const [dates, setDates] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [reminder, setReminder] = useState(false);

  const [newHabitName, setNewHabitName] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const habits = useSelector((state) => state.habits.allHabits);
  const getCalendarDates = () => {
    const days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    const dateArray = [];
    const today = new Date();

    for (let i = 0; i < 5; i++) {
      const date = new Date(today);
      date.setDate(today.getDate() - i);
      dateArray.push({
        day: days[date.getDay()],
        date: date.getDate(),
        isToday: i === 0,
      });
    }
    setDates(dateArray);
  };

  useEffect(() => {
    getCalendarDates();
    dispatch(fetchProtectedResource());
  }, []);

  const handleModalToggle = () => setShowModal(!showModal);

  const handleSaveHabit = () => {
    if (newHabitName.trim() !== "") {
      const frequency = document.getElementById("habitFrequency").value;

      const newHabit = {
        name: newHabitName,
        frequency: frequency,
        reminder: reminder,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        completed: false,
      };

      dispatch(AddNewHabits(newHabit));

      setNewHabitName("");
      setReminder(false);
      handleModalToggle();
    }
  };

  const handleCompletionToggle = (id, currentStatus) => {
    dispatch(updateHabitCompletion(id, !currentStatus));
  };

  const handleChartNavigation = () => {
    navigate("/habit-chart", { state: { habits: habits } });
  };

  const handleDeleteHabit = (habitId) => {
    dispatch(DeleteHabit(habitId));
  };

  return (
    <div className={`${styles.habitPage} min-vh-100 d-flex flex-column py-4 py-md-5`}>
      <div className="container">
        <div className="row justify-content-center mb-4">
          <div className="col-8 col-sm-6 col-md-4 mt-4">
            <img src="/habit.svg" alt="Habit Icon" />
          </div>
        </div>

        <h1 className={`${styles.pageTitle} text-center mb-4`}>Set your goals</h1>

        <div className="d-flex justify-content-end mb-3">
          <button onClick={handleModalToggle} className={`${styles.btnCircle} me-4`}>
            +
          </button>
          <button onClick={handleChartNavigation} className={`${styles.btnCircle} me-4`}>
            〽
          </button>
        </div>

        <div className="row">
          <div className="col-12 d-flex justify-content-between align-items-center">
            <h2>Your habits</h2>
            <div className={`${styles.calendarStrip} d-flex justify-content-between mb-4 mt-4 p-2 rounded`}>
              {dates.map((item, index) => (
                <div key={index} className={`${styles.calendarDay} text-center p-1 rounded ${item.isToday ? styles.today : ""}`}>
                  <div className={styles.dateNumber}>{item.date}</div>
                  <div className={styles.dayName}>{item.day}</div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {habits && habits.content && habits.content.length > 0 ? (
          habits.content.map((habit) => (
            <div key={habit.id} className={`${styles.habitRow} d-flex align-items-center mb-3`}>
              <div className={`${styles.habitName} d-flex align-items-center`}>
                Name: {habit.name} Frequency: {habit.frequency}
                <button className={`${styles.deleteButton} ms-2`} onClick={() => handleDeleteHabit(habit.id)} aria-label="Delete habit">
                  ×
                </button>
              </div>
              <button
                className={`${styles.completionButton} ${habit.completed ? styles.completed : styles.notCompleted}`}
                onClick={() => handleCompletionToggle(habit.id, habit.completed)}
              >
                {habit.completed ? "✔" : "×"}
              </button>
            </div>
          ))
        ) : (
          <div className="flex-grow-1 d-flex align-items-center justify-content-center">
            <div className={`${styles.bigCircle} d-flex align-items-center justify-content-center text-center mt-4 p-3`}>You have no active habits</div>
          </div>
        )}

        <Modal show={showModal} onHide={handleModalToggle} centered>
          <Modal.Header className={`${styles.modalHeader} border-0 justify-content-center`}>
            <Modal.Title className={`${styles.headerModal}`}>Create Habit</Modal.Title>
          </Modal.Header>
          <Modal.Body className={`${styles.modalBody}`}>
            <Form>
              <Form.Group controlId="habitName">
                <Form.Label htmlFor="habitName" className="text-white">
                  Name
                </Form.Label>
                <Form.Control
                  id="habitName"
                  type="text"
                  placeholder="Enter habit name"
                  className={styles.inputField}
                  value={newHabitName}
                  onChange={(e) => setNewHabitName(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="habitFrequency" className="mt-3">
                <Form.Label htmlFor="habitFrequency" className="text-white">
                  Frequency
                </Form.Label>
                <Form.Select id="habitFrequency" className={styles.inputField}>
                  <option value="everyday">Everyday</option>
                  <option value="every3days">Every 3 Days</option>
                  <option value="onceaweek">Once a Week</option>
                </Form.Select>
              </Form.Group>

              <Form.Group controlId="habitReminder" className="mt-3 d-flex align-items-center justify-content-between">
                <Form.Label htmlFor="habitReminder" className="text-white mb-0">
                  Reminder
                </Form.Label>
                <Form.Check type="switch" id="habitReminder" className={styles.switch} checked={reminder} onChange={() => setReminder(!reminder)} />
              </Form.Group>

              <Button variant="outline-light" className={`${styles.saveButton} mt-4 w-100`} onClick={handleSaveHabit}>
                Save Habit
              </Button>
            </Form>
          </Modal.Body>
        </Modal>
      </div>
    </div>
  );
};

export default HabitPage;
