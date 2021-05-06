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
    url: API_BASE_URL + "/users/current",
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
    url: API_BASE_URL + "/api/users/update/imageUrl",
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

export function rooms() {
  return request({
    url: API_BASE_URL + "/api/rooms",
    method: "GET",
  });
}

export function createRoom(createRoomRequest) {
  return request({
    url: API_BASE_URL + "/api/rooms",
    method: "POST",
    body: JSON.stringify(createRoomRequest),
  });
}
