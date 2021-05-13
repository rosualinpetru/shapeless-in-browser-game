export const API_BASE_URL = "http://localhost:31500";
export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = "http://localhost:31700/oauth2/redirect";

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
};

export const ShapeColors = {
  RED: "#d9534f",
  BLUE: "#0275d8",
  GREEN: "#5cb85c",
};
