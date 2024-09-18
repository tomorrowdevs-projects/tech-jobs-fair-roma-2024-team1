import { ADD_NEW_HABITS_FAILURE, ADD_NEW_HABITS_REQUEST, ADD_NEW_HABITS_SUCCESS, RESET_HABITS_STATE } from "../action/habit";

const initialState = {
  loading: false,
  success: false,
  error: false,
  content: null,
  errorMsg: null,
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
    default:
      return state;
  }
};
export default HabitsReducer;
