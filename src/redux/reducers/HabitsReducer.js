import {
  ADD_NEW_HABITS_FAILURE,
  ADD_NEW_HABITS_REQUEST,
  ADD_NEW_HABITS_SUCCESS,
  GET_HABITS,
  GET_HABITS_FAILURE,
  RESET_HABITS_STATE,
  UPDATE_HABIT_FAILURE,
  UPDATE_HABIT_REQUEST,
  UPDATE_HABIT_SUCCESS,
} from "../action/habit";

const initialState = {
  loading: false,
  success: false,
  error: false,
  content: null,
  errorMsg: null,
  allHabits: null,
  completation: [],
};
const HabitsReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_NEW_HABITS_REQUEST:
      return { ...state, loading: true };
    case ADD_NEW_HABITS_SUCCESS:
      return { ...state, loading: false, success: true, content: action.payload, errorMsg: null, error: false };
    case ADD_NEW_HABITS_FAILURE:
      return { ...state, loading: false, errorMsg: action.payload, success: false, error: true };
    case RESET_HABITS_STATE:
      return initialState;
    case GET_HABITS:
      return { ...state, allHabits: action.payload, success: true, error: false, loading: false };
    case GET_HABITS_FAILURE:
      return { ...state, error: true, errorMsg: action.payload, success: false, loading: false };
    case UPDATE_HABIT_REQUEST:
      return { ...state, loading: true };
    case UPDATE_HABIT_SUCCESS:
      return {
        ...state,
        loading: false,
        success: true,
        allHabits: state.allHabits.content.map((habit) => (habit.id === action.payload.id ? { ...habit, ...action.payload } : habit)),
      };
    case UPDATE_HABIT_FAILURE:
      return { ...state, error: true, errorMsg: action.payload, success: false, loading: false };

    default:
      return state;
  }
};
export default HabitsReducer;
