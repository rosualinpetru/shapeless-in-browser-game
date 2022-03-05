export const API_BASE_URL = `http://${
  process.env.REACT_APP_REMOTE === undefined
    ? "localhost"
    : `${process.env.REACT_APP_REMOTE}`
}:31500`;
export const DESIGNER_HOST = `http://${
  process.env.REACT_APP_REMOTE === undefined
    ? "localhost"
    : `${process.env.REACT_APP_REMOTE}`
}:31600/ws`;
export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = `http://${
  process.env.REACT_APP_REMOTE === undefined
    ? "localhost:31700"
    : `${process.env.REACT_APP_REMOTE}`
}/oauth2/redirect`;

export const GOOGLE_AUTH_URL =
  API_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL =
  API_BASE_URL +
  "/oauth2/authorize/facebook?redirect_uri=" +
  OAUTH2_REDIRECT_URI;

export const messageType = {
  updateLobby: "UPDATE_LOBBY",
  gameError: "GAME_ERROR",
  start: "START",
  updateGame: "UPDATE_GAME",
};

export const ShapeColors = {
  RED: "#d9534f",
  BLUE: "#0275d8",
  GREEN: "#5cb85c",
  GRAY: "#c0c0c0",
};
