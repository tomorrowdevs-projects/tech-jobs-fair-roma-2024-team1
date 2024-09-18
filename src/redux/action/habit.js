export const ADD_NEW_HABITS_REQUEST = "ADD_NEW_HABITS_REQUEST";
export const ADD_NEW_HABITS_SUCCESS = "ADD_NEW_HABITS_SUCCESS";
export const ADD_NEW_HABITS_FAILURE = "ADD_NEW_HABITS_FAILURE";
export const RESET_HABITS_STATE = "RESET_HABITS_STATE";
export const GET_HABITS = "GET_HABITS";
export const GET_HABITS_FAILURE = "GET_HABITS_FAILURE";

const addNewHabitsRequest = () => ({
  type: ADD_NEW_HABITS_REQUEST,
});

const addNewHabitsSuccess = (data) => ({
  type: ADD_NEW_HABITS_SUCCESS,
  payload: data,
});

const addNewHabitsFailure = (error) => ({
  type: ADD_NEW_HABITS_FAILURE,
  payload: error,
});
export const resetHabitsState = () => ({
  type: RESET_HABITS_STATE,
});
const fetchResourceSuccess = (data) => ({
  type: GET_HABITS,
  payload: data,
});
const fetchResourceFailure = (error) => ({
  type: GET_HABITS_FAILURE,
  payload: error,
});
export const AddNewHabits = (habitsData) => async (dispatch) => {
  const token = localStorage.getItem("authToken");
  dispatch(addNewHabitsRequest());
  try {
    const response = await fetch("http://localhost:3001/habits", {
      method: "POST",
      body: JSON.stringify(habitsData),
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.error || "Errore durante la registrazione");
    }

    dispatch(addNewHabitsSuccess(data.message));
  } catch (error) {
    dispatch(addNewHabitsFailure(error.message));
  }
};

export const fetchProtectedResource = () => async (dispatch) => {
  const token = localStorage.getItem("authToken");

  try {
    const response = await fetch("http://localhost:3001/habits", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message);
    }

    const data = await response.json();
    dispatch(fetchResourceSuccess(data));
  } catch (error) {
    dispatch(fetchResourceFailure(error.message));
  }
};
