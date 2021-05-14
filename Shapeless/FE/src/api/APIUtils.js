import { API_BASE_URL, ACCESS_TOKEN } from "../constants";

const request = (options) => {
  const headers = new Headers({
    "Content-Type": "application/json",
  });

  if (localStorage.getItem(ACCESS_TOKEN)) {
    headers.append(
      "Authorization",
      "Bearer " + localStorage.getItem(ACCESS_TOKEN)
    );
  }

  const defaults = { headers: headers };
  options = Object.assign({}, defaults, options);

  return fetch(options.url, options).then((response) =>
    response.json().then((json) => {
      if (!response.ok) {
        return Promise.reject(json);
      }
      return json;
    })
  );
};

export function getCurrentUser() {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    return Promise.reject("No access token set.");
  }

  return request({
    url: API_BASE_URL + "/api/users/current",
    method: "GET",
  });
}

export function login(loginRequest) {
  return request({
    url: API_BASE_URL + "/auth/login",
    method: "POST",
    body: JSON.stringify(loginRequest),
  });
}

export function signup(signupRequest) {
  return request({
    url: API_BASE_URL + "/auth/signup",
    method: "POST",
    body: JSON.stringify(signupRequest),
  });
}

export function updateImage(updateImageRequest) {
  return request({
    url: API_BASE_URL + "/api/users/imageUrl",
    method: "POST",
    body: JSON.stringify(updateImageRequest),
  });
}

export function leaderboard() {
  return request({
    url: API_BASE_URL + "/api/users/leaderboard",
    method: "GET",
  });
}

export function games() {
  return request({
    url: API_BASE_URL + "/api/games",
    method: "GET",
  });
}

export function gameData(id) {
  return request({
    url: `${API_BASE_URL}/api/games/${id}`,
    method: "GET",
  });
}

export function amIPlaying() {
  return request({
    url: API_BASE_URL + "/api/users/isPlaying",
    method: "GET",
  });
}

export function createGame(createGameRequest) {
  return request({
    url: API_BASE_URL + "/api/games",
    method: "POST",
    body: JSON.stringify(createGameRequest),
  });
}

export function getPlayersInGame(id) {
  return request({
    url: API_BASE_URL + "/api/players/game/" + id,
    method: "GET",
  });
}

export function getPlayersInActualGame(id) {
  return request({
    url: API_BASE_URL + "/api/players/game/" + id + "/active",
    method: "GET",
  });
}
