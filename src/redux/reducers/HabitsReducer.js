import { ADD_NEW_HABITS_FAILURE, ADD_NEW_HABITS_REQUEST, ADD_NEW_HABITS_SUCCESS, GET_HABITS, GET_HABITS_FAILURE, RESET_HABITS_STATE } from "../action/habit";

const initialState = {
  loading: false,
  success: false,
  error: false,
  content: null,
  errorMsg: null,
  allHabits: [],
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
      return { ...state, allHabits: [...state.allHabits, action.payload], success: true, error: false, loading: false };
    case GET_HABITS_FAILURE:
      return { ...state, error: true, errorMsg: action.payload, success: false, loading: false };
    default:
      return state;
  }
};
export default HabitsReducer;
